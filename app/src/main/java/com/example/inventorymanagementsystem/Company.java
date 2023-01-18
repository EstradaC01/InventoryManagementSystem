package com.example.inventorymanagementsystem;

public class Company {
    private String mCompanyName, mCompanyCode, mCompanyOwner;

    public Company() {

    }

    public Company(String companyName, String companyCode, String companyOwner) {
        mCompanyName = companyName;
        mCompanyCode = companyCode;
        mCompanyOwner = companyOwner;
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
