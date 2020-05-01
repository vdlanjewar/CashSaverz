package com.essensys.cashsaverz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.fragments.AboutUsFragment;
import com.essensys.cashsaverz.fragments.Alert_dialog_fragment;
import com.essensys.cashsaverz.fragments.BrowsePlansFragment;
import com.essensys.cashsaverz.fragments.CartFragment;
import com.essensys.cashsaverz.fragments.ContactUsFragment;
import com.essensys.cashsaverz.fragments.DeliveryHistoryFragment;
import com.essensys.cashsaverz.fragments.ExchangeOfBooksFragment;
import com.essensys.cashsaverz.fragments.HomeFragment;
import com.essensys.cashsaverz.fragments.MyProfileFragment;
import com.essensys.cashsaverz.fragments.MyRentedBooksFragment;
import com.essensys.cashsaverz.fragments.NewBookRequestFragment;
import com.essensys.cashsaverz.fragments.NewBookRequestListFragment;
import com.essensys.cashsaverz.fragments.PaymentHistoryFragment;
import com.essensys.cashsaverz.fragments.PrivacyPolicyFragment;
import com.essensys.cashsaverz.fragments.SettingFragment;
import com.essensys.cashsaverz.fragments.SignInFragment;
import com.essensys.cashsaverz.fragments.TermsConditionFragment;
import com.essensys.cashsaverz.fragments.TodaysDeliveryFragment;
import com.essensys.cashsaverz.fragments.WishlistFragment;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant;
import com.essensys.cashsaverz.localDatabases.DatabaseHandler;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.model.User;
import com.essensys.cashsaverz.networkManager.FacebookLogInManager;
import com.essensys.cashsaverz.preference.SharedPreferenceManager;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView ivHeaderBg, ivProfiles;
    private TextView tvName, tvEmail, tvMembership;
    private Toolbar toolbar;
    // index to identify current nav menu item
    public static int navItemIndex;
    public static int cartItemCount = 0;
    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_ACCOUNT = "account";
    private static final String TAG_ORDER = "order";
    private static final String TAG_WISHLIST = "wishlist";
    private static final String TAG_CART = "cart";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_REGSTRATION = "registration";
    private static final String TAG_BROWSE_PLANS = "browse_plans";
    private static final String TAG_ABOUT_US = "about_us";
    private static final String TAG_PRIVACY_POLICY = "privacy_policy";
    private static final String TAG_TERMS_CONDITION = "terms_condition";
    private static final String TAG_CONTACT_US = "contact_us";
    private static final String TAG_EXCHANGE_OLD_BOOKS = "exchange_of_old_books";
    private static final String TAG_PAYMENT_HISTORY = "payment_history";
    private static final String TAG_TODAYS_DELIVERY = "todays_delivery";
    private static final String TAG_DELIVERY_HISTORY = "delivery_history";
    private static final String TAG_NEW_BOOK_REQUEST = "new_book_request"; // Added by SM
    private static final String TAG_NEW_BOOK_REQUEST_HISTORY = "new_book_request_history"; // Added by SM

    public static String CURRENT_TAG = TAG_HOME;
    public static String membership;
    private String[] activityTitles;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private FacebookLogInManager facebookLogInManager;
    private CustomFragment currentCustomFragment;
    private TextView textCartItemCount;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        facebookLogInManager = new FacebookLogInManager(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        databaseHandler = new DatabaseHandler(HomeActivity.this);
        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        tvName = navHeader.findViewById(R.id.tvName);
        tvEmail = navHeader.findViewById(R.id.tvEmail);
        tvMembership = navHeader.findViewById(R.id.tvMembership);
        ivHeaderBg = navHeader.findViewById(R.id.ivHeaderBg);
        ivProfiles = navHeader.findViewById(R.id.ivProfile);
        ivProfiles.setOnClickListener(this);
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // load nav menu header data
        if (App.isUserLoggedIn(HomeActivity.this)) {
            loadNavHeader("Hi " + App.getCurrentUser(HomeActivity.this).getUserName(),
                    App.getCurrentUser(HomeActivity.this).getUserEmail());
        } else {
            loadNavHeader("Welcome Guest", "");
        }
        // initializing navigation menu
        setUpNavigationView();
        int navIndex = getIntent().getIntExtra(Constant.IntentExtras.NAV_VIEW_ID, 0);

        if (savedInstanceState == null) {
            if (App.isUserLoggedIn(this)) {
                if (App.getCurrentUser(this).getUserType().equals("DeliveryPerson")) {
                    navItemIndex = 11;
                    CURRENT_TAG = TAG_TODAYS_DELIVERY;
                } else {
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_HOME;
                }
            } else {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
            }
            loadHomeFragment();
        }
        if (navIndex == R.id.nav_home) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        } else if (navIndex == R.id.nav_login) {
            navItemIndex = 6;
            CURRENT_TAG = TAG_REGSTRATION;
            loadHomeFragment();
        } else if (navIndex == R.id.nav_browse_plans) {
            navItemIndex = 7;
            CURRENT_TAG = TAG_BROWSE_PLANS;
            loadHomeFragment();
        } else if (navIndex == R.id.nav_cart) {
            navItemIndex = 4;
            CURRENT_TAG = TAG_CART;
            loadHomeFragment();
        }else if (navIndex == R.id.nav_new_book_request) { // Added by SM
            navItemIndex = 14;
            CURRENT_TAG = TAG_NEW_BOOK_REQUEST;
            loadHomeFragment();
        }else if (navIndex == R.id.nav_new_request_book_history) { // Added by SM
            navItemIndex = 17;
            CURRENT_TAG = TAG_NEW_BOOK_REQUEST_HISTORY;
            loadHomeFragment();
        }

        new Thread(new Runnable() {
            public void run() {
                if (!isFinishing()) {
                    try {

                        User loggedInuser = SharedPreferenceManager.with(getApplicationContext()).getLoggedInUser();

                        if ( loggedInuser != null) {
                            if (loggedInuser.getUserPhone().isEmpty()) {

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (SharedPreferenceManager.with(getApplicationContext()).getTimeForAlertMobile() < System.currentTimeMillis()) {
                                            Alert_dialog_fragment dialog = Alert_dialog_fragment.newInstance();
                                            dialog.show(getSupportFragmentManager(), "Alert_dialog_fragment");
                                        }
                                    }
                                });

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader(String name, String email) {
        // name, website
        tvName.setText(name);
        if (email == "") {
            tvEmail.setVisibility(View.GONE);
            tvMembership.setVisibility(View.GONE);
        } else {
            tvEmail.setVisibility(View.VISIBLE);
            tvMembership.setVisibility(View.VISIBLE);
            tvEmail.setText(email);
        }

        ivHeaderBg.setImageResource(R.drawable.education_and_learning);
        // Loading profile image
        ivProfiles.setImageResource(R.drawable.ic_profile);
        // showing dot next to notifications label
        // navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
        if (App.isUserLoggedIn(HomeActivity.this)) {
            navigationView.getMenu().getItem(7).setVisible(false);
            if (App.getCurrentUser(HomeActivity.this).getUserType().equals("DeliveryPerson")) {
                navigationView.getMenu().getItem(8).setVisible(true);
                navigationView.getMenu().getItem(0).setVisible(false);
                navigationView.getMenu().getItem(5).setVisible(true);
                navigationView.getMenu().getItem(9).setVisible(true);
                navigationView.getMenu().getItem(1).setVisible(false);
                navigationView.getMenu().getItem(2).setVisible(false);
                navigationView.getMenu().getItem(3).setVisible(false);
                navigationView.getMenu().getItem(4).setVisible(false);
                navigationView.getMenu().getItem(6).setVisible(false);
                navigationView.getMenu().getItem(7).setVisible(false);
            } else {
                navigationView.getMenu().getItem(8).setVisible(false);
                navigationView.getMenu().getItem(0).setVisible(true);
                navigationView.getMenu().getItem(5).setVisible(true);
                navigationView.getMenu().getItem(9).setVisible(false);
                navigationView.getMenu().getItem(1).setVisible(true);
                navigationView.getMenu().getItem(2).setVisible(true);
                navigationView.getMenu().getItem(3).setVisible(true);
                navigationView.getMenu().getItem(4).setVisible(true);
                navigationView.getMenu().getItem(6).setVisible(true);
                navigationView.getMenu().getItem(7).setVisible(false);
            }
        } else {
            navigationView.getMenu().getItem(7).setVisible(false);
            navigationView.getMenu().getItem(8).setVisible(false);
            navigationView.getMenu().getItem(9).setVisible(false);
        }

    }

    public void setMembershipToNavHeader(String membership) {
        if (membership.length() > 0) {
            tvMembership.setText("Membership : " + membership);
        } else {
            tvMembership.setVisibility(View.GONE);
        }
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        final int container;
        selectNavMenu();
        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                currentCustomFragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.fragment_container, currentCustomFragment,
                        CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();
        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private CustomFragment getHomeFragment() {
        if (App.isUserLoggedIn(this)) {
            if (App.getCurrentUser(HomeActivity.this).getUserType().equals("DeliveryPerson")) {
                currentCustomFragment = new TodaysDeliveryFragment();
            } else {
                currentCustomFragment = new HomeFragment();
            }
        } else {
            currentCustomFragment = new HomeFragment();
        }
        switch (navItemIndex) {
            case 0:
                return new HomeFragment();
            case 1:
                return new MyProfileFragment();
            case 2:
                return new MyRentedBooksFragment();
            case 3:
                return new WishlistFragment();
            case 4:
//                return new CartFragment();
                Intent intent=new Intent(this,CartActivity.class);
                startActivity(intent);
            case 5:
                return new SettingFragment();
            case 6:
                return new SignInFragment();
            case 7:
                return new BrowsePlansFragment();
            case 8:
                return new PaymentHistoryFragment();
            case 9:
                return new AboutUsFragment();
            case 10:
                return new PrivacyPolicyFragment();
            case 11:
                return new TodaysDeliveryFragment();
            case 12:
                return new DeliveryHistoryFragment();
            case 13:
                return new ContactUsFragment();
            case 14:
                return new NewBookRequestFragment();
            case 15:
                return new TermsConditionFragment();
            case 16:
                return new ExchangeOfBooksFragment();
            case 17:
                return new NewBookRequestListFragment();
            default:
                //return new HomeFragment();
        }

        return currentCustomFragment;
    }

    private void setToolbarTitle() {
        //make toolbar transperent when home fragment is opened
        if (navItemIndex == 0) {
            toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        } //make toolbar transperent when login fragment is opened
        else if (navItemIndex == 6) {
            toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            getSupportActionBar().setTitle("");
        } else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorBlack));
            getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        }
    }

    private void selectNavMenu() {
        if (navItemIndex < 7)
            navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        if (!App.isUserLoggedIn(HomeActivity.this)) {
            navigationView.getMenu().findItem(R.id.nav_login).setTitle(R.string.nav_login);
        } else {
            navigationView.getMenu().findItem(R.id.nav_login).setTitle(R.string.nav_logout);
            navigationView.getMenu().findItem(R.id.nav_browse_plans).setVisible(false);
        }

        navigationView.getMenu().findItem(R.id.nav_version).setTitle(CommonUtilities.getAppVersionName(HomeActivity.this));
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_account:
                        if (!App.isUserLoggedIn(HomeActivity.this)) {
                            navItemIndex = 6;
                            CURRENT_TAG = TAG_REGSTRATION;
                        } else {
                            navItemIndex = 1;
                            CURRENT_TAG = TAG_ACCOUNT;
                        }
                        break;
                    case R.id.nav_order:
                        if (!App.isUserLoggedIn(HomeActivity.this)) {
                            navItemIndex = 6;
                            CURRENT_TAG = TAG_REGSTRATION;
                        } else {
                            navItemIndex = 2;
                            CURRENT_TAG = TAG_ORDER;
                        }
                        break;
                    case R.id.nav_wishlist:
                        if (!App.isUserLoggedIn(HomeActivity.this)) {
                            navItemIndex = 6;
                            CURRENT_TAG = TAG_REGSTRATION;
                        } else {
                            navItemIndex = 3;
                            CURRENT_TAG = TAG_WISHLIST;
                        }
                        break;
                    case R.id.nav_cart:
                        if (!App.isUserLoggedIn(HomeActivity.this)) {
                            navItemIndex = 6;
                            CURRENT_TAG = TAG_REGSTRATION;
                        } else {
                            navItemIndex = 4;
                            CURRENT_TAG = TAG_CART;
                        }
                        break;
                    case R.id.nav_settings:
                        if (!App.isUserLoggedIn(HomeActivity.this)) {
                            navItemIndex = 6;
                            CURRENT_TAG = TAG_REGSTRATION;
                        } else {
                            navItemIndex = 5;
                            CURRENT_TAG = TAG_SETTINGS;
                        }
                        break;
                    case R.id.nav_login:
                        if (App.isUserLoggedIn(HomeActivity.this)) {
                            setNavigationDrawerForGuest();
                            drawer.closeDrawers();
                            CommonUtilities.openFragmentContainerActivityWithDelay(
                                    HomeActivity.this, R.id.nav_home, 0);
                            finish();
                            return true;
                        }
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_REGSTRATION;
                        break;
                    case R.id.nav_browse_plans:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_BROWSE_PLANS;
                        break;
                    case R.id.nav_payment_history:
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_PAYMENT_HISTORY;
                        break;

                    case R.id.nav_about_us:
                        navItemIndex = 9;
                        CURRENT_TAG = TAG_ABOUT_US;
                        break;
                    case R.id.nav_privacy_policy:
                        navItemIndex = 10;
                        CURRENT_TAG = TAG_PRIVACY_POLICY;
                        break;
                    case R.id.nav_todays_delivery:
                        navItemIndex = 11;
                        CURRENT_TAG = TAG_TODAYS_DELIVERY;
                        break;
                    case R.id.nav_delivery_history:
                        navItemIndex = 12;
                        CURRENT_TAG = TAG_DELIVERY_HISTORY;
                        break;
                    case R.id.nav_contact_us:
                        navItemIndex = 13;
                        CURRENT_TAG = TAG_CONTACT_US;
                        break;
                    case R.id.nav_new_book_request:
                        navItemIndex = 14;
                        CURRENT_TAG = TAG_NEW_BOOK_REQUEST;
                        break;
                    case R.id.nav_terms_condition:
                        navItemIndex = 15;
                        CURRENT_TAG = TAG_TERMS_CONDITION;
                        break;
                    case R.id.nav_exchange:
                        navItemIndex = 16;
                        CURRENT_TAG = TAG_EXCHANGE_OLD_BOOKS;
                        break;
                    case R.id.nav_new_request_book_history:
                        navItemIndex = 17;
                        CURRENT_TAG = TAG_NEW_BOOK_REQUEST_HISTORY;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                //menuItem.setChecked(true);
                drawer.closeDrawers();
                loadHomeFragment();
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (App.isUserLoggedIn(this)) {
            if (App.getCurrentUser(this).getUserType().equals("DeliveryPerson")) {
                getMenuInflater().inflate(R.menu.main, menu);
            } else {
                getMenuInflater().inflate(R.menu.menu_home, menu);
                final MenuItem menuItem = menu.findItem(R.id.mycart);
                final MenuItem menuItemSearch = menu.findItem(R.id.search_button);
                View actionView = MenuItemCompat.getActionView(menuItem);
                textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
                textCartItemCount.setVisibility(View.GONE);
                actionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onOptionsItemSelected(menuItem);
                    }
                });
            }
        } else {
            if (navItemIndex == 6) {
                getMenuInflater().inflate(R.menu.main, menu);
            } else {
                getMenuInflater().inflate(R.menu.menu_home, menu);
            }
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mycart) {
            if (!App.isUserLoggedIn(HomeActivity.this)) {
                navItemIndex = 6;
                CURRENT_TAG = TAG_REGSTRATION;
            } else {
                navItemIndex = 4;
                CURRENT_TAG = TAG_CART;
            }
            loadHomeFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setNavigationDrawerForGuest() {
        App.setCurrentUser(null);
        SharedPreferenceManager.with(HomeActivity.this).updateLoggedInUser(null);
        navigationView.getMenu().findItem(R.id.nav_login).setTitle(R.string.nav_login);
        facebookLogInManager.startLogOut();
        loadNavHeader("Welcome Guest", "");
        Toasty.warning(HomeActivity.this, "You are signed out from BookMyBook",
                Toast.LENGTH_LONG, true).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.RequestCode.FACEBOOK_SIGN_IN) {
            facebookLogInManager.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onFacebookSingInClicked(View view) {
        facebookLogInManager.startLogin();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ivProfile:
                if (!App.isUserLoggedIn(HomeActivity.this)) {
                    navItemIndex = 6;
                    CURRENT_TAG = TAG_REGSTRATION;
                    loadHomeFragment();
                } else {
                    navItemIndex = 1;
                    CURRENT_TAG = TAG_ACCOUNT;
                    loadHomeFragment();
                }
        }
    }

    @Override
    public void onBackPressed() {
        if (currentCustomFragment != null) {
            currentCustomFragment.onBackPressed();
        } else {
            super.onBackPressed();
            overridePendingTransition(0, 0);
        }
    }

    public  void setupBadge(int count) {

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

    public static void setCartCount(int currentcount) {
        cartItemCount = currentcount;
    }

    public static int getCartCount() {
        return cartItemCount;
    }

}
