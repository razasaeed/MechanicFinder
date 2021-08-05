package motor.mechanic.finder.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.Random;

import static motor.mechanic.finder.fyp.R.drawable.ic_camera_alt_black_24dp;

public class Worker_Detail extends AppCompatActivity {

    TextView nameDetailTextView,CNICDetailTextView,phoneDetailTextView, ExpDetailTextView;
    ImageView workerDetailImageView;

    private void initializeWidgets(){
        nameDetailTextView= findViewById(R.id.tvWorkerName);
        CNICDetailTextView= findViewById(R.id.tvWorkerCNIC);
        phoneDetailTextView= findViewById(R.id.tvPhoneNo);
        ExpDetailTextView= findViewById(R.id.tvWExp);
        workerDetailImageView=findViewById(R.id.worker_img);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker__detail);

        initializeWidgets();

        //RECEIVE DATA FROM ITEMSACTIVITY VIA INTENT
        Intent i=this.getIntent();
        String imageURL =i.getExtras().getString("IMAGE_KEY");
        String name=i.getExtras().getString("NAME_KEY");
        String cnic=i.getExtras().getString("CNIC_KEY");
        String phone=i.getExtras().getString("PHONE_KEY");
        String exp=i.getExtras().getString("EXP_KEY");


        //SET RECEIVED DATA TO TEXTVIEWS AND IMAGEVIEWS
        nameDetailTextView.setText(name);
        CNICDetailTextView.setText(cnic);
        phoneDetailTextView.setText(phone);
        ExpDetailTextView.setText(exp);
        Picasso.with(this)
                .load(imageURL)
                .placeholder(R.drawable.ic_camera_alt_black_24dp)
                .centerCrop()
                .fit()
                .into(workerDetailImageView);
    }
}
