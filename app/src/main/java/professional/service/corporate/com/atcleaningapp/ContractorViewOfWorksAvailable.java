package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ContractorViewOfWorksAvailable extends Fragment implements FragmentChangeListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private ArrayList<AllAvailableJobsClass> m_parts = new ArrayList<AllAvailableJobsClass>();
    private Runnable viewParts;
    private AllAvailableJobsAdapterContractor m_adapter;
    ListView listView;
    Button button;
    String newString;
    int bidder_id;
    SessionManager sm;
    ArrayList<LoginDBClass> lac;
    int cont_id;
    ProgressDialog dialog;
    View view;
    ImageView create_post;
    ImageView delete_post;
    ImageView edit_post;
    TextView create;
    TextView edit;
    TextView delete;
    private String destination;
    GoogleApiClient mGoogleApiClient;
    double latitude;
    double longitude;
    SharedPreferences getSharedPref5;

    Location mLastLocation;
    String userType;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    TextView distance;
    TextView next;
    String address;

    ImageView search_view;
    TextView tvLatlong;
    String origin;
    private String sharedPrefKey;
    String personName;
    String personPhotoUrl;
    String userEmail;
    ImageView search_img;
    private String city;
    private String state;
    Calendar now = Calendar.getInstance();
    int a = now.get(Calendar.AM_PM);
    String broadcast = "stop";
    String saved_back_press;
    LatLng latLng;
    private SharedPreferences sharedPref4;
    EditText search_edittext;
    private SharedPreferences sharedPref5;
    private String knownName;
    private String postalCode;
    private String country;

    //DataBaseHelper dbh;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.work_available_for_bid, container, false);
        Bundle extras = getActivity().getIntent().getExtras();

        delete_post = (ImageView)view.findViewById(R.id.delete_icon);
        edit_post = (ImageView)view.findViewById(R.id.edit_icon);

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else
            Toast.makeText(getActivity(), "Not connected...", Toast.LENGTH_SHORT).show();
        // duration = (TextView)findViewById(R.id.duration);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing since Google Play Services are not available");
            getActivity().finish();
        } else {
            Log.d("onCreate", "Google Play Services available.");
        }
        delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (extras == null) {
            newString = null;
        } else {
            newString = extras.getString("skill_passed");
        }
        // bidder_id = UserSession.getUserSession(getIntent()).getUserId();
        sm = new SessionManager(getActivity());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            cont_id = Integer.parseInt(f.getUser_id());
        }
        bidder_id = cont_id;
        listView = (ListView)view.findViewById(R.id.list_all_jobs);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        dialog = new ProgressDialog(getActivity());
        getProjects();
        Thread thread = new Thread(null, viewParts, "MagentoBackground");
        thread.start();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AllAvailableJobsClass project = (AllAvailableJobsClass) parent.getItemAtPosition(position);

                delete_post.animate().rotation(360).start();
                edit_post.setRotation(edit_post.getRotation()+360);
                int current_project = project.getId();

                checkIfAvailableForBid(current_project, project);

