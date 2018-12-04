package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import pl.droidsonroids.gif.GifImageView;

public class RatingSuccess extends AppCompatActivity{
 GifImageView image;
private SharedPreferences sharedPref1;
private String sharedPrefKey;
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_success);
        image = (GifImageView)findViewById(R.id.like);
        sharedPref1 = getSharedPreferences1();
        Toast.makeText(RatingSuccess.this,"Redirecting in 3 secs",Toast.LENGTH_SHORT);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

@Override
public void run() {
        // change image
        sharedPrefKey = getResources().getString(R.string.user_type_frag_preference_file_key);
        String user_type = (sharedPref1.getString(sharedPrefKey, ""));


        if (user_type.toLowerCase().equalsIgnoreCase("contractor")) {
        Intent i = new Intent(RatingSuccess.this, NavigationBarContractors.class);
        startActivity(i);

        }
        if (user_type.toLowerCase().equalsIgnoreCase("Employer")) {
        Intent i = new Intent(RatingSuccess.this, NavigationBarUser.class);
        startActivity(i);

        }


        }

        }, 3000);
        }

@Override
public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(RatingSuccess.this, NavigationBarUser.class);
        startActivity(i);
        }
private SharedPreferences getSharedPreferences1()
        {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
        getString(R.string.user_type_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;
        }
        }