package com.essensys.cashsaverz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant.ServerEndpoint;
import com.essensys.cashsaverz.localDatabases.SqliteDatabaseHandler;
import com.essensys.cashsaverz.paymentGateway.AppEnvironment;
import com.essensys.cashsaverz.paymentGateway.PayuDataHolder;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces.IPlaceOrder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer.PaymentParam;
import com.payumoney.core.PayUmoneySdkInitializer.PaymentParam.Builder;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.core.entity.TransactionResponse.TransactionStatus;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements OnClickListener, OnCheckedChangeListener, Callback<ResponseBody> {
    private static final String TAG = "MainActivity";
    private String checkedFlg;
    private boolean isDisableExitConfirmation = false;
    private Button mBtnPayNow;
    private Button mBtnSubmit;
    private ImageView mImgBack;
    private PaymentParam mPaymentParams;
    private RadioButton mRdoCOD;
    private RadioButton mRdoPayU;
    private TextView mTxtCharges;
    private TextView mTxtTotalAmt;
    private String merchantHash;
    private TextView mtxtPrice;
    private HashMap<String, RequestBody> valueMap;

    public PaymentActivity() {
        String str = "";
        this.merchantHash = str;
        this.checkedFlg = str;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_payment);
        this.mtxtPrice = (TextView) findViewById(R.id.txt_pricePay1);
        this.mTxtCharges = (TextView) findViewById(R.id.txt_deliveryPay);
        this.mTxtTotalAmt = (TextView) findViewById(R.id.txt_amtPayNow);
        this.mImgBack = (ImageView) findViewById(R.id.img_bck_pay);
        this.mBtnPayNow = (Button) findViewById(R.id.btn_paymentdone);
        this.mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        this.mRdoPayU = (RadioButton) findViewById(R.id.rdo_payU);
        this.mRdoCOD = (RadioButton) findViewById(R.id.rdo_Cod);
        this.mImgBack.setOnClickListener(this);
        this.mBtnPayNow.setOnClickListener(this);
        this.mBtnSubmit.setOnClickListener(this);
        this.mRdoPayU.setChecked(true);
        this.checkedFlg = "Online";
        this.mBtnPayNow.setVisibility(View.VISIBLE);
        this.mRdoPayU.setOnCheckedChangeListener(this);
        this.mRdoCOD.setOnCheckedChangeListener(this);
        getPriceDet();
    }

    private void getPriceDet() {
        this.mtxtPrice.setText(getIntent().getStringExtra("price"));
        this.mTxtCharges.setText(getIntent().getStringExtra("charges"));
        TextView textView = this.mTxtTotalAmt;
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.Rs));
        sb.append(" ");
        sb.append(getIntent().getStringExtra("totalAmt"));
        textView.setText(sb.toString());
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_paymentdone) {
            launchPayUMoneyFlow();
        } else if (id == R.id.btn_submit) {
            successNetworkCall("");
        } else if (id == R.id.img_bck_pay) {
            onBackPressed();
        }
    }

    private void launchPayUMoneyFlow() {
        PaymentActivity paymentActivity;
        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
        payUmoneyConfig.disableExitConfirmation(this.isDisableExitConfirmation);
        Builder builder = new Builder();
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        String str = "";
        sb.append(str);
        String txnId = sb.toString();
        String amount = getIntent().getStringExtra("totalAmt");
        String firstName = App.getCurrentUser(this).getUserName().replace(" ", str);
        String email = App.getCurrentUser(this).getUserEmail();
        PayUmoneyConfig payUmoneyConfig2 = payUmoneyConfig;
        String udf7 = "";
        String udf10 = "";
        AppEnvironment appEnvironment = ((App) getApplication()).getAppEnvironment();
        String str2 = firstName;
        String str3 = udf7;
        String udf72 = "";
        String str4 = udf10;
        builder.setAmount(amount).setTxnId(txnId).setPhone("9144040888").setProductName("Cash Saverz").setFirstName(firstName).setEmail(email).setsUrl(appEnvironment.surl()).setfUrl(appEnvironment.furl()).setUdf1("").setUdf2("").setUdf3("").setUdf4("").setUdf5("").setUdf6("").setUdf7(udf7).setUdf8("").setUdf9(udf72).setUdf10(udf10).setIsDebug(appEnvironment.debug()).setKey(appEnvironment.merchant_Key()).setMerchantId(appEnvironment.merchant_ID());
        try {
            paymentActivity = this;
            try {
                paymentActivity.mPaymentParams = builder.build();
                paymentActivity.mPaymentParams = paymentActivity.calculateServerSideHashAndInitiatePayment1(paymentActivity.mPaymentParams);
                String str5 = udf72;
                Builder builder2 = builder;
                try {
                    PayUmoneyFlowManager.startPayUMoneyFlow(paymentActivity.mPaymentParams, paymentActivity, 2131820561, false);
                } catch (Exception e) {
                    e = e;
                }
            } catch (Exception e2) {

                String str6 = udf72;
                Builder builder3 = builder;
                Toast.makeText(paymentActivity, e2.getMessage(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e3) {

            paymentActivity = this;
            String str62 = udf72;
            Builder builder32 = builder;
            Toast.makeText(paymentActivity, e3.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private PaymentParam calculateServerSideHashAndInitiatePayment1(PaymentParam paymentParam) {
        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        StringBuilder sb = new StringBuilder();
        sb.append((String) params.get("key"));
        String str = "|";
        sb.append(str);
        stringBuilder.append(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append((String) params.get("txnid"));
        sb2.append(str);
        stringBuilder.append(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append((String) params.get("amount"));
        sb3.append(str);
        stringBuilder.append(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append((String) params.get(PayUmoneyConstants.PRODUCT_INFO));
        sb4.append(str);
        stringBuilder.append(sb4.toString());
        StringBuilder sb5 = new StringBuilder();
        sb5.append((String) params.get(PayUmoneyConstants.FIRSTNAME));
        sb5.append(str);
        stringBuilder.append(sb5.toString());
        StringBuilder sb6 = new StringBuilder();
        sb6.append((String) params.get("email"));
        sb6.append(str);
        stringBuilder.append(sb6.toString());
        StringBuilder sb7 = new StringBuilder();
        sb7.append((String) params.get("udf1"));
        sb7.append(str);
        stringBuilder.append(sb7.toString());
        StringBuilder sb8 = new StringBuilder();
        sb8.append((String) params.get("udf2"));
        sb8.append(str);
        stringBuilder.append(sb8.toString());
        StringBuilder sb9 = new StringBuilder();
        sb9.append((String) params.get("udf3"));
        sb9.append(str);
        stringBuilder.append(sb9.toString());
        StringBuilder sb10 = new StringBuilder();
        sb10.append((String) params.get("udf4"));
        sb10.append(str);
        stringBuilder.append(sb10.toString());
        StringBuilder sb11 = new StringBuilder();
        sb11.append((String) params.get("udf5"));
        sb11.append("||||||");
        stringBuilder.append(sb11.toString());
        stringBuilder.append(((App) getApplication()).getAppEnvironment().salt());
        paymentParam.setMerchantHash(hashCal(stringBuilder.toString()));
        return paymentParam;
    }

    public String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            for (byte aMessageDigest : algorithm.digest()) {
                String hex = Integer.toHexString(aMessageDigest & 255);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
        }
        this.merchantHash = hexString.toString();
        return hexString.toString();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        StringBuilder sb = new StringBuilder();
        sb.append("request code ");
        sb.append(requestCode);
        sb.append(" resultcode ");
        sb.append(resultCode);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == -1 && data != null) {
            TransactionResponse transactionResponse = (TransactionResponse) data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
            ResultModel resultModel = (ResultModel) data.getParcelableExtra("result");
            String payuResponse = transactionResponse.getPayuResponse();
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionStatus.SUCCESSFUL)) {
                    successNetworkCall(payuResponse);
                } else {
                    CommonUtilities.showToastOnMainThread(this, "Transaction Failed.Please try again later");
                }
                transactionResponse.getTransactionDetails();
            } else if (resultModel == null || resultModel.getError() == null) {
                Log.d(str, "Both objects are null!");
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Error response : ");
                sb3.append(resultModel.getError().getTransactionResponse());
                Log.d(str, sb3.toString());
            }
        }
    }

    private void successNetworkCall(String payuResponse) {
        String str = "";
        String str2 = SqliteDatabaseHandler.COL_QTY;
        try {
            String pt = this.checkedFlg;
            String qty = getIntent().getStringExtra(str2);
            int visibility = this.mBtnPayNow.getVisibility();
            String str3 = "addedon";
            String str4 = "amount";
            String str5 = "txnid";
            String str6 = PayUmoneyConstants.PAYMENT_ID;
            String str7 = "mihpayid";
            String str8 = "payment_status";
            String str9 = "paymentMethod";
            String str10 = "addressId";
            String str11 = "shippingAddress_id";
            String str12 = "productId";
            String str13 = pt;
            String pt2 = "checkOutFrom";
            if (visibility == 0) {
                JSONObject jsonObject = new JSONObject(payuResponse);
                String str14 = qty;
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject jSONObject = jsonObject;
                JSONObject jSONObject2 = result;
                PayuDataHolder payuDataHolder = (PayuDataHolder) new Gson().fromJson(result.toString(), new TypeToken<PayuDataHolder>() {
                }.getType());
                String amount = payuDataHolder.getAmount();
                this.valueMap = new HashMap<>();
                String str15 = amount;
                this.valueMap.put(str11, getRequestBodyFromString(getIntent().getStringExtra(str10)));
                this.valueMap.put(str9, getRequestBodyFromString(this.checkedFlg));
                this.valueMap.put(pt2, getRequestBodyFromString(getIntent().getStringExtra(pt2)));
                this.valueMap.put(str8, getRequestBodyFromString(payuDataHolder.getStatus()));
                this.valueMap.put(str7, getRequestBodyFromString(payuDataHolder.getMihPayID()));
                this.valueMap.put(str6, getRequestBodyFromString(payuDataHolder.getPayuMoneyId()));
                this.valueMap.put(str5, getRequestBodyFromString(payuDataHolder.getTxnID()));
                this.valueMap.put(str4, getRequestBodyFromString(payuDataHolder.getAmount()));
                this.valueMap.put(str3, getRequestBodyFromString(payuDataHolder.getAddedon()));
                this.valueMap.put(str12, getRequestBodyFromString(getIntent().getStringExtra(str12)));
                this.valueMap.put(str2, getRequestBodyFromString(getIntent().getStringExtra(str2)));
            } else {
                this.valueMap = new HashMap<>();
                this.valueMap.put(str11, getRequestBodyFromString(getIntent().getStringExtra(str10)));
                this.valueMap.put(str9, getRequestBodyFromString(this.checkedFlg));
                this.valueMap.put(pt2, getRequestBodyFromString(getIntent().getStringExtra(pt2)));
                this.valueMap.put(str8, getRequestBodyFromString(str));
                this.valueMap.put(str7, getRequestBodyFromString(str));
                this.valueMap.put(str6, getRequestBodyFromString(str));
                this.valueMap.put(str5, getRequestBodyFromString(str));
                this.valueMap.put(str4, getRequestBodyFromString(getIntent().getStringExtra("totalAmt")));
                this.valueMap.put(str3, getRequestBodyFromString(str));
                this.valueMap.put(str12, getRequestBodyFromString(getIntent().getStringExtra(str12)));
                this.valueMap.put(str2, getRequestBodyFromString(getIntent().getStringExtra(str2)));
            }
            RetrofitClient.getClient(ServerEndpoint.PLACE_ORDER)
                    .create(IPlaceOrder.class)
                    .save(App.getCurrentUser(this).getUserAuth(), this.valueMap)
                    .enqueue(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private RequestBody getRequestBodyFromString(String str) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), str);
    }

    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            CommonUtilities.showToastOnMainThread(this, new JSONObject(((ResponseBody) response.body()).string()).getJSONObject("result").getString("msg_string"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonUtilities.openFragmentContainerActivityWithDelay(this, R.id.nav_home, 0);
    }

    public void onFailure(Call<ResponseBody> call, Throwable t) {
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (this.mRdoPayU.isChecked()) {
            this.checkedFlg = "Online";
            this.mBtnPayNow.setVisibility(View.VISIBLE);
            this.mBtnSubmit.setVisibility(View.GONE);
            this.mRdoCOD.setChecked(false);
            return;
        }
        this.checkedFlg = "COD";
        this.mBtnPayNow.setVisibility(View.GONE);
        this.mBtnSubmit.setVisibility(View.VISIBLE);
        this.mRdoPayU.setChecked(false);
        this.mRdoCOD.setChecked(true);
    }

    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
