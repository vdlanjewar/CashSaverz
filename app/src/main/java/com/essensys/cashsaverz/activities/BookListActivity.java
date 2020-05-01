package com.essensys.cashsaverz.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnCloseListener;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.Interface.InterfaceClickListener;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.adapter.AdapterSubCategory;
import com.essensys.cashsaverz.adapter.BookProductListAdapter;
import com.essensys.cashsaverz.adapter.SimpleViewpagerAdapter;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.CustomViewPager;
import com.essensys.cashsaverz.helper.PaginationScrollListener;
import com.essensys.cashsaverz.localDatabases.DatabaseHandler;
import com.essensys.cashsaverz.model.Product;
import com.essensys.cashsaverz.model.SubCategory;
import com.essensys.cashsaverz.networkManager.ProductListManager;
import com.essensys.cashsaverz.networkManager.ProductListManager.StatusListener;
import com.google.android.material.tabs.TabLayout;
import java.util.List;

public class BookListActivity extends AppCompatActivity implements StatusListener, OnClickListener {
    private static final int PAGE_START = 0;
    private static String catId;
    private static String catName;
    private static boolean isSearchedFromHomePage;
    private static List<Product> productList;
    /* access modifiers changed from: private */
    public static String searchKeyword;
    /* access modifiers changed from: private */
    public int TOTAL_PAGES = 1;
    /* access modifiers changed from: private */
    public int currentPage = 0;
    DatabaseHandler databaseHandler;
    private boolean flagSearch;
    InterfaceClickListener interfaceClickListener = new InterfaceClickListener() {
        public void Onclick(String subcatId, String value) {
            BookListActivity.this.subcat_id = subcatId;
            BookListActivity bookListActivity = BookListActivity.this;
            bookListActivity.fetchData(0, bookListActivity.subcat_id);
        }
    };
    /* access modifiers changed from: private */
    public boolean isLastPage = false;
    /* access modifiers changed from: private */
    public boolean isLoading = false;
    private boolean isMoreDataAvailable = true;
    /* access modifiers changed from: private */
    public boolean isSearchedFromListingPage;
    LinearLayoutManager layoutManager;
    LinearLayoutManager layoutManagerHori;
    private LinearLayout mLinearAll;
    private BookProductListAdapter mProductListAdapter;
    private ProductListManager mProductListManager;
    private RelativeLayout mRelSubCat;
    /* access modifiers changed from: private */
    public TabLayout mTab;
    /* access modifiers changed from: private */
    public CustomViewPager mViewPager;
    /* access modifiers changed from: private */
    public int paginationOffset = 0;
    private RecyclerView recycl_subcat;
    /* access modifiers changed from: private */
    public RecyclerView recyclerView;
    /* access modifiers changed from: private */
    public String subcat_id = "0";
    private TextView textCartItemCount;
    private TextView tvEmptyList;
    private TextView tvEmptyMessage;

    static {
        String str = "";
        searchKeyword = str;
        catName = str;
    }

    public static void setProductList(List<Product> productList2) {
        productList = productList2;
    }

