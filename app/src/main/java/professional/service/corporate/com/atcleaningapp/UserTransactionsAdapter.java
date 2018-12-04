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

public class UserTransactionsAdapter extends ArrayAdapter< BidsByContractirClass> {

    // declaring our ArrayList of items
    private ArrayList< BidsByContractirClass> objects;

    Typeface tvFont;
    AssetManager assetManager = getContext().getAssets();
    OnMarkFinishedListener acceptClickListener;

    public UserTransactionsAdapter(ArrayList< BidsByContractirClass> objects, Context context, final OnMarkFinishedListener onAcceptOrRejectButtonClick) {
        super(context, R.layout.transaction_row_user, objects);
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
            v = inflater.inflate(R.layout.transaction_row_user, null);

        }





        BidsByContractirClass i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.


            TextView ttd = (TextView) v.findViewById(R.id.client_name);
            TextView ttdd = (TextView) v.findViewById(R.id.client_location);
            TextView ccd = (TextView) v.findViewById(R.id.work_to_be_done);
            TextView cctd = (TextView) v.findViewById(R.id.amount_payable_number);
            TextView ctd = (TextView) v.findViewById(R.id.date);
            CircleImageView circleImageView = (CircleImageView)v.findViewById(R.id.circleView);

            TextView mark_finished = (TextView)v.findViewById(R.id.mark_finished);

            mark_finished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int bid_id = Integer.parseInt(String.valueOf(objects.get(position).getBid_id()));
                    // int bid = Integer.parseInt(String.valueOf(objects.get(position).getBid()));
                    int  project_id = Integer.parseInt(String.valueOf(objects.get(position).getProject_id()));

                    int bidder_id = Integer.parseInt(String.valueOf(objects.get(position).getBidder_id()));
                    Log.i("Sourav, test", "project_id" + bid_id);
                    // mMyClickListener.onCustomClickListener(project_id);

                    acceptClickListener.onMarkFinishedTheJob(bid_id, project_id, bidder_id);



                }
            });

            if (ttd != null) {
                ttd.setText((i.getBid_amt_inputted()));
                //ttd.setTypeface(tvFont);
            }

            if (ttdd != null) {
                ttdd.setText((i.getClient_name()));
                //ttd.setTypeface(tvFont);
            }
            if (ccd != null) {
                ccd.setText((i.getClient_location()));
                //ttd.setTypeface(tvFont);
            }

            if (cctd != null) {
                cctd.setText(" " + i.getWork_to_be_done());
                // mt.setTypeface(tvFont);
            }
            if (ctd != null) {
                ctd.setText(" " + i.getDate_paid());
                // mt.setTypeface(tvFont);
            }


        }

        // the view must be returned to our activity
        return v;

    }

}
