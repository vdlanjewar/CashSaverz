package com.essensys.cashsaverz;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.essensys.cashsaverz.model.User;
import com.essensys.cashsaverz.paymentGateway.AppEnvironment;
import com.essensys.cashsaverz.preference.SharedPreferenceManager;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Shraddha on 29/12/17.
 */

public class App extends Application {

    private static User currentUser;
    AppEnvironment appEnvironment;


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        appEnvironment = AppEnvironment.SANDBOX;

//        final String token = FirebaseInstanceId.getInstance().getToken();
//        FCMTokenGeneratorService.copyToken(getApplicationContext(), token);
    }

    public static void setCurrentUser(User currentUser) {
        App.currentUser = currentUser;
    }

    public static User getCurrentUser(Context mContext) {
        if (currentUser == null) {
            currentUser = SharedPreferenceManager.with(mContext).getLoggedInUser();
        }
        return currentUser;
    }

    public static boolean isUserLoggedIn(Context mContext) {
        return getCurrentUser(mContext) != null;
    }

    public AppEnvironment getAppEnvironment() {
        return appEnvironment;
    }

    public void setAppEnvironment(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
    }
}
