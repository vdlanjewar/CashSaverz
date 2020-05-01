package com.essensys.cashsaverz.networkManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.fragments.HomeFragment;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant.ServerEndpoint;
import com.essensys.cashsaverz.model.Product;
import com.essensys.cashsaverz.model.SubCategory;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces;
import com.essensys.cashsaverz.remote.RetrofitInterfaces.FetchProductList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListManager implements Callback<ResponseBody> {
    private StatusListener listener;
    private ArrayList<Product> mArraylistFinalProd = new ArrayList<>();
    private Context mContext;
    private String offset;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;

    public interface StatusListener {
        void onListFetchFailure(String str);

        void onListFetchSuccess(List<Product> list, List<SubCategory> list2);
    }

    public ProductListManager(Context mContext2, StatusListener listener2) {
        this.mContext = mContext2;
        this.listener = listener2;
        this.progressDialog = CommonUtilities.getDefaultLoader(mContext2);
    }

    public void fetchProductList(String cat_Id, String subcat_id, String offset2, String searchKeyword) {
        String deviceId;
        String customerID;
        showLoader();
        this.offset = offset2;
        if (!App.isUserLoggedIn(this.mContext)) {
            customerID = "";
            deviceId = HomeFragment.deviceId;
        } else {
            customerID = App.getCurrentUser(this.mContext).getCustomerId();
            deviceId = "";
        }
        RetrofitClient.getClient(ServerEndpoint.FETCH_PRODUCT_LIST)
                .create(RetrofitInterfaces.FetchProductList.class)
                .post(customerID, cat_Id, subcat_id, offset2, searchKeyword, deviceId)
                .enqueue(this);
    }

    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            JSONObject result = new JSONObject(((ResponseBody) response.body()).string()).getJSONObject("result");
            if (result.getString(NotificationCompat.CATEGORY_MESSAGE).contentEquals("1")) {
                hideLoader();
                JSONArray products = result.getJSONArray("products");
                List<Product> productList = (List) new Gson().fromJson(products.toString(), new TypeToken<List<Product>>() {
                }.getType());
                List<SubCategory> subcategory = (List) new Gson().fromJson(result.getJSONArray("arrSubCatList").toString(), new TypeToken<List<SubCategory>>() {
                }.getType());
                if (!productList.isEmpty()) {
                    this.mArraylistFinalProd.addAll(productList);
                }
                if (this.offset.equalsIgnoreCase("0")) {
                    this.listener.onListFetchSuccess(productList, subcategory);
                } else {
                    this.listener.onListFetchSuccess(this.mArraylistFinalProd, subcategory);
                }
                return;
            }
            this.listener.onListFetchFailure(result.getString("msg_string"));
            hideLoader();
        } catch (Exception e) {
            this.listener.onListFetchFailure(this.mContext.getResources().getString(R.string.something_went_wrong));
            hideLoader();
        }
    }

    public void onFailure(Call<ResponseBody> call, Throwable t) {
        this.listener.onListFetchFailure(this.mContext.getResources().getString(R.string.error_other_issue));
        hideLoader();
    }

    private void showLoader() {
        ProgressDialog progressDialog2 = this.progressDialog;
        if (progressDialog2 != null && !progressDialog2.isShowing()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    ProductListManager.this.progressDialog.show();
                }
            });
        }
    }

    private void hideLoader() {
        ProgressDialog progressDialog2 = this.progressDialog;
        if (progressDialog2 != null && progressDialog2.isShowing()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    ProductListManager.this.progressDialog.dismiss();
                }
            });
        }
    }
}
