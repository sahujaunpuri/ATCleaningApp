package professional.service.corporate.com.atcleaningapp;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddAddressByGooglePlace  extends Fragment implements
    GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks {
    View view;
    EditText building_no;
    EditText apartment_name;
    EditText street_name1;
    EditText street_name2;
    EditText landmark;
    EditText city_name;
    EditText zip_code;
    TextView save_address;
    private ProgressBar mProgressBar;
    int user_id;
    private String address;
    double latitude;
    double longitude;
    String city;
    String state;
    String clinic_name;
    EditText et_message;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private TextView Tv_Message;
    AutoCompleteTextView mAutocompleteTextView;
    String origin;
    LatLng latLng;
    private static final String LOG_TAG = "MainActivity";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    private GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.create_address_place, container, false);
        Bundle b = getArguments();
        user_id = b.getInt("user_id");
        building_no = (EditText)view.findViewById(R.id.building_no);
 //       apartment_name = (EditText)view.findViewById(R.id.apartment_name);
//        street_name1 = (EditText)view.findViewById(R.id.street_name1);
//        street_name2  = (EditText)view.findViewById(R.id.street_name2);
        landmark = (EditText)view.findViewById(R.id.landmark);
    //    city_name = (EditText)view.findViewById(R.id.city_name);
        zip_code = (EditText)view.findViewById(R.id.zip_code);
        save_address = (TextView)view.findViewById(R.id.save_address);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.apartment_name);
        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        save_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAddress();
            }
        });
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        return view;
    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());

                return;
            }
            final Place myPlace = places.get(0);
            LatLng queriedLocation = myPlace.getLatLng();
            clinic_name = myPlace.getName().toString();
            Log.i("Sourav, Latitude", "lat"+queriedLocation.latitude);
            Log.i("Sourav, Longitude", "lat"+queriedLocation.longitude);
            latitude = queriedLocation.latitude;
            longitude = queriedLocation.longitude;

            latLng = new LatLng(queriedLocation.latitude, queriedLocation.longitude);
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
            origin = latitude+","+longitude;
            // clinic_name = addresses.get(0).getAdminArea();
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();

            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

//            mNameTextView.setText(Html.fromHtml(place.getName() + ""));
//            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
//            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
//            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
//            mWebTextView.setText(place.getWebsiteUri() + "");
//            if (attributions != null) {
//                mAttTextView.setText(Html.fromHtml(attributions.toString()));
//            }
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(getActivity(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }
    private void saveAddress() {
        final String building = building_no.getText().toString().trim();
        final String apartment = mAutocompleteTextView.getText().toString().trim();

        final String landmark1 = landmark.getText().toString().trim();

        final String zip = zip_code.getText().toString().trim();
        final String address1 = address;
        final String city1 = city;
        final String state1 = state;
        final double latitude1 = latitude;
        final double longitude1 = longitude;


//        if (!(building_no.getText().toString().trim().equalsIgnoreCase("")) || (apartment_name.getText().toString().trim().equalsIgnoreCase("")) || (street_name1.getText().toString().trim().equalsIgnoreCase("")) || (street_name2.getText().toString().trim().equalsIgnoreCase("")) || (landmark.getText().toString().trim().equalsIgnoreCase("")) || (city_name.getText().toString().trim().equalsIgnoreCase("")) || (zip_code.getText().toString().trim().equalsIgnoreCase(""))) {
//
//            if (building_no.getText().toString().trim().equalsIgnoreCase(""))
//                building_no.setError("Please Enter A Valid Address");
//            if (apartment_name.getText().toString().trim().equalsIgnoreCase(""))
//                apartment_name.setError("Please Enter A Valid Address");
//            if (street_name1.getText().toString().trim().equalsIgnoreCase(""))
//                street_name1.setError("Please Enter A Valid Address");
//            if (street_name2.getText().toString().trim().equalsIgnoreCase(""))
//                street_name2.setError("Please Enter A Valid Address");
//            if (landmark.getText().toString().trim().equalsIgnoreCase(""))
//                landmark.setError("Please Enter A Valid Address");
//            if (city_name.getText().toString().trim().equalsIgnoreCase(""))
//                city_name.setError("Please Enter A Valid Address");
//            if (zip_code.getText().toString().trim().equalsIgnoreCase(""))
//                zip_code.setError("Please Enter A Valid Address");
//            return;
//        } else {
        building_no.setError(null);
        mAutocompleteTextView.setError(null);
        landmark.setError(null);
        zip_code.setError(null);


        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;
            String apiResponseMessage;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());


                HashMap<String, String> params = new HashMap<>();
                int current_user = user_id;
                params.put("building", building);
                params.put("apartment", apartment);
                params.put("street1", address1);
                params.put("state", state1);
                params.put("landmark", landmark1);
                params.put("city", city1);
                params.put("zip", zip);
                params.put("user_id", String.valueOf(current_user));
                params.put("latitude", String.valueOf(latitude1));
                params.put("longitude", String.valueOf(longitude1));

                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_ADD_ADDRESS, params, Request.Method.POST);
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
                mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), apiResponseMessage, Toast.LENGTH_LONG).show();
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    JSONObject apiResponse = (JSONObject) obj.get("response");

                    if (apiResponse.getInt("status") != 200) {
                        Toast.makeText(getActivity(), apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //JSONObject data = (JSONObject) apiResponse.get("data");

                    // Toast.makeText(UserRegister.this, apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                    Fragment fragment = new CreateBidJobPost();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    Bundle b = new Bundle();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

//executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();


    }
}


