package com.essensys.cashsaverz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.adapter.PaymentHistoryAdapter;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.model.PaymentHistory;
import com.essensys.cashsaverz.networkManager.PaymentHistoryManager;

import java.util.List;

import androidx.annotation.NonNull;

public class PaymentHistoryFragment extends CustomFragment implements PaymentHistoryManager
        .StatusListener {
    private ListView listView;
    private TextView tvNoDataAvailable;
    private PaymentHistoryManager paymentHistoryManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_history, container,
                false);

        listView = view.findViewById(R.id.listView);
        tvNoDataAvailable = view.findViewById(R.id.tvNoDataAvailable);
        paymentHistoryManager = new PaymentHistoryManager(getActivity(), this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setupBadge(HomeActivity.getCartCount());
        paymentHistoryManager.fetchPaymentHistoryList();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

    }

    @Override
    public void onBackPressed() {
        CommonUtilities.openFragmentContainerActivityWithDelay(getActivity(), R.id.nav_home,
                0);
    }

    @Override
    public void onListFetchSuccess(List<PaymentHistory> paymentHistoryList) {
        if (paymentHistoryList == null || paymentHistoryList.isEmpty()) {
            tvNoDataAvailable.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            tvNoDataAvailable.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);

            PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(getActivity(),
                    paymentHistoryList);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onListFetchFailure(String errorMsg) {
        CommonUtilities.showToastOnMainThread(getActivity(), errorMsg);
    }
}
