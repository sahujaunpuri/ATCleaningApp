package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EmployerViewOFContractorResponseAdapter extends ArrayAdapter< BidsByContractirClass> {

// declaring our ArrayList of items
private ArrayList< BidsByContractirClass> objects;

        Typeface tvFont;
        AssetManager assetManager = getContext().getAssets();
        OnAcceptOrRejectButtonClick acceptClickListener;

public EmployerViewOFContractorResponseAdapter(ArrayList< BidsByContractirClass> objects, Context context, final OnAcceptOrRejectButtonClick onAcceptOrRejectButtonClick) {
        super(context, R.layout.employer_view_of_job_bid, objects);
        this.objects = objects;
        this.acceptClickListener = onAcceptOrRejectButtonClick;

        }

public View getView(final int position, final View convertView, final ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        // tvFont = Typeface.createFromAsset(assetManager, "fonts/Raleway-Light.ttf");
        if (v == null) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.employer_view_of_job_bid, null);

        }





        BidsByContractirClass i = objects.get(position);

        if (i != null) {

        // This is how you obtain a reference to the TextViews.
        // These TextViews are created in the XML files we defined.


        TextView ttd = (TextView) v.findViewById(R.id.job_status_des);
        TextView mt = (TextView) v.findViewById(R.id.details_inputted);
        TextView mtd = (TextView) v.findViewById(R.id.proposal_inputted);
        TextView mtdd = (TextView) v.findViewById(R.id.extra_services_inputted);
        TextView ctd = (TextView) v.findViewById(R.id.questions_inputted);
        TextView cctd = (TextView) v.findViewById(R.id.bid_amt_inputted_inputted);
        TextView ptd = (TextView) v.findViewById(R.id.bid_text_inputted_days);
        TextView mitd = (TextView) v.findViewById(R.id.milestones_inputted);
        CircleImageView circleImageView = (CircleImageView)v.findViewById(R.id.circleView);
        final TextView accept = (TextView) v.findViewById(R.id.accept);
        TextView reject = (TextView) v.findViewById(R.id.reject);
        TextView awarded_button = (TextView) v.findViewById(R.id.awarded_button);


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bid_id = Integer.parseInt(String.valueOf(objects.get(position).getBid_id()));
               // int bid = Integer.parseInt(String.valueOf(objects.get(position).getBid()));
                int  project_id = ((objects.get(position).getBid_project_id()));

                int bidder_id = Integer.parseInt(String.valueOf(objects.get(position).getBidder_id()));
                Log.i("Sourav, test", "project_id" + bid_id);
                // mMyClickListener.onCustomClickListener(project_id);

                acceptClickListener.onAcceptPressed(bid_id, project_id, bidder_id);



            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bid_id = Integer.parseInt(String.valueOf(objects.get(position).getBid_id()));
                //int bid = Integer.parseInt(String.valueOf(objects.get(position).getBid()));
                int  project_id = ((objects.get(position).getBid_project_id()));

                int bidder_id = Integer.parseInt(String.valueOf(objects.get(position).getBidder_id()));
                Log.i("Sourav, test", "project_id" + bid_id);



                acceptClickListener.onRejectPressed(bid_id, project_id, bidder_id);

            }
        });
        if (ttd != null) {
        ttd.setText((i.getJob_status()));
        //ttd.setTypeface(tvFont);
        }

        if (mt != null) {
        mt.setText(" " + i.getEmployer_name());
        // mt.setTypeface(tvFont);
        }
        if (mtd != null) {
                mtd.setText((i.getProposal_inputted()));
                //ttd.setTypeface(tvFont);
            }
            if (mtdd != null) {
                mtdd.setText(" " + i.getExtra_Services_inputted());
                // mt.setTypeface(tvFont);
            }
            if (ctd != null) {
                ctd.setText((i.getQuestion_inputted()));
                //ttd.setTypeface(tvFont);
            }

            if (cctd != null) {
                cctd.setText(" " + i.getBid_amt_inputted());
                // mt.setTypeface(tvFont);
            }

            if (ptd != null) {
                ptd.setText((i.getBid_text_inputted()));
                //ttd.setTypeface(tvFont);
            }

            if (mitd != null) {
                mitd.setText(" " + i.getMilestone_inputted());
                // mt.setTypeface(tvFont);
            }
           if(i.getBid_status().equalsIgnoreCase("Awaiting")) {
               awarded_button.setVisibility(View.VISIBLE);
               accept.setVisibility(View.GONE);
               reject.setVisibility(View.GONE);
           }
        }

        // the view must be returned to our activity
        return v;

        }

        }
