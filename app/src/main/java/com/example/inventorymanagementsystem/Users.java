package com.example.inventorymanagementsystem;

public class Users {

    private String mFirstName, mLastName, mUserId, mEmail;

    public Users()
    {
        // empty constructor
        // required by Firebase
    }

    // Constructors for all variables
    public Users(String _firstName, String _lastName, String _userId, String _email) {
        mFirstName = _firstName;
        mLastName = _lastName;
        mUserId = _userId;
        mEmail = _email;
    }

    // getters

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getEmail() {
        return mEmail;
    }

    // setters

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public void setEmail(String email) {
        mEmail = email;
    }
}
