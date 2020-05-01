package com.essensys.cashsaverz.remote;

import java.util.HashMap;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public class RetrofitInterfaces {

    public interface AddNewAddress {
        @FormUrlEncoded
        @POST("ADDRESS")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2, @Field("name") String str3, @Field("mobile") String str4, @Field("pincode") String str5, @Field("locality") String str6, @Field("address") String str7, @Field("landmark") String str8, @Field("alternateMobile") String str9, @Field("city") String str10, @Field("addressType") String str11);
    }

    public interface AddToCart {
        @FormUrlEncoded
        @POST("ADDTOCART")
        Call<ResponseBody> post(@Field("user_id") String user_id,
                                @Field("productId") String productId,
                                @Field("deviceId") String deviceId);
    }

    public interface ChangePassword {
        @FormUrlEncoded
        @POST("CHANGEPASSWORD")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2, @Field("currentPassword") String str3, @Field("newPassword") String str4);
    }

    public interface ContactUs {
        @FormUrlEncoded
        @POST("CONTACTUS")
        Call<ResponseBody> post(@Field("name") String str, @Field("email") String str2, @Field("phone") String str3, @Field("message") String str4);
    }

    public interface DeliverBook {
        @FormUrlEncoded
        @POST("DELIVERBOOK")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2, @Field("rentedBookId") String str3, @Field("otp") String str4);
    }

    public interface DoLogin {
        @FormUrlEncoded
        @POST("LOGIN")
        Call<ResponseBody> post(@Field("deviceId") String str, @Field("username") String str2, @Field("password") String str3);
    }

    public interface DoSignUp {
        @FormUrlEncoded
        @POST("SIGNUP")
        Call<ResponseBody> post(@Field("name") String str, @Field("password") String str2, @Field("userEmail") String str3, @Field("userMobile") String str4, @Field("cityId") String str5, @Field("registered_from") String str6);
    }

    public interface EditAddress {
        @FormUrlEncoded
        @POST("EDITADDRESS")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2, @Field("addressId") String str3, @Field("name") String str4, @Field("mobile") String str5, @Field("pincode") String str6, @Field("locality") String str7, @Field("address") String str8, @Field("landmark") String str9, @Field("alternateMobile") String str10, @Field("city") String str11, @Field("addressType") String str12);
    }

    public interface EditBasicInfo {
        @FormUrlEncoded
        @POST("EDITBASICINFO")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2, @Field("name") String str3, @Field("mobile") String str4, @Field("email") String str5);
    }

    public interface FetchCartItemList {
        @FormUrlEncoded
        @POST("CARTLIST")
        Call<ResponseBody> post(@Field("user_id") String str, @Field("deviceId") String str2);
    }

    public interface FetchDeliveryHistoryBookList {
        @FormUrlEncoded
        @POST("DELIVERYHISTORYBOOKLIST")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2, @Field("offset") String str3);
    }

    public interface FetchHomeCategoryList {
        @POST("FOO")
        Call<ResponseBody> post(@Query("user_id") String str, @Query("deviceId") String str2);
    }

    public interface FetchPaymentHistoryList {
        @FormUrlEncoded
        @POST("PAYMENTHISTORY")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2);
    }

    public interface FetchProductList {
        @FormUrlEncoded
        @POST("PRODUCTLIST")
        Call<ResponseBody> post(@Field("user_id") String user_id,
                                @Field("cat_id") String cat_id,
                                @Field("subcat_id") String subcat_id,
                                @Field("offset") String offset,
                                @Field("searchKeyword") String searchKeyword,
                                @Field("deviceId") String deviceId);
    }

    public interface FetchRentedBookList {
        @FormUrlEncoded
        @POST("RENTEDBOOKLIST")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2, @Field("offset") String str3);
    }

    public interface FetchTodaysDeliveryBookList {
        @FormUrlEncoded
        @POST("TODAYSDELIVERYBOOKLIST")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2);
    }

    public interface FetchWishList {
        @POST("WISHLIST")
        Call<ResponseBody> post(@Header("userAuth") String str);
    }

    public interface ForgotPassword {
        @FormUrlEncoded
        @POST("FORGOTPASSWORD")
        Call<ResponseBody> post(@Field("email") String str);
    }

    public interface GetActiveMembershipPlan {
        @FormUrlEncoded
        @POST("GetActiveMembershipPlan")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2);
    }

    public interface GetFieldDetails {
        @FormUrlEncoded
        @POST("FIELDS")
        Call<ResponseBody> post(@Field("user_id") String str);
    }

    public interface IPlaceOrder {
        @POST("PlaceOrder")
        @Multipart
        Call<ResponseBody> save(@Header("userAuth") String str, @PartMap HashMap<String, RequestBody> hashMap);
    }

    public interface IPurchaseMembershipPlan {
        @POST("PLAN")
        @Multipart
        Call<ResponseBody> save(@Header("userAuth") String str, @PartMap HashMap<String, RequestBody> hashMap);
    }

    public interface MyOrders {
        @POST("MYORDERS")
        Call<ResponseBody> post(@Header("userAuth") String str);
    }

    public interface NewBookRequest {
        @FormUrlEncoded
        @POST("NEWBOOKREQUEST")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2, @Field("bookName") String str3, @Field("authorName") String str4, @Field("bookExternalLink") String str5, @Field("name") String str6, @Field("email") String str7, @Field("phone") String str8, @Field("message") String str9);
    }

    public interface NewBookRequestList {
        @FormUrlEncoded
        @POST("NEWBOOKREQUESTLIST")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2);
    }

    public interface OrderSummary {
        @FormUrlEncoded
        @POST("ORDERSUMMARY")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("checkOutFrom") String str2, @Field("productId") String str3, @Field("qty") String str4);
    }

    public interface ReIssueBook {
        @FormUrlEncoded
        @POST("REISSUEBOOK")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2, @Field("rentedBookId") String str3);
    }

    public interface RemoveFromCart {
        @FormUrlEncoded
        @POST("REMOVECART")
        Call<ResponseBody> post(@Field("user_id") String user_id,
                                @Field("cartId") String cartId,
                                @Field("deviceId") String deviceId,
                                @Field("removeAll") boolean removeAll);
    }

    public interface RentBook {
        @FormUrlEncoded
        @POST("RENTBOOK")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2, @Field("bookForRentId") String str3, @Field("deliveryAddressId") String str4, @Field("deliveryTimeSlotId") String str5, @Field("flagRentNow") boolean z);
    }

    public interface ReturnBook {
        @FormUrlEncoded
        @POST("RETURNBOOK")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2, @Field("rentedBookId") String str3);
    }

    public interface SocialLogin {
        @FormUrlEncoded
        @POST("SOCIALLOGIN")
        Call<ResponseBody> post(@Field("outh_uid") String str, @Field("outh_provider") String str2, @Field("email") String str3, @Field("fullname") String str4, @Field("registered_from") String str5, @Field("dev_token_number") String str6, @Field("device_id_imei") String str7);
    }

    public interface UpdateQty {
        @FormUrlEncoded
        @POST("UPDATEQTY")
        Call<ResponseBody> post(@Field("user_id") String str, @Field("cartId") String str2, @Field("qty") String str3, @Field("deviceId") String str4);
    }

    public interface UpdateWishlist {
        @FormUrlEncoded
        @POST("WISHLIST")
        Call<ResponseBody> post(@Header("userAuth") String str, @Field("user_id") String str2, @Field("product_id") String str3);
    }
}
