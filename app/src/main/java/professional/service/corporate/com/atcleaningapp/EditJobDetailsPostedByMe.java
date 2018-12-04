package professional.service.corporate.com.atcleaningapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditJobDetailsPostedByMe extends Fragment {
    View view;
    Spinner dropdown;

    TextView attach;


    TextView buttonSave;

    public SQLiteDatabase db;
    EditText feet;
    EditText inches;
    String skills;
    static final String CALENDAR = "calendar";
    ArrayAdapter<String> adapter;
    // ArrayList<String> data;
    boolean status = false;
    int a;
    TextView next;
    String[] items1;
    String[] items2;
    String[] items3;
    String user;
    private RadioGroup radioSexGroup;
    public static final String UPLOAD_URL = "http://sell4masaricom.ipage.com/contractors/php_web_service/public_html/upload.php";
    //storage permission code



    int by_hour = 0;
    String data;
    String input;
    TextView hire_local;
    ImageView location1;

    TextView title;
    TextView title_input;
    TextView description_input;
    Spinner size_of_house_inputted;
    Spinner soil_level_inputted;
    Spinner bathroom_inputted;
    TextView job_status;
    int project_id;
    AllAvailableJobsClass aj;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.employer_edit_view_of_posted_job, container, false);

        job_status = (TextView)view.findViewById(R.id.job_status_des) ;
        title = (TextView) view.findViewById(R.id.title);
        title_input = (TextView) view.findViewById(R.id.title_inputted);
        description_input = (TextView) view.findViewById(R.id.description_inputted);
        attach = (TextView) view.findViewById(R.id.attach);
        buttonSave = (TextView) view.findViewById(R.id.bid_now);
        size_of_house_inputted = (Spinner) view.findViewById(R.id.size_of_house_inputted);
        soil_level_inputted = (Spinner) view.findViewById(R.id.soil_level_inputted);
        bathroom_inputted = (Spinner) view.findViewById(R.id.bathrooms_inputted);
        Bundle b = getArguments();
      //  aj = (AllAvailableJobsClass)b.getSerializableExtra("projectObj");
        project_id = b.getInt("project_id",0);
        getProjects();
        items1 = new String[]{"Under 1500 Sqft","1500 - 1800 Sqft", "1800 - 2200 Sqft ", "2200 - 2500 Sqft", "2500 - 3000 Sqft", "3000 + Sqft"};
        items2 = new String[]{"1 Full Bathroom","1 Full Bathroom, 1 Half Bathroom", "2 Full Bathroom", "2 Full Bathroom, 1 Half Bathroom", "2 Full Bathroom, 2 Half Bathroom", "3 Full Bathroom, 1 Half Bathroom", "3 Full Bathroom, 2 Half Bathroom", "4 Full Bathroom, 1 Half Bathroom", "4 Full Bathroom, 2 Half Bathroom"};
        items3 = new String[]{"Regularly Cleaned","Been 4 or 5 Weeks", "It's bit Rough", "Needs A Good tough Job", "Really Dirty"};


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items1);
        size_of_house_inputted.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items2);
        bathroom_inputted.setAdapter(adapter2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items3);
        soil_level_inputted.setAdapter(adapter3);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

//                Intent i = new Intent(getActivity(), SuccessActivty.class);
//                startActivity(i);
//                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                int current_project = project_id;
                Fragment fragment = new EmployerViewOfContractorResponse();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment, fragment.toString());
                Bundle b = new Bundle();
                b.putInt("project_id", current_project);
                fragment.setArguments(b);
                fragmentTransaction.addToBackStack(fragment.toString());
                fragmentTransaction.commit();


            }
        });
        return view;
    }




    private void getProjects() {


        class RegisterUser extends AsyncTask<Void, Void, AllAvailableJobsClass> {


            private ProgressBar progressBar;

            @Override
            protected AllAvailableJobsClass doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                int current_project = project_id;
                params.put("project_id", String.valueOf(current_project));
                AllAvailableJobsClass data = new AllAvailableJobsClass();

                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_JOB_VIEW_FOR_EMPLOYER, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for (int i = 0; i < projectData.length(); i++) {
                        JSONObject project = (JSONObject) projectData.get(i);


                        data.slNo = i + 1;
                        //data.projectId = project.getInt("id");
                        data.title = project.getString("title");
                        data.description = project.getString("description");
                        data.post_date = project.getString("post_date");
                        data.bathrooms = project.getString("bathrooms");
                        data.size_of_rooms = project.getString("size_of_house");
                        data.soil_level = project.getString("soil_level");
                        data.status = project.getString("state");


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
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(AllAvailableJobsClass s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                title_input.setText(s.title);
                description_input.setText(s.description);
                for (int i = 0; i < items1.length; i++) {
                    if (items1[i].equals(s.bathrooms)) {
                        bathroom_inputted.setSelection(i);
                    }
                }
                for (int i = 0; i < items2.length; i++) {
                    if (items2[i].equals(s.size_of_rooms)) {
                        size_of_house_inputted.setSelection(i);
                    }
                }
                for (int i = 0; i < items3.length; i++) {
                    if (items3[i].equals(s.soil_level)) {
                        soil_level_inputted.setSelection(i);
                    }
                }




                job_status.setText("Job Post Status " + s.status);
            }
        }
            //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
        }



}

