package com.spacer.event.listener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

public abstract class FireBaseCollectionListener implements OnSuccessListener<QuerySnapshot>, OnFailureListener {
}
