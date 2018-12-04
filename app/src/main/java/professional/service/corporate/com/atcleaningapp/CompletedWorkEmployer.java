package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CompletedWorkEmployer extends Fragment implements AprroveAndRateListener, MilestonePaymentListener {
    private ArrayList<AllAvailableJobsClass> m_parts = new ArrayList<AllAvailableJobsClass>();
    private Runnable viewParts;
    private CompletedWorkAdapterEmployer m_adapter;
    ListView listView;
    Button button;
    String newString;
    int current_hirer_id;
    SessionManager sm;
    ArrayList<LoginDBClass> lac;
    int hirer_id;
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

        view = inflater.inflate(R.layout.completed_work_list, container, false);
        Bundle extras = getActivity().getIntent().getExtras();
        create_post = (ImageView)view.findViewById(R.id.create_icon);

        listView = (ListView)view.findViewById(R.id.list_all_jobs);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if (extras == null) {
            newString = null;
        } else {
            newString = extras.getString("skill_passed");
        }
        // current_hirer_id = UserSession.getUserSession(getIntent()).getUserId();
        sm = new SessionManager(getActivity());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            hirer_id = Integer.parseInt(f.getUser_id());
        }
        current_hirer_id = hirer_id;

        dialog = new ProgressDialog(getActivity());
        getProjects();
        Thread thread = new Thread(null, viewParts, "MagentoBackground");
        thread.start();
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
                int user = current_hirer_id;
                params.put("user_id", String.valueOf(user));


                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_JOBS_BY_ME_COMPLETED_BY_EMPLOYER, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for(int i = 0; i < projectData.length(); i++){
                        JSONObject project = (JSONObject) projectData.get(i);

                        AllAvailableJobsClass data=new AllAvailableJobsClass();
                        data.slNo = i + 1;
                        data.id = project.getInt("id");
                        data.assignee_id = project.getInt("assignee_id");
                        data.project_name = project.getString("title");
                        data.post_date = project.getString("assigned_date");
                        data.state = project.getString("state");
                        data.completion = project.getString("completion");

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

        m_adapter = new CompletedWorkAdapterEmployer(m_parts,AppController.getAppContext(), this, this);
        listView.setAdapter(m_adapter);
        m_adapter.notifyDataSetChanged();



    }

    @Override
    public void approveCompletionRequest(int project_id, int assignee_id) {

        Fragment fragment = new MilestonesCreation();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle b = new Bundle();
        b.putInt("project_id", project_id);
        b.putInt("assignee_id", assignee_id);
        fragment.setArguments(b);
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    @Override
    public void rateContractor(int project_id, int assignee_id) {
        Fragment fragment = new RateContractor();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle b = new Bundle();
        b.putInt("project_id", project_id);
        b.putInt("assignee_id", assignee_id);
        fragment.setArguments(b);
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

    }

    @Override
    public void createAndPayMilestones(int project_id, int assignee_id) {

    }
}

