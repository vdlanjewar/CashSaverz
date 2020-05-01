package com.essensys.cashsaverz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IdSelector {

    @SerializedName("cat_id")
    @Expose
    public static int catid;
    @SerializedName("product_id")
    @Expose
    public static int productid;

    public static int getCatId() {
        return catid;
    }

    public static void setCatId(int id) {
        catid = id;
    }

    public static int getProductId() {
        return productid;
    }

    public static void setProductId(int id) {
        productid = id;
    }

}
