package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class AwardedWorksByEmployersToMe extends Fragment implements FragmentChangeListener , OnAcceptOrRejectProjectButtonClick {
    private ArrayList<AwardedWorksClass> m_parts = new ArrayList<AwardedWorksClass>();
    private Runnable viewParts;
    private AwardedWorksByEmployerToMeAdapter m_adapter;
    ListView listView;
    Button button;
    String newString;
    int assignee_id;
    SessionManager sm;
    ArrayList<LoginDBClass> lac;
    int user_id;
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
    int project_id;
    private int current_contractor_id;
    private  int current_contractor;

    //DataBaseHelper dbh;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.all_received_bids_for_projects, container, false);
        Bundle extras = getActivity().getIntent().getExtras();
        create_post = (ImageView)view.findViewById(R.id.create_icon);
        delete_post = (ImageView)view.findViewById(R.id.delete_icon);
        edit_post = (ImageView)view.findViewById(R.id.edit_icon);
        create = (TextView)view.findViewById(R.id.create) ;
        sm = new SessionManager(getActivity());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            current_contractor_id = Integer.parseInt(f.getUser_id());
        }
        current_contractor = current_contractor_id;

        if (extras == null) {
            newString = null;
        } else {
            newString = extras.getString("skill_passed");
        }
        // bidder_id = UserSession.getUserSession(getIntent()).getUserId();
        sm = new SessionManager(getActivity());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            user_id = Integer.parseInt(f.getUser_id());
        }
        assignee_id = user_id;
        listView = (ListView)view.findViewById(R.id.list_all_jobs_for_bids);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        dialog = new ProgressDialog(getActivity());
        getAllAwardedWorksList();
        Thread thread = new Thread(null, viewParts, "MagentoBackground");
        thread.start();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AwardedWorksClass project = (AwardedWorksClass) parent.getItemAtPosition(position);
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

    private void deleteOnClick() {
        int positon = listView.getSelectedItemPosition();
        Toast.makeText(getActivity(), "DELETING POS", Toast.LENGTH_SHORT).show();
    }


    private void getAllAwardedWorksList() {


        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;
            String assignee_id;
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object


                HashMap<String, String> params = new HashMap<>();
                String s = "Awarded";
                lac = (ArrayList<LoginDBClass>) sm.getUserId();
                for (LoginDBClass f : lac) {
                    assignee_id = f.getUser_id();
                }
                params.put("status",s );
                params.put("assignee_id",assignee_id);

                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());
                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_ALL_AWARDED_WORKS, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for(int i = 0; i < projectData.length(); i++){
                        JSONObject project = (JSONObject) projectData.get(i);

                        AwardedWorksClass data=new AwardedWorksClass();
                        data.slNo = i + 1;
                        data.project_id = project.getInt("project_id");
                        data.project_name = project.getString("title");
                        data.bid= project.getString("bidding_price");
                        data.bid_days = project.getString("bidding_days");
                        data.hirer_name = project.getString("hirer_name");; //project.getString("bids");
                        data.profile_pic = project.getString("url");
                        data.bid_proposal = project.getString("bid_proposal");
                        data.hirer_id = project.getInt("hirer_id");
                        data.bid_id = project.getInt("bid_id");
                        data.state = project.getString("state");
                        data.bid_milestone = project.getString("bid_milestone");


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
                progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
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
        m_adapter = new AwardedWorksByEmployerToMeAdapter(m_parts,AppController.getAppContext(), this );
        m_adapter.notifyDataSetChanged();

        listView.setAdapter(m_adapter);
        Log.e("ERROR", "In displayData call");
        Log.i("Info", "Inside displaydata");

    }


    public void openCreateFragment()
    {
        Fragment fr = new CreateBidJobPost();
        FragmentChangeListener fc=(FragmentChangeListener)getActivity();
        fc.replaceFragment(fr);
    }
    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }
    private View.OnClickListener myButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final int positon = listView.getSelectedItemPosition();
            Toast.makeText(getActivity(), "Edit Or Delete Item number "+positon, Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are You Sure About the Selection?")
                    .setCancelable(false)
                    .setPositiveButton("Yes, I want to Award Now!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            onDeleteButtonClick(positon);
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("No, I want to Go back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().finish();
                            Intent i = new Intent(getActivity(), NavigationBarUser.class);
                            startActivity(i);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }
    };

    private View.OnClickListener onEditButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final int position = listView.getSelectedItemPosition();
            Toast.makeText(getActivity(), "Edit Or Delete Item number " + position, Toast.LENGTH_SHORT).show();

        }
    };


    public void onDeleteButtonClick(int position) {

    }



    public void onAwardButtonClick1(final int project_id, final int hirer_id, final int bid_id) {
        class AcceptOnButtonClick extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());
                String s = "Ongoing";
                String s1 = "Accepted";
                String s2 = "Accepted";
                HashMap<String, String> params = new HashMap<>();
                params.put("project_id", String.valueOf(project_id));
                params.put("status", s);
                params.put("contractor_status", s1);
                //  JSONObject response = requestHandler.sendPostRequest(URLs.URL_UPDATE_STATUS , null, Request.Method.POST);
