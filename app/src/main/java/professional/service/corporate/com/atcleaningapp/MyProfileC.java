package professional.service.corporate.com.atcleaningapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyProfileC   extends android.support.v4.app.Fragment {
    TextView profile_name;
    TextView address;
    TextView availability;
    TextView token_serving;
    View view;
    int doctor_id;
    TextView last_token_served;
    TextView email;
    TextView mobile;
    SessionManager sm;
    ArrayList<LoginDBClass> lac;
    int user_id;
    int current_user_id;
    TimePicker timePicker;
    TextView edit;
    ImageView update_location;
    CircleImageView circleImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.my_profile, container, false);
        profile_name = (TextView)view.findViewById(R.id.profile_holder);
        address = (TextView)view.findViewById(R.id.address);

        mobile = (TextView)view.findViewById(R.id.mobile);

        email = (TextView)view.findViewById(R.id.email);
        edit = (TextView)view.findViewById(R.id.edit);
        update_location = (ImageView)view.findViewById(R.id.update);
        circleImageView = (CircleImageView)view.findViewById(R.id.profile_img);
        // Time Picker

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
                Fragment someFragment = new ProfileEditC();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putInt("doctor_id", doctor_id);
                someFragment.setArguments(bundle);
                transaction.replace(R.id.content_frame, someFragment); // give your fragment container id in first parameter

                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });

//        update_location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String type = "Doctor";
//                Intent i = new Intent(getActivity(), CurrentLocation.class);
//                i.putExtra("type",type );
//                startActivity(i);
//            }
//        });
        return view;
    }
    private void getUserDeatils() {



        class DoctorHandler extends AsyncTask<Void, Void, User> {


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
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_MY_PROFILE, params, Request.Method.POST);
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
                        data.location = project.getString("apartment_name");
                        data.url = project.getString("url");
                        // data.gender = project.getString("gender");
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
                if(s.getUrl()!= "null") {
                    Picasso.with(AppController.getAppContext()).load(s.url).fit().into(circleImageView);
                }


            }
        }
        //executing the async task
        DoctorHandler ru = new DoctorHandler();
        ru.execute();
    }

}

