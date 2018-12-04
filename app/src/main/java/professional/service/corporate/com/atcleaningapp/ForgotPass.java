package professional.service.corporate.com.atcleaningapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPass extends AppCompatActivity {
    Button next;
    EditText enterEmail;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotemail);
        next = (Button)findViewById(R.id.btnVerify);
        enterEmail  = (EditText)findViewById(R.id.enter);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userVerify();
            }
        });

    }

    private void userVerify() {
        final String email = enterEmail.getText().toString();


        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;
            String apiResponseMessage="";
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);


                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_SEND_EMAIL, params, Request.Method.POST);
                    return response.toString();
                }catch (Exception ex){
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

                  progressBar.setVisibility(View.GONE);

                //converting response to json object
                JSONObject obj = null;
                try {
                    obj = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject apiResponse = null;
                try {
                    apiResponse = (JSONObject) obj.get("response");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int status = 0;
                try {
                    status = apiResponse.getInt("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status != 200) {
                        //Toast failure
                    try {
                        Toast.makeText(ForgotPass.this, apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                    }
                try {
                    Toast.makeText(ForgotPass.this, apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(ForgotPass.this, LoginActivity.class);
                intent.putExtra("email", email);
                int listatus = 1;


                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);





            }

        }

//executing the async task
        UserLogin ul = new UserLogin();
        ul.execute();

    }
}


