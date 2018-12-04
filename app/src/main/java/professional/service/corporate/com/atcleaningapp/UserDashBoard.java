package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class UserDashBoard extends Fragment implements  FragmentChangeListener {
    ImageView create_post;
    ImageView delete_post;
    ImageView edit_post;
    TextView create;
    TextView edit;
    TextView delete;
    View view;
    private FragmentChangeListener fc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.post_a_job_for_bid, container, false);
        create_post = (ImageView)view.findViewById(R.id.create_icon);
        create = (TextView)view.findViewById(R.id.create) ;
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new CreateBidJobPost());
            }
        });
        create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new CreateBidJobPost());
            }
        });

    return view;
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }

}


