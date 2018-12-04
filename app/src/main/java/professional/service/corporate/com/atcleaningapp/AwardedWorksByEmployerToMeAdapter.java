package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AwardedWorksByEmployerToMeAdapter extends ArrayAdapter< AwardedWorksClass> {

    // declaring our ArrayList of items
    private ArrayList< AwardedWorksClass> objects;

    Typeface tvFont;
    AssetManager assetManager = getContext().getAssets();
    OnAcceptOrRejectProjectButtonClick acceptClickListener;

    public AwardedWorksByEmployerToMeAdapter(ArrayList< AwardedWorksClass> objects, Context context, final OnAcceptOrRejectProjectButtonClick onAcceptOrRejectButtonClick) {
        super(context, R.layout.awarded_works_view_for_me, objects);
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
            v = inflater.inflate(R.layout.awarded_works_view_for_me, null);

        }





        AwardedWorksClass i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.


            TextView ttd = (TextView) v.findViewById(R.id.job_status_des);
            TextView mt = (TextView) v.findViewById(R.id.details_inputted);
            TextView mtd = (TextView) v.findViewById(R.id.proposal_inputted);
            TextView mtdd = (TextView) v.findViewById(R.id.extra_services_inputted);
            TextView cctd = (TextView) v.findViewById(R.id.bid_amt_inputted_inputted);
            TextView mitd = (TextView) v.findViewById(R.id.milestones_inputted);
            CircleImageView circleImageView = (CircleImageView)v.findViewById(R.id.circleView);
            final TextView accept = (TextView) v.findViewById(R.id.accept);
            TextView reject = (TextView) v.findViewById(R.id.reject);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int  project_id = Integer.parseInt(String.valueOf(objects.get(position).getProject_id()));
                    int  bid_id = Integer.parseInt(String.valueOf(objects.get(position).getBid_id()));
                    int  hirer_id = Integer.parseInt(String.valueOf(objects.get(position).getHirer_id()));
                    acceptClickListener.accepting_a_project_by_Contractor(project_id, hirer_id, bid_id);



                }
            });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int  project_id = Integer.parseInt(String.valueOf(objects.get(position).getProject_id()));
                    int  bid_id = Integer.parseInt(String.valueOf(objects.get(position).getBid_id()));
                    int  hirer_id = Integer.parseInt(String.valueOf(objects.get(position).getHirer_id()));
                    acceptClickListener.rejecting_a_project_by_Contractor(project_id, hirer_id, bid_id);

                }
            });
            if (ttd != null) {
                ttd.setText((i.getProject_name()));
                ttd.setTypeface(tvFont);
            }

            if (mt != null) {
                mt.setText(" " + i.getHirer_name());
                // mt.setTypeface(tvFont);
            }
            if (mtd != null) {
                mtd.setText((i.getBid_proposal()));
                //ttd.setTypeface(tvFont);
            }

            if (cctd != null) {
                cctd.setText((i.getBid()));
                //ttd.setTypeface(tvFont);
            }
            if (mitd != null) {
                mitd.setText((i.getBid_milestone()));
                //ttd.setTypeface(tvFont);
            }


        }

        // the view must be returned to our activity
        return v;

    }

}
