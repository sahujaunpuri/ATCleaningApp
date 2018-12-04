package professional.service.corporate.com.atcleaningapp;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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

public class CreateBidJobPost extends Fragment implements View.OnClickListener{
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
    private ArrayList<AddressClass> dataModels;
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
    ArrayList<Integer> checkbox_pos = new ArrayList<>();
    TextView select;
    String  checkBox_pos_s;
    String user;
    private RadioGroup radioSexGroup;
    public static final String UPLOAD_URL = "http://urzom.com/androidapp/upload.php";
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
    private int REQUEST_CODE1= 1001;
    private int REQUEST_CODE2= 1002;
    private int REQUEST_CODE3= 1003;
    private int REQUEST_CODE4= 1004;
    private int REQUEST_CODE5= 1005;
    private int REQUEST_CODE6= 1006;
    TextView title;
    EditText title_input;
    EditText description_input;
    Spinner dropdown_bathrooms;
    Spinner soil_level;
    GridView gridView;
    ImageView add_image;
    private SessionManager sm;
    private ArrayList<LoginDBClass> lac;
    int user_id;
    int employer_id;
    ListView listView;
    ProgressDialog dialog;
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.create_fragment, container, false);
        sharedPref3 = this.getSharedpreferences3();
        sm = new SessionManager(getActivity());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            user_id = Integer.parseInt(f.getUser_id());
        }
        employer_id = user_id;
        //location1 = (ImageView) view.findViewById(R.id.location);
        title = (TextView) view.findViewById(R.id.title);
        title_input = (EditText) view.findViewById(R.id.title_input);
        description_input = (EditText) view.findViewById(R.id.description_input);
        attach = (TextView) view.findViewById(R.id.attach);
        buttonSave = (TextView) view.findViewById(R.id.post_now);
        dropdown = (Spinner) view.findViewById(R.id.spinner_size);
        dropdown_bathrooms = (Spinner) view.findViewById(R.id.spinner_bathrooms);
        soil_level = (Spinner) view.findViewById(R.id.soil_level);
        //add_image = (ImageView)view.findViewById(R.id.add_address);
        dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dataModels = new ArrayList<AddressClass>();


        //Requesting storage permission
        requestStoragePermission();

        //Initializing views
        imageView = (ImageView) view.findViewById(R.id.imgView);
        imageView2 = (ImageView) view.findViewById(R.id.imgView2);
        imageView3 = (ImageView) view.findViewById(R.id.imgView3);
        imageView4 = (ImageView) view.findViewById(R.id.imgView4);
        imageView5 = (ImageView) view.findViewById(R.id.imgView5);
        imageView6 = (ImageView) view.findViewById(R.id.imgView6);
        String[] items1 = new String[]{"Under 1500 Sqft","1500 - 1800 Sqft", "1800 - 2200 Sqft ", "2200 - 2500 Sqft", "2500 - 3000 Sqft", "3000 + Sqft"};
        String[] items2 = new String[]{"1 Full Bathroom","1 Full Bathroom, 1 Half Bathroom", "2 Full Bathroom", "2 Full Bathroom, 1 Half Bathroom", "2 Full Bathroom, 2 Half Bathroom", "3 Full Bathroom, 1 Half Bathroom", "3 Full Bathroom, 2 Half Bathroom", "4 Full Bathroom, 1 Half Bathroom", "4 Full Bathroom, 2 Half Bathroom"};
        String[] items3 = new String[]{"Regularly Cleaned","Been 4 or 5 Weeks", "It's bit Rough", "Needs A Good tough Job", "Really Dirty"};


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items1);
        dropdown.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items2);
        dropdown_bathrooms.setAdapter(adapter2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items3);
        soil_level.setAdapter(adapter3);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                post_a_job();

            }
        });

        imageView.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);



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

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            UsePhotoIntent(filePath);
            //((ImageView) findViewById(R.id.imgView)).setImageURI(filePath);
        } else if (requestCode == PICK_PDF_REQUEST2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath1 = data.getData();
            UsePhotoIntent1(filePath1);
            // ((ImageView) findViewById(R.id.imgView2)).setImageURI(filePath1);
        } else if (requestCode == PICK_PDF_REQUEST3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath2 = data.getData();
            UsePhotoIntent2(filePath2);
            // ((ImageView) findViewById(R.id.imgView3)).setImageURI(filePath2);
        } else if (requestCode == PICK_PDF_REQUEST4 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath3 = data.getData();
            UsePhotoIntent3(filePath3);
            // ((ImageView) findViewById(R.id.imgView4)).setImageURI(filePath3);
        } else if (requestCode == PICK_PDF_REQUEST5 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath4 = data.getData();
            UsePhotoIntent4(filePath4);
            // ((ImageView) findViewById(R.id.imgView5)).setImageURI(filePath4);
        } else if (requestCode == PICK_PDF_REQUEST6 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath5 = data.getData();
            UsePhotoIntent5(filePath5);
            //((ImageView) findViewById(R.id.imgView6)).setImageURI(filePath5);
        }
        else if(requestCode == REQUEST_CODE1 && resultCode == RESULT_OK )
        {
            String result=data.getStringExtra("result");
            Picasso.with(AppController.getAppContext()).load(result).fit().into(imageView);

        }
        else if(requestCode == REQUEST_CODE1 && resultCode == RESULT_CANCELED )
        {
            showFileChooser();

        }
        else if(requestCode == REQUEST_CODE2 && resultCode == RESULT_OK )
        {
            String result=data.getStringExtra("result");
            Picasso.with(AppController.getAppContext()).load(result).fit().into(imageView2);

        }
        else if(requestCode == REQUEST_CODE2 && resultCode == RESULT_CANCELED )
        {
            showFileChooser1();

        }

        else if(requestCode == REQUEST_CODE3 && resultCode == RESULT_OK )
        {
            String result=data.getStringExtra("result");
            Picasso.with(AppController.getAppContext()).load(result).fit().into(imageView3);

        }
        else if(requestCode == REQUEST_CODE3 && resultCode == RESULT_CANCELED )
        {
            showFileChooser2();

        }

        else if(requestCode == REQUEST_CODE4 && resultCode == RESULT_OK )
        {
            String result=data.getStringExtra("result");
            Picasso.with(AppController.getAppContext()).load(result).fit().into(imageView4);

        }
        else if(requestCode == REQUEST_CODE4 && resultCode == RESULT_CANCELED )
        {
            showFileChooser3();

        }

        else if(requestCode == REQUEST_CODE5 && resultCode == RESULT_OK )
        {
            String result=data.getStringExtra("result");
            Picasso.with(AppController.getAppContext()).load(result).fit().into(imageView5);

        }
        else if(requestCode == REQUEST_CODE5 && resultCode == RESULT_CANCELED )
        {
            showFileChooser4();

        }

        else if(requestCode == REQUEST_CODE6 && resultCode == RESULT_OK )
        {
            String result=data.getStringExtra("result");
            Picasso.with(AppController.getAppContext()).load(result).fit().into(imageView6);

        }
        else if(requestCode == REQUEST_CODE6 && resultCode == RESULT_CANCELED )
        {
            showFileChooser5();

        }

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
    private void post_a_job() {


        final String title_input1 = title_input.getText().toString().trim();
        final String description_input1 = description_input.getText().toString().trim();
        final String room_size = dropdown.getSelectedItem().toString();
        final String bathroom_size = dropdown_bathrooms.getSelectedItem().toString();
        final String soil_level1 = soil_level.getSelectedItem().toString();
        final String post_type = "Draft";
        final String attach = "dummy attach";
        skills = input;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        final String post_date = sdf.format(c.getTime());//String.valueOf(new Date());
        c.add(Calendar.DATE, 7);
        final String expiry_date = sdf.format(c.getTime());

        final int hirer_id = (sharedPref3.getInt(getResources().getString(R.string.user_id), 0));
        if (title_input.getText().toString().trim().equalsIgnoreCase("") || description_input.getText().toString().trim().equalsIgnoreCase("") || dropdown.getSelectedItem().toString().trim().equalsIgnoreCase("") ||dropdown_bathrooms.getSelectedItem().toString().trim().equalsIgnoreCase("") ||soil_level.getSelectedItem().toString().trim().equalsIgnoreCase("")  ) {
            if (title_input.getText().toString().trim().equalsIgnoreCase(""))
                title_input.setError("Please Enter Project Name!!!");
            if (description_input.getText().toString().trim().equalsIgnoreCase(""))
                description_input.setError("Please Enter Description!!!");
            if (dropdown.toString().trim().equalsIgnoreCase("") ) {
                //TextView txt_error = (TextView) view.findViewById(R.id.textView_error);
               // txt_error.setVisibility(View.VISIBLE);
               // txt_error.setText("Error! Please Enter A Valid Value");

                }
            if (dropdown_bathrooms.toString().trim().equalsIgnoreCase("") ) {
                //TextView txt_error = (TextView) view.findViewById(R.id.textView_error);
               // txt_error.setVisibility(View.VISIBLE);
               // txt_error.setText("Error! Please Enter A Valid value");

            }
            if (soil_level.toString().trim().equalsIgnoreCase("") ) {
               // TextView txt_error = (TextView) view.findViewById(R.id.textView_error);
              //  txt_error.setVisibility(View.VISIBLE);
              //  txt_error.setText("Error! Please Enter A Valid Value");

            }

            return;
        } else {
            title_input.setError(null);
            description_input.setError(null);



            class PostAProjectManager extends AsyncTask<Void, Void, String> {



                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                    HashMap<String, String> params = new HashMap<>();

                    params.put("title", title_input1);
                    params.put("description", description_input1);
                    params.put("room_size", room_size);
                    params.put("bathroom_size", bathroom_size);
                    params.put("soil_level", soil_level1);
                    params.put("file", attach);
                    params.put("days", "7");
                    params.put("post_date", post_date);
                    params.put("hirer_id", String.valueOf(hirer_id));
                    params.put("expiry_date",expiry_date);

                   // params.put("checkbox", selected_chk_box);
                    try {
                        JSONObject response = requestHandler.sendPostRequest(URLs.URL_POST, params, Request.Method.POST);


                        return response.toString();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    dialog.setMessage("Please Wait! Working On Background!");
                    dialog.show();
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

                        JSONObject data = (JSONObject) apiResponse.get("data");
                        int project_id = data.getInt("project_id");
                        uploadMultipart(project_id);
                        uploadMultipart1(project_id);
                        uploadMultipart2(project_id);
                        uploadMultipart3(project_id);
                        uploadMultipart4(project_id);
                        uploadMultipart5(project_id);
                        Intent i = new Intent(getActivity(), AddressList.class);

                        int current_employer = employer_id;
                        i.putExtra("user_id", current_employer);
                        i.putExtra("project_id", project_id);
                        startActivity(i);
                        if(dialog.isShowing()){
                            dialog.cancel();
                        }
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
    private void UsePhotoIntent(Uri filePath) {

        Intent i = new Intent(getActivity(), UsePhoto.class);
        i.putExtra("filePath",filePath.toString());
        startActivityForResult(i,REQUEST_CODE1);

    }
    private void UsePhotoIntent1(Uri filePath1) {

        Intent i = new Intent(getActivity(), UsePhoto1.class);
        i.putExtra("filePath1",filePath1.toString());
        startActivityForResult(i,REQUEST_CODE2);

    }
    private void UsePhotoIntent2(Uri filePath2) {

        Intent i = new Intent(getActivity(), UsePhoto2.class);
        i.putExtra("filePath2",filePath2.toString());
        startActivityForResult(i,REQUEST_CODE3);

    }
    private void UsePhotoIntent3(Uri filePath3) {

        Intent i = new Intent(getActivity(), UsePhoto3.class);
        i.putExtra("filePath3",filePath3.toString());
        startActivityForResult(i,REQUEST_CODE4);

    }
    private void UsePhotoIntent4(Uri filePath4) {

        Intent i = new Intent(getActivity(), UsePhoto4.class);
        i.putExtra("filePath4",filePath4.toString());
        startActivityForResult(i,REQUEST_CODE5);

    }
    private void UsePhotoIntent5(Uri filePath5) {

        Intent i = new Intent(getActivity(), UsePhoto5.class);
        i.putExtra("filePath5",filePath5.toString());
        startActivityForResult(i,REQUEST_CODE6);

    }

    @Override
    public void onClick(View v) {
        if (v == imageView) {
            showFileChooser();
        }
        if (v == imageView2) {
            showFileChooser1();
        }
        if (v == imageView3) {
            showFileChooser2();
        }
        if (v == imageView4) {
            showFileChooser3();
        }
        if (v == imageView5) {
            showFileChooser4();
        }
        if (v == imageView6) {
            showFileChooser5();
        }
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

