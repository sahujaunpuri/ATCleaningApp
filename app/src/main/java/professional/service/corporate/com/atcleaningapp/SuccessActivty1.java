package professional.service.corporate.com.atcleaningapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import pl.droidsonroids.gif.GifImageView;

public class SuccessActivty1  extends Activity {
    GifImageView image;
    private SharedPreferences sharedPref1;
    private String sharedPrefKey;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success1);
        image = (GifImageView)findViewById(R.id.check);

        Toast.makeText(SuccessActivty1.this,"Redirecting in 3 secs",Toast.LENGTH_SHORT);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SuccessActivty1.this, NavigationBarContractors.class);
                startActivity(i);



            }

        }, 3000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SuccessActivty1.this, NavigationBarContractors.class);
        startActivity(i);
    }
}