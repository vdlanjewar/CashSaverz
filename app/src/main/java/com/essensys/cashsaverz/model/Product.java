package com.essensys.cashsaverz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

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

    @SerializedName("unit")
    @Expose
    private String unit;

    @SerializedName("item_size")
    @Expose
    private String item_size;

    @SerializedName("mrp")
    @Expose
    private String mrp;

    @SerializedName("offered_price")
    @Expose
    private String offered_price;


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

    @SerializedName("ondate")
    @Expose
    private String ondate;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("flag_favourite")
    @Expose
    private String flag_favourite;

    @SerializedName("cat_name")
    private String catName;

    @SerializedName("cat_id")
    private String catID;

    @SerializedName("brandName")
    private String brandName;

    @SerializedName("avilableProductCount")
    private String avilableBookCount;
    public String getIsProductInCart() {
        return isProductInCart;
    }
    public void setIsProductInCart(String isProductInCart) {
        this.isProductInCart = isProductInCart;
    }
    @SerializedName("isProductInCart")
    private String isProductInCart;

    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getItem_size() {
        return item_size;
    }
    public void setItem_size(String item_size) {
        this.item_size = item_size;
    }
    public String getMrp() {
        return mrp;
    }
    public void setMrp(String mrp) {
        this.mrp = mrp;
    }
    public String getOffered_price() {
        return offered_price;
    }
    public void setOffered_price(String offered_price) {
        this.offered_price = offered_price;
    }
    public String getAvilableBookCount() {
        return avilableBookCount;
    }

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

    public String getOndate() {
        return ondate;
    }

    public void setOndate(String ondate) {
        this.ondate = ondate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlagFavourite() {
        return flag_favourite;
    }

    public void setFlagFavourite(String flag_favourite) {
        this.flag_favourite = flag_favourite;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }
}