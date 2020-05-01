package com.essensys.cashsaverz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityList {

    @SerializedName("drp_dwn_key")
    @Expose
    private String drpDwnKey;
    @SerializedName("drp_dwn_val")
    @Expose
    private String drpDwnVal;

    public String getDrpDwnKey() {
        return drpDwnKey;
    }

    public void setDrpDwnKey(String drpDwnKey) {
        this.drpDwnKey = drpDwnKey;
    }

    public String getDrpDwnVal() {
        return drpDwnVal;
    }

    public void setDrpDwnVal(String drpDwnVal) {
        this.drpDwnVal = drpDwnVal;
    }

}