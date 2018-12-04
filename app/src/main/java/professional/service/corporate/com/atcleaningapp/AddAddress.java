package professional.service.corporate.com.atcleaningapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddAddress  extends Fragment{
    View view;
    EditText building_no;
    EditText apartment_name;
    EditText street_name1;
    EditText street_name2;
    EditText landmark;
    EditText city_name;
    EditText zip_code;
    TextView save_address;
    private ProgressBar mProgressBar;
    int user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.create_address, container, false);
        Bundle b = getArguments();
        user_id = b.getInt("user_id");
        building_no = (EditText)view.findViewById(R.id.building_no);
        apartment_name = (EditText)view.findViewById(R.id.apartment_name);
        street_name1 = (EditText)view.findViewById(R.id.street_name1);
        street_name2  = (EditText)view.findViewById(R.id.street_name2);
        landmark = (EditText)view.findViewById(R.id.landmark);
        city_name = (EditText)view.findViewById(R.id.city_name);
        zip_code = (EditText)view.findViewById(R.id.zip_code);
        save_address = (TextView)view.findViewById(R.id.save_address);
        save_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAddress();
            }
        });
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        return view;
    }

    private void saveAddress() {
        final String building = building_no.getText().toString().trim();
        final String apartment = apartment_name.getText().toString().trim();
        final String street1 = street_name1.getText().toString().trim();
        final String street2 = street_name2.getText().toString().trim();
        final String landmark1 = landmark.getText().toString().trim();
        final String city = city_name.getText().toString().trim();
        final String zip = zip_code.getText().toString().trim();


//        if (!(building_no.getText().toString().trim().equalsIgnoreCase("")) || (apartment_name.getText().toString().trim().equalsIgnoreCase("")) || (street_name1.getText().toString().trim().equalsIgnoreCase("")) || (street_name2.getText().toString().trim().equalsIgnoreCase("")) || (landmark.getText().toString().trim().equalsIgnoreCase("")) || (city_name.getText().toString().trim().equalsIgnoreCase("")) || (zip_code.getText().toString().trim().equalsIgnoreCase(""))) {
//
//            if (building_no.getText().toString().trim().equalsIgnoreCase(""))
//                building_no.setError("Please Enter A Valid Address");
//            if (apartment_name.getText().toString().trim().equalsIgnoreCase(""))
//                apartment_name.setError("Please Enter A Valid Address");
//            if (street_name1.getText().toString().trim().equalsIgnoreCase(""))
//                street_name1.setError("Please Enter A Valid Address");
//            if (street_name2.getText().toString().trim().equalsIgnoreCase(""))
//                street_name2.setError("Please Enter A Valid Address");
//            if (landmark.getText().toString().trim().equalsIgnoreCase(""))
//                landmark.setError("Please Enter A Valid Address");
//            if (city_name.getText().toString().trim().equalsIgnoreCase(""))
//                city_name.setError("Please Enter A Valid Address");
//            if (zip_code.getText().toString().trim().equalsIgnoreCase(""))
//                zip_code.setError("Please Enter A Valid Address");
//            return;
//        } else {
            building_no.setError(null);
            apartment_name.setError(null);
            street_name1.setError(null);
            street_name2.setError(null);
            landmark.setError(null);
            city_name.setError(null);
            zip_code.setError(null);


            class RegisterUser extends AsyncTask<Void, Void, String> {

                private ProgressBar progressBar;
                String apiResponseMessage;

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());


                    HashMap<String, String> params = new HashMap<>();
                    int current_user = user_id;
                    params.put("building", building);
                    params.put("apartment", apartment);
                    params.put("street1", street1);
                    params.put("state", street2);
                    params.put("landmark", landmark1);
                    params.put("city", city);
                    params.put("zip", zip);
                    params.put("user_id", String.valueOf(current_user));


                    try {
                        JSONObject response = requestHandler.sendPostRequest(URLs.URL_ADD_ADDRESS, params, Request.Method.POST);
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
                    mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                    mProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //hiding the progressbar after completion
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), apiResponseMessage, Toast.LENGTH_LONG).show();
                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        JSONObject apiResponse = (JSONObject) obj.get("response");

                        if (apiResponse.getInt("status") != 200) {
                            Toast.makeText(getActivity(), apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            return;
                        }

                       //JSONObject data = (JSONObject) apiResponse.get("data");

                        // Toast.makeText(UserRegister.this, apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        Fragment fragment = new CreateBidJobPost();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        Bundle b = new Bundle();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
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