    public static void setInputparameters(String catId2, String catName2, boolean isSearched, String searchKeyword2, List<Product> productList2) {
        isSearchedFromHomePage = isSearched;
        searchKeyword = searchKeyword2;
        catId = catId2;
        catName = catName2;
        setProductList(productList2);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_book_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle((CharSequence) catName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BookListActivity.this.finish();
            }
        });
        this.mProductListManager = new ProductListManager(this, this);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recycl_subcat = (RecyclerView) findViewById(R.id.recycl_subCategory);
        this.tvEmptyList = (TextView) findViewById(R.id.tvEmptyList);
        this.tvEmptyMessage = (TextView) findViewById(R.id.tvEmptyMessage);
        this.mRelSubCat = (RelativeLayout) findViewById(R.id.rel_subcat);
        this.mLinearAll = (LinearLayout) findViewById(R.id.lin_all);
        this.mLinearAll.setOnClickListener(this);
        this.layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setNestedScrollingEnabled(false);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManagerHori = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.recycl_subcat.setLayoutManager(this.layoutManagerHori);
        this.recycl_subcat.setNestedScrollingEnabled(false);
        this.recycl_subcat.setHasFixedSize(true);
        this.mTab = (TabLayout) findViewById(R.id.tablist);
        this.mViewPager = (CustomViewPager) findViewById(R.id.view_subcat);

        fetchData(this.paginationOffset, this.subcat_id);

        this.recyclerView.addOnScrollListener(new PaginationScrollListener(this.layoutManager) {
            /* access modifiers changed from: protected */
            public void loadMoreItems() {
                BookListActivity.this.isLoading = true;
                BookListActivity bookListActivity = BookListActivity.this;
                bookListActivity.currentPage = bookListActivity.currentPage + 1;
                BookListActivity.this.TOTAL_PAGES = BookListActivity.this.TOTAL_PAGES + 1;
                BookListActivity bookListActivity2 = BookListActivity.this;
                bookListActivity2.paginationOffset = bookListActivity2.paginationOffset + 10;
                BookListActivity bookListActivity3 = BookListActivity.this;
                bookListActivity3.fetchData(bookListActivity3.paginationOffset, BookListActivity.this.subcat_id);
            }

            public int getTotalPageCount() {
                return BookListActivity.this.TOTAL_PAGES;
            }

            public boolean isLastPage() {
                return BookListActivity.this.isLastPage;
            }

            public boolean isLoading() {
                return BookListActivity.this.isLoading;
            }
        });
    }

    private void setViewPager() {
        this.mViewPager.setAdapter(new SimpleViewpagerAdapter(getSupportFragmentManager(), productList));
        this.mTab.post(new Runnable() {
            public void run() {
                BookListActivity.this.mTab.setupWithViewPager(BookListActivity.this.mViewPager);
            }
        });
        this.mViewPager.setPagingEnabled(false);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (App.isUserLoggedIn(this)) {
            setupBadge(HomeActivity.getCartCount());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        final MenuItem menuItemCart = menu.findItem(R.id.mycart);
        MenuItem menuItemSearch = menu.findItem(R.id.search_button);
        if (isSearchedFromHomePage) {
            menuItemSearch.setVisible(false);
        } else {
            menuItemSearch.setVisible(true);
        }
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (menuItemSearch != null) {
            searchView = (SearchView) menuItemSearch.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                BookListActivity.searchKeyword = query;
                BookListActivity.this.isSearchedFromListingPage = true;
                BookListActivity.this.paginationOffset = 0;
                BookListActivity bookListActivity = BookListActivity.this;
                bookListActivity.fetchData(bookListActivity.paginationOffset, "0");
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new OnCloseListener() {
            public boolean onClose() {
                menuItemCart.setVisible(true);
                BookListActivity.searchKeyword = "";
                BookListActivity.this.recyclerView.setVisibility(View.VISIBLE);
                BookListActivity bookListActivity = BookListActivity.this;
                bookListActivity.fetchData(bookListActivity.paginationOffset, "0");
                return false;
            }
        });
        searchView.setOnSearchClickListener(new OnClickListener() {
            public void onClick(View view) {
                menuItemCart.setVisible(false);
            }
        });
        View actionView = MenuItemCompat.getActionView(menuItemCart);
        this.textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge(HomeActivity.getCartCount());
        actionView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BookListActivity.this.onOptionsItemSelected(menuItemCart);
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mycart) {
            if (!App.isUserLoggedIn(this)) {
                startActivity(new Intent(this, CartActivity.class));
                finish();
            } else {
                CommonUtilities.openFragmentContainerActivityWithDelay(this, R.id.nav_cart, 0);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onListFetchSuccess(List<Product> productList2, List<SubCategory> subcategoryList) {
        if (subcategoryList.size() > 0) {
            this.mRelSubCat.setVisibility(View.VISIBLE);
            this.recycl_subcat.setAdapter(new AdapterSubCategory(this, subcategoryList, this.interfaceClickListener));
        } else {
            this.mRelSubCat.setVisibility(View.GONE);
        }
        if (productList2.isEmpty()) {
            if (this.isSearchedFromListingPage) {
                this.tvEmptyList.setVisibility(View.VISIBLE);
                this.recyclerView.setVisibility(View.GONE);
            }
            this.isMoreDataAvailable = false;
            this.paginationOffset = 0;
        }
        if (productList != null) {
            setAdapter(productList2, subcategoryList);
        } else {
            setAdapter(productList2, subcategoryList);
        }
    }

    public void onListFetchFailure(String errorMsg) {
        CommonUtilities.showToastOnMainThread(this, errorMsg);
    }

    public void fetchData(int paginationOffset2, String subcat_id2) {
        String str = "";
        if (isSearchedFromHomePage) {
            ProductListManager productListManager = this.mProductListManager;
            String str2 = catId;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(paginationOffset2);
            productListManager.fetchProductList(str2, subcat_id2, sb.toString(), searchKeyword);
        } else if (this.isSearchedFromListingPage) {
            ProductListManager productListManager2 = this.mProductListManager;
            String str3 = catId;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(paginationOffset2);
            productListManager2.fetchProductList(str3, subcat_id2, sb2.toString(), searchKeyword);
        } else {
            ProductListManager productListManager3 = this.mProductListManager;
            String str4 = catId;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append(paginationOffset2);
            productListManager3.fetchProductList(str4, subcat_id2, sb3.toString(), str);
        }
    }

    public static void appendProductList(List<Product> productList2) {
        productList.addAll(productList2);
    }

    public void setAdapter(List<Product> productList2, List<SubCategory> list) {
        if (productList2 == null || productList2.isEmpty()) {
            this.tvEmptyList.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
            return;
        }
        this.tvEmptyList.setVisibility(View.GONE);
        this.recyclerView.setVisibility(View.VISIBLE);
        BookProductListAdapter bookProductListAdapter = this.mProductListAdapter;
        if (bookProductListAdapter != null) {
            bookProductListAdapter.setData(productList2);
        } else {
            this.mProductListAdapter = new BookProductListAdapter(this, productList2);
            this.recyclerView.setAdapter(this.mProductListAdapter);
        }
        if (this.mProductListAdapter.getItemCount() > 0) {
            this.tvEmptyList.setVisibility(View.GONE);
        }
    }

    public void setupBadge(int count) {
        TextView textView = this.textCartItemCount;
        if (textView == null) {
            return;
        }
        if (count != 0) {
            textView.setText(String.valueOf(count));
            if (this.textCartItemCount.getVisibility() != View.VISIBLE) {
                this.textCartItemCount.setVisibility(View.VISIBLE);
            }
        } else if (textView.getVisibility() != View.GONE) {
            this.textCartItemCount.setVisibility(View.GONE);
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.lin_all) {
            fetchData(this.paginationOffset, "0");
        } else if (id == R.id.tvEmptyList) {
            CommonUtilities.openFragmentContainerActivityWithDelay(this, R.id.nav_new_book_request, 14);
            finish();
        }
    }

    private void checkNewBookRequest() {
        if (isSearchedFromHomePage) {
            this.tvEmptyMessage.setVisibility(this.tvEmptyList.getVisibility());
            this.tvEmptyList.setText(getResources().getString(R.string.new_book_request));
            this.tvEmptyList.setBackgroundColor(getResources().getColor(R.color.button_background));
            this.tvEmptyList.setOnClickListener(this);
            return;
        }
        this.tvEmptyMessage.setVisibility(View.GONE);
        this.tvEmptyList.setOnClickListener(null);
    }
}
