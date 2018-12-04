package professional.service.corporate.com.atcleaningapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PostABidFragment extends Fragment {
    View view;
    Spinner dropdown;

    TextView attach;

    TextView post_msg;

    ArrayList<String> booldata;
    TextView buttonPlaceBid;


    public SQLiteDatabase db;
    EditText feet;
    EditText inches;
    String skills;
    static final String CALENDAR = "calendar";
    ArrayAdapter<String> adapter;
    // ArrayList<String> data;
    boolean status = false;
    int a;
    TextView next;

    String user;


    TextView title;
    EditText title_input;
    EditText description_input;
    Spinner dropdown_bathrooms;
    Spinner soil_level;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private SharedPreferences sharedPref3;
    EditText input_proposal;
    EditText support_input;
    EditText client_info;
    EditText bid_amount_entered;
    EditText bid_days_entered;
    EditText milestone_proposal;
    private AllAvailableJobsClass relatedProject;
    private SessionManager sm;
    int bidder_id;
    int cont_id;
    int project_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.post_a_bid, container, false);
        sharedPref3 = this.getSharedpreferences3();
        project_id =  getArguments().getInt("project_id",0);
       // project_hirer_id = String.valueOf(relatedProject.get());
        input_proposal = (EditText) view.findViewById(R.id.input_proposal);
        support_input = (EditText) view.findViewById(R.id.support_input);
        client_info = (EditText) view.findViewById(R.id.client_info);
        bid_amount_entered = (EditText) view.findViewById(R.id.bid_amt_entered);
        bid_days_entered = (EditText) view.findViewById(R.id.bid_days_entered);
        milestone_proposal = (EditText) view.findViewById(R.id.milestone_proposal);
        buttonPlaceBid = (TextView)view.findViewById(R.id.post_now);

        ArrayList<LoginDBClass> lac;

        sm = new SessionManager(getActivity());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            cont_id = Integer.parseInt(f.getUser_id());
        }
        bidder_id = cont_id;
        //Requesting storage permission
        requestStoragePermission();


        buttonPlaceBid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                post_a_bid();

                Intent i = new Intent(getActivity(), SuccessActivty1.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);


            }
        });
        return view;
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to getActivity() block
            //Here you can explain why you need getActivity() permission
            //Explain here why you need getActivity() permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //getActivity() method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity(), "Reading Storage Available!", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void post_a_bid() {


        final String input_proposal1 = input_proposal.getText().toString().trim();
        final String support_input1 = support_input .getText().toString().trim();
        final String client = client_info.getText().toString();
        final String bid_amount_entered1 = bid_amount_entered.getText().toString();
        final String  bid_days_entered1 = bid_days_entered.getText().toString();
        final String milestone_proposal = input_proposal.getText().toString();
        final int taxable1 = 0;
        final int tax_percent1 = 0;


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        final String post_date = sdf.format(c.getTime());//String.valueOf(new Date());
        c.add(Calendar.DATE, 7);
        final String expiry_date = sdf.format(c.getTime());

        final int hirer_id = (sharedPref3.getInt(getResources().getString(R.string.user_id), 0));
        if (input_proposal.getText().toString().trim().equalsIgnoreCase("") || client_info.getText().toString().trim().equalsIgnoreCase("") || client_info.getText().toString().trim().equalsIgnoreCase("") ||bid_amount_entered.getText().toString().trim().equalsIgnoreCase("") ||bid_days_entered.getText().toString().trim().equalsIgnoreCase("") ||bid_days_entered.getText().toString().trim().equalsIgnoreCase("") ) {
            if (input_proposal.getText().toString().trim().equalsIgnoreCase(""))
                input_proposal.setError("Please Enter Project Name!!!");
            if (client_info.getText().toString().trim().equalsIgnoreCase(""))
                client_info.setError("Please Enter Description!!!");

            if (support_input.getText().toString().trim().equalsIgnoreCase(""))
                support_input.setError("Please Enter Description!!!");
            if (bid_amount_entered.getText().toString().trim().equalsIgnoreCase(""))
                bid_amount_entered.setError("Please Enter Description!!!");
            if (bid_days_entered.getText().toString().trim().equalsIgnoreCase(""))
                bid_days_entered.setError("Please Enter Description!!!");
//            if (milestone_proposal.getText().toString().trim().equalsIgnoreCase(""))
//                milestone_proposal.setError("Please Enter Description!!!");
            return;
        } else {
            input_proposal.setError(null);
            support_input.setError(null);
            bid_amount_entered.setError(null);
            bid_days_entered.setError(null);
            client_info.setError(null);


            class PostAProjectManager extends AsyncTask<Void, Void, String> {

                private ProgressBar progressBar;

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    String bid_status = "Bid Placed";
                    int bidder = bidder_id;
                    int current_project = project_id;
                    params.put("input_proposal", input_proposal1);
                    params.put("support_input",support_input1);
                    params.put("client", client);
                    params.put("bid_amount_entered", bid_amount_entered1);
                    params.put("bid_days_entered", bid_days_entered1);
                    params.put("milestone_proposal",  milestone_proposal);
                    params.put("project_id",  String.valueOf(current_project));
                    params.put("bidder_id", String.valueOf(bidder));
                    params.put("bid_status", bid_status);
                    params.put("taxable", String.valueOf(taxable1));
                    params.put("tax_percent", String.valueOf(tax_percent1));
                    try {
                        JSONObject response = requestHandler.sendPostRequest(URLs.URL_POST_BID, params, Request.Method.POST);


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
                    progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        JSONObject apiResponse = (JSONObject) obj.get("response");

                        if (apiResponse.getInt("status") != 200) {
                            Toast.makeText(getActivity(), apiResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            return;
                        }

                      //  JSONObject data = (JSONObject) apiResponse.get("data");
                     //   int project_id = data.getInt("project_id");


                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            //executing the async task
            PostAProjectManager ru = new PostAProjectManager();
            ru.execute();
        }

    }

    private SharedPreferences getSharedpreferences3() {

        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.user_id), Context.MODE_PRIVATE);

        return sharedPref;


    }

    public class ImageListAdapter extends BaseAdapter {
        private Context context;
        private List<String> imgPic;

        public ImageListAdapter(Context c, List<String> thePic) {
            context = c;
            imgPic = thePic;
        }

        public int getCount() {
            if (imgPic != null)
                return imgPic.size();
            else
                return 0;
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        //---returns an ImageView view---
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            BitmapFactory.Options bfOptions = new BitmapFactory.Options();
            bfOptions.inDither = false;                     //Disable Dithering mode
            bfOptions.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
            bfOptions.inInputShareable = true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
            bfOptions.inTempStorage = new byte[32 * 1024];
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }
            FileInputStream fs = null;
            Bitmap bm;
            try {
                fs = new FileInputStream(new File(imgPic.get(position).toString()));

                if (fs != null) {
                    bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
                    imageView.setImageBitmap(bm);
                    imageView.setId(position);
                    imageView.setLayoutParams(new GridView.LayoutParams(200, 160));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fs != null) {
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return imageView;
        }
    }



}

