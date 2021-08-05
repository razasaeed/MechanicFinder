package motor.mechanic.finder.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import motor.mechanic.finder.fyp.SharedData.MechanicSharedClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Mech_WorkshopInfo extends AppCompatActivity {
    EditText etWorkshopFullName, etAddress, etEmail, etPhone, etNumofWorkers, etPassword;
    Button btnContinue;
    TextView tvWorkshopInfo, tvworkshopname, tvworkshopaddress, tvemail,
            tvPhone, tvNumofWorkers;
    CountryCodePicker countryCodePicker;
    ImageView imgback;
    FirebaseDatabase mFirebaseDatabase;
    StorageReference mStorageRef;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    List<Mech_WorkshopInfo_DataModel> workshopdata = new ArrayList<Mech_WorkshopInfo_DataModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mech__workshop_info);

        initWidgets();
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Mechanic Image");
        myRef = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Mechanic");
        buttonListener();
        countryCodePicker.registerCarrierNumberEditText(etPhone);
    }

    private void buttonListener() {

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mech_WorkshopInfo.this, Mech_PersonalInfo.class);
                startActivity(intent);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MechanicSharedClass.workshop_fullname = etWorkshopFullName.getText().toString();
                MechanicSharedClass.workshop_address = etAddress.getText().toString();
                MechanicSharedClass.workshop_email = etEmail.getText().toString();
                MechanicSharedClass.workshop_phone = countryCodePicker.getFullNumberWithPlus();
                MechanicSharedClass.num_of_workers = etNumofWorkers.getText().toString();
                MechanicSharedClass.workshop_password = etPassword.getText().toString();

                if (isallFieldsFilled(MechanicSharedClass.workshop_fullname, etPhone.getText().toString(),
                        MechanicSharedClass.num_of_workers)) {

                    Intent intent = new Intent(Mech_WorkshopInfo.this, Mech_WorkshopImages.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(Mech_WorkshopInfo.this, "All fields are not filled", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public boolean isallFieldsFilled(String stworkshopname, String stPhone, String stNumofWorkers) {
        if (TextUtils.isEmpty(stworkshopname) || TextUtils.isEmpty(stPhone) || TextUtils.isEmpty(stNumofWorkers)) {
            return false;
        } else {
            return true;
        }
    }

    private void initWidgets() {
        imgback = findViewById(R.id.imgback);
        tvWorkshopInfo = findViewById(R.id.tvWorkshopInfo);
        etWorkshopFullName = findViewById(R.id.etWorkshopName);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhoneNo);
        etPassword = findViewById(R.id.etPassword);
        etNumofWorkers = findViewById(R.id.etNumofWorkers);
        btnContinue = findViewById(R.id.btnContinue);
        tvworkshopname = findViewById(R.id.tvworkshopname);
        tvworkshopaddress = findViewById(R.id.tvworkshopaddress);
        tvemail = findViewById(R.id.tvemail);
        tvPhone = findViewById(R.id.tvPhone);
        tvNumofWorkers = findViewById(R.id.tvNumofWorkers);
        countryCodePicker = findViewById(R.id.ccp);
    }

    @Override
    public void onBackPressed() {
        book_dialog();
    }

    public void book_dialog() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Abort the process ?")
                .setCancelText("No,cancel!")
                .setConfirmText("Yes,stop!")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance, keep widget user state, reset them if you need
                        sDialog.setTitleText("Continue!")
                                .setContentText("Registration process is safe")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {
                        myRef.child(MechanicSharedClass.key).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                startActivity(new Intent(Mech_WorkshopInfo.this, ChoiceActivity.class));
                                finish();
                            }
                        });
                    }
                })
                .show();
    }

}
