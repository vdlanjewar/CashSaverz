package com.essensys.cashsaverz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.localDatabases.SqliteDatabaseHandler;
import com.essensys.cashsaverz.model.CityList;
import com.essensys.cashsaverz.model.DeliveryTimeSlot;
import com.essensys.cashsaverz.model.MembershipPlans;
import com.essensys.cashsaverz.model.User;
import com.essensys.cashsaverz.model.UserAddressList;
import com.essensys.cashsaverz.networkManager.AddToCartManager;
import com.essensys.cashsaverz.networkManager.AddressManager;
import com.essensys.cashsaverz.networkManager.FieldDetailsManager;
import com.essensys.cashsaverz.networkManager.FieldDetailsManager.StatusListener;
import com.essensys.cashsaverz.networkManager.RentBookManager;
import com.essensys.cashsaverz.paymentGateway.PaymentGatewayActivity;
import com.essensys.cashsaverz.preference.SharedPreferenceManager;
import es.dmoral.toasty.Toasty;
import java.util.ArrayList;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity implements OnClickListener, StatusListener, AddressManager.StatusListener, RentBookManager.StatusListener, OnCheckedChangeListener, AddToCartManager.StatusListener {
    private static MembershipPlans item;
    private static String productId;
    /* access modifiers changed from: private */
    public AppCompatAutoCompleteTextView actCity;
    private AddToCartManager addToCartManager;
    private String addressID;
    private UserAddressList addressItem;
    /* access modifiers changed from: private */
    public AddressManager addressManager;
    /* access modifiers changed from: private */
    public String addressType = "Home";
    private List addressesList;
    private Button btAddNewAddress;
    private Button btDeliver;
    private Button btGoBack;
    private Button btSaveAddress;
    /* access modifiers changed from: private */
    public String[] cities;
    /* access modifiers changed from: private */
    public String[] city_id;
    private ArrayAdapter<String> cityadapter;
    private CardView cvAddress;
    private CardView cvTimeSlots;
    private List<DeliveryTimeSlot> deliveryTimeSlots;
    private String deviceId;
    /* access modifiers changed from: private */
    public EditText etAddress;
    /* access modifiers changed from: private */
    public EditText etAlternateMobile;
    /* access modifiers changed from: private */
    public EditText etLandMark;
    /* access modifiers changed from: private */
    public EditText etLocality;
    /* access modifiers changed from: private */
    public EditText etMobile;
    /* access modifiers changed from: private */
    public EditText etName;
    /* access modifiers changed from: private */
    public EditText etPincode;
    private FieldDetailsManager fieldDetailsManager;
    private boolean isaddressvisible = false;
    private LinearLayout llAddress;
    private LinearLayout llAllAddresses;
    private LinearLayout llTimeSlots;
    private LinearLayout llinflatelayout;
    private ArrayList<String> mAddressCheckedList;
    private RadioGroup radioGroupAddress;
    private RadioGroup radioGroupTimeSlots;
    /* access modifiers changed from: private */
    public RadioButton rbtHome;
    private RadioButton rbtWork;
    private RentBookManager rentBookManager;
    /* access modifiers changed from: private */
    public String selectedcityid;
    private String timeSlotID;
    private TextView tvDeliveryLabel;

    public CheckOutActivity() {
        String str = "";
        this.selectedcityid = str;
        this.deviceId = str;
    }

    public static void setRentBookID(String productId2) {
        productId = productId2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_check_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CheckOutActivity.this.finish();
            }
        });
        this.addressManager = new AddressManager(this, this);
        this.fieldDetailsManager = new FieldDetailsManager(this, this);
        this.btAddNewAddress = (Button) findViewById(R.id.btAddNewAddress);
        this.btDeliver = (Button) findViewById(R.id.btDeliver);
        this.llAddress = (LinearLayout) findViewById(R.id.llAddress);
        this.llAllAddresses = (LinearLayout) findViewById(R.id.llAllAddresses);
        this.llTimeSlots = (LinearLayout) findViewById(R.id.llTimeSlots);
        this.cvTimeSlots = (CardView) findViewById(R.id.cvTimeSlots);
        this.cvAddress = (CardView) findViewById(R.id.cvAddress);
        this.tvDeliveryLabel = (TextView) findViewById(R.id.tvDeliveryLabel);
        this.radioGroupAddress = new RadioGroup(this);
        this.radioGroupAddress.setOnCheckedChangeListener(this);
        this.radioGroupTimeSlots = new RadioGroup(this);
        this.radioGroupTimeSlots.setOnCheckedChangeListener(this);
        this.addressesList = new ArrayList();
        this.mAddressCheckedList = new ArrayList<>();
        if (App.isUserLoggedIn(this)) {
            getExistingUserAddress();
        } else {
            CommonUtilities.openFragmentContainerActivityWithDelay(this, R.id.nav_login, 6);
        }
        this.fieldDetailsManager.getFields();
        this.btAddNewAddress.setOnClickListener(this);
        this.btDeliver.setOnClickListener(this);
        this.addToCartManager = new AddToCartManager(this, this);
        this.rentBookManager = new RentBookManager(this, this);
        if (item != null) {
            this.cvTimeSlots.setVisibility(View.GONE);
            this.tvDeliveryLabel.setText("Select Billing Address");
            return;
        }
        this.cvTimeSlots.setVisibility(View.VISIBLE);
        this.tvDeliveryLabel.setText("Select Delivery Address");
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == this.radioGroupAddress) {
            ArrayList<String> arrayList = this.mAddressCheckedList;
            if (arrayList != null) {
                arrayList.clear();
            }
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    this.addressID = ((UserAddressList) App.getCurrentUser(this).getArrUserAddressList().get(i)).getId();
                    this.addressItem = (UserAddressList) App.getCurrentUser(this).getArrUserAddressList().get(i);
                    this.mAddressCheckedList.add(CommonUtilities.addressFormatter(this.addressItem.getName(), this.addressItem.getAddress(), this.addressItem.getLandmark(), this.addressItem.getLocality(), this.addressItem.getCSCName(), this.addressItem.getPincode(), this.addressItem.getMobileNumber(), this.addressItem.getAlternateMobileNumber()));
                    return;
                }
            }
        } else if (group == this.radioGroupTimeSlots) {
            for (int i2 = 0; i2 < group.getChildCount(); i2++) {
                if (group.getChildAt(i2).getId() == checkedId) {
                    this.timeSlotID = ((DeliveryTimeSlot) this.deliveryTimeSlots.get(i2)).getId();
                    return;
                }
            }
        }
    }

    public void getExistingUserAddress() {
        for (int i = 0; i < App.getCurrentUser(this).getArrUserAddressList().size(); i++) {
            this.addressesList.add(CommonUtilities.addressFormatter(((UserAddressList) App.getCurrentUser(this).getArrUserAddressList().get(i)).getName(), ((UserAddressList) App.getCurrentUser(this).getArrUserAddressList().get(i)).getAddress(), ((UserAddressList) App.getCurrentUser(this).getArrUserAddressList().get(i)).getLandmark(), ((UserAddressList) App.getCurrentUser(this).getArrUserAddressList().get(i)).getLocality(), ((UserAddressList) App.getCurrentUser(this).getArrUserAddressList().get(i)).getCSCName(), ((UserAddressList) App.getCurrentUser(this).getArrUserAddressList().get(i)).getPincode(), ((UserAddressList) App.getCurrentUser(this).getArrUserAddressList().get(i)).getMobileNumber(), ((UserAddressList) App.getCurrentUser(this).getArrUserAddressList().get(i)).getAlternateMobileNumber()));
        }
        showAddressList(this.addressesList);
    }

    public void onBackPressed() {
        if (this.isaddressvisible) {
            showNewAddressView(false);
            return;
        }
        setItemDetails(null);
        setRentBookID(null);
        finish();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btAddNewAddress) {
            showNewAddressView(true);
        } else if (id == R.id.btDeliver) {
            if (this.radioGroupAddress.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Please select delivery address", Toast.LENGTH_SHORT).show();
            } else if (this.radioGroupTimeSlots.getCheckedRadioButtonId() == -1 && item == null) {
                Toast.makeText(getApplicationContext(), "Please select delivery time slot", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, OrderSummary.class);
                Bundle bundle = new Bundle();
                bundle.putString("addressId", this.addressID);
                bundle.putStringArrayList("address", this.mAddressCheckedList);
                String str = "flagCartOrder";
                bundle.putString(str, getIntent().getStringExtra(str));
                String str2 = "productId";
                bundle.putString(str2, getIntent().getStringExtra(str2));
                Intent intent2 = getIntent();
                String str3 = SqliteDatabaseHandler.COL_QTY;
                bundle.putString(str3, intent2.getStringExtra(str3));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    private void rentBookNow() {
        this.rentBookManager.rentBook(productId, this.addressID, this.timeSlotID, true);
    }

    private void checkoutSelectedCartItems() {
        this.rentBookManager.rentBook("", this.addressID, this.timeSlotID, false);
    }

    private void checkoutMembershipPlan() {
        PaymentGatewayActivity.setProductInfo(item, this.addressID, CommonUtilities.addressFormatter(this.addressItem.getName(), this.addressItem.getAddress(), this.addressItem.getLandmark(), this.addressItem.getLocality(), this.addressItem.getCSCName(), this.addressItem.getPincode(), this.addressItem.getMobileNumber(), this.addressItem.getAlternateMobileNumber()));
        startActivity(new Intent(this, PaymentGatewayActivity.class));
    }

    public void showNewAddressView(boolean status) {
        if (status) {
            this.llinflatelayout = (LinearLayout) View.inflate(this, R.layout.layout_address, null);
            this.llAddress.addView(this.llinflatelayout);
            this.etName = (EditText) this.llinflatelayout.findViewById(R.id.etName);
            this.etMobile = (EditText) this.llinflatelayout.findViewById(R.id.etMobile);
            this.etPincode = (EditText) this.llinflatelayout.findViewById(R.id.etPincode);
            this.etLocality = (EditText) this.llinflatelayout.findViewById(R.id.etLocality);
            this.etAddress = (EditText) this.llinflatelayout.findViewById(R.id.etAddress);
            this.etLandMark = (EditText) this.llinflatelayout.findViewById(R.id.etLandMark);
            this.rbtHome = (RadioButton) this.llinflatelayout.findViewById(R.id.rbtHome);
            this.rbtWork = (RadioButton) this.llinflatelayout.findViewById(R.id.rbtWork);
            this.etAlternateMobile = (EditText) this.llinflatelayout.findViewById(R.id.etAlternateMobile);
            this.actCity = (AppCompatAutoCompleteTextView) this.llinflatelayout.findViewById(R.id.actCity);
            this.actCity.setAdapter(this.cityadapter);
            this.actCity.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String selection = (String) adapterView.getItemAtPosition(i);
                    int pos = -1;
                    int j = 0;
                    while (true) {
                        if (j >= CheckOutActivity.this.cities.length) {
                            break;
                        } else if (CheckOutActivity.this.cities[j].equals(selection)) {
                            pos = j;
                            break;
                        } else {
                            j++;
                        }
                    }
                    CheckOutActivity checkOutActivity = CheckOutActivity.this;
                    checkOutActivity.selectedcityid = checkOutActivity.city_id[pos];
                }
            });
            this.btSaveAddress = (Button) this.llinflatelayout.findViewById(R.id.btSaveAddress);
            this.btGoBack = (Button) this.llinflatelayout.findViewById(R.id.btGoBack);
            this.btGoBack.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    CheckOutActivity.this.showNewAddressView(false);
                }
            });
            this.btSaveAddress.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    String str = "";
                    if (CheckOutActivity.this.etName.getText().toString().equalsIgnoreCase(str)) {
                        Toast.makeText(CheckOutActivity.this.getApplicationContext(), "Name cannot be blank", Toast.LENGTH_SHORT).show();
                    } else if (CheckOutActivity.this.etMobile.getText().toString().equalsIgnoreCase(str)) {
                        Toast.makeText(CheckOutActivity.this.getApplicationContext(), "Mobile number cannot be blank", Toast.LENGTH_SHORT).show();
                    } else if (CheckOutActivity.this.etPincode.getText().toString().equalsIgnoreCase(str)) {
                        Toast.makeText(CheckOutActivity.this.getApplicationContext(), "Pincode cannot be blank", Toast.LENGTH_SHORT).show();
                    } else if (CheckOutActivity.this.etLocality.getText().toString().equalsIgnoreCase(str)) {
                        Toast.makeText(CheckOutActivity.this.getApplicationContext(), "Locality cannot be blank", Toast.LENGTH_SHORT).show();
                    } else if (CheckOutActivity.this.etAddress.getText().toString().equalsIgnoreCase(str)) {
                        Toast.makeText(CheckOutActivity.this.getApplicationContext(), "Address cannot be blank", Toast.LENGTH_SHORT).show();
                    } else if (CheckOutActivity.this.actCity.getText().toString().equalsIgnoreCase(str)) {
                        Toast.makeText(CheckOutActivity.this.getApplicationContext(), "Please select city", Toast.LENGTH_SHORT).show();
                    } else {
                        if (CheckOutActivity.this.rbtHome.isChecked()) {
                            CheckOutActivity.this.addressType = "Home";
                        } else {
                            CheckOutActivity.this.addressType = "Work";
                        }
                        CheckOutActivity.this.addressManager.addNewAddress(CheckOutActivity.this.etName.getText().toString().trim(), CheckOutActivity.this.etMobile.getText().toString().trim(), CheckOutActivity.this.etPincode.getText().toString().trim(), CheckOutActivity.this.etLocality.getText().toString().trim(), CheckOutActivity.this.etAddress.getText().toString().trim(), CheckOutActivity.this.etLandMark.getText().toString().trim(), CheckOutActivity.this.etAlternateMobile.getText().toString().trim(), CheckOutActivity.this.selectedcityid, CheckOutActivity.this.addressType);
                    }
                }
            });
            this.isaddressvisible = true;
            this.btAddNewAddress.setVisibility(View.GONE);
            this.cvAddress.setVisibility(View.GONE);
            this.cvTimeSlots.setVisibility(View.GONE);
            this.btDeliver.setVisibility(View.GONE);
            return;
        }
        this.isaddressvisible = false;
        this.llAddress.removeView(this.llinflatelayout);
        this.btAddNewAddress.setVisibility(View.VISIBLE);
        this.cvAddress.setVisibility(View.VISIBLE);
        this.cvTimeSlots.setVisibility(View.VISIBLE);
        this.btDeliver.setVisibility(View.VISIBLE);
    }

    public void showAddressList(List addresses) {
        this.llAllAddresses.removeAllViews();
        this.radioGroupAddress.removeAllViews();
        for (int i = 0; i < addresses.size(); i++) {
            RadioButton btn1 = new RadioButton(this);
            LayoutParams params_soiled = new LayoutParams(getBaseContext(), null);
            params_soiled.setMargins(0, 50, 0, 0);
            btn1.setLayoutParams(params_soiled);
            btn1.setText(addresses.get(i).toString());
            btn1.setGravity(48);
            this.radioGroupAddress.addView(btn1, i);
            if (i == 0) {
                btn1.setChecked(true);
            }
        }
        this.llAllAddresses.addView(this.radioGroupAddress);
        showNewAddressView(false);
    }

    public void setCityList(List<CityList> cityList) {
        this.cities = new String[cityList.size()];
        this.city_id = new String[cityList.size()];
        for (int i = 0; i < cityList.size(); i++) {
            this.cities[i] = ((CityList) cityList.get(i)).getDrpDwnVal();
            this.city_id[i] = ((CityList) cityList.get(i)).getDrpDwnKey();
        }
        this.cityadapter = new ArrayAdapter<>(this, R.layout.item_city_list, this.cities);
    }

    public void setTimeSlot(List<DeliveryTimeSlot> deliveryTimeSlots2) {
        this.deliveryTimeSlots = deliveryTimeSlots2;
        this.llTimeSlots.removeAllViews();
        this.radioGroupTimeSlots.removeAllViews();
        for (int i = 0; i < deliveryTimeSlots2.size(); i++) {
            RadioButton btn1 = new RadioButton(this);
            LayoutParams params_soiled = new LayoutParams(getBaseContext(), null);
            params_soiled.setMargins(0, 35, 0, 0);
            btn1.setLayoutParams(params_soiled);
            btn1.setText(((DeliveryTimeSlot) deliveryTimeSlots2.get(i)).getDeliveryTimeSlot());
            btn1.setGravity(16);
            this.radioGroupTimeSlots.addView(btn1, i);
            if (i == 0) {
                btn1.setChecked(true);
            }
        }
        this.llTimeSlots.addView(this.radioGroupTimeSlots);
    }

    public void onFieldDataFetchSuccess(List<CityList> cityList, List<MembershipPlans> list, List<DeliveryTimeSlot> deliveryTimeSlots2) {
        setCityList(cityList);
        setTimeSlot(deliveryTimeSlots2);
    }

    public void onFieldDataFetchFailure(String errorMessage) {
        CommonUtilities.showToastOnMainThread(this, errorMessage);
    }

    public void onDataFetchSuccess(List<UserAddressList> userAddressList) {
        this.addressesList.clear();
        for (int i = 0; i < userAddressList.size(); i++) {
            this.addressesList.add(CommonUtilities.addressFormatter(((UserAddressList) userAddressList.get(i)).getName(), ((UserAddressList) userAddressList.get(i)).getAddress(), ((UserAddressList) userAddressList.get(i)).getLandmark(), ((UserAddressList) userAddressList.get(i)).getLocality(), ((UserAddressList) userAddressList.get(i)).getCSCName(), ((UserAddressList) userAddressList.get(i)).getPincode(), ((UserAddressList) userAddressList.get(i)).getMobileNumber(), ((UserAddressList) userAddressList.get(i)).getAlternateMobileNumber()));
            App.getCurrentUser(this).setArrUserAddressList(userAddressList);
            storeUserDetails(App.getCurrentUser(this));
        }
        showAddressList(this.addressesList);
    }

    public void onDataFetchFailure(String errorMessage) {
        CommonUtilities.showToastOnMainThread(this, errorMessage);
    }

    private void storeUserDetails(User user) {
        try {
            SharedPreferenceManager.with(this).updateLoggedInUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setItemDetails(MembershipPlans item2) {
        item = item2;
    }

    public void onSuccess(String successMessage) {
        this.addToCartManager.removeFromCart("", true);
        CommonUtilities.showToastOnMainThread(this, successMessage);
        CommonUtilities.openFragmentContainerActivityWithDelay(this, R.id.nav_home, 0);
    }

    public void onFailure(String errorMessage) {
        Toasty.error(this, errorMessage, 1, false).show();
    }

    public void onDataFetchSuccess(String successMessage, String total_cart_items) {
        HomeActivity.setCartCount(0);
    }

    public void onDataFetchFailure(String errorMessage, String total_cart_items) {
    }
}
