package com.essensys.cashsaverz.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.adapter.AddressListAdapter;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.model.CityList;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.model.DeliveryTimeSlot;
import com.essensys.cashsaverz.model.MembershipPlans;
import com.essensys.cashsaverz.model.User;
import com.essensys.cashsaverz.model.UserAddressList;
import com.essensys.cashsaverz.networkManager.AddressManager;
import com.essensys.cashsaverz.networkManager.BasicInfoManager;
import com.essensys.cashsaverz.networkManager.FieldDetailsManager;
import com.essensys.cashsaverz.preference.SharedPreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class MyProfileFragment extends CustomFragment implements View.OnClickListener,
        AddressManager.StatusListener, BasicInfoManager.StatusListener,
        FieldDetailsManager.StatusListener {

    private boolean iseditaddressvisible = false;
    private EditText etUserName, etUserEmail, etUserMobile;
    private EditText etName, etMobile, etPincode, etLocality, etAddress, etLandMark,
            etAlternateMobile;
    private TextView tvEditButton, tvUserName, tvPlanName, tvExpiry, tvAddAddress;
    private CardView cvMembershipDetails;
    private RadioButton rbtHome, rbtWork;
    private RadioGroup radioGroup;
    private AutoCompleteTextView actCity;
    private RecyclerView rvAddress;
    private LinearLayout llAllDetails, llEditAddress, llinflatelayout;
    private ArrayAdapter<String> cityadapter;
    private String addressType = "Home";
    private String selectedcityid = "";
    private Button btSaveAddress, btGoBack;
    private List addresses;
    private String[] cities;
    private String[] city_id;
    private AddressManager addressManager;
    private BasicInfoManager basicInfoManager;
    private FieldDetailsManager fieldDetailsManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        etUserName = view.findViewById(R.id.etName);
        etUserEmail = view.findViewById(R.id.etEmail);
        etUserMobile = view.findViewById(R.id.etMobile);
        tvEditButton = view.findViewById(R.id.tvEditButton);
        rvAddress = view.findViewById(R.id.rvAddress);
        llAllDetails = view.findViewById(R.id.llAllDetails);
        llEditAddress = view.findViewById(R.id.llEditAddress);

        tvAddAddress = view.findViewById(R.id.tvAddAddress);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvPlanName = view.findViewById(R.id.tvPlanName);
        tvExpiry = view.findViewById(R.id.tvExpiry);
        cvMembershipDetails = view.findViewById(R.id.cvMembershipDetails);
        setMembershipPlanDetails();
        addressManager = new AddressManager(getActivity(), this);
        basicInfoManager = new BasicInfoManager(getActivity(), this);
        fieldDetailsManager = new FieldDetailsManager(getActivity(), this);
        fieldDetailsManager.getFields();

        RecyclerView.LayoutManager popularbook_layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvAddress.setLayoutManager(popularbook_layoutManager);
        rvAddress.setNestedScrollingEnabled(false);
        rvAddress.setHasFixedSize(true);

        if (!App.isUserLoggedIn(getActivity())) {
            CommonUtilities.openFragmentContainerActivityWithDelay(getActivity(), R.id.nav_login,
                    0);
        }
        setBasicDetails();
        tvEditButton.setOnClickListener(this);
        tvAddAddress.setOnClickListener(this);
        return view;
    }

    public void setBasicDetails() {
        etUserName.setText(App.getCurrentUser(getActivity()).getUserName());
        etUserMobile.setText(App.getCurrentUser(getActivity()).getUserPhone());
        etUserEmail.setText(App.getCurrentUser(getActivity()).getUserEmail());
        rvAddress.setAdapter(new AddressListAdapter(getActivity(), App.getCurrentUser(getActivity())
                .getArrUserAddressList(), this));
    }


    public void showEditAddressView(final boolean isEditedAddress, boolean status, final String addressid, String name, String address, String landmark,
                                    String locality, String cscname, String pincode,
                                    String mobilenumber, String alternate_mobile, final String cityid, String addresstype) {

        if (status) {
            llinflatelayout = (LinearLayout) View.inflate(getActivity(), R.layout.layout_address,
                    null);
            llEditAddress.addView(llinflatelayout);
            etName = llinflatelayout.findViewById(R.id.etName);
            etMobile = llinflatelayout.findViewById(R.id.etMobile);
            etPincode = llinflatelayout.findViewById(R.id.etPincode);
            etLocality = llinflatelayout.findViewById(R.id.etLocality);
            etAddress = llinflatelayout.findViewById(R.id.etAddress);
            etLandMark = llinflatelayout.findViewById(R.id.etLandMark);
            radioGroup = llinflatelayout.findViewById(R.id.radioGroup);
            rbtHome = llinflatelayout.findViewById(R.id.rbtHome);
            rbtWork = llinflatelayout.findViewById(R.id.rbtWork);
            etAlternateMobile = llinflatelayout.findViewById(R.id.etAlternateMobile);
            actCity = llinflatelayout.findViewById(R.id.actCity);
            actCity.setThreshold(2);
            actCity.setAdapter(cityadapter);
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

            setValuesToEdit(name, address, landmark, locality, cscname, pincode, mobilenumber, alternate_mobile, cityid, addresstype);
            btSaveAddress = llinflatelayout.findViewById(R.id.btSaveAddress);
            btGoBack = llinflatelayout.findViewById(R.id.btGoBack);
            btGoBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEditAddressView(true,false, "", "", "", "",
                            "", "", "", "", "",
                            "", "");
                }
            });
            btSaveAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveAddressDetails(isEditedAddress,addressid);
                }
            });
            iseditaddressvisible = true;
            llAllDetails.setVisibility(View.GONE);
        } else {
            iseditaddressvisible = false;
            llEditAddress.removeView(llinflatelayout);
            llAllDetails.setVisibility(View.VISIBLE);
        }

    }

    public void setValuesToEdit(String name, String address, String landmark, String locality,
                                String cscname, String pincode, String mobilenumber,
                                String alternate_mobile, String cityid, String addresstype) {
        etName.setText(name);
        etMobile.setText(mobilenumber);
        etPincode.setText(pincode);
        etLocality.setText(locality);
        etAddress.setText(address);
        etLandMark.setText(landmark);
        etAlternateMobile.setText(alternate_mobile);
        actCity.setText(cscname);
        if (addresstype.equals("Home")) {
            radioGroup.clearCheck();
            rbtHome.setChecked(true);
            rbtWork.setChecked(false);
        } else if (addresstype.equals("Work")) {
            radioGroup.clearCheck();
            rbtHome.setChecked(false);
            rbtWork.setChecked(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setFieldsEditable(boolean status) {
        if (status) {
            tvEditButton.setText("Save");
            tvEditButton.setBackground(getResources().getDrawable(R.drawable.edittext_yellowbg));
            etUserName.setEnabled(true);
            etUserEmail.setEnabled(true);
            etUserMobile.setEnabled(true);
        } else {
            tvEditButton.setText("Edit");
            tvEditButton.setBackground(getResources().getDrawable(R.drawable.edittext_primary));
            etUserName.setEnabled(false);
            etUserEmail.setEnabled(false);
            etUserMobile.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvEditButton:
                if (tvEditButton.getText().toString().equals("Edit")) {
                    setFieldsEditable(true);
                } else {
                    if (etUserName.getText().toString().equalsIgnoreCase(""))
                        Toasty.error(getActivity(), "Please enter name", Toast.LENGTH_SHORT,
                                true).show();
                    else if (!CommonUtilities.isValidEmail(etUserEmail.getText().toString()))
                        Toasty.error(getActivity(), "Please enter valid email id",
                                Toast.LENGTH_SHORT, true).show();
                    else if (etUserMobile.getText().toString().equalsIgnoreCase(""))
                        Toasty.error(getActivity(), "Please enter mobile number",
                                Toast.LENGTH_SHORT, true).show();
                    else {
                        saveBasicDetails(etUserName.getText().toString().trim(),
                                etUserMobile.getText().toString().trim(),
                                etUserEmail.getText().toString().trim());
                    }
                    setFieldsEditable(false);
                }
                break;
            case R.id.tvAddAddress:
                showEditAddressView(false,true,"","","","","","",
                "","","","","");
                break;
        }
    }

    private void saveAddressDetails(boolean isEditedAddress, String addressid) {
        if (etName.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Name cannot be blank",
                    Toast.LENGTH_SHORT).show();
        } else if (etMobile.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Mobile number cannot be blank",
                    Toast.LENGTH_SHORT).show();
        } else if (etPincode.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Pincode cannot be blank",
                    Toast.LENGTH_SHORT).show();
        } else if (etLocality.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Locality cannot be blank",
                    Toast.LENGTH_SHORT).show();
        } else if (etAddress.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Address cannot be blank",
                    Toast.LENGTH_SHORT).show();
        } else if (actCity.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please select city",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (rbtHome.isChecked()) {
                addressType = "Home";
            } else {
                addressType = "Work";
            }

            if(isEditedAddress)
            {
                addressManager.editAddress(addressid,
                        etName.getText().toString().trim(),
                        etMobile.getText().toString().trim(),
                        etPincode.getText().toString().trim(),
                        etLocality.getText().toString().trim(),
                        etAddress.getText().toString().trim(),
                        etLandMark.getText().toString().trim(),
                        etAlternateMobile.getText().toString().trim(),
                        selectedcityid, addressType
                );
            }
            else
                {
                    addressManager.addNewAddress(etName.getText().toString().trim(),
                            etMobile.getText().toString().trim(),
                            etPincode.getText().toString().trim(),
                            etLocality.getText().toString().trim(),
                            etAddress.getText().toString().trim(),
                            etLandMark.getText().toString().trim(),
                            etAlternateMobile.getText().toString().trim(),
                            selectedcityid, addressType
                    );
                }


        }
    }

    public void saveBasicDetails(String name, String mobile, String email) {
        basicInfoManager.editBasicInfo(name, mobile, email);
    }

    //Response from AddressManager
    @Override
    public void onDataFetchSuccess(List<UserAddressList> userAddressList) {
        App.getCurrentUser(getActivity()).setArrUserAddressList(userAddressList);
        storeUserDetails(App.getCurrentUser(getActivity()));
        rvAddress.setAdapter(new AddressListAdapter(getActivity(), App.getCurrentUser(getActivity()).getArrUserAddressList(), this));
        iseditaddressvisible = false;
        llEditAddress.removeView(llinflatelayout);
        llAllDetails.setVisibility(View.VISIBLE);
    }

    //Response from BasicInfoManager
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onDataFetchSuccess(String msg) {
        App.getCurrentUser(getActivity()).setUserName(etUserName.getText().toString());
        App.getCurrentUser(getActivity()).setUserEmail(etUserEmail.getText().toString());
        App.getCurrentUser(getActivity()).setUserPhone(etUserMobile.getText().toString());
        storeUserDetails(App.getCurrentUser(getActivity()));
        setFieldsEditable(false);
        Toast.makeText(getActivity(), "Profile details updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataFetchFailure(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void storeUserDetails(User user) {
        try {
            SharedPreferenceManager.with(getActivity()).updateLoggedInUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCityList(List<CityList> cityList) {
        cities = new String[cityList.size()];
        city_id = new String[cityList.size()];
        for (int i = 0; i < cityList.size(); i++) {
            cities[i] = cityList.get(i).getDrpDwnVal();
            city_id[i] = cityList.get(i).getDrpDwnKey();
        }
        cityadapter = new ArrayAdapter<String>(getActivity(), R.layout.item_city_list, cities);
    }

    public void setMembershipPlanDetails() {
        if (App.getCurrentUser(getActivity()).getUserCurrentMembershipPlan() != null) {
            cvMembershipDetails.setVisibility(View.VISIBLE);
            tvUserName.setText("Hello " + App.getCurrentUser(getActivity()).getUserName());
            tvPlanName.setText(App.getCurrentUser(getActivity()).getUserCurrentMembershipPlan()
                    .get(0).getMembershipPlanTitle());

                tvExpiry.setText("And your membership expires on "
                        + App.getCurrentUser(getActivity()).getUserCurrentMembershipPlan()
                        .get(0).getMembershipEndDate()
                        + ".\nYou will informed via email and can renew only after expiry.");


        } else {
            cvMembershipDetails.setVisibility(View.GONE);
        }
    }

    //Response from FieldDetailsManager
    @Override
    public void onFieldDataFetchSuccess(List<CityList> cityList, List<MembershipPlans> membershipPlans, List<DeliveryTimeSlot> deliveryTimeSlots) {
        setCityList(cityList);
    }

    @Override
    public void onFieldDataFetchFailure(String errorMessage) {
        CommonUtilities.showToastOnMainThread(getActivity(), errorMessage);
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
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setupBadge(HomeActivity.getCartCount());
    }
    public static String addDays(String sdate, int days) {

        Date date = null;
        try {
            date=new SimpleDateFormat("dd/MM/yyyy").parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime().toString();
    }
}
