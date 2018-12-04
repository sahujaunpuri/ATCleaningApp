package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView login;
    TextView signup;
    private SharedPreferences sharedPref;
    private String sharedPrefKey;
    private String sharedPrefKey1;
    private SharedPreferences sharedPref1;
    private SharedPreferences sharedPref3;
    TextView captcha;
    private boolean isFirstStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (TextView) findViewById(R.id.log_in);
        signup = (TextView) findViewById(R.id.sign_up);

        SessionManager sm = new SessionManager(getApplicationContext());
        sharedPref = this.getSharedpreferences();
        sharedPref1 = this.getSharedpreferences1();
        sharedPref3 = this.getSharedpreferences3();
        // next = (TextView)findViewById(R.id.nextBtn);
        Boolean loginStatus = sm.checkLoginStatus();
        Boolean networkAvailablity = false;
        networkAvailablity = isNetworkAvailable();
        if (networkAvailablity == true) {
            sharedPrefKey1 = getResources().getString(R.string.login_frag_preference_file_key);
            int listatus = (sharedPref1.getInt(sharedPrefKey1, 0));
            sharedPrefKey = getResources().getString(R.string.user_type_frag_preference_file_key);

            String userType1 = sharedPref.getString(getString(R.string.user_type_frag_preference_file_key), "");
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    //  Intro App Initialize SharedPreferences
                    SharedPreferences getSharedPreferences = android.preference.PreferenceManager
                            .getDefaultSharedPreferences(getBaseContext());

                    //  Create a new boolean and preference and set it to true
                    isFirstStart = getSharedPreferences.getBoolean("firstStart", true);

                    //  Check either activity or app is open very first time or not and do action
                    if (isFirstStart) {

                        //  Launch application introduction screen
                        Intent i = new Intent(MainActivity.this, MainIntroActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        SharedPreferences.Editor e = getSharedPreferences.edit();
                        e.putBoolean("firstStart", false);
                        e.apply();
                    } else {

                    }

                }
            });
            t.start();
            if ((loginStatus == true) && (userType1.equalsIgnoreCase("Doctor"))) {
                Intent i = new Intent(MainActivity.this, NavigationBarUser.class);

                int hirer_id = sharedPref3.getInt(getString(R.string.user_id), 0);
               // i.putExtra("hirer_id", hirer_id);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
            if ((loginStatus == true) && (userType1.equalsIgnoreCase("patients"))) {
                Intent i = new Intent(MainActivity.this, NavigationBarContractors.class);

                int hirer_id = sharedPref3.getInt(getString(R.string.user_id), 0);
               // i.putExtra("hirer_id", hirer_id);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }


            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);

                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, LoginSelection.class);
                    startActivity(i);

                }
            });
        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private SharedPreferences getSharedpreferences () {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.user_type_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;


    }

    private SharedPreferences getSharedpreferences1 () {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.login_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;


    }
    private SharedPreferences getSharedpreferences3 () {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.user_id), Context.MODE_PRIVATE);

        return sharedPref;


    }
}
