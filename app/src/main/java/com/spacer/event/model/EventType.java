package com.spacer.event.model;

import android.support.annotation.Nullable;

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

/*    public ArrayList<String> getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(ArrayList<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public ArrayList<String> serviceNames = new ArrayList<>();*/

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof EventType) {
            return this.staticName.equals(((EventType)obj).staticName);
        } else if (obj instanceof String) {
            return this.staticName.equals(obj);
        }
        return false;
    }
}
