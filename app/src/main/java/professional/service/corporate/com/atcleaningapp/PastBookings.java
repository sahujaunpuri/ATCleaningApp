package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PastBookings extends Fragment {
    ArrayList<AllAvailableJobsClass> dataModels  =new ArrayList<>();
    ListView listView;
    private  PastBookingsAdapter adapter;
    private View view;
    ArrayList<LoginDBClass> lac;
    SessionManager sm;
    ProgressDialog dialog;
    int hirer_id;
    String date;
    //  final AppController aController = (AppController)getContext();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.my_past_bookings_list, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
//       setSupportActionBar(toolbar);
//        sm = new SessionManager(getActivity());
//        lac = (ArrayList<LoginDBClass>) sm.getUserId();
//        for (LoginDBClass f : lac) {
//            AllAvailableJobsClass_id = Integer.parseInt(f.getUser_id());
//        }
//        current_AllAvailableJobsClass_id = AllAvailableJobsClass_id;
        Bundle b = getArguments();
        hirer_id = b.getInt("hirer_id");
        date = b.getString("date");
        listView=(ListView)view.findViewById(R.id.list);
        dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        //dataModels= new ArrayList<>();

        getAllAvailableJobsClassList();


        return view;
    }

    private void getAllAvailableJobsClassList() {



        class PatientHandler extends AsyncTask<Void, Void, String> {


            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());
                String current_hirer = String.valueOf(hirer_id);
                String cal_date = date;
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("hirer_id", current_hirer);
                params.put("date", cal_date);

                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_COMPLETED_WORKS, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for(int i = 0; i < projectData.length(); i++){
                        JSONObject project = (JSONObject) projectData.get(i);

                        AllAvailableJobsClass data=new AllAvailableJobsClass();
                        data.slNo = i + 1;
                        data.id = project.getInt("id");
                        data.user_name = project.getString("user_name");
                        data.title = project.getString("title");
                        data.contractor_status = project.getString("contractor_status");

                        data.assigned_date = project.getString("assigned_date");
                        data.url = project.getString("url");
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
                dialog.setMessage("Please Wait! Loading ...");
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(dialog.isShowing()){
                    dialog.cancel();
                }
                displayData();


            }
        }
        //executing the async task
        PatientHandler ru = new PatientHandler();
        ru.execute();
    }

    private void displayData() {
        adapter= new PastBookingsAdapter(dataModels,AppController.getAppContext());
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }



}





