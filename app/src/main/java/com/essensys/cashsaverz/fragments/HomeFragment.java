package com.essensys.cashsaverz.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.BookListActivity;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.adapter.CategoryListAdapter;
import com.essensys.cashsaverz.adapter.PopularBookListAdapter;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.model.Category;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.model.Product;
import com.essensys.cashsaverz.model.User;
import com.essensys.cashsaverz.model.UserCurrentCartItemsDetails;
import com.essensys.cashsaverz.model.UserCurrentMembershipPlan;
import com.essensys.cashsaverz.networkManager.HomePageCategoryListManager;
import com.essensys.cashsaverz.preference.SharedPreferenceManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends CustomFragment implements View.OnClickListener,
        HomePageCategoryListManager.StatusListener {
    private RecyclerView categoryRecyclerView, popularBooksRecyclerView;
    private CategoryListAdapter mCategoryAdapter;
    private PopularBookListAdapter mPopularBookListAdapter;
    private LinearLayout llCategoryLayout;
    private Button bSearch, btSubscribeNow;
    private EditText etSearch;
    private String catId="";
    private HomePageCategoryListManager homePageCategoryListManager;
    public static String deviceId = "";
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
   // private UserCurrentMembershipPlanManager userCurrentMembershipPlanManager; // Added by SM
    private List<Category> categoryItemList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        etSearch = view.findViewById(R.id.etSearch);
        btSubscribeNow = view.findViewById(R.id.btSubscribeNow);

        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        llCategoryLayout = view.findViewById(R.id.llCategoryLayout);
        llCategoryLayout.setVisibility(View.GONE);
        //popularBooksRecyclerView = view.findViewById(R.id.popularBooksRecyclerView);

        view.findViewById(R.id.btSubscribeNow).setOnClickListener(this);
        view.findViewById(R.id.bSearch).setOnClickListener(this);

        RecyclerView.LayoutManager category_layoutManager = new GridLayoutManager(getActivity(), 2);
        categoryRecyclerView.setLayoutManager(category_layoutManager);
        categoryRecyclerView.setNestedScrollingEnabled(false);
        categoryRecyclerView.setHasFixedSize(true);

       /* RecyclerView.LayoutManager popularbook_layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        popularBooksRecyclerView.setLayoutManager(popularbook_layoutManager);
        popularBooksRecyclerView.setNestedScrollingEnabled(false);
        popularBooksRecyclerView.setHasFixedSize(true);*/

        homePageCategoryListManager = new HomePageCategoryListManager(getActivity(), this);
        //userCurrentMembershipPlanManager = new UserCurrentMembershipPlanManager(getActivity(), this);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        requestReadPhoneStatePermission();
        homePageCategoryListManager.fetchCategoryList();

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"MissingPermission", "WrongConstant"})
    private void requestReadPhoneStatePermission() {
        String str = "android.permission.READ_PHONE_STATE";
        String str2 = "phone";
        if (ActivityCompat.checkSelfPermission(getActivity(), str) == 0) {
            deviceId = ((TelephonyManager) getActivity().getSystemService(str2)).getImei();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), str)) {
            deviceId = ((TelephonyManager) getActivity().getSystemService(str2)).getImei();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{str}, 0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"MissingPermission", "WrongConstant"})
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 0) {
            return;
        }
        if (grantResults.length == 1 && grantResults[0] == 0) {
            deviceId = ((TelephonyManager) getActivity().getSystemService("phone")).getImei();

        } else {
            Toast.makeText(getActivity(), "Permission Not Granted", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSubscribeNow:
                onPaynowClick();
                break;
            case R.id.bSearch:
                if (etSearch.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter search keyword",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    //commented by ND
//                    BookListActivity.setInputparameters("", "Book My Book", true,
//                            etSearch.getText().toString().trim(), null);
//                    getActivity().startActivity(new Intent(getActivity(), BookListActivity.class));
//
                    BookListActivity.setInputparameters(catId, "Cash Saverz", true,
                    etSearch.getText().toString().trim(), null);
                getActivity().startActivity(new Intent(getActivity(), BookListActivity.class));
        }
                break;

        }
    }

    public void onPaynowClick() {
        CommonUtilities.openFragmentContainerActivityWithDelay(getActivity(), R.id.nav_browse_plans,
                0);
    }

    @Override
    public void onListFetchSuccess(List<Category> categoryItemList, List<Product> popularBooksList,
                                   List<UserCurrentMembershipPlan> userCurrentMembershipPlanList,
                                   List<UserCurrentCartItemsDetails> userCurrentCartItemsDetailsList) {

        this.categoryItemList = categoryItemList;

        for(int i=0;i<categoryItemList.size();i++)
        {
           catId=categoryItemList.get(i).getCatId();
        }
        if (categoryItemList == null) {
            return;
        } else {
            llCategoryLayout.setVisibility(View.VISIBLE);
            mCategoryAdapter = new CategoryListAdapter(getActivity(), categoryItemList);
            categoryRecyclerView.setAdapter(mCategoryAdapter);

          /*  mPopularBookListAdapter = new PopularBookListAdapter(getActivity(), popularBooksList);
            popularBooksRecyclerView.setAdapter(mPopularBookListAdapterularBookListAdapter);*/
            if (userCurrentMembershipPlanList.size() > 0) {
                App.getCurrentUser(getActivity()).setUserCurrentMembershipPlan(userCurrentMembershipPlanList);
                storeUserDetails(App.getCurrentUser(getActivity()));
                setSubscribeNowButtonLabel();
                ((HomeActivity) getActivity()).setMembershipToNavHeader(userCurrentMembershipPlanList.get(0).getMembershipPlanTitle());
            }
        }
        HomeActivity.setCartCount(userCurrentCartItemsDetailsList.get(0).getCartItemCount());
        ((HomeActivity) getActivity()).setupBadge(HomeActivity.getCartCount());
    }

    @Override
    public void onListFetchFailure(String errorMsg) {
        CommonUtilities.showToastOnMainThread(getActivity(), errorMsg);
    }

    @Override
    public void onBackPressed() {
        getActivity().finish();
    }

    private void storeUserDetails(User user) {
        try {
            SharedPreferenceManager.with(getActivity()).updateLoggedInUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSubscribeNowButtonLabel() {
        if (App.isUserLoggedIn(getActivity())) {
            if (!App.getCurrentUser(getActivity()).getUserCurrentMembershipPlan().isEmpty()) {
                btSubscribeNow.setText("Upgrade Now");
            } else {
                btSubscribeNow.setText("Subscribe Now");
            }
        }
    }

}
