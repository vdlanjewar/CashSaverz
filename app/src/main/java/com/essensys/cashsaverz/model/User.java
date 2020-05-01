package com.essensys.cashsaverz.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pavan on 24-08-2018.
 */

public class User {

    @SerializedName("msg")
    private String msg;

    @SerializedName("msg_string")
    private String msgString;

    @SerializedName("customer_id")
    private String customerId = "";

    @SerializedName("userAuth")
    private String userAuth;

    @SerializedName("userType")
    private String userType;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("name")
    private String name;

    @SerializedName("lastvisitDateTime")
    private String lastvisitDateTime;

    @SerializedName("arrUserAddressList")
    private List<UserAddressList> arrUserAddressList = null;

    @SerializedName("membershipPlan")
    private List<UserCurrentMembershipPlan> userCurrentMembershipPlans = null;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgString() {
        return msgString;
    }

    public void setMsgString(String msgString) {
        this.msgString = msgString;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUserEmail() {
        return email;
    }

    public void setUserEmail(String userEmail) {
        this.email = userEmail;
    }

    public String getUserPhone() {
        return phone;
    }

    public void setUserPhone(String userPhone) {
        this.phone = userPhone;
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String userName) {
        this.name = userName;
    }

    public String getLastvisitDateTime() {
        return lastvisitDateTime;
    }

    public void setLastvisitDateTime(String lastvisitDateTime) {
        this.lastvisitDateTime = lastvisitDateTime;
    }

    public List<UserAddressList> getArrUserAddressList() {
        return arrUserAddressList;
    }

    public void setArrUserAddressList(List<UserAddressList> arrUserAddressList) {
        this.arrUserAddressList = arrUserAddressList;
    }

    public List<UserCurrentMembershipPlan> getUserCurrentMembershipPlan() {
        return userCurrentMembershipPlans;
    }

    public void setUserCurrentMembershipPlan(List<UserCurrentMembershipPlan> userCurrentMembershipPlans) {
        this.userCurrentMembershipPlans = userCurrentMembershipPlans;
    }

}
