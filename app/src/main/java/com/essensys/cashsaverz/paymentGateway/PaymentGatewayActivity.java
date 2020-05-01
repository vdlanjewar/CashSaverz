package com.essensys.cashsaverz.paymentGateway;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant;
import com.essensys.cashsaverz.model.MembershipPlans;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentGatewayActivity extends AppCompatActivity implements Callback<ResponseBody>, View.OnClickListener {
    public static final String TAG = "MainActivity : ";
    private boolean isDisableExitConfirmation = false;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private static String itemAmount;
    private static String productInfo;
    private static String planID;
    private static String addressID;
    private static String address;
    private static MembershipPlans item;
    private Button btPayment;
    private TextView tvPlanPrice, tvPlanDescription, tvAddress, tvSecurityDepositInfo, tvPlanOffer;

    public static void setProductInfo(MembershipPlans item, String addressID, String address) {
        PaymentGatewayActivity.item = item;
        PaymentGatewayActivity.productInfo = item.getTitle();
        //PaymentGatewayActivity.itemAmount = item.getPrice();
        // PaymentGatewayActivity.itemAmount = String.valueOf(Float.parseFloat(!TextUtils.isEmpty(item.getPrice())?item.getPrice():"0")+Float.parseFloat(!TextUtils.isEmpty(item.getSecurityDeposit())?item.getSecurityDeposit():"0")); // Added Security Deposit Amount by SM
        double amtWithDeposit;
        if (item.isSecurityDepositRecived())
            amtWithDeposit = Double.parseDouble(!TextUtils.isEmpty(item.getPrice()) ? item.getPrice() : "0");
        else
            amtWithDeposit = Double.parseDouble(!TextUtils.isEmpty(item.getPrice()) ? item.getPrice() : "0") + Double.parseDouble(!TextUtils.isEmpty(item.getSecurityDeposit()) ? item.getSecurityDeposit() : "0");
        double totalAmount = amtWithDeposit - Double.parseDouble(!TextUtils.isEmpty(item.getOfferPriceOff()) ? item.getOfferPriceOff() : "0");

        //PaymentGatewayActivity.itemAmount = String.valueOf(Float.parseFloat(!TextUtils.isEmpty(item.getPrice())?item.getPrice():"0")+Float.parseFloat(!TextUtils.isEmpty(item.getSecurityDeposit())?item.getSecurityDeposit():"0")); // Added Security Deposit Amount by SM
        PaymentGatewayActivity.itemAmount = String.valueOf(totalAmount); // Added Security Deposit Amount by SM
        PaymentGatewayActivity.planID = item.getId();
        PaymentGatewayActivity.addressID = addressID;
        PaymentGatewayActivity.address = address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);

        tvPlanPrice = findViewById(R.id.tvPlanPrice);
        tvPlanDescription = findViewById(R.id.tvPlanDescription);
        tvAddress = findViewById(R.id.tvAddress);
        btPayment = findViewById(R.id.btPayment);
        tvSecurityDepositInfo = findViewById(R.id.tvSecurityDepositInfo);
        tvPlanOffer = findViewById(R.id.tvPlanOffer);
        btPayment.setOnClickListener(this);

        initView();

        ((App) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);

        setupCitrusConfigs();
    }

    private void initView() {
        tvAddress.setText(address);

        tvPlanDescription.setText(item.getDescription());
        tvPlanPrice.setText(item.getPrice());
        if (!item.isSecurityDepositRecived())
            tvSecurityDepositInfo.setText(item.getSecurityDepositText());
        tvPlanOffer.setText(item.getOfferText());
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupCitrusConfigs() {
        AppEnvironment appEnvironment = ((App) getApplication()).getAppEnvironment();
        /*if (appEnvironment == AppEnvironment.PRODUCTION) {
            Toast.makeText(PaymentGatewayActivity.this, "Environment Set to Production", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PaymentGatewayActivity.this, "Environment Set to SandBox", Toast.LENGTH_SHORT).show();
        }*/
    }

    /**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     */
    private void launchPayUMoneyFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        String txnId = System.currentTimeMillis() + "";
        String phone = App.getCurrentUser(this).getUserPhone();
        /*  String productName = mAppPreference.getProductInfo();*/
        String firstName = App.getCurrentUser(this).getUserName()
                .replace(" ", "");
        String email = App.getCurrentUser(this).getUserEmail();
        String udf1 = planID;
        String udf2 = addressID;
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = ((App) getApplication()).getAppEnvironment();
        builder.setAmount(itemAmount)
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productInfo)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();
            /* Hash should always be generated from your server side.
             */
            generateHashFromServer(mPaymentParams);

        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method generates hash from server.
     *
     * @param paymentParam payments params used for hash generation
     */
    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        HashMap<String, String> params = paymentParam.getParams();

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
        postParamsBuffer.append(concatParams("productinfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
        postParamsBuffer.append(concatParams("firstname", params.get(PayUmoneyConstants.FIRSTNAME)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ?
                postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() :
                postParamsBuffer.toString();
        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }


    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btPayment) {
            launchPayUMoneyFlow();
        }
    }

    /**
     * This AsyncTask generates hash from server.
     */
    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PaymentGatewayActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL("http://52.41.183.204/JB085/web-app/ws-account/PayUMoney_Android_SDK_GenerateReverseHash-v3.php");
                // URL url = new URL("https://payu.herokuapp.com/get_hash");

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        /**
                         * This hash is mandatory and needs to be generated from merchant's server side
                         */
                        case "payment_hash":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);
            progressDialog.dismiss();
            if (merchantHash.isEmpty() || merchantHash.equals("")) {
                Toast.makeText(PaymentGatewayActivity.this, "Could not generate hash",
                        Toast.LENGTH_SHORT).show();
            } else {

                mPaymentParams.setMerchantHash(merchantHash);

                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,
                        PaymentGatewayActivity.this, R.style.AppTheme_default,
                        false);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK &&
                data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);
            // Response from Payumoney
            String payuResponse = transactionResponse.getPayuResponse();

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse
                        .TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                    successNetworkCall(payuResponse);
                } else {
                    //Failure Transaction
                    CommonUtilities.showToastOnMainThread(this, "Transaction Failed." +
                            "Please try again later");

                }

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }
        }
    }

    private void successNetworkCall(String payuResponse) {
        try {
            JSONObject jsonObject = new JSONObject(payuResponse);
            JSONObject result = jsonObject.getJSONObject("result");

            PayuDataHolder payuDataHolder = new Gson()
                    .fromJson(result.toString(), new TypeToken<PayuDataHolder>() {
                    }.getType());

            HashMap<String, RequestBody> valueMap = new HashMap<>();
            valueMap.put("user_id", getRequestBodyFromString(App.getCurrentUser(this)
                    .getCustomerId()));
            valueMap.put("mihpayid", getRequestBodyFromString(payuDataHolder.getMihPayID()));
            valueMap.put("mode", getRequestBodyFromString(payuDataHolder.getMode()));
            valueMap.put("status", getRequestBodyFromString(payuDataHolder.getStatus()));
            valueMap.put("txnid", getRequestBodyFromString(payuDataHolder.getTxnID()));
            valueMap.put("amount", getRequestBodyFromString(payuDataHolder.getAmount()));
            valueMap.put("addedon", getRequestBodyFromString(payuDataHolder.getAddedon()));
            valueMap.put("productinfo", getRequestBodyFromString(payuDataHolder.getProductInfo()));
            valueMap.put("PG_TYPE", getRequestBodyFromString(payuDataHolder.getPgTYPE()));
            valueMap.put("encryptedPaymentId", getRequestBodyFromString(""));
            valueMap.put("bank_ref_num", getRequestBodyFromString(payuDataHolder.getBankRefNum()));
            valueMap.put("bankcode", getRequestBodyFromString(payuDataHolder.getBankcode()));
            valueMap.put("error", getRequestBodyFromString(payuDataHolder.getError()));
            valueMap.put("error_Message", getRequestBodyFromString(payuDataHolder.getErrorMessage()));
            valueMap.put("name_on_card", getRequestBodyFromString(payuDataHolder.getNameOnCard()));
            valueMap.put("cardnum", getRequestBodyFromString(payuDataHolder.getCardNumber()));
            valueMap.put("payuMoneyId", getRequestBodyFromString(payuDataHolder.getPayuMoneyId()));
            valueMap.put("discount", getRequestBodyFromString(payuDataHolder.getDiscount()));
            valueMap.put("net_amount_debit", getRequestBodyFromString(payuDataHolder.getNetAmountDebit()));
            valueMap.put("firstname", getRequestBodyFromString(payuDataHolder.getFirstName()));
            valueMap.put("phone", getRequestBodyFromString(payuDataHolder.getUserPhone()));
            valueMap.put("hash", getRequestBodyFromString(payuDataHolder.getHash()));
            valueMap.put("key", getRequestBodyFromString(payuDataHolder.getKey()));
            valueMap.put("email", getRequestBodyFromString(payuDataHolder.getUserEmail()));
            valueMap.put("udf1", getRequestBodyFromString(planID));
            valueMap.put("udf2", getRequestBodyFromString(addressID));

            RetrofitClient
                    .getClient(Constant.ServerEndpoint.MEMBERSHIP_PLAN)
                    .create(RetrofitInterfaces.IPurchaseMembershipPlan.class)
                    .save(App.getCurrentUser(PaymentGatewayActivity.this).getUserAuth(), valueMap)
                    .enqueue(PaymentGatewayActivity.this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private RequestBody getRequestBodyFromString(String str) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), str);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            String reponseResult = response.body().string();
            JSONObject result = new JSONObject(reponseResult).getJSONObject("result");
            CommonUtilities.showToastOnMainThread(this, result.getString("msg_string"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonUtilities.openFragmentContainerActivityWithDelay(this, R.id.nav_home, 0);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }

}

