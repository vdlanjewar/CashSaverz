package com.essensys.cashsaverz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.adapter.MembershipPlansAdapter;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.model.CityList;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.model.DeliveryTimeSlot;
import com.essensys.cashsaverz.model.MembershipPlans;
import com.essensys.cashsaverz.networkManager.FieldDetailsManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BrowsePlansFragment extends CustomFragment implements FieldDetailsManager.StatusListener {

    private FieldDetailsManager fieldDetailsManager;
    private List<MembershipPlans> membershipPlans;
    private RecyclerView rvMembershipPlans;
    private MembershipPlansAdapter membershipPlansAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse_plans, container, false);
        fieldDetailsManager = new FieldDetailsManager(getActivity(), this);
        rvMembershipPlans = view.findViewById(R.id.rvMembershipPlans);
        membershipPlansAdapter = new MembershipPlansAdapter(getActivity(),
                new ArrayList<MembershipPlans>(0),
                new MembershipPlansAdapter.PostItemListener() {
                    @Override
                    public void onPostClick(int itemid, int viewid, int adapterposition) {

                    }
                }) {
        };
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setupBadge(HomeActivity.getCartCount());
        fieldDetailsManager.getFields();
    }

    @Override
    public void onFieldDataFetchSuccess(List<CityList> cityList, List<MembershipPlans> membershipPlans, List<DeliveryTimeSlot> deliveryTimeSlots) {
        this.membershipPlans = membershipPlans;
        RecyclerView.LayoutManager booklist_layoutManager = new LinearLayoutManager(getActivity());
        rvMembershipPlans.setLayoutManager(booklist_layoutManager);
        rvMembershipPlans.setAdapter(membershipPlansAdapter);
        rvMembershipPlans.setHasFixedSize(true);
        membershipPlansAdapter.updatePlans(membershipPlans);
    }

    @Override
    public void onFieldDataFetchFailure(String errorMessage) {
        CommonUtilities.showToastOnMainThread(getActivity(), errorMessage);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    public void onBackPressed() {
        CommonUtilities.openFragmentContainerActivityWithDelay(getActivity(),R.id.nav_home,
                0);
        getActivity().finish();
    }
}
