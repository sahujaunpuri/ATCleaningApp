package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class AddressList extends AppCompatActivity {
    View view;
    ListView listView;
    private ArrayList<AddressClass> dataModels;
    private  AddressListAdapter adapter;
    private ArrayList<LoginDBClass> lac;
    private int user_id;
    private int employer_id;
    private TextView add_new_address;
    private SessionManager sm;
    TextView save_address;
    private SharedPreferences sharedPrefAddress;
    int project_id;
    Intent i;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_list);
        listView=(ListView)findViewById(R.id.address_lv);
        dataModels = new ArrayList<AddressClass>();
        add_new_address = (TextView)findViewById(R.id.add_new_address);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        sm = new SessionManager(getApplicationContext());
        i = getIntent();
        project_id = i.getIntExtra("project_id",0);
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            user_id = Integer.parseInt(f.getUser_id());
        }
        employer_id = user_id;
        dialog = new ProgressDialog(AddressList.this);
        dialog.setCanceledOnTouchOutside(false);
        sharedPrefAddress = getSharedPreferencesAddress();
        save_address = (TextView)findViewById(R.id.save_address);
        getAddressList();

        adapter= new AddressListAdapter(dataModels,AddressList.this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                AddressClass AddressClass= (professional.service.corporate.com.atcleaningapp.AddressClass) parent.getItemAtPosition(position);
                int address_id_to_select = AddressClass.getId();
                SharedPreferences.Editor mEditor3 = sharedPrefAddress.edit();
                if(sharedPrefAddress.getInt(getResources().getString(R.string.selected_address_id), 0)>0){
                    mEditor3.clear();
                }

                mEditor3.putInt(getString(R.string.selected_address_id), address_id_to_select);
                mEditor3.commit();
                Toast.makeText(AddressList.this, "Selected Address " + AddressClass.building + String.valueOf(position), Toast.LENGTH_LONG).show();

                adapter.notifyDataSetChanged();


            }
        });
        add_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddAddressByGooglePlace();
                FragmentTransaction ft = AddressList.this.getSupportFragmentManager().beginTransaction();
                Bundle b = new Bundle();
                //b.putSerializable("projectObj", (project));
                int current_employer = employer_id;
                b.putInt("user_id", current_employer);
                fragment.setArguments(b);
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        });
        save_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              update_address_for_job();
            }
        });

        //return view;
    }

    private SharedPreferences getSharedPreferencesAddress() {
        Context context = AppController.getAppContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.selected_address_id), Context.MODE_PRIVATE);

        return sharedPref;
    }

    private void displayData() {

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
    private void getAddressList() {



        class RegisterUser extends AsyncTask<Void, Void, String> {



            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());
                int current_user = employer_id;

                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(current_user));


                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_GET_ADDRESS_LIST_FOR_USER, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for(int i = 0; i < projectData.length(); i++){
                        JSONObject project = (JSONObject) projectData.get(i);

                        AddressClass data=new AddressClass();
                        data.slNo = i + 1;
                        data.id = project.getInt("id");
                        data.building = project.getString("building_no");
                        data.apartment = project.getString("apartment_name");
                        data.city = project.getString("city");
                        data.state = project.getString("state");
                        data.zip = project.getString("zip");
                        data.landmark = project.getString("landmark");
                        data.street1 = project.getString("street1");
                        dataModels.add(0,data);
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
              dialog.setMessage("Please Wait! Working On Background!");
              dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(dialog.isShowing())
                {
                    dialog.cancel();
                }
                displayData();

            }
        }
        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }
    private void update_address_for_job() {






            class PostAProjectManager extends AsyncTask<Void, Void, String> {

                private ProgressBar progressBar;

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                    HashMap<String, String> params = new HashMap<>();
                    int id_to_be_mapped = sharedPrefAddress.getInt(getString(R.string.selected_address_id), 0);
                    int current_project = project_id;
                    params.put("address_id", String.valueOf(id_to_be_mapped));
                    params.put("project_id", String.valueOf(current_project));

                    try {
                        JSONObject response = requestHandler.sendPostRequest(URLs.URL_UPDATE_ADDRESS_FOR_PROJECT, params, Request.Method.POST);


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
                    progressBar = (ProgressBar) findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.VISIBLE);
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
                            Toast.makeText(AddressList.this, apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(AddressList.this, apiResponse.getString("message"), Toast.LENGTH_SHORT).show();

                        Fragment fragment = new PostCreated();
                        FragmentTransaction ft = AddressList.this.getSupportFragmentManager().beginTransaction();
                        Bundle b = new Bundle();
                        //b.putSerializable("projectObj", (project));
                        int current_project = project_id;
                        int current_employer = employer_id;
                        b.putInt("user_id", current_employer);
                        b.putInt("project_id", current_project);
                        fragment.setArguments(b);
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();

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
