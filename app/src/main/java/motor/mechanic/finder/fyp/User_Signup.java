package motor.mechanic.finder.fyp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import motor.mechanic.finder.fyp.SharedData.UserSharedClass;
import motor.mechanic.finder.fyp.UI.UserLogin_UI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class User_Signup extends AppCompatActivity {
    EditText etPhoneNo;
    Button btnContinue;
    String code, number, phno;
    TextInputLayout textInputLayoutPhoneNo;
    TextView tvEnter, tv2;
    CountryCodePicker countryCodePicker;
    FirebaseAuth mAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__signup);

        initWidgets();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/");
        myRef = mFirebaseDatabase.getReference();
        buttonListener();
        countryCodePicker.registerCarrierNumberEditText(etPhoneNo);
    }

    private void buttonListener() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                number = etPhoneNo.getText().toString().trim();
                if (number.isEmpty() || number.length() < 10) {

                    etPhoneNo.setError("Enter a valid Phone Number!");
                    etPhoneNo.requestFocus();
                    return;
                }
                UserSharedClass.phone = countryCodePicker.getFullNumberWithPlus();
                phno = countryCodePicker.getFullNumberWithPlus();

                Intent intent = new Intent(User_Signup.this, PhoneNo_Verification.class);
                intent.putExtra("phno", phno);
                startActivity(intent);
            }

        });
    }

    private void initWidgets() {
        etPhoneNo = findViewById(R.id.etPhoneNo);
        btnContinue = findViewById(R.id.btnSignup);
        textInputLayoutPhoneNo = findViewById(R.id.textInputPhoneNo);
        tvEnter = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        countryCodePicker = findViewById(R.id.ccp);

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_Signup.this, UserLogin_UI.class));
            }
        });
    }

//    protected void onStart() {
//        super.onStart();
//
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            Intent intent = new Intent(User_Signup.this, User_Option.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//
//        }
//
//    }
}
