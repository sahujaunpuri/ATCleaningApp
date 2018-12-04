package professional.service.corporate.com.atcleaningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class LoginSelection extends AppCompatActivity {
    TextView login;
    TextView next;
    SharedPreferences sharedPref;
    RadioGroup radioGroup;
    RadioButton patients;
    RadioButton doctor;

    RadioButton userType1;
    String selection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_selection);
        sharedPref = this.getSharedpreferences();
        next = (TextView)findViewById(R.id.next);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        patients = (RadioButton)findViewById(R.id.patients);
        doctor = (RadioButton)findViewById(R.id.doctor);

        radioGroup.check(patients.getId());
        int selectedId = radioGroup.getCheckedRadioButtonId();
        userType1 = (RadioButton) findViewById(selectedId);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(radioGroup.getCheckedRadioButtonId()!=-1){
                    int id= radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(id);
                    int radioId = radioGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
                    selection = (String) btn.getText();
                }

                SharedPreferences.Editor mEditor3 = sharedPref.edit();
                mEditor3.putString(getString(R.string.user_type_frag_preference_file_key), selection);
                mEditor3.commit();
                if(selection.equalsIgnoreCase("User")) {
                    Intent i = new Intent(LoginSelection.this, UserRegister.class);
                    startActivity(i);
                }
               else{
                    Intent i = new Intent(LoginSelection.this, ContractorRegister.class);
                    startActivity(i);
                }


            }
        });
    }
    private SharedPreferences getSharedpreferences()
    {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.user_type_frag_preference_file_key), Context.MODE_PRIVATE);

        return sharedPref;
    }
}
