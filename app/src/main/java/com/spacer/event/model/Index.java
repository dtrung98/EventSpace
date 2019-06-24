package com.spacer.event.model;

import java.util.ArrayList;

public class Index {
    public static final String EVENT = "event";
    public static final String SPACE = "space";
    public static final String EVENT_SPACE = "eventspace";
    private int index = 0;
    private String  name = "";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description ="";
    private String type = "";
    private ArrayList<String> data;

    public ArrayList<String> getSpaceData() {
        if(spaceData==null) spaceData = new ArrayList<>();
        return spaceData;
    }

    public void setSpaceData(ArrayList<String> spaceData) {
        this.spaceData = spaceData;
    }

    private ArrayList<String> spaceData;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getData() {
        if(data==null) data = new ArrayList<>();
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
