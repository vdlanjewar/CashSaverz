package com.essensys.cashsaverz.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.adapter.DeliveryHistoryBookListAdapter;
import com.essensys.cashsaverz.adapter.RentedBookListAdapter;
import com.essensys.cashsaverz.adapter.TodaysDeliveryListAdapter;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.model.UserDeliveryBookList;
import com.essensys.cashsaverz.model.UserDeliveryHistoryBookList;
import com.essensys.cashsaverz.model.UserRentedBookList;
import com.essensys.cashsaverz.networkManager.DeliveryHistoryBookFetcher;
import com.essensys.cashsaverz.networkManager.TodaysDeliveryBookFetcher;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeliveryHistoryFragment extends CustomFragment implements DeliveryHistoryBookFetcher.StatusListener{

    private RecyclerView rvDeliveryHistoryBooklist;
    private DeliveryHistoryBookFetcher deliveryHistoryBookFetcher;
    private DeliveryHistoryBookListAdapter deliveryHistoryBookListAdapter;
    private TextView tvEmptyList;
    private int paginationOffset = 0;
    private boolean isMoreDataAvailable = true;
    private List<UserDeliveryHistoryBookList> userDeliveryHistoryBookLists= new ArrayList<UserDeliveryHistoryBookList>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery_history, container, false);
        tvEmptyList = view.findViewById(R.id.tvEmptyList);
        rvDeliveryHistoryBooklist = view.findViewById(R.id.rvDeliveryHistoryBooklist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvDeliveryHistoryBooklist.setLayoutManager(layoutManager);
        rvDeliveryHistoryBooklist.setNestedScrollingEnabled(false);
        rvDeliveryHistoryBooklist.setHasFixedSize(true);

        deliveryHistoryBookFetcher = new DeliveryHistoryBookFetcher(getActivity(), this);
        rvDeliveryHistoryBooklist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int lastCompletelyVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int totalItems = layoutManager.getItemCount();
                if (lastCompletelyVisibleItemPosition == totalItems - 1) {
                    if (isMoreDataAvailable) {
                        paginationOffset = paginationOffset + 10;
                        deliveryHistoryBookFetcher.fetchDeliveryHistoryBookList(paginationOffset);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        userDeliveryHistoryBookLists.clear();
        isMoreDataAvailable=true;
        paginationOffset=0;
        deliveryHistoryBookFetcher.fetchDeliveryHistoryBookList(paginationOffset);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onListFetchSuccess(List<UserDeliveryHistoryBookList> userDeliveryHistoryBookLists) {
        if (userDeliveryHistoryBookLists == null || userDeliveryHistoryBookLists.isEmpty()) {
            if(paginationOffset==0){
                tvEmptyList.setVisibility(View.VISIBLE);}
            isMoreDataAvailable = false;
            paginationOffset = 0;
        } else {
            if (this.userDeliveryHistoryBookLists== null) {
                this.userDeliveryHistoryBookLists = userDeliveryHistoryBookLists;
            } else {
                this.userDeliveryHistoryBookLists.addAll(userDeliveryHistoryBookLists);
            }
            tvEmptyList.setVisibility(View.GONE);
            deliveryHistoryBookListAdapter = new DeliveryHistoryBookListAdapter(getActivity(), this.userDeliveryHistoryBookLists);
            rvDeliveryHistoryBooklist.setAdapter(deliveryHistoryBookListAdapter);
        }


    }

    @Override
    public void onListFetchFailure(String errorMsg) {

    }
}
