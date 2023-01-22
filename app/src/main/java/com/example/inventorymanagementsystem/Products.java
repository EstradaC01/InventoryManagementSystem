package com.example.inventorymanagementsystem;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Products implements Serializable {

    // variables for storing our data
    private String productId, productDescription, productUpc, productPcsPerBox, productTimeAdded, userKey, uniqueIdentifier, imageUri, productOwner;

    public Products()
    {
        // empty constructor
        // required for Firebase
    }

    // Constructor for all variables
    public Products(String _productId, String _productDescription, String _productUpc, String _productPcsPerBox, String _productTimeAdded, String _userKey, String _productOwner, String _imageUri)
    {
        productId = _productId;
        productDescription = _productDescription;
        productUpc = _productUpc;
        productPcsPerBox = _productPcsPerBox;
        productTimeAdded = _productTimeAdded;
        userKey = _userKey;
        productOwner = _productOwner;
        imageUri = _imageUri;
    }

    // getter methods for all variables
    public String getProductId() {
        return productId;
    }
    public String getProductDescription() {
        return productDescription;
    }
    public String getProductUpc() {
        return productUpc;
    }
    public String getProductPcsPerBox() {
        return productPcsPerBox;
    }
    public String getProductTimeAdded() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat currentTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        currentTime.setTimeZone(TimeZone.getTimeZone("EST"));

        return currentTime.format(calendar.getTime());
    }
    public String getUserKey() {
        return userKey;
    }
    public String getProductOwner() {
        return productOwner;
    }
    public String getImageUri() {
        return imageUri;
    }
    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    // setter method for all variables
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    public void setProductUpc(String productUpc) {
        this.productUpc = productUpc;
    }
    public void setProductPcsPerBox(String productPcsPerBox) {
        this.productPcsPerBox = productPcsPerBox;
    }
    public void setProductTimeAdded() {
       productTimeAdded = getProductTimeAdded();
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    public void setProductOwner(String productOwner) {
        this.productOwner = productOwner;
    }
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
