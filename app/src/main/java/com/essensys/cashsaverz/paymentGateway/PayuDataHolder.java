package com.essensys.cashsaverz.paymentGateway;

import com.google.gson.annotations.SerializedName;

public class PayuDataHolder {

    @SerializedName("postBackParamId")
    private String postBackParamId;

    @SerializedName("mihpayid")
    private String mihPayID;

    @SerializedName("mode")
    private String mode;

    @SerializedName("status")
    private String status;

    @SerializedName("addedon")
    private String addedon;

    @SerializedName("txnid")
    private String txnID;

    @SerializedName("amount")
    private String amount;

    @SerializedName("productinfo")
    private String productInfo;

    @SerializedName("firstname")
    private String firstName;

    @SerializedName("email")
    private String userEmail;

    @SerializedName("phone")
    private String userPhone;

    @SerializedName("hash")
    private String hash;

    @SerializedName("bank_ref_num")
    private String bankRefNum;

    @SerializedName("bankcode")
    private String bankcode;

    @SerializedName("error")
    private String error;

    @SerializedName("error_Message")
    private String errorMessage;

    @SerializedName("pg_TYPE")
    private String pgTYPE;

    @SerializedName("payuMoneyId")
    private String payuMoneyId;

    @SerializedName("net_amount_debit")
    private String netAmountDebit;

    @SerializedName("discount")
    private String discount;

    @SerializedName("cardnum")
    private String cardNumber;

    @SerializedName("name_on_card")
    private String nameOnCard;

    @SerializedName("key")
    private String key;


    public String getPostBackParamId() {
        return postBackParamId;
    }

    public String getMihPayID() {
        return mihPayID;
    }

    public String getMode() {
        return mode;
    }

    public String getStatus() {
        return status;
    }

    public String getKey() {
        return key;
    }

    public String getAddedon() {
        return addedon;
    }

    public String getTxnID() {
        return txnID;
    }

    public String getAmount() {
        return amount;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getHash() {
        return hash;
    }

    public String getBankRefNum() {
        return bankRefNum;
    }

    public String getBankcode() {
        return bankcode;
    }

    public String getError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getPgTYPE() {
        return pgTYPE;
    }

    public String getPayuMoneyId() {
        return payuMoneyId;
    }

    public String getNetAmountDebit() {
        return netAmountDebit;
    }

    public String getDiscount() {
        return discount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }
}
