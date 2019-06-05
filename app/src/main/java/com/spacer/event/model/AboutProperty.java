package com.spacer.event.model;

public class AboutProperty {
    private final String mName;
    private final String mValue;
    private AboutProperty() {
        mName = mValue = "";
    }

    public AboutProperty(String mName, String mValue) {
        this.mName = mName;
        this.mValue = mValue;
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }
}
