package com.essensys.cashsaverz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.adapter.RentedBookListAdapter;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.model.UserRentedBookList;
import com.essensys.cashsaverz.networkManager.RentedBooksFetcher;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MyRentedBooksFragment extends CustomFragment implements RentedBooksFetcher.StatusListener {

    private RecyclerView rvRentedBooklist;
    private RentedBooksFetcher rentedBooksFetcher;
    private RentedBookListAdapter rentedBookListAdapter;
    private TextView tvEmptyList;
    private int paginationOffset = 0;
    private boolean isMoreDataAvailable = true;
    private List<UserRentedBookList> userRentedBookList = new ArrayList<UserRentedBookList>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rented_books, container, false);

        rvRentedBooklist = view.findViewById(R.id.rvRentedBooklist);
        tvEmptyList = view.findViewById(R.id.tvEmptyList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvRentedBooklist.setLayoutManager(layoutManager);
        rvRentedBooklist.setNestedScrollingEnabled(false);
        rvRentedBooklist.setHasFixedSize(true);

        rentedBooksFetcher = new RentedBooksFetcher(getActivity(), this);
        rvRentedBooklist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int lastCompletelyVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int totalItems = layoutManager.getItemCount();
                if (lastCompletelyVisibleItemPosition == totalItems - 1) {
                    if (isMoreDataAvailable) {
                        paginationOffset = paginationOffset + 10;
                        rentedBooksFetcher.fetchRentedBookList(paginationOffset);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setupBadge(HomeActivity.getCartCount());
        userRentedBookList.clear();
        isMoreDataAvailable = true;
        paginationOffset = 0;
        rentedBooksFetcher.fetchRentedBookList(paginationOffset);
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
    public void onListFetchSuccess(List<UserRentedBookList> productList) {
        if (productList == null || productList.isEmpty()) {
            if (paginationOffset == 0) {
                tvEmptyList.setVisibility(View.VISIBLE);
            }
            isMoreDataAvailable = false;
            paginationOffset = 0;
        } else {
            if (userRentedBookList == null) {
                userRentedBookList = productList;
            } else {
                userRentedBookList.addAll(productList);
            }
            tvEmptyList.setVisibility(View.GONE);
            rentedBookListAdapter = new RentedBookListAdapter(getActivity(), userRentedBookList);
            rvRentedBooklist.setAdapter(rentedBookListAdapter);
        }

        if (rentedBookListAdapter.getItemCount() > 0) {
            tvEmptyList.setVisibility(View.GONE);
        }

    }

    @Override
    public void onListFetchFailure(String errorMsg) {
        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
    }

}
