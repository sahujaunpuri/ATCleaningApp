package professional.service.corporate.com.atcleaningapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ShareCurrentLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback {

    private Button btnProceed;
    private TextView tvAddress;
    private TextView tvEmpty;
    private RelativeLayout rlPick;
    double latitude;
    double longitude;

    LocationHelper locationHelper;

    Location mLastLocation;
    String userType;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    TextView distance;
    TextView next;
    String address;
    // TextView duration;
    ArrayList<LoginDBClass> lac;
    SessionManager sm;
    int cont_id;
    private ArrayList<UserProfile> m_parts;
    String origin;
    private String sharedPrefKey;
    String personName;
    String personPhotoUrl;
    String userEmail;
    String city;
    String state;
    int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_service);

        locationHelper=new LocationHelper(this);
        locationHelper.checkpermission();
// distance = (TextView)findViewById(R.id.distance);
        userType = getIntent().getStringExtra("type");
        lac = new ArrayList<>();
        sm = new SessionManager(getApplicationContext());
        m_parts = new ArrayList<>();
        personName = getIntent().getStringExtra("name");
        personPhotoUrl = getIntent().getStringExtra("url");
        userEmail = getIntent().getStringExtra("email");
        // user_id = getIntent().getIntExtra("contractor_id",0);
        final SharedPreferences sharedPref1 = getSharedpreferences1();
        //getUserLocations();
        sharedPrefKey = getResources().getString(R.string.user_type_frag_preference_file_key);
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            cont_id = Integer.parseInt(f.getUser_id());
        }
        user_id = cont_id;

        btnProceed = (Button)findViewById(R.id.btnLocation);
        tvAddress = (TextView)findViewById(R.id.tvAddress);
        tvEmpty = (TextView)findViewById(R.id.tvEmpty);
        rlPick = (RelativeLayout) findViewById(R.id.rlPickLocation);

        rlPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLastLocation=locationHelper.getLocation();

                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    getAddress();

                } else {

                    if(btnProceed.isEnabled())
                        btnProceed.setEnabled(false);

                    showToast("Couldn't get the location. Make sure location is enabled on the device");
                }
            }
        });



        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Please wait!");
                addMapsLocationToDatabase();
                String s =  sharedPref1.getString(sharedPrefKey, "").toLowerCase();
                if ((sharedPref1.getString(sharedPrefKey, "")).toLowerCase().equals("contractor")) {
                    Intent i = new Intent(ShareCurrentLocation.this, NavigationBarContractors.class);
                    i.putExtra("personName",personName);
                    i.putExtra("personPhotoUrl",personPhotoUrl);
                    i.putExtra("userEmail",userEmail);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }

                if ((sharedPref1.getString(sharedPrefKey, "")).toLowerCase().equals("employer")) {
                    Intent i = new Intent(ShareCurrentLocation.this, NavigationBarUser.class);
                    i.putExtra("personName",personName);
                    i.putExtra("personPhotoUrl",personPhotoUrl);
                    i.putExtra("userEmail",userEmail);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }

            }
        });

        // check availability of play services
        if (locationHelper.checkPlayServices()) {

            // Building the GoogleApi client
            locationHelper.buildGoogleApiClient();
        }

    }


    public void getAddress()
    {
        Address locationAddress;

        locationAddress=locationHelper.getAddress(latitude,longitude);

        if(locationAddress!=null)
        {

            address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            city = locationAddress.getLocality();
            state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();


            String currentLocation;

            if(!TextUtils.isEmpty(address))
            {
                currentLocation=address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation+="\n"+address1;

                if (!TextUtils.isEmpty(city))
                {
                    currentLocation+="\n"+city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+=" - "+postalCode;
                }
                else
                {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+="\n"+postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation+="\n"+state;

                if (!TextUtils.isEmpty(country))
                    currentLocation+="\n"+country;

                tvEmpty.setVisibility(View.GONE);
                tvAddress.setText(currentLocation);
                tvAddress.setVisibility(View.VISIBLE);

                if(!btnProceed.isEnabled())
                    btnProceed.setEnabled(true);
            }

        }
        else
            showToast("Something went wrong");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationHelper.onActivityResult(requestCode,resultCode,data);
    }



    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Connection failed:", " ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        mLastLocation=locationHelper.getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        locationHelper.connectApiClient();
    }


    // Permission check functions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        locationHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

    public void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void addMapsLocationToDatabase() {
        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;
            String apiResponseMessage;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());
                int contractor_id = user_id;
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                String user_type1 = userType;
                params.put("user_id", String.valueOf(contractor_id));
                params.put("location", address );
                params.put("city", city );
                params.put("state", state );
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("user_type",user_type1);
                Log.i("Sourav,user Type","UT:"+user_type1);
                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_ADD_LOCATION, params, Request.Method.POST);
                    return response.toString();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    JSONObject apiResponse = (JSONObject) obj.get("response");

                    if (apiResponse.getInt("status") != 200) {

                        return;
                    }

                    ///  JSONObject data = (JSONObject) apiResponse.get("data");

                    //  final int contractor_id = UserSession.getUserSession(getIntent()).getUserId();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

//executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();


    }

    private SharedPreferences getSharedpreferences1()
    {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.user_type_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;
    }

}
