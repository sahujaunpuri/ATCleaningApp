package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyBookingsForToday extends Fragment  {
    private ArrayList<AllAvailableJobsClass> m_parts = new ArrayList<AllAvailableJobsClass>();
    private Runnable viewParts;
    private AllAvailableJobsAdapter m_adapter;
    ListView listView;
    Button button;
    String newString;
    int bidder_id;
    SessionManager sm;
    ArrayList<LoginDBClass> lac;
    int cont_id;
    ProgressDialog dialog;
    View view;
    ImageView create_post;
    ImageView delete_post;
    ImageView edit_post;
    TextView create;
    TextView edit;
    boolean firstTouch = false;
    long time;
    TextView delete;
    SharedPreferences sharedPrefListItem;
    SharedPreferences.Editor mEditor4;
    //DataBaseHelper dbh;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.post_a_job_for_bid, container, false);
        Bundle extras = getActivity().getIntent().getExtras();
        create_post = (ImageView)view.findViewById(R.id.create_icon);

        listView = (ListView)view.findViewById(R.id.list_all_jobs);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if (extras == null) {
            newString = null;
        } else {
            newString = extras.getString("skill_passed");
        }
        // bidder_id = UserSession.getUserSession(getIntent()).getUserId();
        sm = new SessionManager(getActivity());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            cont_id = Integer.parseInt(f.getUser_id());
        }
        bidder_id = cont_id;

        dialog = new ProgressDialog(getActivity());
        getProjects();
        Thread thread = new Thread(null, viewParts, "MagentoBackground");
        thread.start();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                AllAvailableJobsClass project = (AllAvailableJobsClass) parent.getItemAtPosition(position);
                mEditor4 = sharedPrefListItem.edit();
                if((sharedPrefListItem.getInt(getResources().getString(R.string.list_item_id),0)>0)){
                    mEditor4.clear();
                }

                mEditor4.putInt(AppController.getAppContext().getString(R.string.list_item_id), position);
                mEditor4.commit();
                listView.setSelected(true);
                int even = 0;
                int odd =0;
                if (position == 0) {
                    delete_post.animate().rotation(360).start();
                    edit_post.animate().rotation(360).start();
                }
                if(position>0) {
                    if (position % 2 == 0) {
                        even = position;
                    }
                    if (position % 2 != 0) {
                        odd = position;
                    }

                    if (position == even) {
                        delete_post.animate().rotation(360).start();
                        edit_post.animate().rotation(360).start();
                    }

                    if (position == odd) {
                        delete_post.animate().rotation(-360).start();
                        edit_post.animate().rotation(-360).start();
                    }
                }
            }
        });

        return view;
    }



    private void getProjects() {
        final String skills = newString;


        class RegisterUser extends AsyncTask<Void, Void, String> {


            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                int user = bidder_id;
                params.put("user_id", String.valueOf(user));


                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_JOBS_BY_ME, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for(int i = 0; i < projectData.length(); i++){
                        JSONObject project = (JSONObject) projectData.get(i);

                        AllAvailableJobsClass data=new AllAvailableJobsClass();
                        data.slNo = i + 1;
                        data.id = project.getInt("id");
                        data.project_name = project.getString("title");
                        data.post_date = project.getString("post_date");
                        data.state = project.getString("state");

                        m_parts.add(0,data);
                    }

                    return mainResponse.getString("status");
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
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);
                displayData();

            }
        }
        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }


    private void displayData() {

        m_adapter = new AllAvailableJobsAdapter(m_parts,AppController.getAppContext());
        listView.setAdapter(m_adapter);
        m_adapter.notifyDataSetChanged();



    }



}

