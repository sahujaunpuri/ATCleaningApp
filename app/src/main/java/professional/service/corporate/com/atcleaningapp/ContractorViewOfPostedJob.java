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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import org.json.JSONArray;
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

public class ContractorViewOfPostedJob extends Fragment {
    View view;
    Spinner dropdown;

    TextView attach;

    TextView post_msg;

    ArrayList<String> booldata;
    TextView buttonSave;
    private GridView lnrImages;
    private Button btnAddPhots;
    private Button btnSaveImages;
    private ArrayList<String> imagesPathList;
    private Bitmap yourbitmap;
    private Bitmap resized;
    private final int PICK_IMAGE_MULTIPLE = 1;

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
    private RadioButton hirer;
    private RadioButton contractor;
    private boolean radioHirer;
    private boolean radioContractor;
    private Spinner dropdown1;
    String user;
    private RadioGroup radioSexGroup;
    public static final String UPLOAD_URL = "http://sell4masaricom.ipage.com/contractors/php_web_service/public_html/upload.php";
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Declaring views
    private Button buttonChoose;
    private Button buttonUpload;
    private EditText editText;
    ImageView imageView;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;
    //Pdf request code
    private int PICK_PDF_REQUEST1 = 1;
    private int PICK_PDF_REQUEST2 = 2;
    private int PICK_PDF_REQUEST3 = 3;
    private int PICK_PDF_REQUEST4 = 4;
    private int PICK_PDF_REQUEST5 = 5;
    private int PICK_PDF_REQUEST6 = 6;
    //Uri to store the image uri
    private TextView budgetTv;
    private TextView hire_rateTv;
    private Uri filePath;
    private Uri filePath1;
    private Uri filePath2;
    private Uri filePath3;
    private Uri filePath4;
    private Uri filePath5;
    private SharedPreferences sharedPref3;
    EditText price_input;
    EditText price_input2;
    int by_hour = 0;
    String data;
    String input;
    TextView hire_local;
    ImageView location1;

    TextView title;
    TextView title_input;
    TextView description_input;
    TextView size_of_house_inputted;
    TextView soil_level_inputted;
    TextView bathroom_inputted;
    TextView job_status;
    int project_id;
    AllAvailableJobsClass aj;
    TextView attached_files;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.contractors_view_of_job_post, container, false);

        job_status = (TextView)view.findViewById(R.id.job_status_des) ;
        title = (TextView) view.findViewById(R.id.title);
        title_input = (TextView) view.findViewById(R.id.title_inputted);
        description_input = (TextView) view.findViewById(R.id.description_inputted);
        attach = (TextView) view.findViewById(R.id.attach);
        buttonSave = (TextView) view.findViewById(R.id.bid_now);
        size_of_house_inputted = (TextView) view.findViewById(R.id.size_of_house_inputted);
        soil_level_inputted = (TextView) view.findViewById(R.id.soil_level_inputted);
        bathroom_inputted = (TextView) view.findViewById(R.id.bathrooms_inputted);
        attached_files = (TextView)view.findViewById(R.id.attached_files);
        Bundle b = getArguments();
        aj = (AllAvailableJobsClass)b.getSerializable("projectObj");
        project_id = aj.getId();
        getProjects();
 

        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               
