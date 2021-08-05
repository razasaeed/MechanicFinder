package motor.mechanic.finder.fyp.UI;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import motor.mechanic.finder.fyp.Adapters.ChooseEmployeeAdapter;
import motor.mechanic.finder.fyp.DataModels.MechanicDataModel;
import motor.mechanic.finder.fyp.DataModels.MechanicRequestDataModel;
import motor.mechanic.finder.fyp.DataModels.WorkshopRequestDataModel;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.MechanicSharedClass;
import motor.mechanic.finder.fyp.SharedData.UserSharedClass;
import motor.mechanic.finder.fyp.SharedData.WorkshopSharedClass;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import im.delight.android.location.SimpleLocation;
import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.view.CreditCardView;

public class MechanicDetails extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    SimpleLocation location;
    Double latitude, longitude = 0.0;
    final String push_url = "https://perkier-reproductio.000webhostapp.com/wheels/push_notifs_mechanic.php";
    LinearLayout orderDetailLayout;
    private GoogleMap mMap;
    CircleImageView imgMechanic;
    ImageView onlineIcon;
    TextView mechanicNameTxt, mechanicStatusTxt, activity_task_detail_title, activity_task_detail_contact,
            activity_task_service_name, activity_task_product_name, activity_task_product_price, activity_task_product_quantity,
            activity_task_total, sub_service, activity_task_sub_product_price;
    FloatingActionButton activity_task_detail_add_audio_attachment, activity_task_detail_services, activity_task_detail_book,
            activity_task_detail_submit;

    DatabaseReference reference, req_reference, req_reference_workshop;
    String[] splitLocation;
    List<MechanicDataModel> mechanicsData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Mechanic");
        req_reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Mechanic_Requests").child("Mechanics");
        req_reference_workshop = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Mechanic_Requests").child("Workshops");
        orderDetailLayout = findViewById(R.id.orderDetailLayout);
        sub_service = findViewById(R.id.sub_service);
        activity_task_sub_product_price = findViewById(R.id.activity_task_sub_product_price);
        activity_task_total = findViewById(R.id.activity_task_total);
        activity_task_detail_add_audio_attachment = findViewById(R.id.activity_task_detail_add_audio_attachment);
        activity_task_detail_submit = findViewById(R.id.activity_task_detail_submit);
        activity_task_detail_book = findViewById(R.id.activity_task_detail_book);
        activity_task_detail_services = findViewById(R.id.activity_task_detail_services);
        activity_task_product_quantity = findViewById(R.id.activity_task_product_quantity);
        activity_task_product_price = findViewById(R.id.activity_task_product_price);
        activity_task_product_name = findViewById(R.id.activity_task_product_name);
        activity_task_service_name = findViewById(R.id.activity_task_service_name);
        imgMechanic = findViewById(R.id.imgMechanic);
        activity_task_detail_contact = findViewById(R.id.activity_task_detail_contact);
        onlineIcon = findViewById(R.id.onlineIcon);
        mechanicNameTxt = findViewById(R.id.mechanicNameTxt);
        mechanicStatusTxt = findViewById(R.id.mechanicStatusTxt);
        activity_task_detail_title = findViewById(R.id.activity_task_detail_title);

        activity_task_detail_submit.setOnClickListener(this);
        activity_task_detail_services.setOnClickListener(this);
        activity_task_detail_book.setOnClickListener(this);

        location = new SimpleLocation(this);
        if (!location.hasLocationEnabled()) {
            SimpleLocation.openSettings(this);
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mechanicsData.size() > 0) {
                    mechanicsData.clear();
                }
                for (DataSnapshot mechanicSnapshot : dataSnapshot.getChildren()) {
                    MechanicDataModel dataModel = mechanicSnapshot.getValue(MechanicDataModel.class);
                    mechanicsData.add(dataModel);
                }

                if (MechanicSharedClass.mechanicDetailCallCheck.equals("mechanic_map")) {
                    activity_task_detail_book.setVisibility(View.VISIBLE);
                    orderDetailLayout.setVisibility(View.GONE);
                    activity_task_detail_services.setVisibility(View.GONE);
                    Picasso.with(MechanicDetails.this).load(MechanicSharedClass.mechanicsDataTwo.get(0).getEmpImg()).into(imgMechanic);
                    mechanicNameTxt.setText(MechanicSharedClass.mechanicsDataTwo.get(0).getFullname());
                    if (MechanicSharedClass.mechanicsDataTwo.get(0).getOnline_status().equals("1")) {
                        onlineIcon.setVisibility(View.VISIBLE);
                        mechanicStatusTxt.setVisibility(View.VISIBLE);
                    } else {
                        onlineIcon.setVisibility(View.GONE);
                        mechanicStatusTxt.setVisibility(View.GONE);
                    }

                    splitLocation = MechanicSharedClass.mechanicsDataTwo.get(0).getLocation().split(",");
                    LatLng sydney = new LatLng(Double.parseDouble(splitLocation[0]), Double.parseDouble(splitLocation[1]));
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                } else if (MechanicSharedClass.mechanicDetailCallCheck.equals("workshop_map")) {
                    if (WorkshopSharedClass.workshop_options_check == 1) {
                        activity_task_detail_submit.setVisibility(View.VISIBLE);
                        activity_task_detail_services.setVisibility(View.GONE);
                        orderDetailLayout.setVisibility(View.VISIBLE);

                        activity_task_service_name.setText(MechanicSharedClass.my_request_title);
                        activity_task_product_name.setText(MechanicSharedClass.product_name);
                        activity_task_product_price.setText(MechanicSharedClass.product_price);
                        sub_service.setText(MechanicSharedClass.sub_product_name);
                        activity_task_sub_product_price.setText(MechanicSharedClass.sub_product_price);
                        activity_task_total.setText(String.valueOf(Integer.parseInt(MechanicSharedClass.product_price) + Integer.valueOf(MechanicSharedClass.sub_product_price)));
                    } else {
                        orderDetailLayout.setVisibility(View.GONE);
                        activity_task_detail_book.setVisibility(View.GONE);
                        orderDetailLayout.setVisibility(View.GONE);
                        activity_task_detail_services.setVisibility(View.VISIBLE);
                    }
                    Picasso.with(MechanicDetails.this).load(WorkshopSharedClass.workshopsDataSingle.get(0).getWorkshop_img()).into(imgMechanic);
                    mechanicNameTxt.setText(WorkshopSharedClass.workshopsDataSingle.get(0).getWorkshop_fullname());
                    if (WorkshopSharedClass.workshopsDataSingle.get(0).getOnline_status().equals("1")) {
                        onlineIcon.setVisibility(View.VISIBLE);
                        mechanicStatusTxt.setVisibility(View.VISIBLE);
                    } else {
                        onlineIcon.setVisibility(View.GONE);
                        mechanicStatusTxt.setVisibility(View.GONE);
                    }

                    splitLocation = WorkshopSharedClass.workshopsDataSingle.get(0).getLocation().split(",");
                    LatLng sydney = new LatLng(Double.parseDouble(splitLocation[0]), Double.parseDouble(splitLocation[1]));
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                } else {
                    Picasso.with(MechanicDetails.this).load(mechanicsData.get(MechanicSharedClass.selected_mechanic_position).getEmpImg()).into(imgMechanic);
                    mechanicNameTxt.setText(mechanicsData.get(MechanicSharedClass.selected_mechanic_position).getFullname());
                    if (mechanicsData.get(MechanicSharedClass.selected_mechanic_position).getOnline_status().equals("1")) {
                        onlineIcon.setVisibility(View.VISIBLE);
                        mechanicStatusTxt.setVisibility(View.VISIBLE);
                    } else {
                        onlineIcon.setVisibility(View.GONE);
                        mechanicStatusTxt.setVisibility(View.GONE);
                    }

                    splitLocation = mechanicsData.get(MechanicSharedClass.selected_mechanic_position).getLocation().split(",");
                    LatLng sydney = new LatLng(Double.parseDouble(splitLocation[0]), Double.parseDouble(splitLocation[1]));
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(Double.parseDouble(splitLocation[0]), Double.parseDouble(splitLocation[1])))      // Sets the center of the map to location user
                        .zoom(13)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (MechanicSharedClass.mechanicsData.size() > 0) {
                    MechanicSharedClass.mechanicsData.clear();
                }
                for (DataSnapshot mechanicSnapshot : dataSnapshot.getChildren()) {
                    MechanicDataModel dataModel = mechanicSnapshot.getValue(MechanicDataModel.class);
                    MechanicSharedClass.mechanicsData.add(dataModel);
                }

                if (MechanicSharedClass.mechanicDetailCallCheck.equals("mechanic_map")) {
                    Picasso.with(MechanicDetails.this).load(MechanicSharedClass.mechanicsDataTwo.get(0).getEmpImg()).into(imgMechanic);
                    mechanicNameTxt.setText(MechanicSharedClass.mechanicsDataTwo.get(0).getFullname());
                    activity_task_detail_title.setText(MechanicSharedClass.mechanicsDataTwo.get(0).getWorkshop_fullname());
                    activity_task_detail_contact.setText(MechanicSharedClass.mechanicsDataTwo.get(0).getPhone_no());

                    if (MechanicSharedClass.mechanicsDataTwo.get(0).getOnline_status().equals("1")) {
                        onlineIcon.setVisibility(View.VISIBLE);
                        mechanicStatusTxt.setVisibility(View.VISIBLE);
                    } else {
                        onlineIcon.setVisibility(View.GONE);
                        mechanicStatusTxt.setVisibility(View.GONE);
                    }
                } else if (MechanicSharedClass.mechanicDetailCallCheck.equals("workshop_map")) {
                    Picasso.with(MechanicDetails.this).load(WorkshopSharedClass.workshopsDataSingle.get(0).getWorkshop_img()).into(imgMechanic);
                    mechanicNameTxt.setText(WorkshopSharedClass.workshopsDataSingle.get(0).getWorkshop_fullname());
                    activity_task_detail_title.setText(WorkshopSharedClass.workshopsDataSingle.get(0).getWorkshop_fullname());
                    activity_task_detail_contact.setText(WorkshopSharedClass.workshopsDataSingle.get(0).getWorkshop_phone());

                    if (WorkshopSharedClass.workshopsDataSingle.get(0).getOnline_status().equals("1")) {
                        onlineIcon.setVisibility(View.VISIBLE);
                        mechanicStatusTxt.setVisibility(View.VISIBLE);
                    } else {
                        onlineIcon.setVisibility(View.GONE);
                        mechanicStatusTxt.setVisibility(View.GONE);
                    }
                } else {
                    Picasso.with(MechanicDetails.this).load(MechanicSharedClass.mechanicsData.get(MechanicSharedClass.selected_mechanic_position).getEmpImg()).into(imgMechanic);
                    mechanicNameTxt.setText(MechanicSharedClass.mechanicsData.get(MechanicSharedClass.selected_mechanic_position).getFullname());
                    activity_task_detail_title.setText(MechanicSharedClass.mechanicsData.get(MechanicSharedClass.selected_mechanic_position).getWorkshop_fullname());
                    activity_task_detail_contact.setText(MechanicSharedClass.mechanicsData.get(MechanicSharedClass.selected_mechanic_position).getPhone_no());

                    if (MechanicSharedClass.mechanicsData.get(MechanicSharedClass.selected_mechanic_position).getOnline_status().equals("1")) {
                        onlineIcon.setVisibility(View.VISIBLE);
                        mechanicStatusTxt.setVisibility(View.VISIBLE);
                    } else {
                        onlineIcon.setVisibility(View.GONE);
                        mechanicStatusTxt.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), NearestMechanicsMap.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_task_detail_services:
                startActivity(new Intent(getApplicationContext(), SelectService_UI.class));
                break;
            case R.id.activity_task_detail_book:
                //Toast.makeText(this, UserSharedClass.username, Toast.LENGTH_SHORT).show();
                paymentDialog("m");
                break;
            case R.id.activity_task_detail_submit:
                req_reference_workshop.child(UserSharedClass.phone + "and" + WorkshopSharedClass.workshopsDataSingle.get(0).getWorkshop_phone()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(MechanicDetails.this, "you've already requested to this workshop", Toast.LENGTH_SHORT).show();
                        } else {
                            paymentDialog("w");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
        }
    }

    public String getDateTime() {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        return dateToStr;
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

    public void sendNotification() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, push_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MechanicDetails.this, "Request Sent", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), NearestMechanicsMap.class));
                finish();
