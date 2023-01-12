package com.example.inventorymanagementsystem;

public class Users {

    private String mFirstName, mLastName, mUserId, mEmail;
    private boolean mIsAdmin;

    public Users()
    {
        // empty constructor
        // required by Firebase
    }

    // Constructors for all variables
    public Users(String _firstName, String _lastName, String _userId, String _email, boolean _isAdmin) {
        mFirstName = _firstName;
        mLastName = _lastName;
        mUserId = _userId;
        mEmail = _email;
        mIsAdmin = _isAdmin;
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
    public boolean getIsAdmin() {
        return mIsAdmin;
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
    public void setIsAdmin(boolean isAdmin) {
        mIsAdmin = isAdmin;
    }
}
