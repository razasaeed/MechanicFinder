package motor.mechanic.finder.fyp;

import android.content.Intent;
import android.media.TimedText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Service_Detail extends AppCompatActivity {
    TextView tvSerID ,tvSerName,tvSerPrice, tvSerDesc;

    private void initializeWidgets(){
        tvSerID = findViewById(R.id.tvSerID);
        tvSerName = findViewById(R.id.tvSerName);
        tvSerPrice = findViewById(R.id.tvSerPrice);
        tvSerDesc = findViewById(R.id.tvSerDesc);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__detail);

        initializeWidgets();

        //RECEIVE DATA FROM ITEMSACTIVITY VIA INTENT
        Intent i=this.getIntent();
        String id = i.getExtras().getString("ID_KEY");
        String name = i.getExtras().getString("NAME_KEY");
        String price = i.getExtras().getString("PRICE_KEY");
        String desc = i.getExtras().getString("DESC_KEY");


        //SET RECEIVED DATA TO TEXTVIEWS AND IMAGEVIEWS
        tvSerID.setText(id);
        tvSerName.setText(name);
        tvSerPrice.setText(price);
        tvSerDesc.setText(desc);

    }
}