//                Intent i = new Intent(getActivity(), SuccessActivty.class);
//                startActivity(i);
//                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                int current_project = project_id;
                Fragment fragment = new PostABidFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment, fragment.toString());
                Bundle b = new Bundle();
                b.putInt("project_id", current_project);
                fragment.setArguments(b);
                fragmentTransaction.addToBackStack(fragment.toString());
                fragmentTransaction.commit();


            }
        });
        attached_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current_project = project_id;
                Fragment fragment = new AttachedImagesForProject();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment, fragment.toString());
                Bundle b = new Bundle();
                b.putInt("project_id", current_project);
                fragment.setArguments(b);
                fragmentTransaction.addToBackStack(fragment.toString());
                fragmentTransaction.commit();
            }
        });
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadMultipart(int project_id) {
        //getting name for the image
        // for(int i = 0; i<6; i++){
        String name = "No";

        //getting the actual path of the image
        String path = FilePath.getPath(getActivity(), filePath);
        final int hirer_id = (sharedPref3.getInt(getResources().getString(R.string.user_id), 0));
        if (path == null) {//path.equals(null) || path.equals("")) {
            Log.i("Sourav, Path Value", "When Null");
            Toast.makeText(getActivity(), "Please Select A File To Upload First", Toast.LENGTH_LONG).show();
        } else {

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();
                Log.i("Sourav, Path Value", "When NOT Null");
                //Creating a multi part request
                new MultipartUploadRequest(getActivity(), uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("project_id", String.valueOf(project_id))
                        .addParameter("hirer_id", String.valueOf(hirer_id))
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();//Starting the upload
                Log.i("Sourav, Path Value", "When NOT Null2");
            } catch (Exception exc) {
                Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
            //  }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void uploadMultipart1(int project_id) {
        //getting name for the image
        // for(int i = 0; i<6; i++){
        String name = "No";

        //getting the actual path of the image
        String path = FilePath.getPath(getActivity(), filePath1);
        final int hirer_id = (sharedPref3.getInt(getResources().getString(R.string.user_id), 0));
        if (path == null) {//path.equals(null) || path.equals("")) {
            Log.i("Sourav, Path Value", "When Null");
            Toast.makeText(getActivity(), "Please Select A File To Upload First", Toast.LENGTH_LONG).show();
        } else {

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();
                Log.i("Sourav, Path Value", "When NOT Null");
                //Creating a multi part request
                new MultipartUploadRequest(getActivity(), uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("project_id", String.valueOf(project_id))
                        .addParameter("hirer_id", String.valueOf(hirer_id))
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();//Starting the upload
                Log.i("Sourav, Path Value", "When NOT Null2");
            } catch (Exception exc) {
                Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
            //  }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadMultipart2(int project_id) {
        //getting name for the image
        // for(int i = 0; i<6; i++){
        String name = "No";

        //getting the actual path of the image
        String path = FilePath.getPath(getActivity(), filePath2);
        final int hirer_id = (sharedPref3.getInt(getResources().getString(R.string.user_id), 0));
        if (path == null) {//path.equals(null) || path.equals("")) {
            Log.i("Sourav, Path Value", "When Null");
            Toast.makeText(getActivity(), "Please Select A File To Upload First", Toast.LENGTH_LONG).show();
        } else {

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();
                Log.i("Sourav, Path Value", "When NOT Null");
                //Creating a multi part request
                new MultipartUploadRequest(getActivity(), uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("project_id", String.valueOf(project_id))
                        .addParameter("hirer_id", String.valueOf(hirer_id))
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();//Starting the upload
                Log.i("Sourav, Path Value", "When NOT Null2");
            } catch (Exception exc) {
                Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
            //  }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadMultipart3(int project_id) {
        //getting name for the image
        // for(int i = 0; i<6; i++){
        String name = "No";

        //getting the actual path of the image
        String path = FilePath.getPath(getActivity(), filePath3);
        final int hirer_id = (sharedPref3.getInt(getResources().getString(R.string.user_id), 0));
        if (path == null) {//path.equals(null) || path.equals("")) {
            Log.i("Sourav, Path Value", "When Null");
            Toast.makeText(getActivity(), "Please Select A File To Upload First", Toast.LENGTH_LONG).show();
        } else {

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();
                Log.i("Sourav, Path Value", "When NOT Null");
                //Creating a multi part request
                new MultipartUploadRequest(getActivity(), uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("project_id", String.valueOf(project_id))
                        .addParameter("hirer_id", String.valueOf(hirer_id))
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();//Starting the upload
                Log.i("Sourav, Path Value", "When NOT Null2");
            } catch (Exception exc) {
                Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
            //  }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadMultipart4(int project_id) {
        //getting name for the image
        // for(int i = 0; i<6; i++){
        String name = "No";

        //getting the actual path of the image
        String path = FilePath.getPath(getActivity(), filePath4);
        final int hirer_id = (sharedPref3.getInt(getResources().getString(R.string.user_id), 0));
        if (path == null) {//path.equals(null) || path.equals("")) {
            Log.i("Sourav, Path Value", "When Null");
            Toast.makeText(getActivity(), "Please Select A File To Upload First", Toast.LENGTH_LONG).show();
        } else {

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();
                Log.i("Sourav, Path Value", "When NOT Null");
                //Creating a multi part request
                new MultipartUploadRequest(getActivity(), uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("project_id", String.valueOf(project_id))
                        .addParameter("hirer_id", String.valueOf(hirer_id))
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();//Starting the upload
                Log.i("Sourav, Path Value", "When NOT Null2");
            } catch (Exception exc) {
                Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
            //  }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadMultipart5(int project_id) {
        //getting name for the image
        // for(int i = 0; i<6; i++){
        String name = "No";

        //getting the actual path of the image
        String path = FilePath.getPath(getActivity(), filePath5);
        final int hirer_id = (sharedPref3.getInt(getResources().getString(R.string.user_id), 0));
        if (path == null) {//path.equals(null) || path.equals("")) {
            Log.i("Sourav, Path Value", "When Null");
            Toast.makeText(getActivity(), "Please Select A File To Upload First", Toast.LENGTH_LONG).show();
        } else {

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();
                Log.i("Sourav, Path Value", "When NOT Null");
                //Creating a multi part request
                new MultipartUploadRequest(getActivity(), uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("project_id", String.valueOf(project_id))
                        .addParameter("hirer_id", String.valueOf(hirer_id))
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();//Starting the upload
                Log.i("Sourav, Path Value", "When NOT Null2");
            } catch (Exception exc) {
                Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
            //  }
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        // intent.setType("image/*|application/pdf|audio/*");
        intent.setType("*/*");
        // intent = new Intent(getActivity(),CustomPhotoGalleryActivity.class);
        // startActivityForResult(intent,PICK_IMAGE_MULTIPLE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST1);

    }

    private void showFileChooser1() {
        Intent intent = new Intent();

        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST2);
    }

    private void showFileChooser2() {
        Intent intent = new Intent();

        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST3);
    }

    private void showFileChooser3() {
        Intent intent = new Intent();

        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST4);
    }

    private void showFileChooser4() {
        Intent intent = new Intent();

        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST5);
    }

    private void showFileChooser5() {
        Intent intent = new Intent();

        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST6);
    }



    private void getProjects() {



        class RegisterUser extends AsyncTask<Void, Void, AllAvailableJobsClass> {


            private ProgressBar progressBar;

            @Override
            protected AllAvailableJobsClass doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                int current_project = project_id;
                params.put("project_id", String.valueOf(current_project));
                AllAvailableJobsClass data=new AllAvailableJobsClass();

                try {
                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_JOB_VIEW_FOR_CONTRACTR, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for(int i = 0; i < projectData.length(); i++){
                        JSONObject project = (JSONObject) projectData.get(i);


                        data.slNo = i + 1;
                        //data.projectId = project.getInt("id");
                        data.title = project.getString("title");
                        data.description = project.getString("description");
                        data.post_date= project.getString("post_date");
                        data.bathrooms = project.getString("bathrooms");
                        data.size_of_rooms = project.getString("size_of_house");
                        data.soil_level = project.getString("soil_level");
                        data.status = project.getString("state");



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
                progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(AllAvailableJobsClass s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

               title_input.setText(s.title);
               description_input.setText(s.description);
               bathroom_inputted.setText(s.bathrooms);
               size_of_house_inputted.setText(s.size_of_rooms);
               soil_level_inputted.setText(s.soil_level);
               job_status.setText("Job Post Status "+s.status);

            }
        }
        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }


}

