package com.essensys.cashsaverz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.adapter.TodaysDeliveryListAdapter;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.model.UserDeliveryBookList;
import com.essensys.cashsaverz.networkManager.TodaysDeliveryBookFetcher;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TodaysDeliveryFragment extends CustomFragment implements TodaysDeliveryBookFetcher.StatusListener {

    private RecyclerView rvTodaysDeliveryBooklist;
    private TodaysDeliveryBookFetcher todaysDeliveryBookFetcher;
    private TodaysDeliveryListAdapter todaysDeliveryListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todays_delivery, container, false);
        rvTodaysDeliveryBooklist = view.findViewById(R.id.rvTodaysDeliveryBooklist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvTodaysDeliveryBooklist.setLayoutManager(layoutManager);
        rvTodaysDeliveryBooklist.setNestedScrollingEnabled(false);
        rvTodaysDeliveryBooklist.setHasFixedSize(true);

        todaysDeliveryBookFetcher = new TodaysDeliveryBookFetcher(getActivity(), this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        todaysDeliveryBookFetcher.fetchTodaysDeliveryBookList();
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
    public void onListFetchSuccess(List<UserDeliveryBookList> userDeliveryBookLists) {
        todaysDeliveryListAdapter = new TodaysDeliveryListAdapter(getActivity(), userDeliveryBookLists);
        rvTodaysDeliveryBooklist.setAdapter(todaysDeliveryListAdapter);
    }

    @Override
    public void onListFetchFailure(String errorMsg) {

    }
}
