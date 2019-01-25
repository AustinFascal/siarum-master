package com.asramaum.siarum.firebaseServices;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import static android.content.ContentValues.TAG;

/**
 * Created by austi on 6/22/2018.
 */

public class FirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}
