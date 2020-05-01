package com.essensys.cashsaverz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRentedBookList {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("isbn")
    @Expose
    private String isbn;
    @SerializedName("noOfPages")
    @Expose
    private String noOfPages;
    @SerializedName("totalBookCount")
    @Expose
    private String totalBookCount;
    @SerializedName("deliveryOption")
    @Expose
    private String deliveryOption;
    @SerializedName("bookFormat")
    @Expose
    private String bookFormat;
    @SerializedName("rentedBookId")
    @Expose
    private String rentedBookId;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("orderOnDatetime")
    @Expose
    private String orderOnDatetime;
    @SerializedName("bookDeliveredToCustomerOndateTime")
    @Expose
    private String bookDeliveredToCustomerOndateTime;
    @SerializedName("returnedDateTime")
    @Expose
    private String returnedDateTime;
    @SerializedName("actualReturnDatetime")
    @Expose
    private String actualReturnDatetime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pickupDatetime")
    @Expose
    private String pickupDatetime;
    @SerializedName("pickupNote")
    @Expose
    private String pickupNote;
    @SerializedName("otp")
    @Expose
    private String otp;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(String noOfPages) {
        this.noOfPages = noOfPages;
    }

    public String getTotalBookCount() {
        return totalBookCount;
    }

    public void setTotalBookCount(String totalBookCount) {
        this.totalBookCount = totalBookCount;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public String getBookFormat() {
        return bookFormat;
    }

    public void setBookFormat(String bookFormat) {
        this.bookFormat = bookFormat;
    }

    public String getRentedBookId() {
        return rentedBookId;
    }

    public void setRentedBookId(String rentedBookId) {
        this.rentedBookId = rentedBookId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderOnDatetime() {
        return orderOnDatetime;
    }

    public void setOrderOnDatetime(String orderOnDatetime) {
        this.orderOnDatetime = orderOnDatetime;
    }

    public String getBookDeliveredToCustomerOndateTime() {
        return bookDeliveredToCustomerOndateTime;
    }

    public void setBookDeliveredToCustomerOndateTime(String bookDeliveredToCustomerOndateTime) {
        this.bookDeliveredToCustomerOndateTime = bookDeliveredToCustomerOndateTime;
    }

    public String getReturnedDateTime() {
        return returnedDateTime;
    }

    public void setReturnedDateTime(String returnedDateTime) {
        this.returnedDateTime = returnedDateTime;
    }

    public String getActualReturnDatetime() {
        return actualReturnDatetime;
    }

    public void setActualReturnDatetime(String actualReturnDatetime) {
        this.actualReturnDatetime = actualReturnDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPickupDatetime() {
        return pickupDatetime;
    }

    public void setPickupDatetime(String pickupDatetime) {
        this.pickupDatetime = pickupDatetime;
    }

    public String getPickupNote() {
        return pickupNote;
    }

    public void setPickupNote(String pickupNote) {
        this.pickupNote = pickupNote;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}