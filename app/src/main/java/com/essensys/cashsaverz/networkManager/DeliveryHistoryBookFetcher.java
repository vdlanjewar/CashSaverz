package com.essensys.cashsaverz.networkManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant;
import com.essensys.cashsaverz.model.UserDeliveryBookList;
import com.essensys.cashsaverz.model.UserDeliveryHistoryBookList;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class DeliveryHistoryBookFetcher implements retrofit2.Callback<ResponseBody> {
    private Context mContext;
    private StatusListener listener;
    private ProgressDialog progressDialog;
    public DeliveryHistoryBookFetcher(Context mContext, StatusListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        this.progressDialog = CommonUtilities.getDefaultLoader(mContext);
    }

    public interface StatusListener {
        void onListFetchSuccess(List<UserDeliveryHistoryBookList> userDeliveryHistoryBookLists);

        void onListFetchFailure(String errorMsg);
    }

    public void fetchDeliveryHistoryBookList(int offset) {
        showLoader();
        RetrofitClient
                .getClient(Constant.ServerEndpoint.FETCH_DELIVERY_HISTORY_BOOK_LIST)
                .create(RetrofitInterfaces.FetchDeliveryHistoryBookList.class)
                .post(App.getCurrentUser(mContext).getUserAuth(),App.getCurrentUser(mContext).getCustomerId(),""+offset)
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
                JSONArray products = result.getJSONArray("deliveryPersonRentedBookHistory");

                List<UserDeliveryHistoryBookList> userDeliveryHistoryBookLists = new Gson()
                        .fromJson(products.toString(), new TypeToken<List<UserDeliveryHistoryBookList>>() {
                        }.getType());

                listener.onListFetchSuccess(userDeliveryHistoryBookLists);

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
