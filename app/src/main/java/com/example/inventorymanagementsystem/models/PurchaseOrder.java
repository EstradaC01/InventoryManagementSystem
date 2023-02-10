package com.example.inventorymanagementsystem.models;

import java.util.ArrayList;

public class PurchaseOrder {
    ArrayList<Products> products;
    String anticipatedArrivalDate, poNumber, shippingFrom, dateCreated, status;

    public PurchaseOrder() {

    }
    public PurchaseOrder(ArrayList<Products> products, String anticipatedArrivalDate, String poNumber, String shippingFrom, String dateCreated, String status) {
        this.products = products;
        this.anticipatedArrivalDate = anticipatedArrivalDate;
        this.poNumber = poNumber;
        this.shippingFrom = shippingFrom;
        this.dateCreated = dateCreated;
        this.status = status;
    }

    public ArrayList<Products> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Products> products) {
        this.products = products;
    }

    public String getAnticipatedArrivalDate() {
        return anticipatedArrivalDate;
    }

    public void setAnticipatedArrivalDate(String anticipatedArrivalDate) {
        this.anticipatedArrivalDate = anticipatedArrivalDate;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getShippingFrom() {
        return shippingFrom;
    }

    public void setShippingFrom(String shippingFrom) {
        this.shippingFrom = shippingFrom;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
