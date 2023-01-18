package com.example.inventorymanagementsystem;

public class Users {

    private String mFirstName, mLastName, mUserKey, mEmail;
    private boolean mIsAdmin;

    public Users()
    {
        // empty constructor
        // required by Firebase
    }

    // Constructors for all variables
    public Users(String _firstName, String _lastName, String _userKey, String _email, boolean _isAdmin) {
        mFirstName = _firstName;
        mLastName = _lastName;
        mUserKey = _userKey;
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

    public String getUserKey() {
        return mUserKey;
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

    public void setUserKey(String userKey) {
        mUserKey = userKey;
    }

    public void setEmail(String email) {
        mEmail = email;
    }
    public void setIsAdmin(boolean isAdmin) {
        mIsAdmin = isAdmin;
    }
}
