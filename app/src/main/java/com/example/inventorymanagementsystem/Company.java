package com.example.inventorymanagementsystem;

import java.io.Serializable;

public class Company implements Serializable {

    private String companyName, companyCode, companyOwner,
            companyAddress, companyCountry, companyState, companyZipcode, companyCity;

    public Company() {

    }

    public Company(String companyName, String companyCode, String companyOwner, String companyAddress,
                   String companyCountry, String companyState, String companyZipcode, String companyCity) {
        this.companyName = companyName;
        this.companyCode = companyCode;
        this.companyOwner = companyOwner;
        this.companyCountry = companyCountry;
        this.companyAddress = companyAddress;
        this.companyState = companyState;
        this.companyZipcode = companyZipcode;
        this.companyCity = companyCity;
    }
    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyCountry() {
        return companyCountry;
    }

    public void setCompanyCountry(String mCompanyCountry) {
        this.companyCountry = mCompanyCountry;
    }

    public String getCompanyState() {
        return companyState;
    }

    public void setCompanyState(String mCompanyState) {
        this.companyState = mCompanyState;
    }

    public String getCompanyZipcode() {
        return companyZipcode;
    }

    public void setCompanyZipcode(String mCompanyZipcode) {
        this.companyZipcode = mCompanyZipcode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String mCompanyName) {
        this.companyName = mCompanyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String mCompanyCode) {
        this.companyCode = mCompanyCode;
    }

    public String getCompanyOwner() {
        return companyOwner;
    }

    public void setCompanyOwner(String mCompanyOwner) {
        this.companyOwner = mCompanyOwner;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }
}
