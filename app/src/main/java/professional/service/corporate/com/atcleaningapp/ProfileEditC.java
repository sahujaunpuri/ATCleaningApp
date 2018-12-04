package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileEditC extends Fragment {
        EditText profile_name;
        TextView address;
        TextView availability;
        TextView token_serving;
        View view;
        int doctor_id;
        TextView last_token_served;
        EditText email;
        EditText mobile;
        SessionManager sm;
        ArrayList<LoginDBClass> lac;
        int user_id;
        int current_user_id;
        TimePicker timePicker;
        TextView edit;
        ProgressDialog pDialog;
        ImageView location;
private SharedPreferences sharedPref;

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.my_profile_ed, container, false);
        profile_name = (EditText) view.findViewById(R.id.profile_name);
        address = (TextView) view.findViewById(R.id.address);

        mobile = (EditText) view.findViewById(R.id.mobile);

        email = (EditText) view.findViewById(R.id.email);
        edit = (TextView)view.findViewById(R.id.edit);
        location = (ImageView)view.findViewById(R.id.edit_loc);
        pDialog = new ProgressDialog(getActivity());
        // Time Picker
        sharedPref = getSharedpreferences();
        sm = new SessionManager(getActivity());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
        user_id = Integer.parseInt(f.getUser_id());
        }
        current_user_id = user_id;
        getUserDeatils();

        edit.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        updateAvailableTime();
//                Fragment someFragment = new ProfileEdit();
//
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                Bundle bundle = new Bundle();
//                bundle.putInt("doctor_id", doctor_id);
//                someFragment.setArguments(bundle);
//                transaction.replace(R.id.content_frame, someFragment); // give your fragment container id in first parameter
//
//                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                transaction.commit();

        }
        });
        location.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {


        Intent i = new Intent(getActivity(), ShareCurrentLocation.class);
        String userType =  sharedPref.getString(getString(R.string.user_type_frag_preference_file_key),"");
        i.putExtra("type", userType);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
        }


        });
        return view;
        }

private SharedPreferences getSharedpreferences() {
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
        getString(R.string.user_type_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;
        }

private void getUserDeatils() {



class RegisterUser extends AsyncTask<Void, Void, User> {


    private ProgressBar progressBar;

    @Override
    protected User doInBackground(Void... voids) {
        //creating request handler object
        RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());
        int current_user = current_user_id;
        //creating request parameters
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(current_user));

        User data = null;
        try {
            JSONObject response = requestHandler.sendPostRequest(URLs.URL_MY_PROFILE_C, params, Request.Method.POST);
            JSONObject mainResponse = (JSONObject) response.get("response");
            //  int apiResponseStatus = mainResponse.getInt("status");
            JSONArray projectData = (JSONArray) mainResponse.get("data");

            for(int i = 0; i < projectData.length(); i++){
                JSONObject project = (JSONObject) projectData.get(i);

                data =new User();
                data.slNo = i + 1;
                data.user_name = project.getString("user_name");
                //data.last_served = project.getInt("last_served");
                data.email = project.getString("user_email");
                data.mobile = project.getString("user_phone");
                data.location = project.getString("location");
                //data.address = project.getString("address");

            }

            return data;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //displaying the progress bar while user registers on the server
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(User s) {
        super.onPostExecute(s);
        //hiding the progressbar after completion
        progressBar.setVisibility(View.GONE);
        if(s.getUser_name().equalsIgnoreCase(null)||s.getUser_name().equalsIgnoreCase(""))
        {
            Toast.makeText(getActivity(), "Sorry! Server is down", Toast.LENGTH_SHORT);
        }
        else {
            profile_name.setText(s.user_name);
        }
        if(s.getEmail().equalsIgnoreCase(null)||s.getEmail().equalsIgnoreCase(""))
        {
            Toast.makeText(getActivity(), "Sorry! Server is down", Toast.LENGTH_SHORT);
        }
        else {
            email.setText(s.email);
        }
        if(s.getMobile().equalsIgnoreCase(null)||s.getMobile().equalsIgnoreCase(""))
        {
            Toast.makeText(getActivity(), "Sorry! Server is down", Toast.LENGTH_SHORT);
        }
        else {
            mobile.setText(s.mobile);
        }
        if(s.getLocation().equalsIgnoreCase(null)||s.getLocation().equalsIgnoreCase(""))
        {
            Toast.makeText(getActivity(), "Sorry! Server is down", Toast.LENGTH_SHORT);
        }
        else {
            address.setText(s.location);
        }
    }
}
    //executing the async task
    RegisterUser ru = new RegisterUser();
        ru.execute();
                }
private void updateAvailableTime() {
final String name = profile_name.getText().toString();
final String email1 = email.getText().toString();
final String mobile1 = mobile.getText().toString();





class RegisterUser extends AsyncTask<Void, Void, String> {

    private ProgressBar progressBar;
    String apiResponseMessage;

    @Override
    protected String doInBackground(Void... voids) {
        //creating request handler object
        RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());


        //creating request parameters
        int current_user = current_user_id;
        HashMap<String, String> params = new HashMap<>();

        params.put("name", name);
        params.put("email", email1);
        params.put("phone", mobile1);


        params.put("doctor_id", String.valueOf(current_user));

        try {
            JSONObject response = requestHandler.sendPostRequest(URLs.URL_UPDATE_PROFILE, params, Request.Method.POST);
            return response.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog.setMessage("Please Wait! Saving Edits...");
        pDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //hiding the progressbar after completion
         if(pDialog.isShowing()){
             pDialog.cancel();
         }
        Toast.makeText(getActivity(), apiResponseMessage, Toast.LENGTH_LONG).show();
        try {
            //converting response to json object
            JSONObject obj = new JSONObject(s);
            JSONObject apiResponse = (JSONObject) obj.get("response");

            if (apiResponse.getInt("status") != 200) {
                Toast.makeText(getActivity(), apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                return;
            }

            //  JSONObject data = (JSONObject) apiResponse.get("data");

            // Toast.makeText(getActivity(), apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), SuccessActivty1.class);

            int listatus = 1;



            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit) ;
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

