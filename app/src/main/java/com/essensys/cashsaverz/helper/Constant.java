package com.essensys.cashsaverz.helper;

import com.facebook.internal.CallbackManagerImpl.RequestCodeOffset;

public class Constant {

    public interface Common {
        public static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";
    }

    public interface IntentExtras {
        public static final String NAV_VIEW_ID = "NAV_VIEW_ID";
    }

    public interface RequestCode {
        public static final int FACEBOOK_SIGN_IN = RequestCodeOffset.Login.toRequestCode();
    }

    public interface ServerEndpoint {
        String ABOUT_US = "http://52.41.183.204/JB097/web-app/ws-cms/ws-cms-about-us.html";
        String ADD_NEW_ADDRESS = "ws-account/ws-add-new-address.php/";
        String ADD_TO_CART = "cart/ws-add-to-cart.php/";
        String CHANGE_PASSSWORD = "ws-account/ws-change-pass-action.php/";
        String CHECKOUT_SUMMARY = "cart/ws-checkout-summary.php/";
        public static final String CONTACT_US = "ws-cms/ws-contact-us-action.php/";
        public static final String DELIVER_BOOK = "ws-account/ws-issue-pending-book.php/";
        public static final String EDIT_ADDRESS = "ws-account/ws-update-address.php/";
        public static final String EDIT_BASIC_INFO = "ws-account/ws-update-profile-info.php/";
        public static final String FETCH_CART_LIST = "cart/ws-cart-list.php/";
        public static final String FETCH_DELIVERY_HISTORY_BOOK_LIST = "ws-account/ws-get-delivery-person-history.php/";
        public static final String FETCH_HOME_CATEGORY_LIST = "ws-products/ws-home-product-list.php/";
        String FETCH_PAYMENT_HISTORY_LIST = "ws-account/ws-payment-history.php/";
        String FETCH_PRODUCT_LIST = "ws-products/ws-product-list.php/";
        String FETCH_RENTED_BOOK_LIST = "ws-account/ws-get-user-rented-book-list.php/";
        String FETCH_TODAYS_DELIVERY_BOOK_LIST = "ws-account/ws-get-pending-delivery-book-list.php/";
        String FETCH_WISH_LIST = "ws-account/ws-wishlist-product-list.php/";
        String FORGOT_PASSWORD = "ws-login/ws-forgot.php/";
        String GET_FIELDS = "ws-products/ws-get-field-details.php/";
        String GET_USER_ACTIVE_MEMBERSHIP_PLAN = "ws-account/ws-get-user-active-membership-plan.php/";
        String LOGIN = "ws-login/ws-login.php/";
        String LOGIN_WITH_SOCIAL_MEDIA = "ws-login/ws-login-with-social-media.php/";
        String MEMBERSHIP_PLAN = "ws-account/ws-purchased-membership-payment-success-status-update.php/";
        String MY_ORDERS = "cart/ws-my-orders.php/";
        public static final String NEW_BOOK_REQUEST = "ws-account/ws-add-new-book-request.php/";
        public static final String NEW_BOOK_REQUEST_LIST = "ws-account/ws-new-book-request-list.php/";
        public static final String PLACE_ORDER = "cart/ws-place-order.php/";
        public static final String PRIVACY_POLICY = "http://52.41.183.204/JB097/web-app/ws-cms/ws-cms-privacy-policy.html";
        public static final String REGISTER = "ws-login/ws-signup.php/";
        public static final String REISSUE_BOOK = "ws-account/ws-reissue-book.php/";
        public static final String REMOVE_CART_ITEM = "cart/ws-remove-from-cart.php/";
        public static final String RENT_BOOK = "ws-account/ws-rent-books.php/";
        public static final String RETURN_BOOK = "ws-account/ws-return-book.php/";
        public static final String TERMS_AND_CONDITIONS = "http://52.41.183.204/JB097/web-app/ws-cms/ws-cms-terms-condition.html";
        public static final String UPDATE_QTY = "cart/ws-update-cart.php/";
        public static final String UPDATE_WISHLIST = "ws-account/ws-add-remove-wishlist-product.php/";
    }

    public interface SharedPreferences {
        public static final String ALERT_ADD_MOBILE_NUMBER = "ALERT_ADD_MOBILE_NUMBER";
        public static final String IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN";
        public static final String LOGGED_IN_USER = "LOGGED_IN_USER";
    }
}

