package com.essensys.cashsaverz.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.essensys.cashsaverz.fragments.FragmentCatSubCat;
import com.essensys.cashsaverz.model.Product;
import java.util.List;

public class SimpleViewpagerAdapter extends FragmentPagerAdapter {
    private List<Product> productList;

    public SimpleViewpagerAdapter(FragmentManager fm, List<Product> productList2) {
        super(fm);
        this.productList = productList2;
    }

    public Fragment getItem(int position) {
        if (position == 0) {
            return new FragmentCatSubCat();
        }
        if (position == 1) {
            return new FragmentCatSubCat();
        }
        if (position == 2) {
            return new FragmentCatSubCat();
        }
        if (position == 3) {
            return new FragmentCatSubCat();
        }
        if (position != 4) {
            return null;
        }
        return new FragmentCatSubCat();
    }

    public int getCount() {
        return 5;
    }

    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "All";
        }
        if (position == 1) {
            return "TabLayout2";
        }
        if (position == 2) {
            return "TabLayout3";
        }
        if (position == 3) {
            return "TabLayout4";
        }
        if (position != 4) {
            return null;
        }
        return "TabLayout5";
    }
}
