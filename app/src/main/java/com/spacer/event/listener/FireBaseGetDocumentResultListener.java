package com.spacer.event.listener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public interface FireBaseGetDocumentResultListener extends OnSuccessListener<DocumentSnapshot>, OnFailureListener { }
