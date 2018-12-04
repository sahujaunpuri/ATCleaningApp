package professional.service.corporate.com.atcleaningapp;

import android.annotation.SuppressLint;
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

public class DialogAdapter extends ArrayAdapter<String> {

    // declaring our ArrayList of items
    private ArrayList<String> objects;

    Typeface tvFont;
    AssetManager assetManager = getContext().getAssets();

    public DialogAdapter(ArrayList<String> objects, Context context) {
        super(context, R.layout.main_page_list_row, objects);
        this.objects = objects;


    }

    @SuppressLint("ResourceAsColor")
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        // tvFont = Typeface.createFromAsset(assetManager, "fonts/Raleway-Light.ttf");
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.action_names, null);
            if(position == 0)
            v.setBackgroundColor(R.color.blue);
            if(position == 1)
                v.setBackgroundColor(R.color.orange1);
            if(position == 2)
                v.setBackgroundColor(R.color.red1);

        }




        // the view must be returned to our activity
        return v;

    }

}