//package com.essensys.cashsaverz.helper;
//
//import com.facebook.internal.CallbackManagerImpl;
//
///**
// * Created by Shraddha on 22-08-2018.
// */
//
//public class Constant {
//
//    public interface ServerEndpoint {
//
////        String PRIVACY_POLICY = "http://bookmybook.net/web-app/ws-cms/ws-cms-privacy-policy.html";
//          String PRIVACY_POLICY = "http://52.41.183.204/JB097/web-app/ws-cms/ws-cms-privacy-policy.html";
//
////        String ABOUT_US = "https://bookmybook.net/web-app/ws-cms/ws-cms-about-us.html";
//        String ABOUT_US = "http://52.41.183.204/JB097/web-app/ws-cms/ws-cms-about-us.html";
//
////        String TERMS_AND_CONDITIONS = "https://bookmybook.net/web-app/ws-cms/ws-cms-terms-condition.html";
//        String TERMS_AND_CONDITIONS = "http://52.41.183.204/JB097/web-app/ws-cms/ws-cms-terms-condition.html";
//
//        String LOGIN = "ws-login/ws-login.php/";
//        String REGISTER = "ws-login/ws-signup.php/";
//        String LOGIN_WITH_SOCIAL_MEDIA = "ws-login/ws-login-with-social-media.php/";
//        String FETCH_HOME_CATEGORY_LIST = "ws-products/ws-home-product-list.php/";//user_id
//        String FETCH_PRODUCT_LIST = "ws-products/ws-product-list.php/";//user_id
//        String FETCH_WISH_LIST = "ws-account/ws-wishlist-product-list.php/";//user_id
//        String FETCH_RENTED_BOOK_LIST = "ws-account/ws-get-user-rented-book-list.php/";//user_id
//        String FETCH_TODAYS_DELIVERY_BOOK_LIST = "ws-account/ws-get-pending-delivery-book-list.php/";//user_id
//        String FETCH_DELIVERY_HISTORY_BOOK_LIST = "ws-account/ws-get-delivery-person-history.php/";//user_id
//        String ADD_NEW_ADDRESS = "ws-account/ws-add-new-address.php/";
//        String EDIT_BASIC_INFO = "ws-account/ws-update-profile-info.php/";
//        String EDIT_ADDRESS = "ws-account/ws-update-address.php/";
//        String GET_FIELDS = "ws-products/ws-get-field-details.php/";
//        String UPDATE_WISHLIST = "ws-account/ws-add-remove-wishlist-product.php/";
//        String RETURN_BOOK = "ws-account/ws-return-book.php/";
//        String DELIVER_BOOK = "ws-account/ws-issue-pending-book.php/";
//        String REISSUE_BOOK = "ws-account/ws-reissue-book.php/";
//        String CHANGE_PASSSWORD = "ws-account/ws-change-pass-action.php/";
//        String ADD_TO_CART = "cart/ws-add-to-cart.php/";
//        String MEMBERSHIP_PLAN = "ws-account/ws-purchased-membership-payment-" +
//                "success-status-update.php/";
//
//        String FETCH_CART_LIST = "cart/ws-cart-list.php/";
//        String REMOVE_CART_ITEM = "cart/ws-remove-from-cart.php/";
//        String CHECKOUT_SUMMARY = "cart/ws-checkout-summary.php/";
//        String FETCH_PAYMENT_HISTORY_LIST = "ws-account/ws-payment-history.php/";
//        String RENT_BOOK = "ws-account/ws-rent-books.php/";
//        String CONTACT_US = "ws-cms/ws-contact-us-action.php/";
//        String NEW_BOOK_REQUEST_LIST = "ws-account/ws-new-book-request-list.php/"; // Added by SM
//        String NEW_BOOK_REQUEST = "ws-account/ws-add-new-book-request.php/"; // Added by SM
//        String FORGOT_PASSWORD = "ws-login/ws-forgot.php/"; // Added by SM
//        String GET_USER_ACTIVE_MEMBERSHIP_PLAN = "ws-account/ws-get-user-active-membership-plan.php/";//user_id // Added by SM
//
//        String UPDATE_QTY="cart/ws-update-cart.php/"; //added by ND
//    }
//
//    public interface RequestCode {
//        int FACEBOOK_SIGN_IN = CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode(); //from Facebook SDK.
//    }
//
//    public interface SharedPreferences {
//        String LOGGED_IN_USER = "LOGGED_IN_USER";
//        String IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN";
//        String ALERT_ADD_MOBILE_NUMBER = "ALERT_ADD_MOBILE_NUMBER"; // Added by SM
//    }
//
//    public interface Common {
//        String SHARED_PREFERENCES = "SHARED_PREFERENCES";
//    }
//
//    public interface IntentExtras {
//        String NAV_VIEW_ID = "NAV_VIEW_ID";
//    }
//}
