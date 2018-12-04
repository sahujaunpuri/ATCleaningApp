package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefManager1 {
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";

    private static SharedPrefManager1 mInstance;
    private static Context mCtx;

    private SharedPrefManager1(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager1 getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager1(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        Log.i("Sourav,token save","token"+token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }

}