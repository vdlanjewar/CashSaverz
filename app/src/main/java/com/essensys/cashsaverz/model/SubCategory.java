package com.essensys.cashsaverz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategory {
    @SerializedName("parent")
    @Expose
    private String parent;
    @SerializedName("cat_id")
    @Expose
    private String subCatId;
    @SerializedName("cat_name")
    @Expose
    private String subCatName;

    public String getSubCatId() {
        return this.subCatId;
    }

    public void setSubCatId(String subCatId2) {
        this.subCatId = subCatId2;
    }

    public String getSubCatName() {
        return this.subCatName;
    }

    public void setSubCatName(String subCatName2) {
        this.subCatName = subCatName2;
    }

    public String getParent() {
        return this.parent;
    }

    public void setParent(String parent2) {
        this.parent = parent2;
    }
}
