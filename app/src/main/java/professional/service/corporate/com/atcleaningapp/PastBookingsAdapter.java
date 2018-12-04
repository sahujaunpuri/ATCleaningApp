package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PastBookingsAdapter extends ArrayAdapter< AllAvailableJobsClass> {

// declaring our ArrayList of items
private ArrayList< AllAvailableJobsClass> objects;

        Typeface tvFont;
        AssetManager assetManager = getContext().getAssets();

public PastBookingsAdapter(ArrayList< AllAvailableJobsClass> objects, Context context) {
        super(context, R.layout.main_page_list_row, objects);
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
        v = inflater.inflate(R.layout.my_past_bookings, null);
        }





        AllAvailableJobsClass i = objects.get(position);

        if (i != null) {

        // This is how you obtain a reference to the TextViews.
        // These TextViews are created in the XML files we defined.


        TextView ttd = (TextView) v.findViewById(R.id.project_title);
        TextView mt = (TextView) v.findViewById(R.id.completion_date);
        TextView post_status = (TextView) v.findViewById(R.id.post_status);
        TextView contractor_name = (TextView) v.findViewById(R.id.contractor_name);
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

        post_status.setText("Project Status " + i.getContractor_status());


        }
        if (contractor_name!= null) {
                contractor_name.setText("Contractor Name " + i.getUser_name());
                }
        }

        // the view must be returned to our activity
        return v;

        }

        }

