package com.essensys.cashsaverz.networkManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant.ServerEndpoint;
import com.essensys.cashsaverz.model.CartItems;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces.OrderSummary;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSummaryManager implements Callback<ResponseBody> {
    private StatusListener listener;
    private Context mContext;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private String strCharges;
    private String strTotalPay;
    private String strTotalPrice;

    public interface StatusListener {
        void onListFetchFailure(String str);

        void onListFetchSuccess(List<CartItems> list, String str, String str2, String str3);
    }

    public OrderSummaryManager(Context mContext2, StatusListener listener2) {
        String str = "";
        this.strTotalPay = str;
        this.strCharges = str;
        this.strTotalPrice = str;
        this.mContext = mContext2;
        this.listener = listener2;
        this.progressDialog = CommonUtilities.getDefaultLoader(mContext2);
    }

    public void fetchOrderSummary(String checkOutFrom, String productId, String qty) {
        showLoader();
        RetrofitClient.getClient(ServerEndpoint.CHECKOUT_SUMMARY).create(OrderSummary.class)
                .post(App.getCurrentUser(this.mContext).getUserAuth(), checkOutFrom, productId, qty)
                .enqueue(this);
    }

    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            JSONObject result = new JSONObject(((ResponseBody) response.body()).string()).getJSONObject("result");
            if (result.getString(NotificationCompat.CATEGORY_MESSAGE).contentEquals("1")) {
                hideLoader();
                JSONArray orderedItems = result.getJSONArray("orderedItems");
                this.strTotalPay = result.getString("totalPaybaleAmount");
                this.strCharges = result.getString("shippingCharges");
                this.strTotalPrice = result.getString("totalPrice");
                this.listener.onListFetchSuccess((List) new Gson().fromJson(orderedItems.toString(), new TypeToken<List<CartItems>>() {
                }.getType()), this.strTotalPay, this.strCharges, this.strTotalPrice);
                return;
            }
            this.listener.onListFetchFailure(result.getString("msg_string"));
        } catch (Exception e) {
            this.listener.onListFetchFailure(this.mContext.getResources().getString(R.string.something_went_wrong));
        }
    }

    public void onFailure(Call<ResponseBody> call, Throwable t) {
    }

    private void showLoader() {
        ProgressDialog progressDialog2 = this.progressDialog;
        if (progressDialog2 != null && !progressDialog2.isShowing()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    OrderSummaryManager.this.progressDialog.show();
                }
            });
        }
    }

    private void hideLoader() {
        ProgressDialog progressDialog2 = this.progressDialog;
        if (progressDialog2 != null && progressDialog2.isShowing()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    OrderSummaryManager.this.progressDialog.dismiss();
                }
            });
        }
    }
}
