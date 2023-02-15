package com.example.inventorymanagementsystem.models;

import java.io.Serializable;

public class Orders implements Serializable {

    private String orderID;
    private String orderDate;
    private String orderReference;
    private String orderCustomer;
    private String orderCompany;
    private String orderStatus;
    private String orderFirstAddress;
    private String orderSecondAddress;
    private String orderState;
    private String orderCity;
    private String orderZipcode;
    private String orderCountry;
    private String orderPhoneNumber;
    private String orderEmailAddress;
    private String orderShippingMethod;


    public Orders() {

    }
    public Orders(String orderID, String orderDate, String orderReference, String orderCustomer, String orderStatus) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.orderReference = orderReference;
        this.orderCustomer = orderCustomer;
        this.orderStatus = orderStatus;
    }

    public String getOrderCompany() {
        return orderCompany;
    }

    public void setOrderCompany(String orderCompany) {
        this.orderCompany = orderCompany;
    }

    public String getOrderFirstAddress() {
        return orderFirstAddress;
    }

    public void setOrderFirstAddress(String orderFirstAddress) {
        this.orderFirstAddress = orderFirstAddress;
    }

    public String getOrderSecondAddress() {
        return orderSecondAddress;
    }

    public void setOrderSecondAddress(String orderSecondAddress) {
        this.orderSecondAddress = orderSecondAddress;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderCity() {
        return orderCity;
    }

    public void setOrderCity(String orderCity) {
        this.orderCity = orderCity;
    }

    public String getOrderZipcode() {
        return orderZipcode;
    }

    public void setOrderZipcode(String orderZipcode) {
        this.orderZipcode = orderZipcode;
    }

    public String getOrderCountry() {
        return orderCountry;
    }

    public void setOrderCountry(String orderCountry) {
        this.orderCountry = orderCountry;
    }

    public String getOrderPhoneNumber() {
        return orderPhoneNumber;
    }

    public void setOrderPhoneNumber(String orderPhoneNumber) {
        this.orderPhoneNumber = orderPhoneNumber;
    }

    public String getOrderEmailAddress() {
        return orderEmailAddress;
    }

    public void setOrderEmailAddress(String orderEmailAddress) {
        this.orderEmailAddress = orderEmailAddress;
    }

    public String getOrderShippingMethod() {
        return orderShippingMethod;
    }

    public void setOrderShippingMethod(String orderShippingMethod) {
        this.orderShippingMethod = orderShippingMethod;
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
