package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ContractorRegister extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {


    // DataBaseHandler dbHandler;
    EditText name;
    EditText mobile_num;
    EditText email;
    RadioGroup radioGroup;
    EditText password;
    String text2;
    String text1;
    String text;
    EditText occupation;
    TextView buttonSave;
    RadioButton gender1;
    SessionManager sm;
    public SQLiteDatabase db;
    private static final String TAG = MainActivity.class.getSimpleName();
    EditText feet;
    EditText inches;
    static final String CALENDAR = "calendar";
    private RadioButton radioButton;
    TextView specialities;
    TextView experience;
    private static final String LOG_TAG = "MainActivity";
    boolean status = false;
    int a;
    TextView next;
    private RadioButton male;
    private RadioButton female;
    double latitude;
    double longitude;
    String userType;
    private ProgressDialog mProgressDialog;
    String origin;
    LatLng latLng;
    private String sharedPrefKey3;
    private TextView check_Captcha;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    String location;

    String user_rec;
    private String sharedPrefKey;
    private SharedPreferences sharedPref;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private  TextView btn_return;

    private ImageView search_image;
    private EditText search_view;
    int cont_id;
    int hirer_id;
    private TextView select_btn;
    private ArrayList<LoginDBClass> lac;
    private String address;
    String city;
    String state;
    String clinic_name;
    EditText et_message;
    private TextView Tv_Message;
    private LinearLayout linearLayoutForm;
    private TextView verify_captcha;


    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));

    private ProgressBar mProgressBar;
    Button locate;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity_for_doctor);
        Intent intent = getIntent();

        //  user_rec = intent.getStringExtra("userType");
        // A loading indicator
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_login);
        sharedPref = this.getSharedpreferences();

        sharedPrefKey = getResources().getString(R.string.user_type_frag_preference_file_key);
        // seek.setProgress(Integer.valueOf(sharedPref2.getInt(sharedPrefWaterKey2, 0)));
        sm = new SessionManager(getApplicationContext());
        radioGroup  = (RadioGroup)findViewById(R.id.radioGroup);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        name = (EditText) findViewById(R.id.name);
        mobile_num = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        occupation = (EditText) findViewById(R.id.occupation);
        password = (EditText) findViewById(R.id.password);
        specialities = (EditText)findViewById(R.id.specialities);
        experience = (EditText)findViewById(R.id.experience);
        //locate = (Button)findViewById(R.id.locate);


        buttonSave = (TextView) findViewById(R.id.sign_up);

        mGoogleApiClient = new GoogleApiClient.Builder(ContractorRegister.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.male:
                        // do operations specific to this selection
                        break;
                    case R.id.female:
                        // do operations specific to this selection
                        break;

                }
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // setRowIdFromIntent();
                //saveState();
                registerUser();

            }
        });



    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());

                return;
            }
            final Place myPlace = places.get(0);
            LatLng queriedLocation = myPlace.getLatLng();
            clinic_name = myPlace.getName().toString();
            Log.i("Sourav, Latitude", "lat"+queriedLocation.latitude);
            Log.i("Sourav, Longitude", "lat"+queriedLocation.longitude);
            latitude = queriedLocation.latitude;
            longitude = queriedLocation.longitude;

            latLng = new LatLng(queriedLocation.latitude, queriedLocation.longitude);
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
            origin = latitude+","+longitude;
           // clinic_name = addresses.get(0).getAdminArea();
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();

            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

