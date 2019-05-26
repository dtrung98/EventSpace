package com.spacer.event.model;

import java.util.ArrayList;

public class Space {
    private String id;
    private String name;
    private String address;
    private String detail;
    private ArrayList<String> images;
    private String phone;
    private ArrayList<String> supportEvents;
    private ArrayList<String> eventTypeNames = new ArrayList<>();

    public Space() {
        id =name = address = detail = phone = "";
        images = new ArrayList<>();
        supportEvents = new ArrayList<>();
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<String> getSupportEvents() {
        return supportEvents;
    }

    public void setSupportEvents(ArrayList<String> support_events) {
        this.supportEvents = support_events;
    }

    @Override
    public String toString() {
        return "Space{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", detail='" + detail + '\'' +
                ", images=" + images +
                ", phone='" + phone + '\'' +
                ", supportEvents=" + supportEvents +
                '}';
    }

    public ArrayList<String> getEventTypeNames() {
        return eventTypeNames;
    }

    public void setEventTypeNames(ArrayList<String> eventTypeNames) {
        this.eventTypeNames = eventTypeNames;
    }
}
