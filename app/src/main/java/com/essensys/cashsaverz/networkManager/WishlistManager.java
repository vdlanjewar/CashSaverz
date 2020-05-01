package com.essensys.cashsaverz.networkManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
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

public class WishlistManager implements retrofit2.Callback<ResponseBody> {

    private Context mContext;
    private StatusListener listener;
    private ProgressDialog progressDialog;
    public WishlistManager(Context mContext, StatusListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        this.progressDialog = CommonUtilities.getDefaultLoader(mContext);
    }

    public interface StatusListener {
        void onDataFetchSuccess(String successMessage);

        void onDataFetchFailure(String errorMessage);
    }

    public void updateWishlist(String product_id) {
        RetrofitClient
                .getClient(Constant.ServerEndpoint.UPDATE_WISHLIST)
                .create(RetrofitInterfaces.UpdateWishlist.class)
                .post(App.getCurrentUser(mContext).getUserAuth(),App.getCurrentUser(mContext).getCustomerId(),product_id)
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
                String msg_string = result.getString("flag_wishlist_msg");
                listener.onDataFetchSuccess(msg_string);
            } else {

                String msg_string = result.getString("flag_wishlist_msg");
                listener.onDataFetchFailure(msg_string);
            }
        } catch (Exception e) {
            listener.onDataFetchFailure(mContext.getResources().getString(R.string.something_went_wrong));
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        listener.onDataFetchFailure(mContext.getResources().getString(R.string.error_other_issue));

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
