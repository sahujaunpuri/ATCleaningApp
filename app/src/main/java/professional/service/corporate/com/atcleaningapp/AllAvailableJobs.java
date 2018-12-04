package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AllAvailableJobs extends AppCompatActivity {
    private ArrayList<AllAvailableJobsClass> m_parts = new ArrayList<AllAvailableJobsClass>();
    private Runnable viewParts;
    private AllAvailableJobsAdapter m_adapter;
    ListView listView;
    Button button;
    String newString;
    int bidder_id;
    SessionManager sm;
    ArrayList<LoginDBClass> lac;
    int cont_id;
    ProgressDialog dialog;
    EditText city;
    TextView go;
    TextView change;

    //DataBaseHelper dbh;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_posted_jobs);
        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            newString = null;
        } else {
            newString = extras.getString("skill_passed");
        }
        // bidder_id = UserSession.getUserSession(getIntent()).getUserId();
        sm = new SessionManager(getApplicationContext());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            cont_id = Integer.parseInt(f.getUser_id());
        }
        bidder_id = cont_id;
        listView = (ListView) findViewById(R.id.listview);
        dialog = new ProgressDialog(AllAvailableJobs.this);
        Thread thread = new Thread(null, viewParts, "MagentoBackground");
        thread.start();
      
       
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AllAvailableJobsClass project = (AllAvailableJobsClass) parent.getItemAtPosition(position);

                Intent intent = new Intent(AllAvailableJobs.this, PostABidActivity.class);
                
                intent.putExtra("projectObj", project);
                startActivity(intent);
            }
        });
    }

   

    
    

    private void displayData() {

        m_adapter = new AllAvailableJobsAdapter(m_parts, AllAvailableJobs.this);
        m_adapter.notifyDataSetChanged();

        listView.setAdapter(m_adapter);
        Log.e("ERROR", "In displayData call");
        Log.i("Info", "Inside displaydata");
        //Result rItem= resultlist.get(i);
        //Log.i("INFO", response);
        // responseView.setText(response);
        // ProgressBar.dismiss();
    }



    
}

