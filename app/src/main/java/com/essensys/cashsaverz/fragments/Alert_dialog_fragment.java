package com.essensys.cashsaverz.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.fragments.AboutUsFragment;
import com.essensys.cashsaverz.preference.SharedPreferenceManager;

import java.util.Calendar;
import java.util.Date;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class Alert_dialog_fragment extends DialogFragment {

    AlertDialog alertDialog;

    public static Alert_dialog_fragment newInstance() {
        Alert_dialog_fragment f = new Alert_dialog_fragment();
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Create the AlertDialog object and return it*/
        alertDialog = builder.create();
        ShowVersionControlDialog();
        return alertDialog;
    }

    public void ShowVersionControlDialog() {
        View vAlertDialogVersionControl = getActivity().getLayoutInflater().inflate(
                R.layout.alert_dialog_layout, null);
        TextView dialogTitle = (TextView) vAlertDialogVersionControl
                .findViewById(R.id.version_control_alert_dialog_title_textView);
        TextView dialogContent = (TextView) vAlertDialogVersionControl
                .findViewById(R.id.version_control_alert_dialog_content_textView);
        TextView dialogExit = (TextView) vAlertDialogVersionControl
                .findViewById(R.id.version_control_alert_dialog_exit_option_textView);
        TextView dialogUpdate = (TextView) vAlertDialogVersionControl
                .findViewById(R.id.version_control_alert_dialog_update_option_textView);
        dialogExit.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
                1f));
        dialogUpdate.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 1f));



        dialogTitle.setText("Please add your mobile number to avail full service");
        dialogContent.setText("Please add your mobile number to avail service" );
        dialogExit.setText("Later");
        dialogUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alertDialog.hide();
                ShowAccountFragment();
            }
        });
        dialogExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                setAlertTimeForMobileNumber();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setView(vAlertDialogVersionControl);
        alertDialog.show();
    }

    // Added to set time arrival for alert by SM
    private void setAlertTimeForMobileNumber() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.HOUR,48);
            SharedPreferenceManager.with(getActivity()).setTimeForAlertMobile(calendar.getTimeInMillis());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void ShowAccountFragment() {
        MyProfileFragment myProfileFragment=new MyProfileFragment();
        loadFragment(myProfileFragment);
    }
}
