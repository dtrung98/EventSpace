package com.spacer.event.listener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

public interface FireBaseDocumentVoidResultListener extends OnSuccessListener<Void>, OnFailureListener { }
