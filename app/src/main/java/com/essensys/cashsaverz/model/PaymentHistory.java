package com.essensys.cashsaverz.model;

import com.google.gson.annotations.SerializedName;

public class PaymentHistory {

    @SerializedName("txnid")
    private String transactionID;

    @SerializedName("amount")
    private String amount;

    @SerializedName("addedon")
    private String addedOn;

    @SerializedName("membershipPlanTitle")
    private String membershipPlanTitle;

    @SerializedName("membershipStartDate")
    private String membershipStartDate;

    @SerializedName("membershipEndDate")
    private String membershipEndDate;

    @SerializedName("status")
    private String status;

    public String getTransactionID() {
        return transactionID;
    }

    public String getAmount() {
        return amount;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public String getMembershipPlanTitle() {
        return membershipPlanTitle;
    }
    public String getMembershipEndDate() {
        return membershipEndDate;
    }

    public void setMembershipEndDate(String membershipEndDate) {
        this.membershipEndDate = membershipEndDate;
    }
    public String getMembershipStartDate() {
        return membershipStartDate;
    }

    public void setMembershipStartDate(String membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    }
    public String getStatus() {
        return status;
    }
}
