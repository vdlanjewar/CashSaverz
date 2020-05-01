package com.essensys.cashsaverz.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.adapter.ImageAdapter;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.localDatabases.DatabaseHandler;
import com.essensys.cashsaverz.model.Product;
import com.essensys.cashsaverz.networkManager.AddToCartManager;
import com.essensys.cashsaverz.networkManager.WishlistManager;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;

public class BookDetailsActivity extends AppCompatActivity implements View.OnClickListener,
        OnLikeListener, WishlistManager.StatusListener, AddToCartManager.StatusListener {
    public static int navItemIndex = 0;
    private ArrayAdapter<String> coloradapter;
    public static MenuItem notificationMenuItem;
    private List imageList = new ArrayList();
    private DatabaseHandler databaseHandler;
    private WishlistManager wishlistManager;
    private Toolbar toolbar;
    public static Product bookDetails;
    private TextView tvBookName, tvPrice,tvMrpPrice,tvSize, tvQuantity, tvBrand, tvLanguage, tvBinding,
            tvDescription, tvProdDesc, tvCategory;
    private Button btRentNow, btAddToCart, btNotAvailable;
    private LikeButton btLike, btShare;
    private ViewPager vpBookImageSlider;
    private TextView textCartItemCount;
    private AddToCartManager addToCartManager;

    public static void setProductDetails(Product bookDetails) {
        BookDetailsActivity.bookDetails = bookDetails;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wishlistManager = new WishlistManager(BookDetailsActivity.this, this);
        addToCartManager = new AddToCartManager(BookDetailsActivity.this, this);
        tvBookName = findViewById(R.id.tvBookName);

        tvPrice = findViewById(R.id.tvPrice);
        tvMrpPrice=findViewById(R.id.tvMrpDetPrice);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvBrand = findViewById(R.id.tvBrand);
        tvCategory = findViewById(R.id.tvCategory);
        tvProdDesc = findViewById(R.id.tvProdDesc);
        tvSize=findViewById(R.id.tvSize);

        tvLanguage = findViewById(R.id.tvLanguage);
        tvDescription = findViewById(R.id.tvDescription);
        tvBinding = findViewById(R.id.tvBinding);
        btRentNow = findViewById(R.id.btRentNow);
        btAddToCart = findViewById(R.id.btAddToCart);
        btNotAvailable = findViewById(R.id.btNotAvailable);
        btShare = findViewById(R.id.btShare);
        btLike = findViewById(R.id.btLike);
        vpBookImageSlider = findViewById(R.id.vpBookImageSlider);

        //Sqlite databse initialization
        databaseHandler = new DatabaseHandler(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btAddToCart.setOnClickListener(this);
        btRentNow.setOnClickListener(this);
        btShare.setOnLikeListener(this);
        btLike.setOnLikeListener(this);

        setBookDetails(bookDetails);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.isUserLoggedIn(this)) {
            setupBadge(HomeActivity.getCartCount());
        }
    }

    private void setBookDetails(Product bookDetails) {

        if (bookDetails.getAvilableBookCount().equals("0")) {
            btNotAvailable.setVisibility(View.VISIBLE);
            btAddToCart.setVisibility(View.GONE);
            btRentNow.setVisibility(View.GONE);
        } else {
            btNotAvailable.setVisibility(View.GONE);
            btAddToCart.setVisibility(View.VISIBLE);
            btRentNow.setVisibility(View.VISIBLE);
        }

        imageList.add(bookDetails.getImageUrl());
        ImageAdapter adapterView = new ImageAdapter(this, imageList);
        vpBookImageSlider.setAdapter(adapterView);
        tvBookName.setText(bookDetails.getProductName());
//        tvAuthorName.setText("Author:  " + bookDetails.getAuthor());
//        tvDescription.setText(bookDetails.getShortDescription());

        //added by ND
        tvCategory.setText(bookDetails.getCatName());
        tvProdDesc.setText(bookDetails.getShortDescription());
        tvPrice.setText(getString(R.string.Rs)+" "+bookDetails.getOffered_price());
        tvMrpPrice.setText(bookDetails.getMrp());
        tvMrpPrice.setPaintFlags(tvMrpPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        tvSize.setText(bookDetails.getItem_size()+" "+bookDetails.getUnit());
        tvQuantity.setText(bookDetails.getAvilableBookCount());
        tvBrand.setText(bookDetails.getBrandName());
//        tvPublisher.setText(bookDetails.getPublisher());
//        tvIsbn.setText(bookDetails.getIsbn());
//        tvBinding.setText(bookDetails.getBookFormat());
//        tvPages.setText(bookDetails.getNoOfPages());

        tvCategory.setText(bookDetails.getCatName());
        if (bookDetails.getFlagFavourite().equals("1")) {
            btLike.setLiked(true);
        } else {
            btLike.setLiked(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        final MenuItem menuItem = menu.findItem(R.id.mycart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        if (App.isUserLoggedIn(this)) {
            setupBadge(HomeActivity.getCartCount());
        }

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mycart) {
            navigateToCart();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPaynowClick(View view) {
        /*Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);*/
    }

    public void setToolbarTitle() {
        if (navItemIndex == 0)//make toolbar transperent when home fragment is opened
        {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorBlack));
            getSupportActionBar().setTitle("Book Details");
        } else {
            toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            getSupportActionBar().setTitle("Login To Continue");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btRentNow:
                // CheckOutActivity.setAmount(item.getPrice());
                if (!App.isUserLoggedIn(this)) {
                    CommonUtilities.openFragmentContainerActivityWithDelay(this,
                            R.id.nav_login, 6);
                } else {
                    if (App.getCurrentUser(BookDetailsActivity.this).getUserCurrentMembershipPlan() != null) {
                        CheckOutActivity.setRentBookID(bookDetails.getProductId());
                        startActivity(new Intent(BookDetailsActivity.this,
                                CheckOutActivity.class));
                    } else {
                        CommonUtilities.openFragmentContainerActivityWithDelay(this, R.id.nav_browse_plans,
                                0);
                    }

                }

                break;
            case R.id.btAddToCart:
                if (!App.isUserLoggedIn(this)) {
                    CommonUtilities.openFragmentContainerActivityWithDelay(this,
                            R.id.nav_login, 6);
                } else {
                    /*databaseHandler.insertCartItemToLocalDb(App.getCurrentUser(
                            BookDetailsActivity.this).getCustomerId(),
                            bookDetails.getProductId(), bookDetails.getProductName(),
                            bookDetails.getAuthor(), bookDetails.getImageUrl());
                    try {
                        setupBadge(databaseHandler.getCartItems(App.getCurrentUser(this).getCustomerId()).length());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    addToCartManager.addToCart(bookDetails.getProductId());
                }
                break;
        }
    }

    @Override
    public void liked(LikeButton likeButton) {
        switch (likeButton.getId()) {
            case R.id.btLike:
                if (!App.isUserLoggedIn(this)) {
                    CommonUtilities.openFragmentContainerActivityWithDelay(BookDetailsActivity.this,
                            R.id.nav_login, 6);
                } else {
                    wishlistManager.updateWishlist(bookDetails.getProductId());
                }
                break;
            case R.id.btShare:
                shareDetails(bookDetails.getProductName() + " By " + bookDetails.getAuthor());
                break;
        }
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        switch (likeButton.getId()) {
            case R.id.btLike:
                wishlistManager.updateWishlist(bookDetails.getProductId());
                break;
        }
    }

    private void shareDetails(String bookdetails) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, bookdetails + ".\n" +
                "Check out this book at: " +
                "https://play.google.com/store/apps/details?id=com.essensys.cashsaverz");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
        btShare.setLiked(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDataFetchSuccess(String successMessage) {
        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataFetchFailure(String errorMessage) {
        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    public void navigateToCart() {
        if (!App.isUserLoggedIn(BookDetailsActivity.this)) {
            setToolbarTitle();
            CommonUtilities.openFragmentContainerActivityWithDelay(this,
                    R.id.nav_login, 0);
            finish();
        } else {
            CommonUtilities.openFragmentContainerActivityWithDelay(BookDetailsActivity.this, R.id.nav_cart,
                    0);
            finish();
        }
    }

    public void setupBadge(int count) {

        if (textCartItemCount != null) {
            if (count == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(count));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onDataFetchSuccess(String successMessage, String total_cart_items) {
        HomeActivity.setCartCount(Integer.parseInt(total_cart_items));
        setupBadge(HomeActivity.getCartCount());
        Toast.makeText(BookDetailsActivity.this, successMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataFetchFailure(String errorMessage, String total_cart_items) {
        HomeActivity.setCartCount(Integer.parseInt(total_cart_items));
        setupBadge(HomeActivity.getCartCount());
        Toast.makeText(BookDetailsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

    }
}
