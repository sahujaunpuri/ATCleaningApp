package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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

public class AttachedFilesForProject  extends AppCompatActivity {


    GridView gridView;

    int hirer_id;
    ImageView like;
    ImageView dislike;
    EditText enter_comment;
    ListView comments_list;
    ImageView send_comment;
    private ArrayList<HirerImages> m_parts = new ArrayList<>();
    // private ArrayList<AllCommentsClass> m_parts1 = new ArrayList<>();
    private Runnable viewParts;
    
    private AttachedFilesGridViewAdapter m_adapter;
    ArrayList<LoginDBClass> lac;
    ListView listView;
    Button button;
    String newString;
    int bidder_id;

    int project_id;
    int cont_id;
    int receiver_id;
    ProgressDialog progressDialog;
   View view;
    
    //DataBaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.attached_files);
        Intent i = getIntent();
        project_id = i.getIntExtra("project_id",0);


        gridView = (GridView)findViewById(R.id.gridView);
        registerUser();

    }

    private void registerUser() {
        final String skills = newString;


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
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
        m_adapter = new AttachedFilesGridViewAdapter(AttachedFilesForProject.this,R.layout.attached_files, m_parts);
        m_adapter.notifyDataSetChanged();

        gridView.setAdapter(m_adapter);
        Log.e("ERROR", "In displayData call");
        Log.i("Info", "Inside displaydata");

    }




}









