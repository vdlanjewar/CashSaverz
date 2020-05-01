package com.essensys.cashsaverz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends CustomFragment implements View.OnClickListener, Callback<ResponseBody> {
    private EditText etCurrentPassword, etNewPass, etRe_enterNewPass;
    private Button btnChangePassword;
    private TextInputLayout tilConfirmPassword, tilNewPassword;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        etCurrentPassword = view.findViewById(R.id.etCurrentPassword);
        etNewPass = view.findViewById(R.id.etNewPass);
        etRe_enterNewPass = view.findViewById(R.id.etRe_enterNewPass);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(this);
        tilConfirmPassword = view.findViewById(R.id.tilConfirmPassword);
        tilNewPassword = view.findViewById(R.id.tilNewPassword);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    public void onBackPressed() {
        CommonUtilities.openFragmentContainerActivityWithDelay(getActivity(), R.id.nav_home,
                0);
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnChangePassword:

                String currentPassword = etCurrentPassword.getText().toString();
                String newPassword = etNewPass.getText().toString();
                String confirmPassword = etRe_enterNewPass.getText().toString();
                if (currentPassword.equals("")) {
                    Toast.makeText(getActivity(), "Please enter the Password.", Toast.LENGTH_SHORT).show();
                } else if (newPassword.length() < 6) {
                    tilNewPassword.setError("Not a valid password!");
                } else if (confirmPassword.equals("")) {
                    Toast.makeText(getActivity(), "Please enter retype password.", Toast.LENGTH_SHORT).show();
                } else if (confirmPassword.length() < 6) {
                    tilConfirmPassword.setError("Not a valid password!");
                } else if (!confirmPassword.equals(newPassword)) {
                    Toast.makeText(getActivity(), "Entered retype password doesn't match with password.", Toast.LENGTH_SHORT).show();
                } else {
                    changePassword(currentPassword, newPassword);
                }
                break;
        }

    }

    private void changePassword(String currentPassword, String newPassword) {
        RetrofitClient
                .getClient(Constant.ServerEndpoint.CHANGE_PASSSWORD)
                .create(RetrofitInterfaces.ChangePassword.class)
                .post(App.getCurrentUser(getActivity()).getUserAuth(),App.getCurrentUser(getActivity()).getCustomerId(), currentPassword, newPassword)
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
                String msg_string = result.getString("msg_string");
                CommonUtilities.showToastOnMainThread(getActivity(), msg_string);
                CommonUtilities.openFragmentContainerActivityWithDelay(getActivity(), R.id.nav_home,
                        0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setupBadge(HomeActivity.getCartCount());
    }
}
