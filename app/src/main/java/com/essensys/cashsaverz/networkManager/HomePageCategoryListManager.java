package com.essensys.cashsaverz.networkManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.essensys.cashsaverz.fragments.HomeFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant;
import com.essensys.cashsaverz.model.Category;
import com.essensys.cashsaverz.model.Product;
import com.essensys.cashsaverz.model.UserCurrentCartItemsDetails;
import com.essensys.cashsaverz.model.UserCurrentMembershipPlan;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class HomePageCategoryListManager implements retrofit2.Callback<ResponseBody> {
    private Context mContext;
    private StatusListener listener;
    private ProgressDialog progressDialog;

    public HomePageCategoryListManager(Context mContext, StatusListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        this.progressDialog = CommonUtilities.getDefaultLoader(mContext);
    }

    public interface StatusListener {
        void onListFetchSuccess(List<Category> categoryItemList, List<Product> popularBooksList, List<UserCurrentMembershipPlan> userCurrentMembershipPlanList, List<UserCurrentCartItemsDetails> userCurrentCartItemsDetailsList);
//        void onListFetchSuccess(List<Category> categoryItemList, List<Product> popularBooksList,  List<UserCurrentCartItemsDetails> userCurrentCartItemsDetailsList);

        void onListFetchFailure(String errorMsg);
    }

    public void fetchCategoryList() {
        showLoader();
        String customerID,deviceId;
        if (!App.isUserLoggedIn(mContext)) {
            customerID = "";
            deviceId = HomeFragment.deviceId;

        } else {
            customerID = App.getCurrentUser(mContext).getCustomerId();
            deviceId = "";

        }
        RetrofitClient
                .getClient(Constant.ServerEndpoint.FETCH_HOME_CATEGORY_LIST)
                .create(RetrofitInterfaces.FetchHomeCategoryList.class)
                .post(customerID,deviceId)
                .enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            String stringResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(stringResponse);
            JSONObject result = jsonObject.getJSONObject("result");
            String msg = result.getString("msg");

            if (msg.contentEquals("1")) {
                hideLoader();
                JSONArray categories = result.getJSONArray("categories");
                JSONArray popularBooks = result.getJSONArray("popularBooks");
                JSONArray userCurrentMembershipPlan = result.getJSONArray("userCurrentMembershipPlan");
                JSONArray userCurrentcartItemDetails = result.getJSONArray("cartDetails");

                List<Category> categoryItemList = new Gson()
                        .fromJson(categories.toString(), new TypeToken<List<Category>>() {
                        }.getType());

                List<Product> popularBooksList = new Gson()
                        .fromJson(popularBooks.toString(), new TypeToken<List<Product>>() {
                        }.getType());

                List<UserCurrentMembershipPlan> userCurrentMembershipPlanList = new Gson()
                        .fromJson(userCurrentMembershipPlan.toString(), new TypeToken<List<UserCurrentMembershipPlan>>() {
                        }.getType());

                List<UserCurrentCartItemsDetails> userCurrentCartItemsDetailsList = new Gson()
                        .fromJson(userCurrentcartItemDetails.toString(), new TypeToken<List<UserCurrentCartItemsDetails>>() {
                        }.getType());


                listener.onListFetchSuccess(categoryItemList, popularBooksList, userCurrentMembershipPlanList, userCurrentCartItemsDetailsList);
//                listener.onListFetchSuccess(categoryItemList, popularBooksList, userCurrentCartItemsDetailsList);

            } else {

                String msg_string = result.getString("msg_string");
                listener.onListFetchFailure(msg_string);
            }
        } catch (Exception e) {
            listener.onListFetchFailure(mContext.getResources().getString(R.string.something_went_wrong));
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        listener.onListFetchFailure(mContext.getResources().getString(R.string.error_other_issue));
        hideLoader();
    }

    private void showLoader() {
        if (progressDialog == null || progressDialog.isShowing()) {
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });
    }

    private void hideLoader() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }
}
