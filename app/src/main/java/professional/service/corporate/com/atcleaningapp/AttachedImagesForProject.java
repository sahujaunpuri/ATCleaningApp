package professional.service.corporate.com.atcleaningapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AttachedImagesForProject extends Fragment {
    View view;
    int project_id;
    private GridView gridView;
    private AttachedFilesGridViewAdapter m_adapter;
    private ArrayList<HirerImages> m_parts = new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.attached_files, container, false);
        Bundle b = getArguments();
        project_id = b.getInt("project_id",0);

        gridView = (GridView)view.findViewById(R.id.gridView);
        registerUser();

        return view;
    }

    private void registerUser() {



        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());
                int project_id1 = project_id;
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();

                params.put("project_id", String.valueOf((project_id1)));


                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.GRID_IMAGES_ATTACHED, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for(int i = 0; i < projectData.length(); i++){
                        JSONObject project = (JSONObject) projectData.get(i);

                        HirerImages data=new HirerImages();

                        // data.user_id = project.getInt("hirer_id");
                        data.url = project.getString("url");


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
        m_adapter = new AttachedFilesGridViewAdapter(getActivity(),R.layout.grid_item_layout, m_parts);
        m_adapter.notifyDataSetChanged();

        gridView.setAdapter(m_adapter);
        Log.e("ERROR", "In displayData call");
        Log.i("Info", "Inside displaydata");

    }




}
