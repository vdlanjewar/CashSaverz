package com.essensys.cashsaverz.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.adapter.NewBookRequestListAdapter;
import com.essensys.cashsaverz.adapter.RentedBookListAdapter;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.model.NewBookRequestList;
import com.essensys.cashsaverz.model.UserRentedBookList;
import com.essensys.cashsaverz.networkManager.RentedBooksFetcher;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */

public class NewBookRequestListFragment extends CustomFragment implements Callback<ResponseBody> {

    private RecyclerView rvRequestedBooksList;
    //private RentedBooksFetcher rentedBooksFetcher;
    private NewBookRequestListAdapter bookRequestListAdapter;
    private TextView tvEmptyList;
    private int paginationOffset = 0;
    private boolean isMoreDataAvailable = true;
    private List<NewBookRequestList> bookRequestList = new ArrayList<NewBookRequestList>();

    public NewBookRequestListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_book_request_list, container, false);

        rvRequestedBooksList = view.findViewById(R.id.rvRequestedBooksList);
        tvEmptyList = view.findViewById(R.id.tvEmptyList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvRequestedBooksList.setLayoutManager(layoutManager);
        rvRequestedBooksList.setNestedScrollingEnabled(false);
        rvRequestedBooksList.setHasFixedSize(true);

        // rentedBooksFetcher = new RentedBooksFetcher(getActivity(), this);
//        rvRequestedBooksList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
//                int lastCompletelyVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
//                int totalItems = layoutManager.getItemCount();
//                if (lastCompletelyVisibleItemPosition == totalItems - 1) {
//                    if (isMoreDataAvailable) {
//                        paginationOffset = paginationOffset + 10;
//                       // rentedBooksFetcher.fetchRentedBookList(paginationOffset);
//                    }
//                }
//            }
//        });

        if (!App.isUserLoggedIn(getActivity())) {
            CommonUtilities.openFragmentContainerActivityWithDelay(getActivity(), R.id.nav_login,
                    0);
        }
        fetchBooksRequestData();
        return view;
    }

    private void fetchBooksRequestData() {
        bookRequestList.clear();
        RetrofitClient
                .getClient(Constant.ServerEndpoint.NEW_BOOK_REQUEST_LIST)
                .create(RetrofitInterfaces.NewBookRequestList.class)
                .post(App.getCurrentUser(getActivity()).getUserAuth()!=null?App.getCurrentUser(getActivity()).getUserAuth():"", App.getCurrentUser(getActivity()).getCustomerId())
                .enqueue(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // ((HomeActivity) getActivity()).setupBadge(HomeActivity.getCartCount());
        // bookRequestList.clear();
        //isMoreDataAvailable = true;
        // paginationOffset = 0;
        //  rentedBooksFetcher.fetchRentedBookList(paginationOffset);
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

    //   @Override
//    public void onListFetchSuccess(List<UserRentedBookList> productList) {
//        if (productList == null || productList.isEmpty()) {
//            if (paginationOffset == 0) {
//                tvEmptyList.setVisibility(View.VISIBLE);
//            }
//            isMoreDataAvailable = false;
//            paginationOffset = 0;
//        } else {
//            if (userRentedBookList == null) {
//                userRentedBookList = productList;
//            } else {
//                userRentedBookList.addAll(productList);
//            }
//            tvEmptyList.setVisibility(View.GONE);
//            rentedBookListAdapter = new RentedBookListAdapter(getActivity(), userRentedBookList);
//            rvRequestedBooksList.setAdapter(rentedBookListAdapter);
//        }
//
//        if (rentedBookListAdapter.getItemCount() > 0) {
//            tvEmptyList.setVisibility(View.GONE);
//        }

    //   }

//    @Override
//    public void onListFetchFailure(String errorMsg) {
//       // Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            String stringResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(stringResponse);
            JSONObject result = jsonObject.getJSONObject("result");
            String msg = result.getString("msg");

            if (msg.contentEquals("1")) {
                JSONArray products = result.getJSONArray("arrNewBookList");

                bookRequestList = new Gson()
                        .fromJson(products.toString(), new TypeToken<List<NewBookRequestList>>() {
                        }.getType());

                if (bookRequestList == null || bookRequestList.isEmpty()) {
                    tvEmptyList.setVisibility(View.VISIBLE);
                } else {
                    tvEmptyList.setVisibility(View.GONE);
                    bookRequestListAdapter = new NewBookRequestListAdapter(getActivity(), bookRequestList);
                    rvRequestedBooksList.setAdapter(bookRequestListAdapter);
                }

                if (bookRequestListAdapter.getItemCount() > 0) {
                    tvEmptyList.setVisibility(View.GONE);
                }

            } else {

                String msg_string = result.getString("msg_string");
                Toast.makeText(getActivity(), msg_string, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
    }

}
