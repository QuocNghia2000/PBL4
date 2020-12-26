package com.example.myapplication.model;

import java.io.Serializable;

public class User implements Serializable {
    public int userID;
    public String username;
    public String password;
    public String fullName;
    public User(int _userID,String _username,String _password,String _fullname)
    {
        this.userID=_userID;
        this.username=_username;
        this.password=_password;
        this.fullName=_fullname;
    }
    public User(int _UserID,String _Fullname)
    {
        this.userID=_UserID;
        this.fullName=_Fullname;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
