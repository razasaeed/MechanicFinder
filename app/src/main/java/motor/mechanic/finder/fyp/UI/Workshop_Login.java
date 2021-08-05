package motor.mechanic.finder.fyp.UI;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import motor.mechanic.finder.fyp.DataModels.WorkshopDataModel;
import motor.mechanic.finder.fyp.ForgotPassword;
import motor.mechanic.finder.fyp.Mechanic_Signup;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.WorkshopSharedClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Workshop_Login extends AppCompatActivity {
    EditText etEmail, etPwd;
    Button btnlogin;
    String stEmail, stpwd;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    TextView tvemail, tvpwd, tvLOGIN, tvDont, tvSignupHere, tvforgotPassword;
    ImageView imgback;
    List<WorkshopDataModel> workshopData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_login);

        initWidgets();
        databaseReference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Workshop").child("All");
        firebaseAuth = FirebaseAuth.getInstance();
        buttonListener();
    }

    private void buttonListener() {
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stEmail = etEmail.getText().toString();
                stpwd = etPwd.getText().toString();
                WorkshopSharedClass.email = stEmail;
                WorkshopSharedClass.password = stpwd;
                if (isallFieldsFilled(stEmail, stpwd)) {
                    if (isEmailMatches(stEmail)) {

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (WorkshopSharedClass.workshopsData.size() > 0) {
                                    WorkshopSharedClass.workshopsData.clear();
                                }

                                for (DataSnapshot workshopSnapshot : dataSnapshot.getChildren()) {
                                    WorkshopDataModel dataModel = workshopSnapshot.getValue(WorkshopDataModel.class);
                                    try {
                                        if (dataModel.getWorkshop_email().equals(stEmail) && dataModel.getWorkshop_password().equals(stpwd)) {
                                            WorkshopSharedClass.workshopsData.add(dataModel);
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                if (WorkshopSharedClass.workshopsData.size() > 0) {
                                    Intent intent = new Intent(Workshop_Login.this, WorkshopChoiceActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Workshop_Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }

                                /*for (DataSnapshot workshopSnapshot : dataSnapshot.getChildren()) {
                                    WorkshopDataModel dataModel = workshopSnapshot.getValue(WorkshopDataModel.class);
                                    if (dataModel.getWorkshop_email().equals(stEmail) && dataModel.getWorkshop_password().equals(stpwd)) {
                                        WorkshopSharedClass.workshopsData.add(dataModel);
                                    }
                                }
                                if (WorkshopSharedClass.workshopsData.size() > 0) {
                                    Intent intent = new Intent(Workshop_Login.this, WorkshopChoiceActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Workshop_Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }*/
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(Workshop_Login.this, "Please type valid Email address", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Workshop_Login.this, "All fields are not filled", Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workshop_Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workshop_Login.this, Mechanic_Signup.class);
                startActivity(intent);
            }
        });
        tvSignupHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Workshop_Login.this, Mechanic_Signup.class);
                startActivity(intent);
            }
        });
    }


    public boolean isallFieldsFilled(String stEmail, String stpwd) {
        if (TextUtils.isEmpty(stEmail) || TextUtils.isEmpty(stpwd)) {
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
        imgback = findViewById(R.id.imgback);
        etEmail = findViewById(R.id.etEmail);
        etPwd = findViewById(R.id.etPassword);
        btnlogin = findViewById(R.id.btn_login);
        tvemail = findViewById(R.id.tvemail);
        tvpwd = findViewById(R.id.tvPwd);
        tvLOGIN = findViewById(R.id.tvLOGIN);
        tvDont = findViewById(R.id.tvDont);
        tvSignupHere = findViewById(R.id.tvSignupHere);
        tvforgotPassword = findViewById(R.id.tvforgotPassword);
    }
}
