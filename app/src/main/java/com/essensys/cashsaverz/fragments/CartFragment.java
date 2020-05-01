package com.essensys.cashsaverz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.essensys.cashsaverz.Interface.InterfaceClickListener;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.BookDetailsActivity;
import com.essensys.cashsaverz.activities.CheckOutActivity;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.adapter.CartListAdapter;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.model.CartItems;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.model.IdSelector;
import com.essensys.cashsaverz.networkManager.AddToCartManager;
import com.essensys.cashsaverz.networkManager.CartItemListManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartFragment extends CustomFragment implements View.OnClickListener,
        CartItemListManager.StatusListener, AddToCartManager.StatusListener {

    private RecyclerView recyclerView;
    private Button btCheckOut, btRemoveAll;
    private CartListAdapter mProductAdapter;
    private List<CartItems> cartItemList;
    private TextView tvEmptyCart;
    private CartItemListManager cartItemListManager;
    private AddToCartManager addToCartManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartItemListManager = new CartItemListManager(getActivity(), this);
        addToCartManager = new AddToCartManager(getActivity(), this);

        recyclerView = view.findViewById(R.id.recyclerView);
        btCheckOut = view.findViewById(R.id.btCheckOut);
        btCheckOut.setOnClickListener(this);
        btRemoveAll = view.findViewById(R.id.btRemoveAll);
        btRemoveAll.setOnClickListener(this);
        tvEmptyCart = view.findViewById(R.id.tvEmptyCart);

        mProductAdapter = new CartListAdapter(getActivity(), new ArrayList<CartItems>(0),
                addToCartManager,interfaceClickListener, "", new CartListAdapter.PostItemListener() {
            @Override
            public void onPostClick(int itemid, int viewid, int adapterposition) {
                IdSelector.setProductId(adapterposition);
                Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
                getActivity().startActivity(intent);
            }
        });

        RecyclerView.LayoutManager booklist_layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(booklist_layoutManager);
        recyclerView.setHasFixedSize(true);

        return view;
    }
    InterfaceClickListener interfaceClickListener=new InterfaceClickListener() {
        @Override
        public void Onclick(String id, String value) {
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setupBadge(HomeActivity.getCartCount());
        cartItemListManager.fetchCartItemList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

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
            case R.id.btCheckOut:
                CheckOutActivity.setRentBookID(null);
                startActivity(new Intent(getActivity(), CheckOutActivity.class));
                break;
            case R.id.btRemoveAll:
                addToCartManager.removeFromCart("", true);
                break;
        }
    }

    @Override
    public void onListFetchSuccess(List<CartItems> cartItemLists) {
        this.cartItemList = cartItemLists;

        if (!cartItemList.isEmpty()) {
            mProductAdapter.updateProducts(cartItemList);
            btCheckOut.setVisibility(View.VISIBLE);
            btRemoveAll.setVisibility(View.VISIBLE);
            tvEmptyCart.setVisibility(View.GONE);
        } else {
            mProductAdapter.updateProducts(cartItemList);
            tvEmptyCart.setVisibility(View.VISIBLE);
            btCheckOut.setVisibility(View.GONE);
            btRemoveAll.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(mProductAdapter);

    }

    @Override
    public void onListFetchFailure(String errorMsg) {
        CommonUtilities.showToastOnMainThread(getActivity(), errorMsg);
    }

    @Override
    public void onDataFetchSuccess(String successMessage, String total_cart_items) {
        HomeActivity.setCartCount(Integer.parseInt(total_cart_items));
        ((HomeActivity) getActivity()).setupBadge(HomeActivity.getCartCount());
        cartItemListManager.fetchCartItemList();
    }

    @Override
    public void onDataFetchFailure(String errorMessage, String total_cart_items) {
        HomeActivity.setCartCount(Integer.parseInt(total_cart_items));
        ((HomeActivity) getActivity()).setupBadge(HomeActivity.getCartCount());
        CommonUtilities.showToastOnMainThread(getActivity(), errorMessage);
    }
}
