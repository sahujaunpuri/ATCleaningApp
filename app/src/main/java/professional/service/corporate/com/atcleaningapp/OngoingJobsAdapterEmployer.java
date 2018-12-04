package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OngoingJobsAdapterEmployer extends ArrayAdapter< AllAvailableJobsClass> {

    // declaring our ArrayList of items
    private ArrayList< AllAvailableJobsClass> objects;
    AprroveAndRateListener aprl;
    MilestonePaymentListener pml;
    PayMilestoneAmtListener mpl;
    Typeface tvFont;
    AssetManager assetManager = getContext().getAssets();

    public OngoingJobsAdapterEmployer(ArrayList< AllAvailableJobsClass> objects, Context context, AprroveAndRateListener aprl, MilestonePaymentListener pml, PayMilestoneAmtListener mpl) {
        super(context, R.layout.main_page_list_row, objects);
        this.objects = objects;
        this.aprl = aprl;
        this.pml = pml;
        this.mpl = mpl;
    }

    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        // tvFont = Typeface.createFromAsset(assetManager, "fonts/Raleway-Light.ttf");
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.ongoing_page_list_row, null);
        }





        final AllAvailableJobsClass i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.


            TextView ttd = (TextView) v.findViewById(R.id.project_title);
            TextView mt = (TextView) v.findViewById(R.id.post_date);
            TextView post_status = (TextView) v.findViewById(R.id.post_status);
            TextView completion_request = (TextView) v.findViewById(R.id.completion_request);
            TextView rate_feedback = (TextView) v.findViewById(R.id.rate_feedback);
            CircleImageView circleImageView = (CircleImageView)v.findViewById(R.id.circleView);
            TextView safe_deposit = (TextView) v.findViewById(R.id.safe_deposit);

            if (ttd != null) {
                ttd.setText((i.getProject_name()));
                //ttd.setTypeface(tvFont);
            }

            if (mt != null) {
                mt.setText(" " + i.getPost_date());
                // mt.setTypeface(tvFont);
            }
            if (post_status!= null) {
                if(i.getState().equalsIgnoreCase("Rejected")) {
                    post_status.setText("Project Status " + i.getState());
                    post_status.setBackgroundColor(Color.RED);
                }
                if(i.getState().equalsIgnoreCase("Awarded")) {
                    post_status.setText("Project Status " + i.getState());
                    post_status.setBackgroundColor(Color.BLUE);
                }
                if(i.getState().equalsIgnoreCase("Ongoing")) {
                    post_status.setText("Project Status " + i.getState());
                    post_status.setBackgroundColor(Color.GREEN);
                }
            }
            if (completion_request!= null) {
                if(i.getCompletion().equalsIgnoreCase("Requested")) {
                    completion_request.setText("Click To Approve Completion Request");
                    completion_request.setBackgroundColor(Color.GREEN);
                }
                if(i.getCompletion().equalsIgnoreCase("Approved")) {
                    completion_request.setText("Project Completion Request Approved");
                    completion_request.setBackgroundColor(Color.GREEN);
                }

            }
            if (rate_feedback!= null) {
                if(i.getCompletion().equalsIgnoreCase("Approved")) {
                    rate_feedback.setVisibility(View.VISIBLE);

                }

            }
            completion_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int project_id = i.getId();
                    int assignee_id = i.getAssignee_id();
                    aprl.approveCompletionRequest(project_id, assignee_id);
                }
            });
           safe_deposit.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   int project_id = i.getId();
                   int assignee_id = i.getAssignee_id();
                   mpl.paySafeDeposit(project_id, assignee_id);
               }
           });



        }


        // the view must be returned to our activity
        return v;

    }

}
