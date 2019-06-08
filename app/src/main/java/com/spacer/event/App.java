package com.spacer.event;

import android.app.Application;

import com.spacer.event.util.PreferenceUtil;

public class App extends Application {
    private static App mInstance;

    public static synchronized App getInstance() {
        return mInstance;
    }

    public PreferenceUtil getPreferencesUtility() {
        return PreferenceUtil.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
