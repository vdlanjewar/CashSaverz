package com.essensys.cashsaverz.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.essensys.cashsaverz.helper.Constant;
import com.essensys.cashsaverz.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Shraddha on 19/12/17.
 * Manages shared preferences for the app.
 */

public class SharedPreferenceManager {
    private static SharedPreferenceManager INSTANCE;
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public static SharedPreferenceManager with(Context mContext) {
        if (INSTANCE == null) {
            INSTANCE = new SharedPreferenceManager(mContext);
        }
        return INSTANCE;
    }

    /**
     * Private constructor for instantiating the singleton.
     */
    private SharedPreferenceManager(Context mContext) {
        //It is important to store the application context
        //in order to avoid memory leaks.
        this.mContext = mContext.getApplicationContext();
        this.sharedPreferences = mContext.getSharedPreferences(Constant.Common.SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(Constant.SharedPreferences.IS_USER_LOGGED_IN, false);
    }

    public void updateLoggedInUser(User user) {
        if (user == null) {
            editor.remove(Constant.SharedPreferences.LOGGED_IN_USER);
            editor.putLong(Constant.SharedPreferences.ALERT_ADD_MOBILE_NUMBER, 0); // Added by SM
        } else {
            editor.putString(Constant.SharedPreferences.LOGGED_IN_USER, new Gson().toJson(user));
        }
        editor.commit();
    }


    public User getLoggedInUser() {
        String json = sharedPreferences.getString(Constant.SharedPreferences.LOGGED_IN_USER, null);
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, new TypeToken<User>() {
        }.getType());
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setUserLoggedIn(boolean isUserLoggedIn) {
        editor.putBoolean(Constant.SharedPreferences.IS_USER_LOGGED_IN, isUserLoggedIn);
        editor.commit();
    }

    // Added by SM
    public void setTimeForAlertMobile(long value) {
        editor.putLong(Constant.SharedPreferences.ALERT_ADD_MOBILE_NUMBER, value);
        editor.commit();
    }
    // Added by SM
    public long getTimeForAlertMobile() {
        return sharedPreferences.getLong(Constant.SharedPreferences.ALERT_ADD_MOBILE_NUMBER, 0);
    }
}
