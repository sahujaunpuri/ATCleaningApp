package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class UploadProfilePIc extends AppCompatActivity implements View.OnClickListener {
    public static final String UPLOAD_URL = "http://urzom.com/androidapp/upload1.php";

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Declaring views
    private Button buttonChoose;
    private Button buttonUpload;
    private EditText editText;
    //Pdf request code
    private int PICK_PDF_REQUEST = 1;
    //Uri to store the image uri
    private Uri filePath;
    private Button upload;
    SessionManager sm;
    ArrayList<LoginDBClass> lac;
    int cont_id;
    int user_id;
    SharedPreferences sharedPref1;
    String sharedPrefKey;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_dp);
        sharedPrefKey = getResources().getString(R.string.user_type_frag_preference_file_key);
        requestStoragePermission();
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonChoose.setOnClickListener(this);
        sharedPref1 = getSharedpreferences();
        upload = (Button) findViewById(R.id.upload);
        editText = (EditText) findViewById(R.id.editTextName);
        sm = new SessionManager(getApplicationContext());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            cont_id = Integer.parseInt(f.getUser_id());
        }
        user_id = cont_id;
        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                // setRowIdFromIntent();
                //saveState();

                uploadMultipart();



            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadMultipart() {
        //getting name for the image
        // String name = editText.getText().toString().trim();

        //getting the actual path of the image
        String path = FilePath.getPath(UploadProfilePIc.this, filePath);
        TextView prompt = (TextView) findViewById(R.id.prompt);
        String name = "nothing";
        if (path == null) {
            prompt.setVisibility(View.VISIBLE);
            Toast.makeText(UploadProfilePIc.this, "Please Select An Image And retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            prompt.setVisibility(View.GONE);
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(UploadProfilePIc.this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("user_id", String.valueOf(user_id))
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();//Starting the upload

            } catch (Exception exc) {
                Toast.makeText(UploadProfilePIc.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }

            if ((sharedPref1.getString(sharedPrefKey, "")).toLowerCase().equals("User")) {
                Intent i = new Intent(UploadProfilePIc.this, NavigationBarUser.class);

                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
            if ((sharedPref1.getString(sharedPrefKey, "")).toLowerCase().equals("NavigationBarContractors")) {
                Intent i = new Intent(UploadProfilePIc.this, NavigationBarContractors.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

        }
    }

    //method to show file chooser
    private void showFileChooser() {

        Intent intent = new Intent();
        // intent.setType("image/*|application/pdf|audio/*");
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            File f = new File(String.valueOf(filePath));

            String imageName = f.getName();
            Log.i("Sourav, Image NAme", "Gallery" + imageName);
            ((ImageView) findViewById(R.id.imgView)).setImageURI(filePath);
        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(UploadProfilePIc.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(UploadProfilePIc.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to UploadProfilePIc.this block
            //Here you can explain why you need UploadProfilePIc.this permission
            //Explain here why you need UploadProfilePIc.this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(UploadProfilePIc.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //UploadProfilePIc.this method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(UploadProfilePIc.this, "Reading Storage Available!", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(UploadProfilePIc.this, "permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        // if (v == buttonSave) {
        //  uploadMultipart();
        // }
    }


    private SharedPreferences getSharedpreferences() {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.user_type_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;


    }


}
