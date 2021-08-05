package motor.mechanic.finder.fyp;

import android.app.assist.AssistStructure;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

public class Worker_Upload extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageButton btn_submit;
    TextView tvUpload;
    EditText etWorkerFullName, etCNIC, etPhone, etWorkExp;
    ImageView worker_img;
    Toolbar toolbar;
    CountryCodePicker countryCodePicker;
    private Uri mImageUri;
    StorageReference mStorageRef;
     DatabaseReference mDatabaseRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    private int REQUEST_CODE = 1;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker__upload);

        initWidgets();
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Worker Image");
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/");
        mDatabaseRef = mFirebaseDatabase.getReference();
        buttonListener();

        countryCodePicker.registerCarrierNumberEditText(etPhone);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent = new Intent(Worker_Upload.this, Mech_WorkersInfo.class);
                  startActivity(intent);
            }
        });

    }

    private void buttonListener() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUploadTask != null && mUploadTask.isInProgress()) {

                    Toast.makeText(Worker_Upload.this, "An Upload is Still in Progress....", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  openFileChooser();
                selectImage();
            }
        });

    }

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Worker_Upload.this);
        builder.setTitle("Add Photos!");
        builder.setIcon(android.R.drawable.ic_menu_camera);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Take Photo")){
                    Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE);

                }else if(items[which].equals("Choose from Gallery")){

                    openFileChooser();

                }else if(items[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(worker_img);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));


            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, 500);

                            Toast.makeText(Worker_Upload.this, "Upload Successful!!!", Toast.LENGTH_LONG).show();

                            String stname = etWorkerFullName.getText().toString();
                            String stCNIC = etCNIC.getText().toString();
                            String stPhone = etPhone.getText().toString().trim();
                            String phno = countryCodePicker.getFullNumberWithPlus();
                            String stWorkExp = etWorkExp.getText().toString();

                            if (isallFieldsFilled(stname, stPhone,stCNIC, stWorkExp)) {

                            if(!stPhone.equals("")){
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userID = user.getUid();

                                mDatabaseRef.child(userID).child("Worker_Personal_Information").child(stname).setValue("true");
                                mDatabaseRef.child(userID).child("Worker_Personal_Information").child(stCNIC).setValue("true");
                                mDatabaseRef.child(userID).child("Worker_Personal_Information").child(phno).setValue("true");
                                mDatabaseRef.child(userID).child("Worker_Personal_Information").child(stWorkExp).setValue("true");

                                //reset the text
                                etWorkerFullName.setText("");
                                etCNIC.setText("");
                                etPhone.setText("");
                                etWorkExp.setText("");

                            }

                            openImagesActivity ();

                        } else {
                            Toast.makeText(Worker_Upload.this, "All fields are not filled", Toast.LENGTH_SHORT).show();
                        }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //uploadProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Worker_Upload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            //uploadProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "You haven't Selected Any file!!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isallFieldsFilled(String stname, String stPhone, String stCNIC, String stWorkExp) {
        if (TextUtils.isEmpty(stname) || TextUtils.isEmpty(stPhone)|| TextUtils.isEmpty(stCNIC)|| TextUtils.isEmpty(stWorkExp)) {
            return false;
        } else {
            return true;
        }
    }

    private void openImagesActivity(){
        Intent intent = new Intent(this, Mech_WorkersInfo.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void initWidgets() {
        toolbar = findViewById(R.id.toolbar);
        worker_img = findViewById(R.id.worker_image);
        etWorkerFullName = findViewById(R.id.etWorkerName);
        etCNIC = findViewById(R.id.etWorkerCNIC);
        etPhone = findViewById(R.id.etPhoneNo);
        etWorkExp = findViewById(R.id.etWExp);
        countryCodePicker = findViewById(R.id.ccp);
        btn_submit = findViewById(R.id.btn_submit);
        tvUpload = findViewById(R.id.tvUpload);
    }
}
