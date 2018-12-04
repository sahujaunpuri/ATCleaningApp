package professional.service.corporate.com.atcleaningapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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

public class CreateANewAddress   extends Fragment {
    View view;
    EditText building_no;
    EditText apartment_name;
    EditText street_name1;
    EditText street_name2;
    EditText landmark;
    EditText city_name;
    EditText zip_code;
    TextView save_address;
    private ProgressBar mProgressBar;
    int user_id;
    ListView listView;
    private ArrayList<AddressClass> dataModels;
    ArrayList<Integer> checkbox_pos = new ArrayList<>();
    TextView select;
    String  checkBox_pos_s;
    String user;
    private AddressListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.create_new_address, container, false);
        Bundle b = getArguments();
        user_id = b.getInt("user_id");
        listView=(ListView)view.findViewById(R.id.address_lv);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        adapter= new AddressListAdapter(dataModels,getActivity());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                AddressClass AddressClass= dataModels.get(position);
                int address_id_to_select = AddressClass.getId();
                AddressClass.checked = !AddressClass.checked;
                if(AddressClass.checked){
                    checkbox_pos.add(address_id_to_select);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Selected "+AddressClass.building+String.valueOf(position), Toast.LENGTH_LONG).show();
                    select.setVisibility(View.VISIBLE);

                }
                else{
                    for(int i = 0 ; i < checkbox_pos.size() ; i++) {
                        checkbox_pos.get(i);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(getActivity(), "Removed " + AddressClass.building + String.valueOf(position), Toast.LENGTH_LONG).show();
                        if (checkbox_pos.size() == 0) {
                            select.setVisibility(View.GONE);
                        }
                    }
                }
                adapter.notifyDataSetChanged();


            }
        });
        getAddressList();
        return view;
    }
    private void getAddressList() {



        class RegisterUser extends AsyncTask<Void, Void, String> {


            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());
                int current_user = user_id;
                //creating request parameters
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

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


}