//                    m_adapter.notifyDataSetChanged();

                params.put("bid_Status", s2);
                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_ACCEPT_WORK_OFFER, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    int apiResponseStatus = mainResponse.getInt("status");
                    String apiResponseMessage = mainResponse.getString("message");
                    Log.i("Sourav, web response", response.toString());


//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            m_adapter.notifyDataSetChanged();
//                            listView.invalidateViews();
//                        }
//                    });
                    return apiResponseMessage;
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new SuccessActivityForAcceptingProjects(), "NewFragmentTag");
                ft.commit();
                m_adapter.notifyDataSetChanged();
                listView.invalidateViews();
                displayData();


            }
        }
        //executing the async task
        AcceptOnButtonClick ru = new AcceptOnButtonClick();
        ru.execute();
    }



        private void onDeleteButtonClick1(final int project_id, final int hirer_id, final int bid_id) {
            class DeclineOnButtonClick extends AsyncTask<Void, Void, String> {

                private ProgressBar progressBar;

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());
                    String s = "Rejected";
                    String s1 = "Rejected";
                    String s2 = "Rejected";
                    HashMap<String, String> params = new HashMap<>();
                    params.put("project_id", String.valueOf(project_id));
                    params.put("status", s);
                    params.put("contractor_status",s1);
                    params.put("bid_Status",s2);
                    //  JSONObject response = requestHandler.sendPostRequest(URLs.URL_UPDATE_STATUS , null, Request.Method.POST);
//                    m_adapter.notifyDataSetChanged();
                    String apiResponseMessage = "";
                    try {
                        JSONObject   response = requestHandler.sendPostRequest(URLs.URL_DELETE_WORK_OFFER, params, Request.Method.POST);
                        JSONObject mainResponse = (JSONObject) response.get("response");
                        int apiResponseStatus = mainResponse.getInt("status");
                        apiResponseMessage = mainResponse.getString("message");
                        Log.i("Sourav, web response", response.toString());

                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }

                    return apiResponseMessage;
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
                    m_adapter.notifyDataSetChanged();
                    listView.invalidateViews();
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new SuccessActivityForRejectingProjects(), "NewFragmentTag");
                    ft.commit();
                    m_adapter.notifyDataSetChanged();
                    listView.invalidateViews();
                    displayData();

                }
            }
            //executing the async task
            DeclineOnButtonClick ru = new DeclineOnButtonClick();
            ru.execute();

            //  viewPager.setCurrentItem(1);

            m_adapter.notifyDataSetChanged();

        }

    @Override
    public void accepting_a_project(final int bid_id, final int project_id, final int bidder_id) {


    }

    @Override
    public void rejecting_a_project(final int bid_id, final int project_id, final int bidder_id) {

    }

    @Override
    public void accepting_a_project_by_Contractor(final int project_id, final int hirer_id, final int bid_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are You Sure To Accept The Project?")
                .setCancelable(false)
                .setPositiveButton("Yes, I am sure to Accept!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onAwardButtonClick1(project_id, hirer_id, bid_id);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No, I Mistakenly pressed!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finish();
                        Intent i = new Intent(getActivity(), NavigationBarUser.class);
                        startActivity(i);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void rejecting_a_project_by_Contractor(final int project_id, final int hirer_id, final int bid_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do You want to Reject And Delete Bid?")
                .setCancelable(false)
                .setPositiveButton("Yes, I am sure to Reject!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onDeleteButtonClick1(project_id, hirer_id, bid_id);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No, I want to Quit Here", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finish();
                        Intent i = new Intent(getActivity(), NavigationBarUser.class);
                        startActivity(i);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

