package professional.service.corporate.com.atcleaningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class UsePhoto4 extends AppCompatActivity {

    ImageView use_photo;
    String result;
    TextView no;
    TextView use;
    String no1 = "no";
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_photo);
        use_photo = (ImageView) findViewById(R.id.use_photo);
        use = (TextView)findViewById(R.id.use);
        no = (TextView)findViewById(R.id.no);
        final String filepath = getIntent().getExtras().getString("filePath4");
        Picasso.with(AppController.getAppContext()).load(filepath).into(use_photo);
        use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",filepath.toString());
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",no1);
                setResult(RESULT_CANCELED,returnIntent);
                finish();
            }
        });

    }
}
