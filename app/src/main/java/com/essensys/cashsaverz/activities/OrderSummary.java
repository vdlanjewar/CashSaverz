package com.essensys.cashsaverz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.Interface.InterfaceClickListener;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.adapter.CartListAdapter;
import com.essensys.cashsaverz.adapter.CartListAdapter.PostItemListener;
import com.essensys.cashsaverz.fragments.HomeFragment;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant.ServerEndpoint;
import com.essensys.cashsaverz.localDatabases.SqliteDatabaseHandler;
import com.essensys.cashsaverz.model.CartItems;
import com.essensys.cashsaverz.networkManager.AddToCartManager;
import com.essensys.cashsaverz.networkManager.AddToCartManager.StatusListener;
import com.essensys.cashsaverz.networkManager.OrderSummaryManager;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces.UpdateQty;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSummary extends AppCompatActivity implements OnClickListener, StatusListener, OrderSummaryManager.StatusListener, Callback<ResponseBody> {
    private AddToCartManager addToCartManager;
    private String addressId;
    private ArrayList<String> addressList;
    private Bundle b;
    private List<CartItems> cartItemList;
    /* access modifiers changed from: private */
    public String checkOutFrom;
    private String deviceId;
    private String flagCartOrder;
    InterfaceClickListener interfaceClickListener = new InterfaceClickListener() {
        public void Onclick(String id, String value) {
            OrderSummary.this.qty = value;
            if (OrderSummary.this.checkOutFrom.equalsIgnoreCase("cart")) {
                OrderSummary.this.updatequantity(id, value);
            } else {
                OrderSummary.this.orderSummaryManager.fetchOrderSummary("ordernow", id, value);
            }
        }
    };
    private Button mBtnChangeAdd;
    private Button mBtnContinue;
    private FrameLayout mFrame;
    private ImageView mImgBack;
    private LinearLayoutManager mLinearLayoutManager;
    private CartListAdapter mProductAdapter;
    private RecyclerView mRecyclerView;
    private TextView mTxtAddress;
    private TextView mTxtAmt;
    private TextView mTxtDelivery;
    private TextView mTxtPrice1;
    private TextView mtxtPrice;
    /* access modifiers changed from: private */
    public OrderSummaryManager orderSummaryManager;
    private String productId;
    /* access modifiers changed from: private */
    public String qty;
    private String strProdName;
    private String strTotalAmt;
    private TextView textCartItemCount;



    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_order_summary);
        this.mTxtAddress = (TextView) findViewById(R.id.txt_orderAdd);
        this.mTxtPrice1 = (TextView) findViewById(R.id.txt_price1);
        this.mtxtPrice = (TextView) findViewById(R.id.txt_price);
        this.mTxtAmt = (TextView) findViewById(R.id.txt_amtPay);
        this.mTxtDelivery = (TextView) findViewById(R.id.txt_delivery);
        this.mBtnChangeAdd = (Button) findViewById(R.id.btn_chngAdd);
        this.mBtnContinue = (Button) findViewById(R.id.btn_cnt);
        this.mImgBack = (ImageView) findViewById(R.id.img_bck_order);
        this.textCartItemCount = (TextView) findViewById(R.id.cart_badgeOrder);
        this.mFrame = (FrameLayout) findViewById(R.id.frame_ordersum);
        this.addToCartManager = new AddToCartManager(this, this);
        this.orderSummaryManager = new OrderSummaryManager(this, this);
        this.mImgBack.setOnClickListener(this);
        this.mBtnContinue.setOnClickListener(this);
        this.mBtnChangeAdd.setOnClickListener(this);
        this.flagCartOrder = getIntent().getStringExtra("flagCartOrder");
        CartListAdapter cartListAdapter = new CartListAdapter(this, new ArrayList(0), this.addToCartManager, this.interfaceClickListener, "", new PostItemListener() {
            public void onPostClick(int itemid, int viewid, int adapterposition) {
            }
        });
        this.mProductAdapter = cartListAdapter;
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recyOrder);
        this.mLinearLayoutManager = new LinearLayoutManager(this);
        this.mRecyclerView.setLayoutManager(this.mLinearLayoutManager);
        this.mRecyclerView.setHasFixedSize(true);
        this.b = getIntent().getExtras();
        getAddress();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        setupBadge(getCartCount());
        if (this.flagCartOrder.equalsIgnoreCase("0")) {
            this.checkOutFrom = "cart";
        } else {
            this.checkOutFrom = "ordernow";
            this.productId = this.b.getString("productId");
            this.qty = this.b.getString(SqliteDatabaseHandler.COL_QTY);
        }
        this.orderSummaryManager.fetchOrderSummary(this.checkOutFrom, this.productId, this.qty);
    }

    public void setupBadge(int count) {
        FrameLayout frameLayout = this.mFrame;
        if (frameLayout == null) {
            return;
        }
        if (count != 0) {
            this.textCartItemCount.setText(String.valueOf(count));
            if (this.mFrame.getVisibility() != View.VISIBLE) {
                this.mFrame.setVisibility(View.VISIBLE);
            }
        } else if (frameLayout.getVisibility() != View.GONE) {
            this.mFrame.setVisibility(View.GONE);
        }
    }

    public static void setCartCount(int currentcount) {
        HomeActivity.cartItemCount = currentcount;
    }

    public static int getCartCount() {
        return HomeActivity.cartItemCount;
    }

    private void getAddress() {
        this.addressList = new ArrayList<>();
        this.addressList = getIntent().getExtras().getStringArrayList("address");
        if (this.addressList != null) {
            this.addressId = getIntent().getStringExtra("addressId");
            this.mTxtAddress.setText(((String) this.addressList.get(0)).toString());
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chngAdd :
                onBackPressed();
                return;
            case R.id.btn_cnt :
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra("price", this.mTxtPrice1.getText().toString());
                intent.putExtra("charges", this.mTxtDelivery.getText().toString());
                intent.putExtra("totalAmt", this.strTotalAmt);
                String str = "addressId";
                intent.putExtra(str, getIntent().getStringExtra(str));
                intent.putExtra("checkOutFrom", this.checkOutFrom);
                intent.putExtra(SqliteDatabaseHandler.COL_QTY, this.qty);
                intent.putExtra("productId", this.productId);
                startActivity(intent);
                return;
            case R.id.img_bck_order /*2131296611*/:
                onBackPressed();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void updatequantity(String cartId, String value) {
        String customerId;
        if (App.isUserLoggedIn(this)) {
            this.deviceId = "";
            customerId = App.getCurrentUser(this).getCustomerId();
        } else {
            this.deviceId = HomeFragment.deviceId;
            customerId = "";
        }
        RetrofitClient.getClient(ServerEndpoint.UPDATE_QTY)
                .create(UpdateQty.class)
                .post(customerId, cartId, value, this.deviceId)
                .enqueue(this);
    }

    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            if (new JSONObject(((ResponseBody) response.body()).string()).getJSONObject("result").getString(NotificationCompat.CATEGORY_MESSAGE).equalsIgnoreCase("1")) {
                this.orderSummaryManager.fetchOrderSummary(this.checkOutFrom, this.productId, this.qty);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onFailure(Call<ResponseBody> call, Throwable t) {
    }

    public void onDataFetchSuccess(String successMessage, String total_cart_items) {
        setCartCount(Integer.parseInt(total_cart_items));
        setupBadge(HomeActivity.getCartCount());
        this.orderSummaryManager.fetchOrderSummary(this.checkOutFrom, this.productId, this.qty);
    }

    public void onDataFetchFailure(String errorMessage, String total_cart_items) {
        setCartCount(Integer.parseInt(total_cart_items));
        setupBadge(HomeActivity.getCartCount());
        CommonUtilities.showToastOnMainThread(this, errorMessage);
    }

    public void onListFetchSuccess(List<CartItems> cartItemLists, String strTotalPay, String strCharges, String strTotalPrice) {
        this.cartItemList = cartItemLists;
        this.strTotalAmt = strTotalPay;
        TextView textView = this.mTxtPrice1;
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.Rs));
        String str = " ";
        sb.append(str);
        sb.append(strTotalPrice);
        textView.setText(sb.toString());
        TextView textView2 = this.mTxtDelivery;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getString(R.string.Rs));
        sb2.append(str);
        sb2.append(strCharges);
        textView2.setText(sb2.toString());
        TextView textView3 = this.mTxtAmt;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getString(R.string.Rs));
        sb3.append(str);
        sb3.append(strTotalPay);
        textView3.setText(sb3.toString());
        this.mProductAdapter.updateProducts(this.cartItemList);
        this.mRecyclerView.setAdapter(this.mProductAdapter);
    }

    public void onListFetchFailure(String errorMsg) {
        CommonUtilities.showToastOnMainThread(this, errorMsg);
    }

    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
