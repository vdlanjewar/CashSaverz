package com.essensys.cashsaverz.networkManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.model.User;
import com.essensys.cashsaverz.preference.SharedPreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import es.dmoral.toasty.Toasty;

/**
 * Created by Shraddha on 22-08-2018.
 */

public class FacebookLogInManager implements FacebookCallback<LoginResult>,
        GraphRequest.GraphJSONObjectCallback {

    private Activity mActivity;
    private CallbackManager callbackManager;

    public FacebookLogInManager(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void startLogin() {
        callbackManager = CallbackManager.Factory.create();
        AccessToken.setCurrentAccessToken(null);
        LoginManager.getInstance().registerCallback(callbackManager, this);
        LoginManager.getInstance().logInWithReadPermissions(mActivity,
                Arrays.asList("public_profile,email"));
    }

    public void startLogOut() {
        LoginManager.getInstance().logOut();
    }

    /**
     * Called when login is successful via the facebook popup.
     * We need to query Graph API again for user details.
     */
    @Override
    public void onSuccess(LoginResult loginResult) {
        handleLogInResult(loginResult);
    }


    private void handleLogInResult(LoginResult loginResult) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                this);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {
        onLoginFailed();
    }

    private void onLoginFailed() {
        CommonUtilities.showToastOnMainThread(mActivity, "Login error");
    }

    @Override
    public void onCompleted(JSONObject object, GraphResponse response) {
        String name = "";
        try {
            name = object.getString("name");
        } catch (JSONException e) {
            //ignore
        }
        String email = "";
        try {
            email = object.getString("email");
        } catch (JSONException e) {
            //ignore
        }
        String id = "";
        try {
            id = object.getString("id");
        } catch (JSONException e) {
            //ignore
        }

        String token = response.getRequest().getAccessToken().getToken();

        new SocialLogInBackground(mActivity, new SocialLogInBackground.StatusListener() {
            @Override
            public void onDataFetchSuccess(User userDetails) {
                onSignInSuccess(userDetails);
            }

            @Override
            public void onDataFetchFailure(String errorMessage) {

            }
        }).socialLogIn(mActivity, name, token, email,
                "Facebook", "android-app", "", "");
        SharedPreferenceManager.with(mActivity).setUserLoggedIn(true);

    }

    private void onSignInSuccess(User result) {
        Toasty.success(mActivity, "Login Successful", Toast.LENGTH_LONG,
                true).show();
        storeUserDetails(result);
        if (mActivity != null && !mActivity.isFinishing()) {
            mActivity.startActivity(new Intent(mActivity, HomeActivity.class));
            mActivity.finish();
        }

    }

    private void storeUserDetails(User user) {
        try {
            SharedPreferenceManager.with(mActivity).updateLoggedInUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }
}
