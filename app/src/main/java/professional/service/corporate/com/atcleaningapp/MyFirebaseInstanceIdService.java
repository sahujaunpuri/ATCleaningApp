package professional.service.corporate.com.atcleaningapp;

import android.app.Service;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private static final  String SENDER_ID = "948343076940";
    @Override
    public void onTokenRefresh() {
        String refreshedToken = null;
        try {
            refreshedToken = FirebaseInstanceId.getInstance().getToken(SENDER_ID, "FCM");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Sourav, Refreshed token: " + refreshedToken);

        storeToken(refreshedToken);

    }

    private void storeToken(String token) {
        //saving the token on shared preferences
        SharedPrefManager1.getInstance(getApplicationContext()).saveDeviceToken(token);
    }

}