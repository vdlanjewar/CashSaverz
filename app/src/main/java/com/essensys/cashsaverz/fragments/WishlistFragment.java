package com.essensys.cashsaverz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.adapter.WishListAdapter;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.model.Product;
import com.essensys.cashsaverz.networkManager.WishlistFetcher;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WishlistFragment extends CustomFragment implements WishlistFetcher.StatusListener {

    private RecyclerView rvWishlist;
    private WishlistFetcher wishlistFetcher;
    private WishListAdapter wishListAdapter;
    private TextView tvEmptyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        rvWishlist = view.findViewById(R.id.rvWishlist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvWishlist.setLayoutManager(layoutManager);
        rvWishlist.setNestedScrollingEnabled(false);
        rvWishlist.setHasFixedSize(true);

        wishlistFetcher = new WishlistFetcher(getActivity(), this);

        tvEmptyList = view.findViewById(R.id.tvEmptyList);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setupBadge(HomeActivity.getCartCount());
        wishlistFetcher.fetchWishList();
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
    public void onListFetchSuccess(List<Product> productList) {
        if (productList == null || productList.isEmpty()) {
            tvEmptyList.setVisibility(View.VISIBLE);
        } else {
            tvEmptyList.setVisibility(View.GONE);
            wishListAdapter = new WishListAdapter(getActivity(), productList);
            rvWishlist.setAdapter(wishListAdapter);
        }

        if(wishListAdapter.getItemCount()>0)
        {
            tvEmptyList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onListFetchFailure(String errorMsg) {
        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
    }
}
