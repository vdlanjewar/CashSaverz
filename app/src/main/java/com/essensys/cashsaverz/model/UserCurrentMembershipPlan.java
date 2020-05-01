package com.essensys.cashsaverz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCurrentMembershipPlan {

    @SerializedName("currentMembershipPlanId")
    @Expose
    private String currentMembershipPlanId;
    @SerializedName("membershipStartDate")
    @Expose
    private String membershipStartDate;
    @SerializedName("membershipEndDate")
    @Expose
    private String membershipEndDate;
    @SerializedName("membershipEndDate_2")
    @Expose
    private String membershipEndDate2;
    @SerializedName("membershipPlanTitle")
    @Expose
    private String membershipPlanTitle;
    @SerializedName("totalBooksForRent")
    @Expose
    private String totalBooksForRent;
    @SerializedName("bookRentDurationInDays")
    @Expose
    private String bookRentDurationInDays;
    @SerializedName("status")
    @Expose
    private String status;

    public String getCurrentMembershipPlanId() {
        return currentMembershipPlanId;
    }

    public void setCurrentMembershipPlanId(String currentMembershipPlanId) {
        this.currentMembershipPlanId = currentMembershipPlanId;
    }

    public String getMembershipStartDate() {
        return membershipStartDate;
    }

    public void setMembershipStartDate(String membershipStratDate) {
        this.membershipStartDate = membershipStratDate;
    }

    public String getMembershipEndDate() {
        return membershipEndDate;
    }

    public void setMembershipEndDate(String membershipEndDate) {
        this.membershipEndDate = membershipEndDate;
    }

    public String getMembershipEndDate2() {
        return membershipEndDate2;
    }

    public void setMembershipEndDate2(String membershipEndDate2) {
        this.membershipEndDate2 = membershipEndDate2;
    }

    public String getMembershipPlanTitle() {
        return membershipPlanTitle;
    }

    public void setMembershipPlanTitle(String membershipPlanTitle) {
        this.membershipPlanTitle = membershipPlanTitle;
    }

    public String getTotalBooksForRent() {
        return totalBooksForRent;
    }

    public void setTotalBooksForRent(String totalBooksForRent) {
        this.totalBooksForRent = totalBooksForRent;
    }

    public String getBookRentDurationInDays() {
        return bookRentDurationInDays;
    }

    public void setBookRentDurationInDays(String bookRentDurationInDays) {
        this.bookRentDurationInDays = bookRentDurationInDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}