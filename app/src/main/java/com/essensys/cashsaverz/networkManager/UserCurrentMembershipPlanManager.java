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

public class UserCurrentMembershipPlanManager implements retrofit2.Callback<ResponseBody> {
    private Context mContext;
    private StatusListener listener;
    private ProgressDialog progressDialog;

    public UserCurrentMembershipPlanManager(Context mContext, StatusListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        this.progressDialog = CommonUtilities.getDefaultLoader(mContext);
    }

    public interface StatusListener {
        void onUserMembershipPlanFetchSuccess(List<UserCurrentMembershipPlan> userCurrentMembershipPlanList);

        void onUserMembershipPlanFetchFailure(String errorMsg);
    }

    public void fetchUserActiveMembershipPlan() {
        showLoader();
        String customerID;
        String userAuth;
        if (!App.isUserLoggedIn(mContext)) {
            customerID = "";
            userAuth = "";
        } else {
            customerID = App.getCurrentUser(mContext).getCustomerId();
            userAuth = App.getCurrentUser(mContext).getUserAuth();
        }
        RetrofitClient
                .getClient(Constant.ServerEndpoint.GET_USER_ACTIVE_MEMBERSHIP_PLAN)
                .create(RetrofitInterfaces.GetActiveMembershipPlan.class)
                .post(userAuth,customerID)
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
                JSONArray userCurrentMembershipPlan = result.getJSONArray("userCurrentMembershipPlan");

                List<UserCurrentMembershipPlan> userCurrentMembershipPlanList = new Gson()
                        .fromJson(userCurrentMembershipPlan.toString(), new TypeToken<List<UserCurrentMembershipPlan>>() {
                        }.getType());

                listener.onUserMembershipPlanFetchSuccess(userCurrentMembershipPlanList);

            } else {
                hideLoader();
                String msg_string = result.getString("msg_string");
                listener.onUserMembershipPlanFetchFailure(msg_string);
            }
        } catch (Exception e) {
            hideLoader();
            listener.onUserMembershipPlanFetchFailure(mContext.getResources().getString(R.string.something_went_wrong));
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        hideLoader();
        listener.onUserMembershipPlanFetchFailure(mContext.getResources().getString(R.string.error_other_issue));
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
