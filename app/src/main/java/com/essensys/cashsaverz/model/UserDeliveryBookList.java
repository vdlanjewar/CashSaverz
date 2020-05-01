package com.essensys.cashsaverz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDeliveryBookList {

@SerializedName("rentedBookId")
@Expose
private String rentedBookId;
@SerializedName("orderId")
@Expose
private String orderId;
@SerializedName("product_name")
@Expose
private String productName;
@SerializedName("isbn")
@Expose
private String isbn;
@SerializedName("orderOnDatetime")
@Expose
private String orderOnDatetime;
@SerializedName("status")
@Expose
private String status;
@SerializedName("id")
@Expose
private String id;
@SerializedName("deliveryName")
@Expose
private String deliveryName;
@SerializedName("deliveryMobile")
@Expose
private String deliveryMobile;
@SerializedName("deliveryPincode")
@Expose
private String deliveryPincode;
@SerializedName("deliveryLocality")
@Expose
private String deliveryLocality;
@SerializedName("deliveryAddress")
@Expose
private String deliveryAddress;
@SerializedName("deliveryCityId")
@Expose
private String deliveryCityId;
@SerializedName("deliveryCityName")
@Expose
private String deliveryCityName;
@SerializedName("deliveryLandmark")
@Expose
private String deliveryLandmark;
@SerializedName("deliveryAlternateMobileNumber")
@Expose
private String deliveryAlternateMobileNumber;
@SerializedName("deliveryAlternateAddress")
@Expose
private String deliveryAlternateAddress;
@SerializedName("deliveryAddressType")
@Expose
private String deliveryAddressType;
@SerializedName("pickupNote")
@Expose
private String pickupNote;
@SerializedName("deliveryTimeSlot")
@Expose
private String deliveryTimeSlot;

public String getRentedBookId() {
return rentedBookId;
}

public void setRentedBookId(String rentedBookId) {
this.rentedBookId = rentedBookId;
}

public String getOrderId() {
return orderId;
}

public void setOrderId(String orderId) {
this.orderId = orderId;
}

public String getProductName() {
return productName;
}

public void setProductName(String productName) {
this.productName = productName;
}

public String getIsbn() {
return isbn;
}

public void setIsbn(String isbn) {
this.isbn = isbn;
}

public String getOrderOnDatetime() {
return orderOnDatetime;
}

public void setOrderOnDatetime(String orderOnDatetime) {
this.orderOnDatetime = orderOnDatetime;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getDeliveryName() {
return deliveryName;
}

public void setDeliveryName(String deliveryName) {
this.deliveryName = deliveryName;
}

public String getDeliveryMobile() {
return deliveryMobile;
}

public void setDeliveryMobile(String deliveryMobile) {
this.deliveryMobile = deliveryMobile;
}

public String getDeliveryPincode() {
return deliveryPincode;
}

public void setDeliveryPincode(String deliveryPincode) {
this.deliveryPincode = deliveryPincode;
}

public String getDeliveryLocality() {
return deliveryLocality;
}

public void setDeliveryLocality(String deliveryLocality) {
this.deliveryLocality = deliveryLocality;
}

public String getDeliveryAddress() {
return deliveryAddress;
}

public void setDeliveryAddress(String deliveryAddress) {
this.deliveryAddress = deliveryAddress;
}

public String getDeliveryCityId() {
return deliveryCityId;
}

public void setDeliveryCityId(String deliveryCityId) {
this.deliveryCityId = deliveryCityId;
}

public String getDeliveryCityName() {
return deliveryCityName;
}

public void setDeliveryCityName(String deliveryCityName) {
this.deliveryCityName = deliveryCityName;
}

public String getDeliveryLandmark() {
return deliveryLandmark;
}

public void setDeliveryLandmark(String deliveryLandmark) {
this.deliveryLandmark = deliveryLandmark;
}

public String getDeliveryAlternateMobileNumber() {
return deliveryAlternateMobileNumber;
}

public void setDeliveryAlternateMobileNumber(String deliveryAlternateMobileNumber) {
this.deliveryAlternateMobileNumber = deliveryAlternateMobileNumber;
}

public String getDeliveryAlternateAddress() {
return deliveryAlternateAddress;
}

public void setDeliveryAlternateAddress(String deliveryAlternateAddress) {
this.deliveryAlternateAddress = deliveryAlternateAddress;
}

public String getDeliveryAddressType() {
return deliveryAddressType;
}

public void setDeliveryAddressType(String deliveryAddressType) {
this.deliveryAddressType = deliveryAddressType;
}

public String getPickupNote() {
return pickupNote;
}

public void setPickupNote(String pickupNote) {
this.pickupNote = pickupNote;
}

public String getDeliveryTimeSlot() {
return deliveryTimeSlot;
}

public void setDeliveryTimeSlot(String deliveryTimeSlot) {
this.deliveryTimeSlot = deliveryTimeSlot;
}

}