//
//                Toast.makeText(getActivity(), "Selected"+position, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), ContractorViewOfPostedJob.class);
//
//                intent.putExtra("projectObj", project);
//                startActivity(intent);
            }
        });
        return view;
    }




    private void getProjects() {
        final String skills = newString;


        class RegisterUser extends AsyncTask<Void, Void, String> {


            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                int user = bidder_id;
                params.put("user_id", String.valueOf(user));


                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_ALL_AVAIAILBLE_JOBS, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for (int i = 0; i < projectData.length(); i++) {
                        JSONObject project = (JSONObject) projectData.get(i);

                        AllAvailableJobsClass data = new AllAvailableJobsClass();
                        data.slNo = i + 1;
                        data.id = project.getInt("id");
                        data.project_name = project.getString("title");
                        data.post_date = project.getString("post_date");
                        data.state = project.getString("state");
                        data.availability = project.getString("availability");
                        data.latitude = project.getString("latitude");
                        data.longitude = project.getString("longitude");
                        data.url = project.getString("url");

                       /* destination = data.latitude + "," + data.longitude;
                        URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origin + "&destinations=" + destination + "&mode=driving&language=fr-FR&avoid=tolls&key=AIzaSyBaPqB_4MNFxYU93fPIsNOEeTXx337dkmQ");
                        try {
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("GET");
                            con.connect();
                            int statuscode = con.getResponseCode();
                            if (statuscode == HttpURLConnection.HTTP_OK) {
                                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                                StringBuilder sb = new StringBuilder();
                                String line = br.readLine();
                                while (line != null) {
                                    sb.append(line);
                                    line = br.readLine();
                                }

                                String json = sb.toString();
                                Log.d("JSON", json);
                                JSONObject root = new JSONObject(json);
                                JSONArray array_rows = root.getJSONArray("rows");
                                Log.d("JSON", "array_rows:" + array_rows);
                                JSONObject object_rows = array_rows.getJSONObject(0);
                                Log.d("JSON", "object_rows:" + object_rows);
                                JSONArray array_elements = object_rows.getJSONArray("elements");
                                Log.i("Sourav, JSON", "array_elements:" + array_elements);
                                JSONObject object_elements = array_elements.getJSONObject(0);
                                Log.d("JSON", "object_elements:" + object_elements);
                                JSONObject object_duration = object_elements.getJSONObject("duration");
                                JSONObject object_distance = object_elements.getJSONObject("distance");

                                data.min = ((object_duration.getString("text")));
                                data.dist = ((object_distance.getString("text")));
                                Log.d("JSON", "object_duration:" + object_duration);


                            }*/


                        m_parts.add(0, data);


                        return mainResponse.getString("status");
                    }
//                        catch (JSONException e) {
//                            Log.d("error", "error3");
//                        }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


                return null;
            }
//                catch (MalformedURLException e) {
//                    Log.d("error", "error1");
//                } catch (IOException e) {
//                    Log.d("error", "error2");
//                } catch (JSONException e) {
//                    Log.d("error", "error3");
//                }
            //              return null;
        //}


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);
                displayData();

            }
        }
        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }


    private void displayData() {

        m_adapter = new AllAvailableJobsAdapterContractor(m_parts,AppController.getAppContext());
        listView.setAdapter(m_adapter);
        m_adapter.notifyDataSetChanged();


        Log.e("ERROR", "In displayData call");
        Log.i("Info", "Inside displaydata");
        //Result rItem= resultlist.get(i);
        //Log.i("INFO", response);
        // responseView.setText(response);
        // ProgressBar.dismiss();
    }


    public void openCreateFragment()
    {
        Fragment fr = new CreateBidJobPost();
        FragmentChangeListener fc=(FragmentChangeListener)getActivity();
        fc.replaceFragment(fr);
    }
    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }
    private void checkIfAvailableForBid(final int id, final AllAvailableJobsClass project) {
        class BidResponse extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;
            TextView txtPrompt;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();

                params.put("project_id", String.valueOf(id));

                BidsByContractirClass data = null;

                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_CHECK_IF_BID_POSSIBLE, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray bidsData = (JSONArray) mainResponse.get("data");

                    for(int i = 0; i < bidsData.length(); i++) {
                        JSONObject bids = (JSONObject) bidsData.get(i);

                        data = new BidsByContractirClass();

                        data.status = bids.getString("status");


                        return data.status;
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                return "Not Added";
            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion

                if(s.equals("Open")||s.equals("Not Added")  ) {
                    Fragment fragment = new ContractorViewOfPostedJob();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    Bundle b = new Bundle();
                    b.putSerializable("projectObj", (project));
                    b.putString("project_id", String.valueOf(id));
                    fragment.setArguments(b);
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();


                }
                else{
                    Toast.makeText(getActivity(), "Can Not Place Bid", Toast.LENGTH_SHORT).show();

                }


            }
        }
        //executing the async task
        BidResponse ru = new BidResponse();
        ru.execute();
    }
    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getActivity());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result,
                        0).show();
            }
            return false;
        }
        return true;
    }






    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();


        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(AppController.getAppContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        origin = latitude+","+longitude;
        if(addresses!=null) {
            address = addresses.get(0).getAddressLine(0);
        }// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        if(address==null){
            Toast.makeText(getActivity(), "Try Again, Error getting Address", Toast.LENGTH_SHORT);
        }
        if(addresses==null){
            Toast.makeText(AppController.getAppContext(), "Oops! Slow Internet! Could not get Location!", Toast.LENGTH_SHORT).show();
        }
        else {
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        }
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");
        tvLatlong.setText(address);
        dialog.hide();
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        //  mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
}

