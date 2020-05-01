package com.essensys.cashsaverz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartItems {

    @SerializedName("cartId")
    @Expose
    private String cartId;

    @SerializedName("productId")
    @Expose
    private String productId;

    @SerializedName("product_name")
    @Expose
    private String product_name;
    public String getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
    @SerializedName("totalPrice")
    @Expose
    private String totalPrice;

    @SerializedName("mrp")
    @Expose
    private String mrp;

    @SerializedName("item_size")
    @Expose
    private String item_size;

    @SerializedName("unit")
    @Expose
    private String unit;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("brandName")
    @Expose
    private String brandName;

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getProduct_name() {
        return product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    public String getMrp() {
        return mrp;
    }
    public void setMrp(String mrp) {
        this.mrp = mrp;
    }
    public String getItem_size() {
        return item_size;
    }
    public void setItem_size(String item_size) {
        this.item_size = item_size;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getCartId() {
        return cartId;
    }



    public String getProductId() {
        return productId;
    }
    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    public String getQty() {
        return qty;
    }
    public void setQty(String qty) {
        this.qty = qty;
    }
}