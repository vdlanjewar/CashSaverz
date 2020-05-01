package com.essensys.cashsaverz.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.model.CityList;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.model.DeliveryTimeSlot;
import com.essensys.cashsaverz.model.MembershipPlans;
import com.essensys.cashsaverz.model.User;
import com.essensys.cashsaverz.networkManager.FieldDetailsManager;
import com.essensys.cashsaverz.networkManager.SignupManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import es.dmoral.toasty.Toasty;
public class SignUpFragment extends CustomFragment implements SignupManager.StatusListener,
        View.OnClickListener, FieldDetailsManager.StatusListener {
    private Button btLogin, btSignUp;
    private EditText etUsername, etEmail, etContact, etPassword, etConfirmPassword;
    private AutoCompleteTextView actCity;
    private SignupManager signupManager;
    private FieldDetailsManager fieldDetailsManager;
    private ArrayAdapter<String> cityadapter;
    private String[] cities;
    private String[] city_id;
    private String selectedcityid = "";
    public SignUpFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        btLogin = view.findViewById(R.id.btLogin);
        btSignUp = view.findViewById(R.id.btSignUp);
        etUsername = view.findViewById(R.id.etUsername);
        etEmail = view.findViewById(R.id.etEmail);
        etContact = view.findViewById(R.id.etContact);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        actCity = view.findViewById(R.id.actCity);
        btLogin.setOnClickListener(this);
        btSignUp.setOnClickListener(this);
        signupManager = new SignupManager(getActivity(), this);
        fieldDetailsManager = new FieldDetailsManager(getActivity(), this);
        fieldDetailsManager.getFields();
        actCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String) adapterView.getItemAtPosition(i);
                int pos = -1;
                for (int j = 0; j < cities.length; j++) {
                    if (cities[j].equals(selection)) {
                        pos = j;
                        break;
                    }
                }
                selectedcityid = city_id[pos];
            }
        });
        return view;
    }
    @Override
    public void onDataFetchSuccess(User userDetails) {
        CommonUtilities.showToastOnMainThread(getActivity(), userDetails.getMsgString());
        if (userDetails.getMsg().equals("1")) {
            sendToLoginScreen();
        }
    }
    @Override
    public void onDataFetchFailure(String errorMessage) {
        CommonUtilities.showToastOnMainThread(getActivity(), errorMessage);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btLogin:
                sendToLoginScreen();
                break;
            case R.id.btSignUp:
                if (etUsername.getText().toString().equalsIgnoreCase(""))
                    Toasty.error(getActivity(), "Please enter user name", Toast.LENGTH_SHORT, true).show();
                else if (!CommonUtilities.isValidEmail(etEmail.getText().toString()))
                    Toasty.error(getActivity(), "Please enter valid email id", Toast.LENGTH_SHORT, true).show();
                else if (etContact.getText().toString().equalsIgnoreCase(""))
                    Toasty.error(getActivity(), "Please enter contact number", Toast.LENGTH_SHORT, true).show();
                else if (actCity.getText().toString().equalsIgnoreCase("")) {
                    Toasty.error(getActivity(), "Please select city", Toast.LENGTH_SHORT, true).show();
                } else if (etPassword.getText().toString().equalsIgnoreCase(""))
                    Toasty.error(getActivity(), "Please enter password", Toast.LENGTH_SHORT, true).show();
                else if (etConfirmPassword.getText().toString().equalsIgnoreCase(""))
                    Toasty.error(getActivity(), "Please confirm password", Toast.LENGTH_SHORT, true).show();
                else if (!etPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim()))
                    Toasty.error(getActivity(), "Password do not match", Toast.LENGTH_SHORT, true).show();
                else if (selectedcityid.equalsIgnoreCase(""))
                    Toasty.error(getActivity(), "Please select city from available list only", Toast.LENGTH_SHORT, true).show();
                else {
                    signupManager.doSignUp(etUsername.getText().toString().trim(), etPassword.getText().toString().trim(), etEmail.getText().toString().trim(), etContact.getText().toString().trim(), selectedcityid, "android-app");
                }
                break;
        }
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
    private void sendToLoginScreen() {
        SignInFragment signInFragment = new SignInFragment();
        loadFragment(signInFragment);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }
    @Override
    public void onBackPressed() {
        getActivity().finish();
    }
    @Override
    public void onFieldDataFetchSuccess(List<CityList> cityList, List<MembershipPlans> membershipPlans, List<DeliveryTimeSlot> deliveryTimeSlots) {
        setCityList(cityList);
    }
    @Override
    public void onFieldDataFetchFailure(String errorMessage) {
    }
    public void setCityList(List<CityList> cityList) {
        cities = new String[cityList.size()];
        city_id = new String[cityList.size()];
        for (int i = 0; i < cityList.size(); i++) {
            cities[i] = cityList.get(i).getDrpDwnVal();
            city_id[i] = cityList.get(i).getDrpDwnKey();
        }
        cityadapter = new ArrayAdapter<String>(getActivity(), R.layout.item_city_list, cities);
        actCity.setThreshold(2);
        actCity.setAdapter(cityadapter);
    }
}
