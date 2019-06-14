package com.spacer.event.model;

import java.util.ArrayList;

public class UserInfo {
    public static int GENDER_NO_SET = -1;
    public static int GENDER_MALE = 0;
    public static int GENDER_FEMALE = 1;
    public static int GENDER_OTHER = 2;

    public static String ADMIN = "admin";
    public static String CUSTOMER = "customer";

    private String id;
    private String fullName;
    private int gender;
    private String address;
    private String avaUrl;
    private int balance;
    private String birthDay;
    private String email;
    private String phoneNumber;
    private String userType;
    private ArrayList<String> orderIDs;

    public UserInfo() {
        id= email = fullName= address = avaUrl =birthDay=phoneNumber=userType="";
        gender = -1;
        balance = 0;
        orderIDs = new ArrayList<>();

    }

    public UserInfo(String id, String fullName, int gender, String address, String avaUrl, int balance, String birthDay, String email, String phoneNumber, String userType, ArrayList<String> orderIDs) {
        this.id = id;
        this.fullName = fullName;
        this.gender = gender;
        this.address = address;
        this.avaUrl = avaUrl;
        this.balance = balance;
        this.birthDay = birthDay;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.orderIDs = orderIDs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvaUrl() {
        return avaUrl;
    }

    public void setAvaUrl(String avaUrl) {
        this.avaUrl = avaUrl;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public ArrayList<String> getOrderIDs() {
        return orderIDs;
    }

    public void setOrderIDs(ArrayList<String> orderIDs) {
        this.orderIDs = orderIDs;
    }
}
