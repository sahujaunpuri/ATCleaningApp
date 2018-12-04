package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
public class PayPalActivity2 extends AppCompatActivity {
    EditText recepient;
    Button release;
    String payee_id;
    TextView recipient_email_tv;
    ImageView editReceipt;
    Button release1;
    String milestone_amt;
    String description;
    private SharedPreferences sharedPref;
    private String sharedPrefEmailKey;
    SessionManager sm;
    ArrayList<LoginDBClass> lac;
    int cont_id;
    int hirer_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal2);
        recepient = (EditText) findViewById(R.id.recepient);
        release = (Button) findViewById(R.id.button);
        release1 = (Button) findViewById(R.id.button1);
        recipient_email_tv = (TextView) findViewById(R.id.recepient_tv);
        editReceipt = (ImageView)findViewById(R.id.edit_rec);
        sm = new SessionManager(getApplicationContext());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            cont_id = Integer.parseInt(f.getUser_id());
        }
        hirer_id = cont_id;
        Bundle b = getIntent().getExtras();
        payee_id = b.getString("payee", "");
        milestone_amt = b.getString("milestone_amt");
        description = b.getString("description");
        payee(payee_id);
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userReceipt();
            }
        });
        release1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userReceipt_tv();
            }
        });
        editReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editReceipt.setVisibility(View.GONE);
                recepient.setVisibility(View.VISIBLE);
                recipient_email_tv.setVisibility(View.GONE);
            }
        });
    }

    private void userReceipt() {
        //first getting the values
        final String rcp_email = recepient.getText().toString();
        final String payee_id1 = payee_id;
        final String milestone_amt1 = milestone_amt;
        final String payer = String.valueOf(hirer_id);
        final String description1 = "Released Payment";
        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;
            String apiResponseMessage = "";


            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(getApplicationContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("receiver", rcp_email);
                params.put("assignee_id",payee_id1);
                params.put("milestone_amt", (milestone_amt1));
                params.put("sender_id",payer);
                params.put("description",description1);
                try {
                    JSONObject response = requestHandler.sendPostRequest(Config.URL_SEND_PAYOUT, params, Request.Method.POST);
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
                //  progressBar = (ProgressBar) findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Intent i = new Intent(PayPalActivity2.this, SuccessActivity1.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);


            }

        }

//executing the async task
        UserLogin ul = new UserLogin();
        ul.execute();

    }

    private void payee(final String id) {
        //first getting the values

        final String id1 = id;
        //if everything is fine


        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;
            String apiResponseMessage = "";

            @Override
            protected String doInBackground(Void... voids) {


                RequestHandler requestHandler = new RequestHandler(getApplicationContext());
                HashMap<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id1));
                String user_email="";
                try {
                    JSONObject response = requestHandler.sendPostRequest(Config.URL_GET_PAYEE, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for (int i = 0; i < projectData.length(); i++) {
                        JSONObject product = (JSONObject) projectData.get(i);

                        user_email = product.getString("user_email");

                    }

                    return user_email;
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                return user_email;
            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                //  progressBar = (ProgressBar) findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                recipient_email_tv.setText(s);
                Log.i("Sourav, EMAIL ","EMAIL"+s);
//                Intent i = new Intent(PayPalActivity2.this, PaypalState.class);
//                startActivity(i);
                SharedPreferences mSharedPrefs =getSharedpreferences();
                SharedPreferences.Editor mEditor = mSharedPrefs.edit();
                mEditor.putString(getString(R.string.email_frag_preference_file_key), s);
                Log.i("Sourav, decrease", "commited value: " + mSharedPrefs.getString(getString(R.string.email_frag_preference_file_key), ""));
                mEditor.commit();


            }

        }
        //executing the async task
        UserLogin ul = new UserLogin();
        ul.execute();
    }
    private void userReceipt_tv() {

        sharedPref = this.getSharedpreferences();
        sharedPrefEmailKey = getResources().getString(R.string.email_frag_preference_file_key);

        final String rcp_email = (sharedPref.getString(sharedPrefEmailKey, ""));
        final String payee_id1 = payee_id;

        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;
            String apiResponseMessage = "";


            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(getApplicationContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("receiver", rcp_email);
                params.put("assignee_id",payee_id1);

                try {
                    JSONObject response = requestHandler.sendPostRequest(Config.URL_SEND_PAYOUT, params, Request.Method.POST);
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
                //  progressBar = (ProgressBar) findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Intent i = new Intent(PayPalActivity2.this, PaypalState.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);


            }

        }

//executing the async task
        UserLogin ul = new UserLogin();
        ul.execute();

    }
    private SharedPreferences getSharedpreferences()
    {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.email_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;
    }
}