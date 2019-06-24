package com.spacer.event.ui.main;

import android.support.v7.app.AppCompatActivity;

import com.spacer.event.listener.FireBaseCollectionListener;

import java.util.ArrayList;

public abstract class BaseEventSpaceActivity extends AppCompatActivity {
    private static final String TAG = "BaseEventSpace";

    ArrayList<FireBaseCollectionListener> mEventCollectionListeners = new ArrayList<>();
    ArrayList<FireBaseCollectionListener> mSpaceCollectionListeners = new ArrayList<>();
    ArrayList<FireBaseCollectionListener> mServiceCollectionListeners = new ArrayList<>();

    public void addEventListener(FireBaseCollectionListener listener) {
        if(!mEventCollectionListeners.contains(listener))
            mEventCollectionListeners.add(listener);
    }

    public void removeEventListener(FireBaseCollectionListener listener) {
        mEventCollectionListeners.remove(listener);
    }

    public void addSpaceListener(FireBaseCollectionListener listener) {
        if(!mSpaceCollectionListeners.contains(listener))
            mSpaceCollectionListeners.add(listener);
    }

    public void removeSpaceListener(FireBaseCollectionListener listener) {
        mSpaceCollectionListeners.remove(listener);
    }
    public void addServiceListener(FireBaseCollectionListener listener) {
        if(!mServiceCollectionListeners.contains(listener))
            mServiceCollectionListeners.add(listener);
    }

    public void removeServiceListener(FireBaseCollectionListener listener) {
        mServiceCollectionListeners.remove(listener);
    }
}