//            mNameTextView.setText(Html.fromHtml(place.getName() + ""));
//            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
//            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
//            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
//            mWebTextView.setText(place.getWebsiteUri() + "");
//            if (attributions != null) {
//                mAttTextView.setText(Html.fromHtml(attributions.toString()));
//            }
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPause() {
        super.onPause();
        // db = dbHandler.getWritableDatabase();
    }



    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private SharedPreferences getSharedStatus() {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.saved_status_hire_contract), Context.MODE_PRIVATE);

        return sharedPref;
    }


    private void registerUser() {


        final String email1 = email.getText().toString().trim();
        final String password1 = password.getText().toString().trim();
        final String occupation1 = occupation.getText().toString().trim();
        final String experience1 = experience.getText().toString().trim();
        final String specialities1 = specialities.getText().toString().trim();
        final String name1 = name.getText().toString().trim();
        final String mobile_num1 = mobile_num.getText().toString().trim();
        final String clinic_address = mAutocompleteTextView.getText().toString().trim();

        final String status1 = "Pending";
       // radioGroup.check(male.getId());
        int selectedId = radioGroup.getCheckedRadioButtonId();
        gender1 = (RadioButton) findViewById(selectedId);
        // user_rec = (sharedPref.getString(sharedPrefKey, ""));

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if ( !email.getText().toString().matches(emailPattern) ||email.getText().toString().trim().equalsIgnoreCase("") || (password.getText().toString().trim().equalsIgnoreCase("")) || (name.getText().toString().trim().equalsIgnoreCase(""))|| (experience.getText().toString().trim().equalsIgnoreCase(""))|| (specialities.getText().toString().trim().equalsIgnoreCase("")) ||(mobile_num.getText().toString().trim().equalsIgnoreCase(""))||mobile_num.getText().toString().length()<10) {

            if (!email.getText().toString().matches(emailPattern) || email.getText().toString().trim().equalsIgnoreCase(""))
                email.setError("Please Enter A Valid Email!!!");
            if (password.getText().toString().trim().equalsIgnoreCase(""))
                password.setError("Please Enter Password!!!");
            if (name.getText().toString().trim().equalsIgnoreCase(""))
                name.setError("Please Enter Name!!!");
            if (experience.getText().toString().trim().equalsIgnoreCase(""))
                experience.setError("Please Enter Name!!!");
            if (specialities.getText().toString().trim().equalsIgnoreCase(""))
               specialities.setError("Please Enter Name!!!");
            if (mAutocompleteTextView.getText().toString().trim().equalsIgnoreCase(""))
                mAutocompleteTextView.setError("Please Enter Name!!!");
            if (mobile_num.getText().toString().trim().equalsIgnoreCase("") || mobile_num.getText().toString().length()<10)
                mobile_num.setError("Please Enter At least 10 digit  Number!");

            return;
        } else {
            name.setError(null);

            email.setError(null);
            password.setError(null);
            experience.setError(null);
            specialities.setError(null);
            mAutocompleteTextView.setError(null);
            userType = sharedPref.getString(getResources().getString(R.string.user_type_frag_preference_file_key), "");


            class RegisterUser extends AsyncTask<Void, Void, String> {

                private ProgressBar progressBar;
                String apiResponseMessage;

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());
                    String city1 = "Dummy City";//city;
                    String state1 = "Dummy State";
                    double latitude1 = latitude;
                    double longitude1 = longitude;
                    String clinic_name1 = "Dummy CLinic";//clinic_name;
                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    String user_type1 = userType.toLowerCase();
                    String gender = gender1.getText().toString();
                    params.put("name", name1);
                    params.put("phone", mobile_num1);
                    params.put("email", email1);
                    params.put("gender", gender);
                    params.put("password", password1);
                    params.put("type", user_type1);
                    params.put("occupation",occupation1);
                    params.put("experience", experience1);
                    params.put("specialities", specialities1);
                    params.put("profile_status", status1);
                    params.put("address", clinic_address);
                    params.put("city", city1);
                   params.put("state", state1);
                    params.put("latitude", String.valueOf(latitude1));
                    params.put("longitude", String.valueOf(longitude1));
                    params.put("clinic_name", clinic_name1);
                    try {
                        JSONObject response = requestHandler.sendPostRequest(URLs.URL_REGISTER, params, Request.Method.POST);
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
                    mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_login);
                    mProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //hiding the progressbar after completion
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(ContractorRegister.this, apiResponseMessage, Toast.LENGTH_LONG).show();
                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        JSONObject apiResponse = (JSONObject) obj.get("response");

                        if (apiResponse.getInt("status") != 200) {
                            Toast.makeText(ContractorRegister.this, apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONObject data = (JSONObject) apiResponse.get("data");

                        // Toast.makeText(ContractorRegister.this, apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ContractorRegister.this, DetermineUser.class);

                        int listatus = 1;
                        sm.addUserLoginDetails(getApplicationContext(), String.valueOf(data.getInt("user_id")), String.valueOf(listatus));

                        //  final int user_id = UserSession.getUserSession(getIntent()).getUserId();

                        sharedPrefKey3 = getResources().getString(R.string.user_id);

                        // SharedPreferences.Editor mEditor3 = sharedPref3.edit();
                        SharedPreferences.Editor mEditor = sharedPref.edit();
//                        mEditor3.putInt(getString(R.string.user_id), data.getInt("user_id"));
//                        mEditor3.commit();

                        mEditor.putString(getString(R.string.user_type_frag_preference_file_key), userType);
                        mEditor.commit();
                        String ut = sharedPref.getString(getString(R.string.user_type_frag_preference_file_key), "");
                        Log.i("Sourav, ut", "UT" + ut);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit) ;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

//executing the async task
            RegisterUser ru = new RegisterUser();
            ru.execute();


        }

    }



    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private SharedPreferences getSharedpreferences() {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.user_type_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;
    }


}





