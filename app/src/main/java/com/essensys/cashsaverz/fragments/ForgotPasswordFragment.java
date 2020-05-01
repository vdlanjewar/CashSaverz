package com.essensys.cashsaverz.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces;

import org.json.JSONObject;

import java.util.Calendar;

import androidx.fragment.app.DialogFragment;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordFragment extends DialogFragment implements Callback<ResponseBody> {

    AlertDialog alertDialog;
    EditText etEmail;

    public static ForgotPasswordFragment newInstance() {
        ForgotPasswordFragment f = new ForgotPasswordFragment();
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
                R.layout.alert_dialog_forgot_password, null);
        TextView dialogTitle = vAlertDialogVersionControl.findViewById(R.id.tvTitle);
         etEmail = vAlertDialogVersionControl.findViewById(R.id.etEmail);
       // TextView dialogContent = (TextView) vAlertDialogVersionControl
       //         .findViewById(R.id.version_control_alert_dialog_content_textView);
        TextView dialogExit = vAlertDialogVersionControl.findViewById(R.id.tvCancel);
        TextView dialogUpdate = vAlertDialogVersionControl.findViewById(R.id.tvSubmit);
        dialogExit.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
                1f));
        dialogUpdate.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 1f));



        dialogTitle.setText("Forgot Password");
        dialogExit.setText("Cancel");
        dialogUpdate.setText("Submit");
        dialogUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //alertDialog.dismiss();
                sendData();
            }
        });
        dialogExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setView(vAlertDialogVersionControl);
        alertDialog.show();
    }

    private void sendData() {
        String email = etEmail.getText().toString();

         if (!CommonUtilities.isValidEmail(etEmail.getText().toString()))
            Toasty.error(getActivity(), "Please enter valid email id", Toast.LENGTH_SHORT,
                    true).show();
        else {
            RetrofitClient
                    .getClient(Constant.ServerEndpoint.FORGOT_PASSWORD)
                    .create(RetrofitInterfaces.ForgotPassword.class)
                    .post(email)
                    .enqueue(this);
        }

    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            String stringResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(stringResponse);
            JSONObject result = jsonObject.getJSONObject("result");
            String msg = result.getString("msg");
            String msg_string = result.getString("msg_string");

            if (msg.contentEquals("1")) {
                Toast.makeText(getActivity(), msg_string, Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }else
                Toast.makeText(getActivity(), msg_string, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }
}
