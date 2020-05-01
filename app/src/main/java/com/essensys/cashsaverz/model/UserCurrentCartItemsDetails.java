package com.essensys.cashsaverz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCurrentCartItemsDetails {

@SerializedName("cartItemCount")
@Expose
private Integer cartItemCount;

public Integer getCartItemCount() {
return cartItemCount;
}

public void setCartItemCount(Integer cartItemCount) {
this.cartItemCount = cartItemCount;
}

}