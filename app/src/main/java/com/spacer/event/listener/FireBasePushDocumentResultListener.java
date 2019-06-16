package com.spacer.event.listener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

public interface FireBasePushDocumentResultListener extends OnSuccessListener<Void>, OnFailureListener { }
