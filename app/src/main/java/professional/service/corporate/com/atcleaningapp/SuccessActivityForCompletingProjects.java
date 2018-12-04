package professional.service.corporate.com.atcleaningapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import pl.droidsonroids.gif.GifImageView;

public class SuccessActivityForCompletingProjects extends Fragment {
    View view;
    GifImageView image;
    private SharedPreferences sharedPref1;
    private String sharedPrefKey;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.success5, container, false);
        image = (GifImageView)view.findViewById(R.id.check);

        Toast.makeText(getActivity(),"Redirecting in 3 secs",Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {


                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new MyAllBookingsContractor(), "NewFragmentTag");
                ft.commit();


            }

        }, 3000);
        return view;
    }


}
