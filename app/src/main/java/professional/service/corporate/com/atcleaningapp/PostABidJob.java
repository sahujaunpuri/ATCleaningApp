package professional.service.corporate.com.atcleaningapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PostABidJob extends AppCompatActivity implements  FragmentChangeListener {
    ImageView create_post;
    ImageView delete_post;
    ImageView edit_post;
    TextView create;
    TextView edit;
    TextView delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_a_job_for_bid);
        create_post = (ImageView)findViewById(R.id.create_icon);
        create = (TextView)findViewById(R.id.create) ;
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateFragment();
            }
        });
        create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateFragment();
            }
        });


    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }
    public void openCreateFragment()
    {
        Fragment fr = new CreateBidJobPost();
        FragmentChangeListener fc=(FragmentChangeListener)getApplicationContext();
        fc.replaceFragment(fr);
    }
}
