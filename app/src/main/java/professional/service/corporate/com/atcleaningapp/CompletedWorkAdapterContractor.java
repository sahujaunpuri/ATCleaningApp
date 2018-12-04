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

public class CompletedWorkAdapterContractor  extends ArrayAdapter< AllAvailableJobsClass> {

    // declaring our ArrayList of items
    private ArrayList< AllAvailableJobsClass> objects;

    Typeface tvFont;
    AssetManager assetManager = getContext().getAssets();

    public CompletedWorkAdapterContractor(ArrayList< AllAvailableJobsClass> objects, Context context) {
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
            v = inflater.inflate(R.layout.completed_page_list_row, null);
        }





        AllAvailableJobsClass i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.


            TextView ttd = (TextView) v.findViewById(R.id.project_title);
            TextView mt = (TextView) v.findViewById(R.id.post_date);
            TextView post_status = (TextView) v.findViewById(R.id.post_status);
            TextView completion_status = (TextView) v.findViewById(R.id.completion_status);
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

                if(i.getState().equalsIgnoreCase("Completed")) {
                    post_status.setText("Project Status " + i.getState());
                    post_status.setBackgroundColor(Color.BLUE);
                }

            }
            if (completion_status != null) {
                completion_status.setText(" " + i.getCompletion_date());
                // mt.setTypeface(tvFont);
            }

        }

        // the view must be returned to our activity
        return v;

    }

}
