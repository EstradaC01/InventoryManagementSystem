package com.example.inventorymanagementsystem;

public class Products {

    // variables for storing our data
    private String mProductId, mProductDescription, mProductUpc, mProductQty, mProductPcsPerBox, mProductTimeAdded;

    public Products()
    {
        // empty constructor
        // required for Firebase
    }

    // Constructor for all variables
    public Products(String _productId, String _productDescription, String _productUpc, String _productQty, String _productPcsPerBox, String _productTimeAdded)
    {
        mProductId = _productId;
        mProductDescription = _productDescription;
        mProductUpc = _productUpc;
        mProductQty = _productQty;
        mProductPcsPerBox = _productPcsPerBox;
        mProductTimeAdded = _productTimeAdded;
    }

    // getter methods for all variables

    public String getProductId() {
        return mProductId;
    }

    public String getProductDescription() {
        return mProductDescription;
    }

    public String getProductUpc() {
        return mProductUpc;
    }

    public String getProductQty() {
        return mProductQty;
    }

    public String getProductPcsPerBox() {
        return mProductPcsPerBox;
    }
    public String getProductTimeAdded() {
        return mProductTimeAdded;
    }
    // setter method for all variables

    public void setProductId(String productId) {
        mProductId = productId;
    }

    public void setProductDescription(String productDescription) {
        mProductDescription = productDescription;
    }

    public void setProductUpc(String productUpc) {
        mProductUpc = productUpc;
    }

    public void setProductQty(String productQty) {
        mProductQty = productQty;
    }

    public void setProductPcsPerBox(String productPcsPerBox) {
        mProductPcsPerBox = productPcsPerBox;
    }
    public void setProductTimeAdded(String productTimeAdded) {
       mProductTimeAdded = productTimeAdded;
    }
}
