package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetermineUser extends AppCompatActivity {
    SharedPreferences sharedPref1;
    String sharedPrefKey;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_determine);
        sharedPrefKey = getResources().getString(R.string.user_type_frag_preference_file_key);
        sharedPref1 = getSharedpreferences1();
        if ((sharedPref1.getString(sharedPrefKey, "")).toLowerCase().equalsIgnoreCase("user")) {
            Intent i = new Intent(DetermineUser.this, NavigationBarUser.class);
            i.putExtra("type", "user");
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if ((sharedPref1.getString(sharedPrefKey, "")).toLowerCase().equalsIgnoreCase("contractors")) {

            Intent i = new Intent(DetermineUser.this, NavigationBarContractors.class);
            i.putExtra("type", "contractors");
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }



    }
    private SharedPreferences getSharedpreferences1()
    {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.user_type_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;
    }
}
