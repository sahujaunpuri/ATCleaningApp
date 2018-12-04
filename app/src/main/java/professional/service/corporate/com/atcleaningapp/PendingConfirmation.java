package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class PendingConfirmation extends Fragment{
    View view;
    TextView title_of_project;
    TextView bid_amt;
    int project_id;
    ProgressDialog dialog;
    TextView send_reuest;
    private SessionManager sm;
    private ArrayList<LoginDBClass> lac;
    int cont_id;
    int bidder_id;

    private SharedPreferences sharedPref3;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.completion_request_for_employer, container, false);
        title_of_project = (TextView)view.findViewById(R.id.title_of_project);
        bid_amt = (TextView)view.findViewById(R.id.total_amt);
        send_reuest = (TextView)view.findViewById(R.id.send_request);
        sm = new SessionManager(getActivity());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            cont_id = Integer.parseInt(f.getUser_id());
        }
        bidder_id = cont_id;
        Bundle b = getArguments();
        project_id = b.getInt("project_id");
        dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        getProjectDetails();

        sharedPref3 = getSharedpreferences2();


        send_reuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              int project_id = sharedPref3.getInt(getResources().getString(R.string.project_id_for_project), 0);
              sendRequestToEmployer(project_id);
            }
        });
        return view;
    }

    private void sendRequestToEmployer(final int project_id) {
        class RegisterUser extends AsyncTask<Void, Void, String> {


            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                int current_project = project_id;

                params.put("project_id", String.valueOf(current_project));

                String data = null;

                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_SEND_PROJECT_COMPLETION_REQUEST_TO_EMPLOYER, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    int apiResponseStatus = mainResponse.getInt("status");
                    String apiResponseMessage = mainResponse.getString("message");
                    Log.i("Sourav, web response", response.toString());

                    return apiResponseMessage;
                }catch (JSONException ex){
                    ex.printStackTrace();
                }

                return data;
            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Sending A Request To Employer!");
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                dialog.hide();



            }
        }
        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }


    private void getProjectDetails() {



        class RegisterUser extends AsyncTask<Void, Void, AllAvailableJobsClass> {


            private ProgressBar progressBar;

            @Override
            protected AllAvailableJobsClass doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                int current_project = project_id;
                int current_bidder = bidder_id;
                params.put("project_id", String.valueOf(current_project));
                params.put("bidder_id", String.valueOf(current_bidder));
                AllAvailableJobsClass data = null;

                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_RETRIEVE_ONGOING_PROJECT_DETAILS, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for(int i = 0; i < projectData.length(); i++){
                        JSONObject project = (JSONObject) projectData.get(i);

                        data=new AllAvailableJobsClass();
                        data.slNo = i + 1;

                        data.project_name = project.getString("title");
                        data.hirer_id = project.getInt("hirer_id");
                        //data.assignee_id = project.getInt("hirer_id");
                        data.project_id = project.getInt("id");
                        data.bid = project.getString("bidding_price");

                    }

                    return data;
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                return data;
            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Please Wait While retrieving Details!");
                dialog.show();
            }

            @Override
            protected void onPostExecute(AllAvailableJobsClass s) {
                super.onPostExecute(s);
                title_of_project.setText(s.getProject_name());
                bid_amt.setText(s.getBid());

                SharedPreferences.Editor mEditor2 = sharedPref3.edit();
                mEditor2.putInt(getString(R.string.project_id_for_project), s.getProject_id());
                mEditor2.commit();

                dialog.hide();
                Fragment fragment = new SuccessActivityForCompletingProjects();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();


            }
        }
        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }


    private SharedPreferences getSharedpreferences2() {
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.project_id_for_project), Context.MODE_PRIVATE);

        return sharedPref;
    }

}
