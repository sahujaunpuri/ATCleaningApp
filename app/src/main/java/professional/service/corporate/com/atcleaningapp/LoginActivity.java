package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private ProgressDialog mProgressDialog;
    EditText editTextEmail, editTextPassword;
    TextView login;
    String token;
    private SharedPreferences sharedPref;
    private String sharedPrefLoginKey;
    private String sharedPrefKey3;
    private SharedPreferences sharedPref3;
    private SharedPreferences sharedPref2;
    SessionManager sm;
    Long mRowId;
    TextView frgt;
    private String sharedPrefKey;
    private String gender;
    private TextView buttonRegister;

    private SharedPreferences sharedPref4;
    private SharedPreferences sharedPref5;
    private SharedPreferences sharedPref6;
    String user_rec;

    private ProgressBar mProgressBar;
    private RelativeLayout mLoginLayout;
    TextView reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        sm = new SessionManager(getApplicationContext());
        login = (TextView)findViewById(R.id.login_button);
        reset = (TextView)findViewById(R.id.reset_pass);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        // A loading indicator
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        sharedPref = this.getSharedpreferences();
        sharedPref3 = this.getSharedpreferences3();
        sharedPref2 = this.getSharedpreferences2();
        sharedPref4 = this.getSharedpreferences4();
        user_rec = (sharedPref2.getString(sharedPrefKey, ""));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        token = SharedPrefManager1.getInstance(this).getDeviceToken();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
       reset.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(LoginActivity.this, ForgotPass.class);
               startActivity(i);
               overridePendingTransition(R.anim.enter, R.anim.exit);
           }
       });

    }





    private void userLogin() {
        //first getting the values
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        //if everything is fine
        if (!editTextEmail.getText().toString().matches(emailPattern) ||editTextEmail.getText().toString().trim().equalsIgnoreCase("") || (editTextPassword.getText().toString().trim().equalsIgnoreCase(""))) {
            if (!editTextEmail.getText().toString().matches(emailPattern) || editTextEmail.getText().toString().trim().equalsIgnoreCase(""))
                editTextEmail.setError("Please Enter A Valid Email!!!");

            if (editTextPassword.getText().toString().trim().equalsIgnoreCase(""))
                editTextPassword.setError("Please Enter Password!!!");


            return;
        } else {
            class UserLogin extends AsyncTask<Void, Void, String> {

                ProgressBar progressBar;
                String apiResponseMessage = "";


                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());
                    JSONObject response = null;
                    //creating request parameters
                    String token_generated = token;
                    HashMap<String, String> params = new HashMap<>();
                    Map<String, String> headers = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
//                    headers.put("Accept", "application/json");
//                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    params.put("token", token_generated);

                    try {
                        response = requestHandler.sendPostRequest(URLs.URL_LOGIN, params, Request.Method.POST);
                        return response.toString();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    return response.toString();
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //displaying the progress bar while user registers on the server
                    mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
                    mProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    mProgressBar.setVisibility(View.VISIBLE);

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        JSONObject apiResponse = (JSONObject) obj.get("response");

                        if (apiResponse.getInt("status") != 200) {
                            Toast.makeText(LoginActivity.this, apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONObject data = (JSONObject) apiResponse.get("data");

                        // Toast.makeText(LoginActivity.this, apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, DetermineUser1.class);


                        int listatus = 1;
                        sm.addUserLoginDetails(getApplicationContext(), String.valueOf(data.getInt("user_id")), String.valueOf(listatus));
                        //  final int contractor_id = UserSession.getUserSession(getIntent()).getUserId();

                        sharedPrefKey3 = getResources().getString(R.string.user_id);

                        SharedPreferences.Editor mEditor3 = sharedPref3.edit();

                        mEditor3.putInt(getString(R.string.user_id), data.getInt("user_id"));
                        mEditor3.commit();
                        SharedPreferences.Editor mEditor = sharedPref2.edit();
                        mEditor.putString(getString(R.string.user_type_frag_preference_file_key), data.getString("user_type"));
                        mEditor.commit();
                        String ut = sharedPref2.getString(getString(R.string.user_type_frag_preference_file_key), "");
                        Log.i("Sourav, ut", "UT" + ut);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mProgressBar.setVisibility(View.GONE);
                }

            }

//executing the async task
            UserLogin ru = new UserLogin();
            ru.execute();

        }
        }

    private SharedPreferences getSharedpreferences() {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.login_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;
    }
    private SharedPreferences getSharedpreferences3()
    {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.user_id), Context.MODE_PRIVATE);

        return sharedPref;
    }
    private SharedPreferences getSharedpreferences2()
    {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.user_type_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;
    }

    private SharedPreferences getSharedpreferences4() {

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.username_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;
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



}
