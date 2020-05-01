package com.payu.payuui.Adapter;

import android.os.Bundle;
import androidx.core.app.Fragment;
import androidx.core.app.FragmentManager;
import androidx.core.app.FragmentStatePagerAdapter;

import com.payu.india.Model.PayuResponse;
import com.payu.india.Payu.PayuConstants;
import com.payu.payuui.Fragment.CreditDebitFragment;
import com.payu.payuui.Fragment.NetBankingFragment;
import com.payu.payuui.Fragment.SavedCardsFragment;
import com.payu.payuui.SdkuiUtil.SdkUIConstants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by piyush on 29/7/15.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> mTitles;
    private PayuResponse payuResponse;
    private PayuResponse valueAddedResponse;
    private HashMap<String, String> oneClickCardTokens;
    private HashMap<Integer, Fragment> mPageReference = new HashMap<Integer, Fragment>();

    public PagerAdapter(FragmentManager fragmentManager, ArrayList<String> titles, PayuResponse payuResponse, PayuResponse valueAddedResponse, HashMap<String, String> oneClickCardTokens) {
        super(fragmentManager);
        this.mTitles = titles;
        this.payuResponse = payuResponse;
        this.valueAddedResponse = valueAddedResponse;
        this.oneClickCardTokens = oneClickCardTokens;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (mTitles.get(i)) {
            case SdkUIConstants.SAVED_CARDS:
                fragment = new SavedCardsFragment();
                bundle.putParcelableArrayList(PayuConstants.STORED_CARD, payuResponse.getStoredCards());
                bundle.putSerializable(SdkUIConstants.VALUE_ADDED, valueAddedResponse.getIssuingBankStatus());
                bundle.putInt(SdkUIConstants.POSITION, i);
                bundle.putSerializable(PayuConstants.ONE_CLICK_CARD_TOKENS, oneClickCardTokens);
                fragment.setArguments(bundle);
                mPageReference.put(i, fragment);
                return fragment;

            case SdkUIConstants.CREDIT_DEBIT_CARDS:
                fragment = new CreditDebitFragment();
                bundle.putParcelableArrayList(PayuConstants.CREDITCARD, payuResponse.getCreditCard());
                bundle.putParcelableArrayList(PayuConstants.DEBITCARD, payuResponse.getDebitCard());
                bundle.putSerializable(SdkUIConstants.VALUE_ADDED, valueAddedResponse.getIssuingBankStatus());
                bundle.putInt(SdkUIConstants.POSITION, i);
                fragment.setArguments(bundle);
                mPageReference.put(i, fragment);
                return fragment;

            case SdkUIConstants.NET_BANKING:
                fragment = new NetBankingFragment();
                bundle.putParcelableArrayList(PayuConstants.NETBANKING, payuResponse.getNetBanks());
                bundle.putSerializable(SdkUIConstants.VALUE_ADDED, valueAddedResponse.getNetBankingDownStatus());
                fragment.setArguments(bundle);
                mPageReference.put(i, fragment);
                return fragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        if (mTitles != null)
            return mTitles.size();
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    public Fragment getFragment(int key) {
        return mPageReference.get(key);
    }


}
