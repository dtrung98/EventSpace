package com.spacer.event.model;

public class Service {
    private String id ="";
    private String staticName = "";
    private String name = "";
    private int primaryPrice = 0;
    private String detail = "";
    private boolean isBonusService = false;
    private boolean isCountingService = false;

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

    public boolean isBonusService() {
        return isBonusService;
    }

    public void setBonusService(boolean bonusService) {
        isBonusService = bonusService;
    }

    public boolean isCountingService() {
        return isCountingService;
    }

    public void setCountingService(boolean countingService) {
        isCountingService = countingService;
    }
}
