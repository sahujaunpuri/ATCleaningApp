package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllAvailableJobsAdapter extends ArrayAdapter< AllAvailableJobsClass> {

    // declaring our ArrayList of items
    private ArrayList< AllAvailableJobsClass> objects;

    Typeface tvFont;
    AssetManager assetManager = getContext().getAssets();

    public AllAvailableJobsAdapter(ArrayList< AllAvailableJobsClass> objects, Context context) {
        super(context, R.layout.main_page_list_row_all, objects);
        this.objects = objects;


    }

    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
       // tvFont = Typeface.createFromAsset(assetManager, "fonts/Raleway-Light.ttf");
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.main_page_list_row, null);
        }





        AllAvailableJobsClass i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.


            TextView ttd = (TextView) v.findViewById(R.id.project_title);
            TextView mt = (TextView) v.findViewById(R.id.post_date);
            TextView post_status = (TextView) v.findViewById(R.id.post_status);
            TextView availability = (TextView) v.findViewById(R.id.availability);
            TextView bids_rcv = (TextView) v.findViewById(R.id.bids);
            CircleImageView circleImageView = (CircleImageView)v.findViewById(R.id.circleView);


            if (ttd != null) {
                ttd.setText((i.getProject_name()));
                //ttd.setTypeface(tvFont);
            }

            if (mt != null) {
                mt.setText(" " + i.getPost_date());
               // mt.setTypeface(tvFont);
            }
            if (post_status!= null) {
                if(i.getState().equalsIgnoreCase("Deleted")) {
                    post_status.setText("Project Status " + i.getState());
                    post_status.setBackgroundColor(Color.RED);
                }
                if(i.getState().equalsIgnoreCase("Awarded")) {
                    post_status.setText("Project Status " + i.getState());
                    post_status.setBackgroundColor(Color.BLUE);
                }
                if(i.getState().equalsIgnoreCase("Open")) {
                    post_status.setText("Project Status " + i.getState());
                    post_status.setBackgroundColor(Color.GREEN);
                }
            }
            if (availability!= null) {
                if(i.getAvailability().equalsIgnoreCase("Deleted")) {
                    availability.setText( i.getAvailability());
                    availability.setBackgroundColor(Color.RED);
                }
                if(i.getAvailability().equalsIgnoreCase("Closed For Bidding")) {
                    availability.setText(i.getAvailability());
                    availability.setBackgroundColor(Color.YELLOW);
                }
                if(i.getAvailability().equalsIgnoreCase("Open For Bidding")) {
                    availability.setText( i.getAvailability());
                    availability.setBackgroundColor(Color.GREEN);
                }
            }
            if (bids_rcv != null) {
                bids_rcv.setText("No Bids Received " + i.getBid_count());
                // mt.setTypeface(tvFont);
            }
        }

        // the view must be returned to our activity
        return v;

    }

}
