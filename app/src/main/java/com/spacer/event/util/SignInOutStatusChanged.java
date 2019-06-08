package com.spacer.event.util;

import android.support.annotation.IntDef;

import com.google.firebase.auth.FirebaseUser;


public interface SignInOutStatusChanged {

    void onJustSignIn(FirebaseUser user);
    void onJustSignOut();
}
