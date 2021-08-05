package motor.mechanic.finder.fyp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import motor.mechanic.finder.fyp.Mechanic_Signup;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.WorkshopSharedClass;
import motor.mechanic.finder.fyp.User_Signup;


public class WorkshopChoiceActivity extends AppCompatActivity {
    Toolbar toolbar;
    CardView mechanic_cardview, user_cardview, myempcard;
    TextView workshop_toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_choice);

        initWidgets();
        buttonListener();
    }

    private void buttonListener() {
        user_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkshopChoiceActivity.this, Workshop_Requests.class);
                startActivity(intent);
                finish();
            }
        });
        mechanic_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkshopChoiceActivity.this, Mechanic_Signup.class);
                WorkshopSharedClass.addMechanicCheck = "through_workshop";
                startActivity(intent);
            }
        });
        myempcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkshopChoiceActivity.this, MyEmployees.class);
                startActivity(intent);
            }
        });
    }

    private void initWidgets() {
        myempcard = findViewById(R.id.myempcard);
        user_cardview = findViewById(R.id.view_requests_cardview);
        mechanic_cardview = findViewById(R.id.add_mechanic_cardview);
        toolbar = findViewById(R.id.toolbar);
        workshop_toolbar_title = findViewById(R.id.workshop_toolbar_title);
        workshop_toolbar_title.setText(WorkshopSharedClass.workshopsData.get(0).getWorkshop_fullname());
    }


}
