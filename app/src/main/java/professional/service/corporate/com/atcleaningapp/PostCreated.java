package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PostCreated  extends Fragment {
    private ArrayList<PostCreatedClass> m_parts = new ArrayList<PostCreatedClass>();
    private Runnable viewParts;

    Button button;
    String newString;
    Button publish_a_project;
    TextView textSuccess;
    SessionManager sm;
    ArrayList<LoginDBClass> lac;
    int hirer_id;
    int cont_id;
    TextView description;
    TextView attach;
    //DataBaseHelper dbh;
    TextView subject_line;
    TextView description_line;
    TextView size_of_house;
    TextView bathroom_nos;
    ProgressDialog dialog;
    TextView soil_level_input;
    TextView post_a_project;
    int project_id;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.post_created, container, false);

        Bundle extras = getArguments();
        project_id = extras.getInt("project_id");
        attach = (TextView)view.findViewById(R.id.attach);
        subject_line = (TextView)view.findViewById(R.id.subject_line);
        description_line = (TextView)view.findViewById(R.id.description_line);
        size_of_house = (TextView)view.findViewById(R.id.size_of_house);
        bathroom_nos = (TextView)view.findViewById(R.id.bathroom_nos);
        soil_level_input = (TextView)view.findViewById(R.id.soil_level_input);
        post_a_project = (TextView)view.findViewById(R.id.post_a_project);
        dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        sm = new SessionManager(getActivity());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            cont_id = Integer.parseInt(f.getUser_id());
        }
        hirer_id = cont_id;
        if (extras == null) {
            newString = null;
        } else {
            newString = extras.getString("skill_passed");
        }


        getProject();
        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AttachedFilesForProject.class);
                i.putExtra("project_id", project_id);
                startActivity(i);
            }
        });
        post_a_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_a_job();
            }
        });
        // here we call the thread we just defined - it is sent to the hand`ler below.
        Thread thread = new Thread(null, viewParts, "MagentoBackground");
        thread.start();

    return view;

    }
    private void getProject() {
        final String skills = newString;


        class CreateProject extends AsyncTask<Void, Void, PostCreatedClass> {



            @Override
            protected PostCreatedClass doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(getActivity());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                int current_project= project_id;
                params.put("project_id", String.valueOf(current_project));

                PostCreatedClass data = null;
                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_MY_CREATED_PROJECT, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for(int i = 0; i < projectData.length(); i++){
                        JSONObject project = (JSONObject) projectData.get(i);

                        data=new PostCreatedClass();
                        data.slNo = i + 1;
                        data.projectId = project.getInt("id");
                        data.projectTitle = project.getString("title");
                        data.description= project.getString("description");
                        data.size_of_house = project.getString("size_of_house");
                        data.bathroom = project.getString("bathrooms");
                        data.soil_level = project.getString("soil_level");

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
                dialog.setMessage("Please Wait! Loading Your Post!");
                dialog.show();
            }

            @Override
            protected void onPostExecute(PostCreatedClass s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                subject_line.setText(s.projectTitle);
                description_line.setText(s.description);
                size_of_house.setText(s.size_of_house);
                bathroom_nos.setText(s.bathroom);
                soil_level_input.setText(s.soil_level);
                if(dialog.isShowing()){
                    dialog.cancel();
                }


            }
        }
        //executing the async task
        CreateProject ru = new CreateProject();
        ru.execute();
    }

    private void post_a_job() {





            class PostAProjectManager extends AsyncTask<Void, Void, String> {



                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                    HashMap<String, String> params = new HashMap<>();
                    int current_project = project_id;
                    params.put("project_id", String.valueOf(current_project));

                    // params.put("checkbox", selected_chk_box);
                    try {
                        JSONObject response = requestHandler.sendPostRequest(URLs.URL_CREATE_POST_UPDATE, params, Request.Method.POST);


                        return response.toString();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    dialog.setMessage("Please Wait! Publishing Your Post!");
                    dialog.show();
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        JSONObject apiResponse = (JSONObject) obj.get("response");

                        if (apiResponse.getInt("status") != 200) {
                            Toast.makeText(getActivity(), apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getActivity(), apiResponse.getString("message"), Toast.LENGTH_SHORT).show();


                        if(dialog.isShowing()){
                            dialog.cancel();
                        }
                        Intent i = new Intent(getActivity(), SuccessActivty2.class);
                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            //executing the async task
            PostAProjectManager ru = new PostAProjectManager();
            ru.execute();
        }

    }











