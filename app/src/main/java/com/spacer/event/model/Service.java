package com.spacer.event.model;

public class Service {
    private String id ="";
    private String staticName = "";
    private String name = "";
    private int primaryPrice = 0;

    public int getPrice() {
        if(isBonusService) return 0;
        else if(!isCountService) return primaryPrice;
        if(count<1) count = 1;
        return count*primaryPrice;
    }

    private String detail = "";
    private boolean isBonusService = false;
    private boolean isCountService = false;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count = 1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaticName() {
        return staticName;
    }

    public void setStaticName(String staticName) {
        this.staticName = staticName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrimaryPrice() {
        return primaryPrice;
    }

    public void setPrimaryPrice(int primaryPrice) {
        this.primaryPrice = primaryPrice;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean getIsBonusService() {
        return isBonusService;
    }

    public void setIsBonusService(boolean bonusService) {
        isBonusService = bonusService;
    }

    public boolean getIsCountService() {
        return isCountService;
    }

    public void setIsCountService(boolean countingService) {
        isCountService = countingService;
    }
}
