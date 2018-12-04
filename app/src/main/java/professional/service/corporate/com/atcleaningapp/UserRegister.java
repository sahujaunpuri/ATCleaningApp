package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UserRegister extends AppCompatActivity {


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
    TextView check_Captcha;
    public SQLiteDatabase db;
    private static final String TAG = MainActivity.class.getSimpleName();
    EditText feet;
    EditText inches;
    static final String CALENDAR = "calendar";
    private RadioButton radioButton;

    boolean status = false;
    int a;
    TextView next;
    private RadioButton male;
    private RadioButton female;

    String userType;
    private ProgressDialog mProgressDialog;
    private String token;

    EditText et_message;
    private TextView Tv_Message;
    private LinearLayout linearLayoutForm;
    private TextView verify_captcha;


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
    private String sharedPrefKey3;
    SharedPreferences sharedPref3;
    private ProgressBar mProgressBar;
   // private static final String TAG = "reCAPTCHA_Activity";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
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
        token = SharedPrefManager1.getInstance(this).getDeviceToken();
       // linearLayoutForm = (LinearLayout)findViewById(R.id.linearLayoutForm);
        buttonSave = (TextView) findViewById(R.id.sign_up);

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
      //  final String occupation1 = occupation.getText().toString().trim();
        // final String usertype = password.getText().toString().trim();
        final String name1 = name.getText().toString().trim();
        final String mobile_num1 = mobile_num.getText().toString().trim();
       // radioGroup.check(male.getId());
        int selectedId = radioGroup.getCheckedRadioButtonId();

        gender1 = (RadioButton) findViewById(selectedId);

       // user_rec = (sharedPref.getString(sharedPrefKey, ""));
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if ( !email.getText().toString().matches(emailPattern)||(email.getText().toString().trim().equalsIgnoreCase("")) || (password.getText().toString().trim().equalsIgnoreCase("")) || (name.getText().toString().trim().equalsIgnoreCase(""))||(mobile_num.getText().toString().trim().equalsIgnoreCase(""))||mobile_num.getText().toString().length()<10) {

            if (!email.getText().toString().matches(emailPattern) || email.getText().toString().trim().equalsIgnoreCase(""))
                email.setError("Please Enter A Valid Email!!!");
            if (password.getText().toString().trim().equalsIgnoreCase(""))
                password.setError("Please Enter Password!!!");
            if (name.getText().toString().trim().equalsIgnoreCase(""))
                name.setError("Please Enter Name!!!");
            if (mobile_num.getText().toString().trim().equalsIgnoreCase("") || mobile_num.getText().toString().length()<10)
                mobile_num.setError("Please Enter At least 10 digit  Number!");

            return;
        } else {
            name.setError(null);

            email.setError(null);
            password.setError(null);
            userType = sharedPref.getString(getResources().getString(R.string.user_type_frag_preference_file_key), "");
      

            class RegisterUser extends AsyncTask<Void, Void, String> {

                private ProgressBar progressBar;
                String apiResponseMessage;

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());
                    final String token_generated = token;
                    String dummy ="dummy";
                    HashMap<String, String> params = new HashMap<>();
                    String user_type1 = userType.toLowerCase();
                    String gender = gender1.getText().toString();
                    params.put("name", name1);
                    params.put("phone", mobile_num1);
                    params.put("email", email1);
                    params.put("gender", gender);
                    params.put("password", password1);
                    params.put("type", user_type1);
                    params.put("occupation",dummy);
                    params.put("token", token_generated);



                    try {
                        JSONObject response = requestHandler.sendPostRequest(URLs.URL_REGISTER_USER, params, Request.Method.POST);
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

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        JSONObject apiResponse = (JSONObject) obj.get("response");

                        if (apiResponse.getInt("status") != 200) {
                            Toast.makeText(UserRegister.this, apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONObject data = (JSONObject) apiResponse.get("data");

                        // Toast.makeText(UserRegister.this, apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserRegister.this, DetermineUser.class);
                
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




