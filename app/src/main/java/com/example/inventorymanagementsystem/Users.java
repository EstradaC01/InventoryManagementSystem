package com.example.inventorymanagementsystem;

import java.io.Serializable;

public class Users implements Serializable {

    private String firstName;
    private String lastName;
    private String userKey;
    private String email;
    private String systemId;
    private String role;
    private boolean isAdmin;

    public Users()
    {
        // empty constructor
        // required by Firebase
    }

    // Constructors for all variables
    public Users(String _firstName, String _lastName, String _userKey, String _email, boolean _isAdmin) {
        firstName = _firstName;
        lastName = _lastName;
        userKey = _userKey;
        email = _email;
        isAdmin = _isAdmin;
    }

    // getters

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserKey() {
        return userKey;
    }

    public String getEmail() {
        return email;
    }
    public String getSystemId() {
        return systemId;
    }
    public boolean getIsAdmin() {
        return isAdmin;
    }
    // setters

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
