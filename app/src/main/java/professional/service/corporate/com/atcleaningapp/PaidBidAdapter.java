package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PaidBidAdapter extends ArrayAdapter<SafeDepositClass> {

    // declaring our ArrayList of items
    private ArrayList<SafeDepositClass> objects;

    /* here we must override the constructor for ArrayAdapter
     * the only variable we care about now is ArrayList<Exercise> objects,
     * because it is the list of objects we want to display.
     */
    public PaidBidAdapter(Context context, int textViewResourceId, ArrayList<SafeDepositClass> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.paid_work_item, null);
        }

        /*
         * Recall that the variable position is sent in as an argument to this method.
         * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
         * iterates through the list we sent it)
         *
         * Therefore, i refers to the current Exercise object.
         */

        SafeDepositClass i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.


            TextView ttd = (TextView) v.findViewById(R.id.user_name);
            TextView mt = (TextView) v.findViewById(R.id.project_title);
            TextView mtd = (TextView) v.findViewById(R.id.post_status);
            TextView bt = (TextView) v.findViewById(R.id.post_date);
            TextView btd = (TextView) v.findViewById(R.id.amt);

            // check to see if each individual textview is null.
            // if not, assign some text!

            if (ttd != null){
                ttd.setText(String.valueOf(i.getUser_name()));
            }

            if (mt != null){
                mt.setText(" " + i.getProject_name());
            }

            if (mtd != null){
                mtd.setText("Payment Status" + i.getStatus());
            }
            if (bt != null){
                bt.setText("Payment Date" + i.getPayment_date());
            }
            if (btd != null){
                btd.setText("Amount Paid " + i.getMilestone_amt()+" "+ "CAD");
            }

        }

        // the view must be returned to our activity
        return v;

    }

}