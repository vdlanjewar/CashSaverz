package com.essensys.cashsaverz.networkManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.CartActivity;
import com.essensys.cashsaverz.fragments.HomeFragment;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Pavan on 19-09-2018.
 */

public class AddToCartManager implements retrofit2.Callback<ResponseBody> {

    private Context mContext;
    private StatusListener listener;
    private ProgressDialog progressDialog;

    public AddToCartManager(Context mContext, StatusListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        this.progressDialog = CommonUtilities.getDefaultLoader(mContext);
    }

    public interface StatusListener {
        void onDataFetchSuccess(String successMessage, String total_cart_items);

        void onDataFetchFailure(String errorMessage, String total_cart_items);
    }

    public void addToCart(String product_id) {
        String customerId,deviceId;
        showLoader();
        if (App.isUserLoggedIn(this.mContext)) {
            deviceId = "";
            customerId = App.getCurrentUser(this.mContext).getCustomerId();
        } else {
            deviceId = HomeFragment.deviceId;
            customerId = "";
        }
        RetrofitClient
                .getClient(Constant.ServerEndpoint.ADD_TO_CART)
                .create(RetrofitInterfaces.AddToCart.class)
                .post(customerId,product_id,deviceId)
                .enqueue(this);
    }

    public void removeFromCart(String cartId, boolean removeAll) {
        String customerId,deviceId;
        showLoader();
        if (App.isUserLoggedIn(this.mContext)) {
            deviceId = "";
            customerId = App.getCurrentUser(this.mContext).getCustomerId();
        } else {
            deviceId = HomeFragment.deviceId;
            customerId = "";
        }
        RetrofitClient
                .getClient(Constant.ServerEndpoint.REMOVE_CART_ITEM)
                .create(RetrofitInterfaces.RemoveFromCart.class)
                .post(customerId, cartId,deviceId, removeAll)
                .enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            String stringResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(stringResponse);
            JSONObject result = jsonObject.getJSONObject("result");
            String msg = result.getString("msg");
            String total_cart_items;
            if (msg.contentEquals("1")) {
                hideLoader();
                String msg_string = result.getString("msg_string");
                total_cart_items = result.getString("cartItemCount");
//                Intent intent=new Intent(mContext, CartActivity.class);
//                mContext.startActivity(intent);

                listener.onDataFetchSuccess(msg_string, total_cart_items);
            } else {
                hideLoader();
                String msg_string = result.getString("msg_string");
                total_cart_items = result.getString("cartItemCount");
                listener.onDataFetchFailure(msg_string, total_cart_items);
            }
        } catch (Exception e) {
            listener.onDataFetchFailure(mContext.getResources().getString(R.string.something_went_wrong), "");
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        listener.onDataFetchFailure(mContext.getResources().getString(R.string.error_other_issue), "");

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
