package com.essensys.cashsaverz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MembershipPlans {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("description2")
    @Expose
    private String description2;
    @SerializedName("description3")
    @Expose
    private String description3;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("durationInDays")
    @Expose
    private String durationInDays;
    @SerializedName("totalBooksForRent")
    @Expose
    private String totalBooksForRent;
    @SerializedName("bookRentDurationInDays")
    @Expose
    private String bookRentDurationInDays;

    @SerializedName("choosePlanbtn")
    private boolean choosePlanbtn;

    @SerializedName("offerText")
    private String offerText;

    @SerializedName("membershipExpierText")
    private String membershipExpierText;

    @SerializedName("activePlan")
    private boolean activePlan;

    // Added by SM
    @SerializedName("securityDepositText")
    private String securityDepositText;

    @SerializedName("securityDeposit")
    private String securityDeposit;

    @SerializedName("offerPriceOff")
    private String offerPriceOff;

    @SerializedName("membershipEndDate")
    private String membershipEndDate;

    @SerializedName("securityDepositAmountRecived")
    private String securityDepositAmountRecived;

    @SerializedName("isSecurityDepositRecived")
    private boolean isSecurityDepositRecived;

    public String getDescription2() {
        return description2;
    }

    public String getDescription3() {
        return description3;
    }

    public boolean getChoosePlanbtn() {
        return choosePlanbtn;
    }

    public String getOfferText() {
        return offerText;
    }

    public String getMembershipExpierText() {
        return membershipExpierText;
    }

    public boolean getActivePlan() {
        return activePlan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(String durationInYear) {
        this.durationInDays = durationInDays;
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


    public String getSecurityDepositText() {
        return securityDepositText;
    }

    public void setSecurityDepositText(String securityDepositText) {
        this.securityDepositText = securityDepositText;
    }

    public String getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(String securityDeposit) {
        this.securityDeposit = securityDeposit;
    }


    public String getOfferPriceOff() {
        return offerPriceOff;
    }

    public void setOfferPriceOff(String offerPriceOff) {
        this.offerPriceOff = offerPriceOff;
    }

    public String getMembershipEndDate() {
        return membershipEndDate;
    }

    public void setMembershipEndDate(String membershipEndDate) {
        this.membershipEndDate = membershipEndDate;
    }


    public String getSecurityDepositAmountRecived() {
        return securityDepositAmountRecived;
    }

    public void setSecurityDepositAmountRecived(String securityDepositAmountRecived) {
        this.securityDepositAmountRecived = securityDepositAmountRecived;
    }

    public boolean isSecurityDepositRecived() {
        return isSecurityDepositRecived;
    }

    public void setSecurityDepositRecived(boolean securityDepositRecived) {
        isSecurityDepositRecived = securityDepositRecived;
    }

}