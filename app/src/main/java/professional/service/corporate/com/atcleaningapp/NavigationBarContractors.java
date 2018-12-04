package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public  class NavigationBarContractors extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView login;
    TextView signup;
    SessionManager sm;
    ArrayList<LoginDBClass> lac;
    int doctor_id;
    int current_doctor_id;
    NetworkImageView circleView;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private TextView name;
    TextView changedp;
    private SharedPreferences sharedPref2;
    private SharedPreferences sharedPref3;
    private SharedPreferences sharedPrefLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sm = new SessionManager(getApplicationContext());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            doctor_id = Integer.parseInt(f.getUser_id());
        }
        current_doctor_id = doctor_id;

        ppicOfUser();
        resetTokenAvailableIdDaily();
        sharedPrefLogout = getSharedpreferencesLogout();
        sharedPref2 = getSharedpreferences2();
        sharedPref3 = getSharedpreferences3();
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.arcNavigationView);
        navigationView.setNavigationItemSelectedListener(NavigationBarContractors.this);
        //LayoutInflater inflater = getLayoutInflater();
        //View listHeaderView = inflater.inflate(R.layout.header_layout, null, false);
        View listHeaderView = navigationView.inflateHeaderView(R.layout.header_layout);
        //navigationView.addHeaderView(listHeaderView);

        circleView = (NetworkImageView) listHeaderView.findViewById(R.id.circleView);
        name = (TextView)listHeaderView.findViewById(R.id.name);
        changedp = (TextView)listHeaderView.findViewById(R.id.change_dp);
        changedp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(NavigationBarContractors.this,UploadProfilePIc.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        Fragment fragment = new ContractorViewOfWorksAvailable();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//       // getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_contractor:
                fragment = new MyProfileC();
                break;

            case R.id.nav_dashboard_c:
                fragment = new AwardedWorksByEmployersToMe();
                break;
            case R.id.nav_bid_c:
                fragment = new ContractorViewOfWorksAvailable();
                break;
            case R.id.nav_history_c:
                fragment = new CalendarHistory();
                break;
            case R.id.nav_apt_c:
                fragment = new MyAllBookingsContractor();
                break;
            case R.id.nav_bids:
                fragment = new MyAllBidsManagement();
                break;

            case R.id.nav_transaction_c:
                fragment = new ContractorDashBoard();
                break;
            case R.id.nav_logout_c:
                sharedPref3 = this.getSharedpreferences3();
                sharedPref2 = this.getSharedpreferences2();
                sm = new SessionManager(getApplicationContext());
                lac = (ArrayList<LoginDBClass>) sm.getUserId();
                for (LoginDBClass f : lac) {
                    current_doctor_id = Integer.parseInt(f.getUser_id());
                }
                int listatus =0;

                sm.logout(current_doctor_id, String.valueOf(listatus));


                SharedPreferences.Editor editor = sharedPref2.edit();
                editor.clear();
                editor.commit();
                finish();
                SharedPreferences.Editor editor2 = sharedPref3.edit();
                editor2.clear();
                editor2.commit();
                finish();
                Intent intent = new Intent(NavigationBarContractors.this, LoginActivity.class);
                editor = sharedPrefLogout.edit();
                editor.putString(String.valueOf(R.string.logout_frag_preference_file_key),"Logout");
                editor.commit();
                finish();
                startActivity(intent);
                finish();

                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    private void ppicOfUser() {



        class CreateProject extends AsyncTask<Void, Void, ProfilePic> {

            private ProgressBar progressBar;

            @Override
            protected ProfilePic doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                String state = "Draft";
                int user_id = current_doctor_id;
                params.put("user_id", String.valueOf(user_id));
                // params.put("status", String.valueOf(state));

                ProfilePic data = new ProfilePic();
                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_PROFILE_PIC, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for (int i = 0; i < projectData.length(); i++) {
                        JSONObject project = (JSONObject) projectData.get(i);


                        data.user_name = project.getString("user_name");
                        data.image = project.getString("url");
                        data.gender = project.getString("gender");

                    }

                    return data;
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                return data;
            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                // progressBar = (ProgressBar) findViewById(R.id.progressBar);
                //    progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(ProfilePic s) {
                super.onPostExecute(s);
                if (s != null) {
                    if (s.image != null) {
                        Picasso.with(AppController.getAppContext()).load(s.image).fit().into(circleView);
                    } else {
                        if (s.gender.equalsIgnoreCase("Male")) {
                            Picasso.with(AppController.getAppContext()).load(R.drawable.patient_male).fit().into(circleView);
                        }
                        if (s.gender.equalsIgnoreCase("Female")) {
                            Picasso.with(AppController.getAppContext()).load(R.drawable.patient_female).fit().into(circleView);
                        }
                    }
                    name.setText(String.valueOf(s.user_name));

                } else {
                    Toast.makeText(getApplicationContext(), "Oops! No Information Received!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        //executing the async task
        CreateProject ru = new CreateProject();
        ru.execute();
    }

    private void resetTokenAvailableIdDaily() {

        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;
            String apiResponseMessage;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());



                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_UPDATE_TOKEN_DAILY, null, Request.Method.POST);
                    return response.toString();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar)findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AppController.getAppContext(), apiResponseMessage, Toast.LENGTH_LONG).show();
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    JSONObject apiResponse = (JSONObject) obj.get("response");

                    if (apiResponse.getInt("status") != 200) {
                        Toast.makeText(NavigationBarContractors.this, apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

//executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();



    }

    private SharedPreferences getSharedpreferences3() {

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.user_id), Context.MODE_PRIVATE);

        return sharedPref;


    }
    private SharedPreferences getSharedpreferences2()
    {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.user_type_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;
    }
    private SharedPreferences getSharedpreferencesLogout() {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.logout_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;


    }
}

