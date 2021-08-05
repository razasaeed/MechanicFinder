package motor.mechanic.finder.fyp;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import motor.mechanic.finder.fyp.SharedData.UserSharedClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User_Name extends AppCompatActivity {

    EditText etFullName;
    Button btnContinue;
    TextView tvName, tv2;
    TextInputLayout textInputLayoutName;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__name);

        initWidgets();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/");
        myRef = mFirebaseDatabase.getReference("User_Signup");
        buttonListener();

    }

    private void buttonListener() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stname = etFullName.getText().toString();
                if (!stname.equals("")) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                }
                Intent intent = new Intent(User_Name.this, User_Password.class);
                UserSharedClass.username = etFullName.getText().toString();
                startActivity(intent);
            }
        });
    }

    private void initWidgets() {
        etFullName = findViewById(R.id.etFullName);
        btnContinue = findViewById(R.id.btnContinue);
        tvName = findViewById(R.id.tvName);
        tv2 = findViewById(R.id.tv2);
        textInputLayoutName = findViewById(R.id.textInputFullName);
    }
}
