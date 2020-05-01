package com.essensys.cashsaverz.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.HomeActivity;

import java.util.Random;

public class CommonUtilities {

    public static int getRandomNumber(int max) {
        Random rand = new Random();
        return rand.nextInt(max) + 1;
    }

    public static String getAppVersionName(Context context) {
        String version = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "Version "+version;
    }

    public static void showToastOnMainThread(final Context mContext, final String msg) {
        if (mContext == null) {
            return;
        }
        final Context appContext = mContext.getApplicationContext();
        if (appContext == null) {
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void openFragmentContainerActivityWithDelay(final Context mContext,
                                                              final int navViewId,
                                                              final int navItemIndex) {
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.putExtra(Constant.IntentExtras.NAV_VIEW_ID, navViewId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }

    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static ProgressDialog getDefaultLoader(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getString(R.string.loading));
        return progressDialog;
    }

    public static String addressFormatter(String name, String address, String landmark,
                                          String locality, String cscname, String pincode,
                                          String mobilenumber, String alternate_mobile) {
        String formattedAddress;
        if(landmark.equals("") || landmark==null)
        {
            formattedAddress=name + ", \n" + address + ", " +locality + ", \n" +
                    cscname + " - " + pincode + " \n" + mobilenumber + ", " + alternate_mobile;
        }
        else
        {
            formattedAddress=name + ", \n" + address + ", " + landmark + ", " + locality + ", \n" +
                    cscname + " - " + pincode + " \n" + mobilenumber + ", " + alternate_mobile;
        }
        return formattedAddress;
    }
}
