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
import com.essensys.cashsaverz.model.CartItems;
import com.essensys.cashsaverz.model.IdSelector;
import com.essensys.cashsaverz.networkManager.AddToCartManager;
import com.essensys.cashsaverz.networkManager.CartItemListManager;
import com.essensys.cashsaverz.networkManager.CartItemListManager.StatusListener;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces.UpdateQty;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements OnClickListener, StatusListener, AddToCartManager.StatusListener, Callback<ResponseBody> {
    private AddToCartManager addToCartManager;
    private Button btCheckOut;
    private Button btRemoveAll;
    private List<CartItems> cartItemList;
    private CartItemListManager cartItemListManager;
    private String deviceId = "";
    InterfaceClickListener interfaceClickListener = new InterfaceClickListener() {
        public void Onclick(String cartId, String value) {
            CartActivity.this.updatequantity(cartId, value);
        }
    };
    private FrameLayout mFrame;
    private ImageView mImgBack;
    private CartListAdapter mProductAdapter;
    private RecyclerView recyclerView;
    private TextView textCartItemCount;
    private TextView tvEmptyCart;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_cart);
        this.cartItemListManager = new CartItemListManager(this, this);
        this.addToCartManager = new AddToCartManager(this, this);
        this.textCartItemCount = (TextView) findViewById(R.id.cart_badgeMy);
        this.mFrame = (FrameLayout) findViewById(R.id.frame_mycart);
        this.mImgBack = (ImageView) findViewById(R.id.imgBckMyCart);
        this.mImgBack.setOnClickListener(this);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.btCheckOut = (Button) findViewById(R.id.btCheckOut);
        this.btCheckOut.setOnClickListener(this);
        this.btRemoveAll = (Button) findViewById(R.id.btRemoveAll);
        this.btRemoveAll.setOnClickListener(this);
        this.tvEmptyCart = (TextView) findViewById(R.id.tvEmptyCart);
        CartListAdapter cartListAdapter = new CartListAdapter(this, new ArrayList(0), this.addToCartManager, this.interfaceClickListener, "", new PostItemListener() {
            public void onPostClick(int itemid, int viewid, int adapterposition) {
                IdSelector.setProductId(adapterposition);
                CartActivity.this.startActivity(new Intent(CartActivity.this, BookDetailsActivity.class));
            }
        });
        this.mProductAdapter = cartListAdapter;
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setHasFixedSize(true);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.cartItemListManager.fetchCartItemList();
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

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btCheckOut) {
            CheckOutActivity.setRentBookID(null);
            Intent intent = new Intent(this, CheckOutActivity.class);
            intent.putExtra("flagCartOrder", "0");
            startActivity(intent);
        } else if (id == R.id.btRemoveAll) {
            this.addToCartManager.removeFromCart("", true);
        } else if (id == R.id.imgBckMyCart) {
            onBackPressed();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    public void onDataFetchSuccess(String successMessage, String total_cart_items) {
        this.cartItemListManager.fetchCartItemList();
    }

    public void onDataFetchFailure(String errorMessage, String total_cart_items) {
        CommonUtilities.showToastOnMainThread(this, errorMessage);
    }

    public void onListFetchSuccess(List<CartItems> cartItemLists) {
        this.cartItemList = cartItemLists;
        if (!this.cartItemList.isEmpty()) {
            this.mProductAdapter.updateProducts(this.cartItemList);
            this.btCheckOut.setVisibility(View.VISIBLE);
            this.btRemoveAll.setVisibility(View.VISIBLE);
            this.tvEmptyCart.setVisibility(View.GONE);
        } else {
            this.mProductAdapter.updateProducts(this.cartItemList);
            this.tvEmptyCart.setVisibility(View.VISIBLE);
            this.btCheckOut.setVisibility(View.GONE);
            this.btRemoveAll.setVisibility(View.GONE);
        }
        this.recyclerView.setAdapter(this.mProductAdapter);
    }

    public void onListFetchFailure(String errorMsg) {
        CommonUtilities.showToastOnMainThread(this, errorMsg);
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
        ((UpdateQty) RetrofitClient.getClient(ServerEndpoint.UPDATE_QTY).create(UpdateQty.class)).post(customerId, cartId, value, this.deviceId).enqueue(this);
    }

    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            if (new JSONObject(((ResponseBody) response.body()).string()).getJSONObject("result").getString(NotificationCompat.CATEGORY_MESSAGE).equalsIgnoreCase("1")) {
                this.cartItemListManager.fetchCartItemList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onFailure(Call<ResponseBody> call, Throwable t) {
    }
}
