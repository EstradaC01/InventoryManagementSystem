package com.example.inventorymanagementsystem;

public class Company {
    private String mCompanyName, mCompanyCode, mCompanyOwner,
            mCompanyAddress, mCompanyCountry, mCompanyState, mCompanyZipcode;



    public Company() {

    }

    public Company(String companyName, String companyCode, String companyOwner, String companyAddress,
                   String companyCountry, String companyState, String companyZipcode) {
        mCompanyName = companyName;
        mCompanyCode = companyCode;
        mCompanyOwner = companyOwner;
        mCompanyCountry = companyCountry;
        mCompanyAddress = companyAddress;
        mCompanyState = companyState;
        mCompanyZipcode = companyZipcode;
    }
    public String getCompanyAddress() {
        return mCompanyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        mCompanyAddress = companyAddress;
    }

    public String getCompanyCountry() {
        return mCompanyCountry;
    }

    public void setCompanyCountry(String mCompanyCountry) {
        this.mCompanyCountry = mCompanyCountry;
    }

    public String getCompanyState() {
        return mCompanyState;
    }

    public void setCompanyState(String mCompanyState) {
        this.mCompanyState = mCompanyState;
    }

    public String getCompanyZipcode() {
        return mCompanyZipcode;
    }

    public void setCompanyZipcode(String mCompanyZipcode) {
        this.mCompanyZipcode = mCompanyZipcode;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public void setCompanyName(String mCompanyName) {
        this.mCompanyName = mCompanyName;
    }

    public String getCompanyCode() {
        return mCompanyCode;
    }

    public void setCompanyCode(String mCompanyCode) {
        this.mCompanyCode = mCompanyCode;
    }

    public String getCompanyOwner() {
        return mCompanyOwner;
    }

    public void setCompanyOwner(String mCompanyOwner) {
        this.mCompanyOwner = mCompanyOwner;
    }
}
