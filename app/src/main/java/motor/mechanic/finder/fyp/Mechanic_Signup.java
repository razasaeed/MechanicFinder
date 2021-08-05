package motor.mechanic.finder.fyp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import motor.mechanic.finder.fyp.SharedData.MechanicSharedClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.Arrays;

public class Mechanic_Signup extends AppCompatActivity {

    EditText etcity, etemail, etpassword, etphno;
    Button btnSignup;
    TextView tvCreate, tvAlready, tvSigninhere, tvcity, tvemail, tvphone, tvpwd;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    CountryCodePicker countryCodePicker;
    String[] listItems;
    ImageView imgback;
    private final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic__signup);

        initWidgets();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference();
        buttonListener();
        countryCodePicker.registerCarrierNumberEditText(etphno);

        // listItems = getResources().getStringArray(R.array.cities_of_PaKistan);

    }

    private void buttonListener() {
        etcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build an AlertDialog
                final AlertDialog.Builder alertDialogFuel = new AlertDialog.Builder(Mechanic_Signup.this);

                // Set a title for alert dialog
                alertDialogFuel.setTitle("CITIES");

                // add a list
                final String[] fuel = {"Islamabad", "Rawalpindi", "Karachi", "Peshawar", "Lahore", "Quetta"};
                alertDialogFuel.setItems(fuel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedText = Arrays.asList(fuel).get(which);
                        etcity.setText(selectedText);
                    }
                });
                alertDialogFuel.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alertDialogFuel.create();
                alertDialogFuel.show();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MechanicSharedClass.city = etcity.getText().toString();
                MechanicSharedClass.email = etemail.getText().toString();
//                MechanicSharedClass.phone_no = etphno.getText().toString();
                MechanicSharedClass.phone_no = countryCodePicker.getFullNumberWithPlus();
                MechanicSharedClass.password = etpassword.getText().toString();
                if (isallFieldsFilled(etemail.getText().toString(), etpassword.getText().toString(), etcity.getText().toString(),
                        etphno.getText().toString())) {
                    if (isEmailMatches(etemail.getText().toString())) {
                        Intent intent = new Intent(Mechanic_Signup.this, Mech_PersonalInfo.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Mechanic_Signup.this, "Please type valid Email address", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Mechanic_Signup.this, "All fields are not filled", Toast.LENGTH_SHORT).show();
                }
            }

        });

        tvSigninhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mechanic_Signup.this, Mechanic_Login.class);
                startActivity(intent);
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mechanic_Signup.this, ChoiceActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isallFieldsFilled(String stEmail, String stpassword, String stCity, String stphno) {
        if (TextUtils.isEmpty(stEmail) || TextUtils.isEmpty(stpassword) || TextUtils.isEmpty(stCity) || TextUtils.isEmpty(stphno)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isEmailMatches(String stEmail) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            if (Patterns.EMAIL_ADDRESS.matcher(stEmail).matches()) {
                return true;
            } else

            {
                return false;
            }
        }
        return false;
    }

    private void initWidgets() {
        btnSignup = findViewById(R.id.btn_signup);
        etcity = findViewById(R.id.etCity);
        etemail = findViewById(R.id.etEmail);
        etpassword = findViewById(R.id.etPassword);
        etphno = findViewById(R.id.etPhone);
        countryCodePicker = findViewById(R.id.ccp);
        tvCreate = findViewById(R.id.tvCreate);
        tvAlready = findViewById(R.id.tvAlready);
        tvSigninhere = findViewById(R.id.tvSigninHere);
        tvcity = findViewById(R.id.tvcity);
        tvemail = findViewById(R.id.tvemail);
        tvpwd = findViewById(R.id.tvPwd);
        tvphone = findViewById(R.id.tvPhone);
        imgback = findViewById(R.id.imgback);

    }
}
