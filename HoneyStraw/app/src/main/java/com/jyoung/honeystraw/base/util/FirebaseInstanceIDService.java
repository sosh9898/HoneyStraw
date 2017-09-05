package com.jyoung.honeystraw.base.util;

/**
 * Created by jyoung on 2017. 8. 18..
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    SharedPreferencesService preferencesService;

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        preferencesService = new SharedPreferencesService();
        preferencesService.load(getApplicationContext());
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        preferencesService.setPrefData("token", refreshedToken);

    }
}


