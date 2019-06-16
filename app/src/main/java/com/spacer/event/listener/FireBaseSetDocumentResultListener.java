package com.spacer.event.listener;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

public interface FireBaseSetDocumentResultListener extends OnSuccessListener<AuthResult>, OnFailureListener { }
