package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AttachedFilesGridViewAdapter extends ArrayAdapter<HirerImages> {

    // declaring our ArrayList of items
    private ArrayList<HirerImages> objects;
    AssetManager assetManager = getContext().getAssets();
//    final Typeface tvFont = Typeface.createFromAsset(assetManager, "fonts/Raleway-Light.ttf");

    /* here we must override the constructor for ArrayAdapter
     * the only variable we care about now is ArrayList<Exercise> objects,
     * because it is the list of objects we want to display.
     */

    public AttachedFilesGridViewAdapter(Context context, int textViewResourceId, ArrayList<HirerImages> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;






    }


    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_item_layout, null);
        }




        HirerImages i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.



            ImageView mt = (ImageView) v.findViewById(R.id.imageView);

            // if not, assign some text!


            if (mt != null) {
                Picasso.with(AppController.getAppContext()).load(i.url).fit().into(mt);
            }

        }

        // the view must be returned to our activity
        return v;

    }

}

