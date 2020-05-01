package com.essensys.cashsaverz.networkManager;

import android.app.Activity;
import android.content.Context;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.helper.Constant;
import com.essensys.cashsaverz.model.User;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SocialLogInBackground implements retrofit2.Callback<ResponseBody> {
    private Context mContext;
    private StatusListener listener;

    public SocialLogInBackground(Context mContext, StatusListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    public interface StatusListener {
        void onDataFetchSuccess(User userDetails);
        void onDataFetchFailure(String errorMessage);
    }

    public void socialLogIn(Activity mActivity, String name, String token, String email,
                                   String facebook, String android, String s1, String s2) {
        RetrofitClient
                .getClient(Constant.ServerEndpoint.LOGIN_WITH_SOCIAL_MEDIA)
                .create(RetrofitInterfaces.SocialLogin.class)
                .post(token, facebook,email,name,android,"","")
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
                User userDetails = new Gson().fromJson(result.toString(),
                        new TypeToken<User>() {}.getType());

                listener.onDataFetchSuccess(userDetails);
            } else {

                String msg_string = result.getString("msg_string");
                listener.onDataFetchFailure(msg_string);
            }
        } catch (Exception e) {
            listener.onDataFetchFailure(mContext.getResources()
                    .getString(R.string.something_went_wrong));
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        listener.onDataFetchFailure(mContext.getResources().getString(R.string.error_other_issue));
    }
}