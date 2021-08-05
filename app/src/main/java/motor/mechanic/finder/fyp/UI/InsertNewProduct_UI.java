package motor.mechanic.finder.fyp.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import motor.mechanic.finder.fyp.DataModels.ProductsDataModel;
import motor.mechanic.finder.fyp.Mech_PersonalInfo;
import motor.mechanic.finder.fyp.Mechanic_Signup;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.MechanicSharedClass;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class InsertNewProduct_UI extends AppCompatActivity {

    KProgressHUD hud;
    CircleImageView product_image;
    TextView tvUpload;
    EditText productName, productType, productPrice;
    Button btnInsert;
    DatabaseReference reference;
    StorageReference mStorageRef;

    private int REQUEST_CODE = 1;
    private StorageTask mUploadTask;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_new_product__ui);

        reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Products");
        mStorageRef = FirebaseStorage.getInstance().getReference("Products");

        product_image = findViewById(R.id.product_image);
        tvUpload = findViewById(R.id.tvUpload);
        productName = findViewById(R.id.productName);
        productType = findViewById(R.id.productType);
        productPrice = findViewById(R.id.productPrice);
        btnInsert = findViewById(R.id.btnInsert);

        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        productType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogFuel = new AlertDialog.Builder(InsertNewProduct_UI.this);

                alertDialogFuel.setTitle("Types");
                final String[] fuel = {"Oil", "Brake", "Battery", "AC"};
                alertDialogFuel.setItems(fuel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedText = Arrays.asList(fuel).get(which);
                        productType.setText(selectedText);
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

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MechanicSharedClass.mechanicImg == null) {
                    Toast.makeText(InsertNewProduct_UI.this, "Choose Image", Toast.LENGTH_SHORT).show();
                } else if (productName.getText().toString().equals("")) {
                    productName.setError("enter product");
                } else if (productPrice.getText().toString().equals("")) {
                    productPrice.setError("enter price");
                } else if (productType.getText().toString().equals("")) {
                    productType.setError("enter type");
                } else {
                    hud = KProgressHUD.create(InsertNewProduct_UI.this)
                            .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                            .setLabel("Please wait")
                            .setMaxProgress(100)
                            .show();
                    uploadFile();
                }
            }
        });

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

            Picasso.with(this).load(MechanicSharedClass.mechanicImg).into(product_image);
        }
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
                        registerProduct(taskResult.toString());
                    }
                }
            });
        } else {
            hud.dismiss();
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void registerProduct(final String url) {
        String key = reference.push().getKey();
        ProductsDataModel productsDataModel = new ProductsDataModel(key, productName.getText().toString(), url,
                productPrice.getText().toString());
        reference.child(productType.getText().toString()).child(key).setValue(productsDataModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(InsertNewProduct_UI.this, "Product Registered Successful", Toast.LENGTH_SHORT).show();
                hud.dismiss();
            }
        });
    }

}
