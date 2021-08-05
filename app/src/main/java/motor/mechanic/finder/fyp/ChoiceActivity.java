package motor.mechanic.finder.fyp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import motor.mechanic.finder.fyp.UI.InsertNewProduct_UI;
import motor.mechanic.finder.fyp.UI.WorkshopRegistration;


public class ChoiceActivity extends AppCompatActivity {
    Toolbar toolbar;
    CardView automart_cardview, mechanic_cardview, user_cardview;
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        initWidgets();
        buttonListener();
    }

    private void buttonListener() {
       user_cardview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(ChoiceActivity.this, User_Signup.class);
               startActivity(intent);
           }
       });
        mechanic_cardview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(ChoiceActivity.this, Mechanic_Signup.class);
               startActivity(intent);
           }
       });

        automart_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ChoiceActivity.this, InsertNewProduct_UI.class);
                Intent intent = new Intent(ChoiceActivity.this, WorkshopRegistration.class);
                startActivity(intent);
//                Toast.makeText(ChoiceActivity.this, "no automart class", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initWidgets() {
        user_cardview = findViewById(R.id.user_cardview);
        mechanic_cardview = findViewById(R.id.mechanic_cardview);
        automart_cardview = findViewById(R.id.automart_cardview);
        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
    }


}
