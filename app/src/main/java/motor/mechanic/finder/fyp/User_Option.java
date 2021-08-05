package motor.mechanic.finder.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class User_Option extends AppCompatActivity {

    Button btnSignOut, btnMap, btnMart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__option);

        initWidgets();
        buttonListener();
    }

    private void buttonListener() {
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_Option.this, User_Signup.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_Option.this, User_MapsActivity.class);
                startActivity(intent);
            }
        });

        btnMart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(User_Option.this, AutoMart.class);
//                startActivity(intent);
                Toast.makeText(User_Option.this, "AutoMart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initWidgets() {
        btnMap = findViewById(R.id.btnMap);
        btnSignOut = findViewById(R.id.btnSignOut);
        btnMart = findViewById(R.id.btnMart);
    }
}
