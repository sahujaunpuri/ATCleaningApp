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

public class DetermineProjectStatus  extends Fragment implements FragmentChangeListener , OnAcceptOrRejectButtonClick {
    private ArrayList<BidsByContractirClass> m_parts = new ArrayList<BidsByContractirClass>();
    private Runnable viewParts;
    private EmployerViewOFContractorResponseAdapter m_adapter;
    ListView listView;
    Button button;
    String newString;
    int hirer_id;
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
    //DataBaseHelper dbh;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.all_received_bids_for_projects, container, false);
        Bundle extras = getActivity().getIntent().getExtras();
        create_post = (ImageView)view.findViewById(R.id.create_icon);
        delete_post = (ImageView)view.findViewById(R.id.delete_icon);
        edit_post = (ImageView)view.findViewById(R.id.edit_icon);
        create = (TextView)view.findViewById(R.id.create) ;
        project_id = getArguments().getInt("project_id");


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
        hirer_id = user_id;
        listView = (ListView)view.findViewById(R.id.list_all_jobs_for_bids);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        dialog = new ProgressDialog(getActivity());
        getBids();
        Thread thread = new Thread(null, viewParts, "MagentoBackground");
        thread.start();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                BidsByContractirClass project = (BidsByContractirClass) parent.getItemAtPosition(position);
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
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                Toast.makeText(getActivity(), "Position long click"+pos, Toast.LENGTH_SHORT).show();
                BidsByContractirClass project = (BidsByContractirClass) arg0.getItemAtPosition(pos);
                Fragment fragment = new JobDetailsPostedByMe();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle b = new Bundle();
                b.putSerializable("projectObj", (project));
                b.putString("project_id", String.valueOf(project.getProject_id()));
                fragment.setArguments(b);
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                return true;
            }
        });
        return view;
    }

    private void deleteOnClick() {
        int positon = listView.getSelectedItemPosition();
        Toast.makeText(getActivity(), "DELETING POS", Toast.LENGTH_SHORT).show();
    }


    private void getBids() {
        final String skills = newString;


        class RegisterUser extends AsyncTask<Void, Void, String> {


            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                int current_project = project_id;
                params.put("project_id", String.valueOf(current_project));


                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_BIDS_FOR_PERTICULAR_PROJECT, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for(int i = 0; i < projectData.length(); i++){
                        JSONObject project = (JSONObject) projectData.get(i);

                        BidsByContractirClass data=new BidsByContractirClass();
                        data.slNo = i + 1;
                        // data.id = project.getInt("id");
                        data.job_status = project.getString("state");
                        data.details_inputted = project.getString("post_date");
                        data.proposal_inputted = project.getString("bid_proposal");
                        data.extra_Services_inputted = project.getString("extra_support");
                        data.question_inputted = project.getString("bid_question");
                        data.bid_amt_inputted = project.getString("bidding_price");
                        data.bid_text_inputted = project.getString("bidding_days");
                        data.milestone_inputted = project.getString("bid_milestone");
                        data.bidder_id = project.getInt("bidder_id");
                        data.bid_id = project.getInt("id");
                        data.bid_status = project.getString("bid_status");
                        data.bid_project_id = project.getInt("project_id");
                        data.employer_name = project.getString("user_name");

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

        m_adapter = new EmployerViewOFContractorResponseAdapter(m_parts,getActivity(),this);
        listView.setAdapter(m_adapter);
        m_adapter.notifyDataSetChanged();



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
                    .setPositiveButton("Yes, I want to Delete It!", new DialogInterface.OnClickListener() {
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


    @Override
    public void onAcceptPressed(final int bid_id, final int project_id, final int bidder_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are You Sure About the Selection?")
                .setCancelable(false)
                .setPositiveButton("Yes, I want to Award Now!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onAwardButtonClick1(bid_id, project_id, bidder_id);
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
    public void onAwardButtonClick1(final int bidId,final int project_id, final int project_bidder_id){
        class AwardResponse extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());
                HashMap<String, String> params = new HashMap<>();
                String status = "Awarded";
                String contractor_status ="Awaiting";
                int current_user = hirer_id;
                params.put("hirer_id", String.valueOf(current_user));

                params.put("assignee_id", String.valueOf(project_bidder_id));
                // params.put("email", now());
                params.put("status", status);
                params.put("project_id", String.valueOf(project_id));
                params.put("bid_id", String.valueOf(bidId));
                params.put("contractor_status", contractor_status);
                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_ASSIGN_WORK, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    int apiResponseStatus = mainResponse.getInt("status");
                    String apiResponseMessage = mainResponse.getString("message");
                    Log.i("Sourav, web response", response.toString());

                    return apiResponseMessage;
                }catch (JSONException ex){
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
//                Intent i = new Intent(Response.this, PostCreatedForHirer.class );
//                startActivity(i);
                getActivity().recreate();

            }
        }

        //executing the async task
        AwardResponse ru = new AwardResponse();
        ru.execute();


    }

    @Override
    public void onRejectPressed(final int bid_id, final int project_id, final int bidder_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do You want to Reject And Delete Bid?")
                .setCancelable(false)
                .setPositiveButton("Yes, I am sure to Reject!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onDeleteButtonClick1(bid_id, project_id, bidder_id);
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

    private void onDeleteButtonClick1(final int bid_id, final int project_id, final int bidder_id) {
        class AwardResponse extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());
                HashMap<String, String> params = new HashMap<>();
                String status = "Deleted";
                String contractor_status ="Deleted";
                int current_user = hirer_id;
                params.put("hirer_id", String.valueOf(current_user));

                params.put("assignee_id", String.valueOf(bidder_id));
                // params.put("email", now());
                params.put("status", status);
                params.put("project_id", String.valueOf(project_id));
                params.put("bid_id", String.valueOf(bid_id));
                params.put("contractor_status", contractor_status);
                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_DELETE_WORK, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    int apiResponseStatus = mainResponse.getInt("status");
                    String apiResponseMessage = mainResponse.getString("message");
                    Log.i("Sourav, web response", response.toString());

                    return apiResponseMessage;
                }catch (JSONException ex){
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
//                Intent i = new Intent(Response.this, PostCreatedForHirer.class );
//                startActivity(i);
                getActivity().recreate();

            }
        }

        //executing the async task
        AwardResponse ru = new AwardResponse();
        ru.execute();


    }

    private void checkIfAwardedOrNot() {



        class BidResponse extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;
            TextView txtPrompt;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();

                params.put("project_id", String.valueOf(project_id));

                BidsByContractirClass data = null;

                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_CHECK_RESPONSE, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray bidsData = (JSONArray) mainResponse.get("data");

                    for(int i = 0; i < bidsData.length(); i++) {
                        JSONObject bids = (JSONObject) bidsData.get(i);

                        data = new BidsByContractirClass();

                        data.status = bids.getString("status");
                        data.bidder_id = bids.getInt("assignee_id");
                        data.project_id = bids.getString("project_id");


                        return data.status;
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                return "Not Added";
            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            }
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

                    JSONObject data = (JSONObject) apiResponse.get("data");
                    int assignee_id = data.getInt("assignee_id");



                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }
        //executing the async task
        BidResponse ru = new BidResponse();
        ru.execute();
    }


}


