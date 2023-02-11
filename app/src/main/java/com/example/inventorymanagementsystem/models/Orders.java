package com.example.inventorymanagementsystem.models;

import java.io.Serializable;

public class Orders implements Serializable {

    private String orderID;
    private String orderDate;
    private String orderReference;
    private String orderCustomer;
    private String orderStatus;


    public Orders() {

    }
    public Orders(String orderID, String orderDate, String orderReference, String orderCustomer, String orderStatus) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.orderReference = orderReference;
        this.orderCustomer = orderCustomer;
        this.orderStatus = orderStatus;
    }
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }

    public String getOrderCustomer() {
        return orderCustomer;
    }

    public void setOrderCustomer(String orderCustomer) {
        this.orderCustomer = orderCustomer;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
