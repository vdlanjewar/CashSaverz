package com.essensys.cashsaverz.networkManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant;
import com.essensys.cashsaverz.model.UserAddressList;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Pavan on 24-08-2018.
 */

public class AddressManager implements retrofit2.Callback<ResponseBody> {

    private Context mContext;
    private StatusListener listener;
    private ProgressDialog progressDialog;

    public AddressManager(Context mContext, StatusListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        this.progressDialog = CommonUtilities.getDefaultLoader(mContext);
    }

    public interface StatusListener {
        void onDataFetchSuccess(List<UserAddressList> userAddressList);

        void onDataFetchFailure(String errorMessage);
    }

    public void addNewAddress(String name, String mobile, String pincode, String locality, String address, String landmark, String alternateMobile, String city, String addressType) {
        showLoader();
        RetrofitClient
                .getClient(Constant.ServerEndpoint.ADD_NEW_ADDRESS)
                .create(RetrofitInterfaces.AddNewAddress.class)
                .post(App.getCurrentUser(mContext).getUserAuth(),App.getCurrentUser(mContext).getCustomerId(), name, mobile, pincode, locality, address, landmark, alternateMobile, city, addressType)
                .enqueue(this);
    }

    public void editAddress(String addressid, String name, String mobile, String pincode, String locality, String address, String landmark, String alternateMobile, String city, String addressType) {
        showLoader();
        RetrofitClient
                .getClient(Constant.ServerEndpoint.EDIT_ADDRESS)
                .create(RetrofitInterfaces.EditAddress.class)
                .post(App.getCurrentUser(mContext).getUserAuth(),App.getCurrentUser(mContext).getCustomerId(),
                        addressid, name, mobile, pincode, locality, address, landmark, alternateMobile, city, addressType)
                .enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            hideLoader();
            String stringResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(stringResponse);
            JSONObject result = jsonObject.getJSONObject("result");
            String msg = result.getString("msg");

            if (msg.contentEquals("1")) {
                JSONArray arrUserAddressList = result.getJSONArray("arrUserAddressList");

                List<UserAddressList> addresslist = new Gson()
                        .fromJson(arrUserAddressList.toString(), new TypeToken<List<UserAddressList>>() {
                        }.getType());
                listener.onDataFetchSuccess(addresslist);


            } else {

                String msg_string = result.getString("msg_string");
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