//                Toast.makeText(getApplicationContext(), "User Added Successfully", Toast.LENGTH_SHORT).show();
                Log.d("notif", "sent");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "Communication Error!", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "Authentication Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "Server Side Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "Parse Error!", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(MainActivity.this, "Error in connection insertion"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", MechanicSharedClass.mechanicsDataTwo.get(0).getEmail());
//                parameters.put("Email", "")
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    public void paymentDialog(final String check) {
        final Dialog dialog = new Dialog(MechanicDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_payment_ui);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        final CreditCardView creditCardView = dialog.findViewById(R.id.act_creditcard_view);

        creditCardView.chooseFlag(IssuerCode.VISACREDITO);
        creditCardView.setTextExpDate("12/19");
        creditCardView.setTextNumber("5555 4444 3333 1111");
        creditCardView.setTextOwner("Felipe Silvestre");
        creditCardView.setTextCVV("432");

        ((EditText) dialog.findViewById(R.id.ed_owner_name)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                creditCardView.setTextOwner(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) dialog.findViewById(R.id.ed_card_no)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                creditCardView.setTextNumber(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) dialog.findViewById(R.id.ed_cvv_no)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                creditCardView.setTextCVV(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button submitBtn = dialog.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.equals("m")) {
                    String key = req_reference.push().getKey();
                    MechanicRequestDataModel requestDataModel = new MechanicRequestDataModel(key, UserSharedClass.id, UserSharedClass.username,
                            String.valueOf(latitude) + "," + String.valueOf(longitude), getCompleteAddress(latitude, longitude), UserSharedClass.phone,
                            getDateTime(), MechanicSharedClass.mechanicsData.get(MechanicSharedClass.selected_mechanic_position).getEmail(), "0", "Mechanic");
                    req_reference.child(key).setValue(requestDataModel, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            sendNotification();
                        }
                    });
                } else {
                    String w_key = req_reference_workshop.push().getKey();
                    WorkshopRequestDataModel workshopRequestDataModel = new WorkshopRequestDataModel(w_key, UserSharedClass.id, UserSharedClass.username,
                            String.valueOf(latitude) + "," + String.valueOf(longitude), getCompleteAddress(latitude, longitude), UserSharedClass.phone,
                            getDateTime(), "", "0", "Workshop", MechanicSharedClass.product_name, MechanicSharedClass.product_price,
                            MechanicSharedClass.product_pic, MechanicSharedClass.sub_product_name, MechanicSharedClass.sub_product_price,
                            "", "", "", "", "", "", WorkshopSharedClass.workshopsDataSingle.get(0).getWorkshop_fullname(),
                            WorkshopSharedClass.workshopsDataSingle.get(0).getWorkshop_address(), WorkshopSharedClass.workshopsDataSingle.get(0).getWorkshop_img(),
                            WorkshopSharedClass.workshopsDataSingle.get(0).getWorkshop_email(), WorkshopSharedClass.workshopsDataSingle.get(0).getWorkshop_phone(),
                            "In Progress");
                    req_reference_workshop.child(UserSharedClass.phone + "and" + WorkshopSharedClass.workshopsDataSingle.get(0).getWorkshop_phone()).setValue(workshopRequestDataModel, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                        sendNotification();
                        }
                    });
                }
            }
        });

        dialog.findViewById(R.id.bt_flip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (creditCardView.isShowingFront()) {
                    creditCardView.flipToBack();
                } else {
                    creditCardView.flipToFront();
                }
            }
        });

        final TextView cancelBtn = (TextView) dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
