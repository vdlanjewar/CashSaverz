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
import com.essensys.cashsaverz.model.CityList;
import com.essensys.cashsaverz.model.DeliveryTimeSlot;
import com.essensys.cashsaverz.model.MembershipPlans;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Pavan on 24-08-2018.
 */

public class FieldDetailsManager implements retrofit2.Callback<ResponseBody> {

    private Context mContext;
    private StatusListener listener;
    private ProgressDialog progressDialog;
    private String customerID;

    public FieldDetailsManager(Context mContext, StatusListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        this.progressDialog = CommonUtilities.getDefaultLoader(mContext);
    }

    public interface StatusListener {
        void onFieldDataFetchSuccess(List<CityList> cityList,List<MembershipPlans> membershipPlans, List<DeliveryTimeSlot> deliveryTimeSlots);

        void onFieldDataFetchFailure(String errorMessage);
    }

    public void getFields() {
        if(App.isUserLoggedIn(mContext)){
            customerID = App.getCurrentUser(mContext).getCustomerId();
        }else {
             customerID = "";
        }
        showLoader();
        RetrofitClient
                .getClient(Constant.ServerEndpoint.GET_FIELDS)
                .create(RetrofitInterfaces.GetFieldDetails.class)
                .post(customerID)
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
                JSONArray arrDrpDwnActiveCityList = result.getJSONArray("arrDrpDwnActiveCityList");
                JSONArray membershipPlans = result.getJSONArray("arrMembershipPlanList");
                JSONArray deliveryTimeSlot = result.getJSONArray("arrDeliveryTimeSlot");

                List<CityList> cityList = new Gson()
                        .fromJson(arrDrpDwnActiveCityList.toString(), new TypeToken<List<CityList>>() {
                        }.getType());
                List<MembershipPlans> membershipplans = new Gson()
                        .fromJson(membershipPlans.toString(), new TypeToken<List<MembershipPlans>>() {
                        }.getType());
                List<DeliveryTimeSlot> deliverytimeslot = new Gson()
                        .fromJson(deliveryTimeSlot.toString(), new TypeToken<List<DeliveryTimeSlot>>() {
                        }.getType());
                listener.onFieldDataFetchSuccess(cityList, membershipplans, deliverytimeslot);
                hideLoader();
            } else {

                String msg_string = result.getString("msg_string");
                listener.onFieldDataFetchFailure(msg_string);
                hideLoader();
            }
        } catch (Exception e) {
            listener.onFieldDataFetchFailure(mContext.getResources().getString(R.string.something_went_wrong));
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        listener.onFieldDataFetchFailure(mContext.getResources().getString(R.string.error_other_issue));

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
