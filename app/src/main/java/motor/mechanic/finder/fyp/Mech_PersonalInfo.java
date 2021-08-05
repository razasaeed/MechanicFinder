package motor.mechanic.finder.fyp;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import motor.mechanic.finder.fyp.Adapters.ChooseWorkshopAdapter;
import motor.mechanic.finder.fyp.Customization.RecyclerTouchListener;
import motor.mechanic.finder.fyp.DataModels.MechanicDataModel;
import motor.mechanic.finder.fyp.DataModels.WorkshopDataModel;
import motor.mechanic.finder.fyp.SharedData.MechanicSharedClass;
import motor.mechanic.finder.fyp.SharedData.WorkshopSharedClass;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.view.LayoutInflater;
import android.app.Dialog;
import android.view.Window;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import im.delight.android.location.SimpleLocation;
import motor.mechanic.finder.fyp.UI.WorkshopChoiceActivity;

public class Mech_PersonalInfo extends AppCompatActivity {

    RequestQueue requestQueue;
    final String url = "https://perkier-reproductio.000webhostapp.com/wheels/register.php";
    AlertDialog.Builder builder;
    AlertDialog.Builder workshop_builder;
    SimpleLocation location;
    Double latitude, longitude = 0.0;
    KProgressHUD hud;
    TextView tvPersonal, tvname, tvCNIC, tvPhone, tvWorkExp, tvUpload;
    EditText etFullName, etCNIC, etPhone, etWorkExp, etWType;
    Button btnContinue;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    DatabaseReference myRef, workshopRef;
    CountryCodePicker countryCodePicker;
    ImageView imgback, owner_image;
    List<WorkshopDataModel> workshopData = new ArrayList<>();
    StorageReference mStorageRef;
    private int REQUEST_CODE = 1;
    int workshop_pos = 0;

