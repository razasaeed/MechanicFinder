package motor.mechanic.finder.fyp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;

public class Service_Upload extends AppCompatActivity {
    EditText etSerID, etSerName, etSerPrice, etSerDesc, etSerVehType;
    ImageButton btnSubmit;
    Toolbar toolbar;

    String[] VehTypelistItems, SerNamelistItems;

    DatabaseReference mDatabaseRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__upload);

        initWidgets();
        buttonListener();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/");
        mDatabaseRef = mFirebaseDatabase.getReference();

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Service_Upload.this, Mech_ServicesDetails.class);
                startActivity(intent);
            }
        });

        VehTypelistItems = getResources().getStringArray(R.array.vehicle_type);
        SerNamelistItems = getResources().getStringArray(R.array.Service_name);
    }

    private void buttonListener() {
        etSerVehType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build an AlertDialog
                final AlertDialog.Builder alertDialogFuel = new AlertDialog.Builder(Service_Upload.this);
                // Set a title for alert dialog
                alertDialogFuel.setTitle("Select your Vehicle type");
               // add a list
                final String[] fuel = {"Car", "Bus, Van, Truck", "Motorcycle or Scooter", "Tractor or Trailers", "Rickshaw or Chingchi", "Other Vehicle"};
                alertDialogFuel.setItems(fuel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedText = Arrays.asList(fuel).get(which);
                        etSerVehType.setText(selectedText);
                    }
                });
                alertDialogFuel.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alertDialogFuel.create();
                alertDialogFuel.show();            }
        });

        etSerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build an AlertDialog
                final AlertDialog.Builder alertDialogName = new AlertDialog.Builder(Service_Upload.this);
                // Set a title for alert dialog
                alertDialogName.setTitle("Service Type");
                // add a list
                final String[] services = {"General Maintenance", "General Repair Services", "Engine Services", "Other Services"};

                alertDialogName.setItems(services, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                String selectedGeneral = Arrays.asList(services).get(which);
                                etSerName.setText(selectedGeneral);
                                generalMaintenance();
                            case 1:
                                String selectedSer = Arrays.asList(services).get(which);
                                etSerName.setText(selectedSer);
                                generalRepairServices();
                            case 2:
                                String selectedEngine = Arrays.asList(services).get(which);
                                etSerName.setText(selectedEngine);
                                engineServices();
                            case 3:
                                String selectedOther = Arrays.asList(services).get(which);
                                etSerName.setText(selectedOther);
                                otherServices(); 

                        }
                    }
                });

                alertDialogName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alertDialogName.create();
                alertDialogName.show();

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stID = etSerID.getText().toString();
                String stVehType = etSerVehType.getText().toString();
                String stName = etSerName.getText().toString();
                String stPrice = etSerPrice.getText().toString().trim();
                String stDesc = etSerDesc.getText().toString();

                if (isallFieldsFilled(stID, stVehType,stName, stPrice, stDesc)) {

                if(!stID.equals("")){
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                    mDatabaseRef.child(userID).child("Service_Details").child(stID).setValue("true");
                    mDatabaseRef.child(userID).child("Service_Details").child(stVehType).setValue("true");
                    mDatabaseRef.child(userID).child("Service_Details").child(stName).setValue("true");
                    mDatabaseRef.child(userID).child("Service_Details").child(stPrice).setValue("true");
                    mDatabaseRef.child(userID).child("Service_Details").child(stDesc).setValue("true");

                    //reset the text
                    etSerID.setText("");
                    etSerVehType.setText("");
                    etSerName.setText("");
                    etSerPrice.setText("");
                    etSerDesc.setText("");

                }

                Intent intent = new Intent(Service_Upload.this, Mech_ServicesDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } else {
                Toast.makeText(Service_Upload.this, "All fields are not filled", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    private void otherServices() {
        AlertDialog.Builder alertDialogOther =  new AlertDialog.Builder(Service_Upload.this);
        // Set a title for alert dialog
        alertDialogOther.setTitle("OTHER SERVICES");

        final String[] service = {"Clutch Repair & Replacement",
                "Axle Replacement",
                "Driveline Maintenance & Repairt",
                "Others"};

        alertDialogOther.setItems(service, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedOther = Arrays.asList(service).get(which);
                etSerName.setText(selectedOther);

            }
        });
        alertDialogOther.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alertDialogOther.create();
        alertDialogOther.show();
    }

    private void engineServices() {
        AlertDialog.Builder alertDialogEngine =  new AlertDialog.Builder(Service_Upload.this);
        // Set a title for alert dialog
        alertDialogEngine.setTitle("ENGINE SERVICES");

        final String[] service = {"Engine Repair",
                "Engine Replacement",
                "Engine Performance Check", "Belt & Hose Replacement",
                "Driveability Diagnostics & Repair","Fuel Injection Service & Repair",
                "Fuel System Maintenance & Repair",
                "Ignition System Maintenance & Repair"};

        alertDialogEngine.setItems(service, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedEngine = Arrays.asList(service).get(which);
                etSerName.setText(selectedEngine);

            }
        });
        alertDialogEngine.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alertDialogEngine.create();
        alertDialogEngine.show();
    }

    private void generalRepairServices() {
        AlertDialog.Builder alertDialogEngine =  new AlertDialog.Builder(Service_Upload.this);
        // Set a title for alert dialog
        alertDialogEngine.setTitle("GENERAL REPAIR SERVICES");

        final String[] service = {"Air Conditioning Service & Repair",
                "Suspension & Steering Repair",
                "Shocks & Struts", "Cooling System Service & Repair",
                "Exhaust Systems & Mufflers"," Custom Exhaust",
                "Pre-Purchase Inspections",
                " Fleet Services","Hybrid Services",
                "Chassis & Suspension","Power Steering Repair",
                "Power Accessory Repair"};

        alertDialogEngine.setItems(service, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedRepair = Arrays.asList(service).get(which);
                etSerName.setText(selectedRepair);

            }
        });
        alertDialogEngine.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alertDialogEngine.create();
        alertDialogEngine.show();

    }

    private void generalMaintenance() {
        AlertDialog.Builder alertDialogGeneral =  new AlertDialog.Builder(Service_Upload.this);
        // Set a title for alert dialog
        alertDialogGeneral.setTitle("GENERAL MAINTENANCE");

        final String[] service = {"Change the engine oil",
                "Replace the oil filter",
               "Replace the air filter", "Replace the fuel filter",
                "Replace the cabin filter"," Replace the spark plugs",
                "Check level and refill brake fluid/clutch fluid",
                " Check Brake Pads/Liners, Brake Discs/Drums, and replace if worn out.",
               "Check level and refill power steering fluid"," Check level and refill Automatic/Manual Transmission Fluid",
               "Grease and lubricate components","Check condition of the tires", " Check for proper operation of all lights, wipers etc."};

        alertDialogGeneral.setItems(service, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedMaintenance = Arrays.asList(service).get(which);
                etSerName.setText(selectedMaintenance);

            }
        });
        alertDialogGeneral.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alertDialogGeneral.create();
        alertDialogGeneral.show();
    }

    public boolean isallFieldsFilled(String stID, String stVehType, String stSerName, String stSerPrice, String stDesc) {
        if (TextUtils.isEmpty(stID) || TextUtils.isEmpty(stVehType)|| TextUtils.isEmpty(stSerName)|| TextUtils.isEmpty(stSerPrice)||TextUtils.isEmpty(stDesc)) {
            return false;
        } else {
            return true;
        }
    }


    private void initWidgets() {
        toolbar = findViewById(R.id.toolbar);
        etSerID = findViewById(R.id.etSerID);
        etSerVehType = findViewById(R.id.etSerVehType);
        etSerName =findViewById(R.id.etSerName);
        etSerPrice = findViewById(R.id.etSerPrice);
        etSerDesc = findViewById(R.id.etSerDesc);
        btnSubmit = findViewById(R.id.btnSubmit);
    }
}
