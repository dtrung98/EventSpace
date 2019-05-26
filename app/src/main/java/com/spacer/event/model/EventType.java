package com.spacer.event.model;

import java.util.ArrayList;

public class EventType {
    private String id = "";
    private String name ="";
    private String staticName="";
    private String icon = "";
    private String detail = "";
    private ArrayList<String> services = new ArrayList<>();
    private ArrayList<Integer> prices = new ArrayList<>();
    private ArrayList<Integer> counts = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaticName() {
        return staticName;
    }

    public void setStaticName(String staticName) {
        this.staticName = staticName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ArrayList<String> getServices() {
        return services;
    }

    public void setServices(ArrayList<String> services) {
        this.services = services;
    }

    public ArrayList<Integer> getPrices() {
        return prices;
    }

    public void setPrices(ArrayList<Integer> prices) {
        this.prices = prices;
    }

    public ArrayList<Integer> getCounts() {
        return counts;
    }

    public void setCounts(ArrayList<Integer> counts) {
        this.counts = counts;
    }
}
