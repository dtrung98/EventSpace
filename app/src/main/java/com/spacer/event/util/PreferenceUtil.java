package com.spacer.event.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.spacer.event.App;

public final class PreferenceUtil {
    private static final String REMEMBER_ACCOUNT = "remember_account";
    public static final String EMAIL = "email";
    private static PreferenceUtil sInstance;
    private final SharedPreferences mSharedPreferences;
    private PreferenceUtil(@NonNull final Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceUtil getInstance() {
        if (sInstance == null) {
            sInstance = new PreferenceUtil(App.getInstance().getApplicationContext());
        }
        return sInstance;
    }

    public final boolean isRememberAccount() {
        return mSharedPreferences.getBoolean(REMEMBER_ACCOUNT,false);
    }

    public final void setRememberAccount(boolean rememberAccount) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(REMEMBER_ACCOUNT,rememberAccount);
        editor.apply();
    }

    public final String getSavedAccount() {
        return mSharedPreferences.getString(EMAIL,null);
    }

    public final void setSavedAccount(String email){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(EMAIL,email);
        editor.apply();
    }

}
