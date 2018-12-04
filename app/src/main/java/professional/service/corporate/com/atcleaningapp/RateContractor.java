package professional.service.corporate.com.atcleaningapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RateContractor extends Fragment {
    View view;
    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private TextView btnSubmit;
    private EditText comment;

    CircleImageView dp;
    TextView name;
    TextView location;
    int project_id ;
    int assignee_id ;
    int hirer_id ;
    TextView project_title;
    private SessionManager sm;
    private ArrayList<LoginDBClass> lac;
    private int user_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.rate_contractor, container, false);
        dp = (CircleImageView) view.findViewById(R.id.circleView);
        name = (TextView)view.findViewById(R.id.name);
        comment = (EditText)view.findViewById(R.id.feedback);
        sm = new SessionManager(getActivity());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            hirer_id = Integer.parseInt(f.getUser_id());
        }
        user_id = hirer_id;
        project_id = getArguments().getInt("project_id");
        assignee_id = getArguments().getInt("assignee_id");

        project_title = (TextView)view.findViewById(R.id.title_of_project);
        addListenerOnRatingBar();
        addListenerOnButton();
        showRating();
        return view;
    }
public void addListenerOnRatingBar() {

    ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
    ratingBar.setIsIndicator(false);
    //txtRatingValue = (TextView) view.findViewById(R.id.txtRatingValue);

    //if rating value is changed,
    //display the current rating value in the result (textview) automatically
    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
        public void onRatingChanged(RatingBar ratingBar, float rating,
                                    boolean fromUser) {

           // txtRatingValue.setText(String.valueOf(rating));

        }
    });
}

    public void addListenerOnButton() {

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        btnSubmit = (TextView) view.findViewById(R.id.done_request);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                rateUser(ratingBar.getRating());
                Toast.makeText(AppController.getAppContext(), String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), RatingSuccess.class);
                startActivity(i);

            }

        });

    }

    public void rateUser(final float rating) {

        final String inputComment = comment.getText().toString();
        class RateUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;


            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                final int hirer_id1 = user_id;
                params.put("value", String.valueOf(rating));
                final int project_id1 = (project_id);
                final int receiver = assignee_id;
                params.put("project_id", String.valueOf(project_id1));
                params.put("provider", String.valueOf(hirer_id1));
                params.put("receiver", String.valueOf(receiver));
                params.put("comment",inputComment);
                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_RATING, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    String apiResponseStatus = mainResponse.getString("status");
                    String apiResponseMessage = mainResponse.getString("message");
                    Log.i("Sourav, web response", response.toString());

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

            }
        }

        //executing the async task
        RateUser ru = new RateUser();
        ru.execute();


    }

    public void showRating() {

        class AwardResponse extends AsyncTask<Void, Void, UserRatingClass> {

            private ProgressBar progressBar;

            protected UserRatingClass doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                int assignee_id1 = assignee_id;
                int current_project = project_id;
                params.put("user_id", String.valueOf(assignee_id1));
                params.put("project_id", String.valueOf(current_project));

                UserRatingClass data = null;
                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_SHOW_CONTRACTOR_DETAILS, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for (int i = 0; i < projectData.length(); i++) {
                        JSONObject project = (JSONObject) projectData.get(i);

                        data = new UserRatingClass();
                        //data.slNo = i + 1;

                        data.receiver_name = project.getString("user_name");
                        data.project_name = project.getString("title");
                        data.url = project.getString("url");
                        //setRating(data.value);
                        //data.bids = ""; //project.getString("bids");
                        // m_parts.add(data);
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
                // progressBar = (ProgressBar) getActivity().view.findViewById(R.id.progressBar);
                //  progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(UserRatingClass s) {
                super.onPostExecute(s);
                if(s!=null){
                if(s.url!= "null") {
                    Picasso.with(AppController.getAppContext()).load(s.url).into(dp);
                }
                else{
                    Picasso.with(AppController.getAppContext()).load(R.drawable.male).into(dp);
                }
                    name.setText(s.receiver_name);
                    project_title.setText(s.project_name);
                }
                else{
                    Toast.makeText(getActivity(), "Oops! Could not receive Response!",Toast.LENGTH_SHORT ).show();
                }


            }
        }
        //executing the async task
        AwardResponse ru = new AwardResponse();
        ru.execute();
    }

    private void setRating(float value) {
        ratingBar.setRating(value);
    }
}