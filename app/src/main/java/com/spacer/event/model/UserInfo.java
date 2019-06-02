package com.spacer.event.model;

public class UserInfo {
    private String userName;
    private String email;
    private String password;
    private String userType;
    private String avtUrl;

    public UserInfo(String userName, String email, String password, String userType, String avtUrl) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.avtUrl = avtUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAvtUrl() {
        return avtUrl;
    }

    public void setAvtUrl(String avtUrl) {
        this.avtUrl = avtUrl;
    }
}
