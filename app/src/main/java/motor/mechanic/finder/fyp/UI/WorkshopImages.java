package motor.mechanic.finder.fyp.UI;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import motor.mechanic.finder.fyp.ChoiceActivity;
import motor.mechanic.finder.fyp.DataModels.WorkshopDataModel;
import motor.mechanic.finder.fyp.Mech_WorkersInfo;
import motor.mechanic.finder.fyp.Mechanic_Login;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.MechanicSharedClass;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import im.delight.android.location.SimpleLocation;

public class WorkshopImages extends AppCompatActivity implements
        BSImagePicker.OnMultiImageSelectedListener {

    SimpleLocation location;
    Double latitude, longitude = 0.0;
    KProgressHUD hud;
    DatabaseReference databaseReference;
    List<Uri> ImageList = new ArrayList<>();
    FirebaseAuth auth;
    StorageReference mStorageReference;
    private int REQUEST_CODE = 1;
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ProgressDialog progressDialog;
    private int upload_count = 0;

    private ImageView ivImage1, ivImage2, ivImage3, ivImage4, ivImage5, ivImage6, ivImage7, ivImage8, ivImage9, ivImage10, ivImage11, ivImage12;
    Toolbar toolbar;

    ImageButton btnAddImg;
    private Button btnContinue;
    Target target;
    // ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_images);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Image uploading please wait...");
        progressDialog.setCancelable(false);

        location = new SimpleLocation(this);
        if (!location.hasLocationEnabled()) {
            SimpleLocation.openSettings(this);
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        auth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference("Workshop");
        databaseReference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Workshop").child("All");
        initWidgets();
        buttonListener();

        //Tap Target
        final Display display = getWindowManager().getDefaultDisplay();
        final Drawable droid = ContextCompat.getDrawable(this, R.drawable.ic_person_black_24dp);
        final Rect droidTarget = new Rect(0, 0, droid.getIntrinsicWidth() * 2, droid.getIntrinsicHeight() * 2);
        droidTarget.offset(display.getWidth() / 2, display.getHeight() / 2);

        // You don't always need a sequence, and for that there's a single time tap target
        final SpannableString spannedDesc = new SpannableString("It will allow you to add workshop photos...");
        //  spannedDesc.setSpan(new UnderlineSpan(), spannedDesc.length() - "TapTargetView".length(), spannedDesc.length(), 0);
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.btnAddImg), "This is an Add a Photo button!", spannedDesc)
                .cancelable(false)
                .drawShadow(true)
                .outerCircleColor(R.color.orange)
                .textColor(android.R.color.white)
                .descriptionTextColor(R.color.colorwhite)
                .titleTextDimen(R.dimen.title_text_size)
                .descriptionTextDimen(R.dimen.desc_text_size)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                // .. which evidently starts the sequence we defined earlier
                //sequence.start();
            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
                Toast.makeText(view.getContext(), "You clicked the outer circle!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetViewSample", "You dismissed me :(");
            }
        });
    }

    private void initWidgets() {
        // gridView = findViewById(R.id.gridview);
        //  progressBar = findViewById(R.id.progress_bar);
        toolbar = findViewById(R.id.toolbar);
        btnAddImg = findViewById(R.id.btnAddImg);
        btnContinue = findViewById(R.id.btnContinue);
        ivImage1 = findViewById(R.id.iv_image1);
        ivImage2 = findViewById(R.id.iv_image2);
        ivImage3 = findViewById(R.id.iv_image3);
        ivImage4 = findViewById(R.id.iv_image4);
        ivImage5 = findViewById(R.id.iv_image5);
        ivImage6 = findViewById(R.id.iv_image6);
        ivImage7 = findViewById(R.id.iv_image7);
        ivImage8 = findViewById(R.id.iv_image8);
        ivImage9 = findViewById(R.id.iv_image9);
        ivImage10 = findViewById(R.id.iv_image10);
        ivImage11 = findViewById(R.id.iv_image11);
        ivImage12 = findViewById(R.id.iv_image12);
    }

    //set Listeners
    private void buttonListener() {

        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hud = KProgressHUD.create(WorkshopImages.this)
                        .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                        .setLabel("Please wait")
                        .setMaxProgress(100)
                        .show();
                for (upload_count = 0; upload_count < ImageList.size(); upload_count++) {
                    Uri individualImage = ImageList.get(upload_count);
                    final StorageReference ImageName = mStorageReference.child("Image" + individualImage.getLastPathSegment());
                    ImageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // mProgressBar.setProgress(0);
                                        }
                                    }, 500);

                                    String url = String.valueOf(uri);
                                    String key = databaseReference.push().getKey();
                                    WorkshopDataModel dataModel = new WorkshopDataModel(key, MechanicSharedClass.workshop_fullname,
                                            MechanicSharedClass.workshop_address, MechanicSharedClass.workshop_email,
                                            MechanicSharedClass.workshop_password, MechanicSharedClass.workshop_phone,
                                            MechanicSharedClass.num_of_workers, url, "0", String.valueOf(latitude + "," + longitude),
                                            getCompleteAddress(latitude, longitude));

                                    databaseReference.child(key).setValue(dataModel, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            hud.dismiss();
                                            startActivity(new Intent(WorkshopImages.this, Workshop_Login.class));
                                            finish();
                                        }
                                    });

                                }
                            });
                        }
                    });
                }
            }
        });
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

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(WorkshopImages.this);
        builder.setTitle("Add Photos!");
        builder.setIcon(android.R.drawable.ic_menu_camera);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE);

                } else if (items[which].equals("Choose from Gallery")) {
                    //openFileChooser();
                    BSImagePicker pickerDialog = new BSImagePicker.Builder("com.asksira.imagepickersheetdemo.fileprovider")
                            .setMaximumDisplayingImages(Integer.MAX_VALUE)
                            .isMultiSelect()
                            .setMinimumMultiSelectCount(1)
                            .setMaximumMultiSelectCount(1)
                            .build();
                    pickerDialog.show(getSupportFragmentManager(), "picker");

                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        for (int i = 0; i < uriList.size(); i++) {
            if (i >= 11) return;
            ImageView iv;
            switch (i) {
                case 0:
                    iv = ivImage1;
                    break;
                case 1:
                    iv = ivImage2;
                    break;
                case 2:
                    iv = ivImage3;
                    break;
                case 3:
                    iv = ivImage4;
                    break;
                case 4:
                    iv = ivImage5;
                    break;
                case 5:
                    iv = ivImage6;
                    break;
                case 6:
                    iv = ivImage7;
                    break;
                case 7:
                    iv = ivImage8;
                    break;
                case 8:
                    iv = ivImage9;
                    break;
                case 9:
                    iv = ivImage10;
                    break;
                case 10:
                    iv = ivImage11;
                    break;
                default:
                    iv = ivImage12;
                    break;

            }

            Glide.with(this).load(uriList.get(i)).into(iv);
            ImageList = uriList;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(ivImage1);

        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {

            uploadFile();
        }
    }


    private void uploadFile() {

        if (mImageUri != null) {

//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
            StorageReference fileReference = mStorageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            //  mUploadTask = fileReference.putBytes(byteData);

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

                            Toast.makeText(WorkshopImages.this, "Upload Successful!!!", Toast.LENGTH_LONG).show();

                            //  openImagesActivity ();
                            Intent intent = new Intent(WorkshopImages.this, Mech_WorkersInfo.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //  progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(WorkshopImages.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "You haven't Selected Any file!!", Toast.LENGTH_SHORT).show();
        }
    }

}


