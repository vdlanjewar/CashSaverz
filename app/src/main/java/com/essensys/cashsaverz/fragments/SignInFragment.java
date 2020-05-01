package com.essensys.cashsaverz.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.model.User;
import com.essensys.cashsaverz.networkManager.LoginManager;
import com.essensys.cashsaverz.preference.SharedPreferenceManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import es.dmoral.toasty.Toasty;

public class SignInFragment extends CustomFragment implements LoginManager.StatusListener,
        View.OnClickListener {
    private LoginManager loginManager;
    private Button btRegistration, btSignIn;
    private String deviceId = "";
    private EditText etUsername, etPassword;
    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        loginManager = new LoginManager(getActivity(), this);
        btRegistration = view.findViewById(R.id.btRegistration);
        btSignIn = view.findViewById(R.id.btSignIn);
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btSignIn.setOnClickListener(this);
        btRegistration.setOnClickListener(this);
        view.findViewById(R.id.tvForgotPassword).setOnClickListener(this); // Added by SM
        requestReadPhoneStatePermission();
        return view;
    }
    private void requestReadPhoneStatePermission() {
        String str = "android.permission.READ_PHONE_STATE";
        if (ActivityCompat.checkSelfPermission(getActivity(), str) == 0) {
            this.deviceId = HomeFragment.deviceId;
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), str)) {
            this.deviceId = HomeFragment.deviceId;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{str}, 0);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 0) {
            return;
        }
        if (grantResults.length == 1 && grantResults[0] == 0) {
            this.deviceId = HomeFragment.deviceId;
        } else {
            Toast.makeText(getActivity(), "Permission Not Granted", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onDataFetchSuccess(User userDetails) {
        if (userDetails != null) {
            onSignInSuccess(userDetails);
        }
    }

    @Override
    public void onDataFetchFailure(String errorMessage) {
        CommonUtilities.showToastOnMainThread(getActivity(), errorMessage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btSignIn:
                if (!CommonUtilities.isValidEmail(etUsername.getText().toString())) {
                    Toasty.error(activity, "Please enter valid email id",
                            Toast.LENGTH_SHORT, true).show();
                } else if (etPassword.getText().toString().equalsIgnoreCase("")) {
                    Toasty.error(activity, "Please enter password", Toast.LENGTH_SHORT,
                            true).show();
                } else {
                    loginManager.doLogin(deviceId,etUsername.getText().toString().trim(),
                            etPassword.getText().toString().trim());
                }
                break;
            case R.id.btRegistration:
                SignUpFragment signUpFragment = new SignUpFragment();
                loadSignUpFragment(signUpFragment);
                break;
            case R.id.tvForgotPassword:
                ForgotPasswordFragment passwordFragment = ForgotPasswordFragment.newInstance();
                passwordFragment.show(getActivity().getSupportFragmentManager(), "ForgotPasswordFragment");
                break;
        }
    }

    private void loadSignUpFragment(CustomFragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void onSignInSuccess(User result) {
        Toasty.success(activity, "Login Successful", Toast.LENGTH_LONG,
                true).show();
        storeUserDetails(result);
        redirectIfNeeded();
        if (getActivity() != null && !getActivity().isFinishing()) {
            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
        }

    }

    private void redirectIfNeeded() {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
    }

    private void storeUserDetails(User user) {
        try {
            SharedPreferenceManager.with(getActivity()).updateLoggedInUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {
        CommonUtilities.openFragmentContainerActivityWithDelay(activity, R.id.nav_home,
                0);
        activity.finish();
    }
}