    private StorageTask mUploadTask;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mech__personal_info);

        initWidgets();
        requestQueue = Volley.newRequestQueue(this);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/");
        workshopRef = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Workshop").child("All");
        myRef = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Mechanic");
        mStorageRef = FirebaseStorage.getInstance().getReference("Mechanic Image");
        buttonListener();
        countryCodePicker.registerCarrierNumberEditText(etPhone);

        location = new SimpleLocation(this);
        if (!location.hasLocationEnabled()) {
            SimpleLocation.openSettings(this);
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }

    private void buttonListener() {
        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mech_PersonalInfo.this, Mechanic_Signup.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(Mech_PersonalInfo.this, "An Upload is Still in Progress....", Toast.LENGTH_SHORT).show();
                } else {
                    CheckValidations();
                }

            }
        });

        etWType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType();
            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Mech_PersonalInfo.this);
        builder.setTitle("Add Photos!");
        builder.setIcon(android.R.drawable.ic_menu_camera);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE);

                } else if (items[which].equals("Choose from Gallery")) {

                    openFileChooser();

                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void selectType() {
        final CharSequence[] items = {"Individual", "Under Workshop"};
        builder = new AlertDialog.Builder(Mech_PersonalInfo.this);
        builder.setTitle("Employement Type");
        builder.setIcon(android.R.drawable.btn_plus);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Individual")) {
                    etWType.setText("Individual");
                } else if (items[which].equals("Under Workshop")) {
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.dismiss();
                    WorkshopSharedClass.addMechanicCheck = "through_workshop";
                    workshopsDialog();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void workshopsDialog() {

        final Dialog dialog = new Dialog(Mech_PersonalInfo.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_workshop_cell);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        final RecyclerView choosrWorkshopRV = (RecyclerView) dialog.findViewById(R.id.choosrWorkshopRV);
        final TextView cancelBtn = (TextView) dialog.findViewById(R.id.cancelBtn);
        choosrWorkshopRV.setLayoutManager(new LinearLayoutManager(Mech_PersonalInfo.this));

        workshopRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (workshopData.size() > 0) {
                    workshopData.clear();
                }
                for (DataSnapshot workshops : dataSnapshot.getChildren()) {
                    WorkshopDataModel workshopDataModel = workshops.getValue(WorkshopDataModel.class);
                    workshopData.add(workshopDataModel);
                }
                ChooseWorkshopAdapter workshopAdapter = new ChooseWorkshopAdapter(Mech_PersonalInfo.this, workshopData);
                choosrWorkshopRV.setAdapter(workshopAdapter);
                choosrWorkshopRV.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), choosrWorkshopRV, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        workshop_pos = position;
                        etWType.setText("Workshop: " + workshopData.get(position).getWorkshop_fullname());
                        dialog.dismiss();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            MechanicSharedClass.mechanicImg = data.getData();

            Picasso.with(this).load(MechanicSharedClass.mechanicImg).into(owner_image);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void CheckValidations() {
        if (MechanicSharedClass.mechanicImg != null) {
            MechanicSharedClass.fullname = etFullName.getText().toString();
            MechanicSharedClass.cnic = etCNIC.getText().toString();
            MechanicSharedClass.contact = countryCodePicker.getFullNumberWithPlus();
            MechanicSharedClass.work_exp = etWorkExp.getText().toString();

            if (isallFieldsFilled(MechanicSharedClass.fullname, MechanicSharedClass.cnic, MechanicSharedClass.contact,
                    MechanicSharedClass.work_exp)) {
                hud = KProgressHUD.create(Mech_PersonalInfo.this)
                        .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                        .setLabel("Please wait")
                        .setMaxProgress(100)
                        .show();

                uploadFile();
            } else {
                Toast.makeText(Mech_PersonalInfo.this, "All fields are not filled", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "You haven't Selected Any file!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser(final String pic) {

        mAuth.createUserWithEmailAndPassword(MechanicSharedClass.email, MechanicSharedClass.password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            MechanicDataModel mechanicDataModel;
                            String key = myRef.push().getKey();

                            mechanicDataModel = new MechanicDataModel(MechanicSharedClass.key, MechanicSharedClass.city, MechanicSharedClass.email,
                                    MechanicSharedClass.phone_no, MechanicSharedClass.password, pic, MechanicSharedClass.fullname, MechanicSharedClass.cnic,
                                    MechanicSharedClass.contact, MechanicSharedClass.work_exp, "", "", "", "", "", "", "", "0", String.valueOf(latitude) + "," + String.valueOf(longitude),
                                    getCompleteAddress(latitude, longitude));

                            myRef.child(key).setValue(mechanicDataModel, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    hud.dismiss();
                                    Intent intent = new Intent(Mech_PersonalInfo.this, Mechanic_Login.class);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            hud.dismiss();
                        }
                    }
                });

    }

    public boolean isallFieldsFilled(String stname, String stCNIC, String stPhone, String stWorkExp) {
        if (TextUtils.isEmpty(stCNIC) || TextUtils.isEmpty(stname) || TextUtils.isEmpty(stPhone) || TextUtils.isEmpty(stWorkExp)) {
            return false;
        } else {
            return true;
        }
    }

    private void initWidgets() {
        owner_image = findViewById(R.id.owner_image);
        tvUpload = findViewById(R.id.tvUpload);
        imgback = findViewById(R.id.imgback);
        tvPersonal = findViewById(R.id.tvPersonal);
        etFullName = findViewById(R.id.etName);
        etCNIC = findViewById(R.id.etCNIC);
        etPhone = findViewById(R.id.etPhone);
        etWorkExp = findViewById(R.id.etWExp);
        btnContinue = findViewById(R.id.btnContinue);
        tvname = findViewById(R.id.tvname);
        tvCNIC = findViewById(R.id.tvCNIC);
        tvPhone = findViewById(R.id.tvPhone);
        tvWorkExp = findViewById(R.id.tvWorkExp);
        countryCodePicker = findViewById(R.id.ccp);
        etWType = findViewById(R.id.etWType);
    }

    private void uploadFile() {
        if (MechanicSharedClass.mechanicImg != null) {
            final StorageReference fileReference = mStorageRef.child(MechanicSharedClass.mechanicImg.getLastPathSegment());
            UploadTask uploadTask = fileReference.putFile(MechanicSharedClass.mechanicImg);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri taskResult = task.getResult();
                        registerUser(taskResult.toString());
                    }
                }
            });
        } else {
            hud.dismiss();
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public String getCompleteAddress(Double latitude, Double longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getAddressLine(1);
        String country = addresses.get(0).getAddressLine(2);

        return address;
    }

    public void register_me() {
        final String token = FirebaseInstanceId.getInstance().getToken();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                uploadFile();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Mech_PersonalInfo.this, "Error in connection insertion", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("token", token);
                parameters.put("email", MechanicSharedClass.email);
                return parameters;
            }
        };
        requestQueue.add(request);
    }

}
