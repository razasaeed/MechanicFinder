package motor.mechanic.finder.fyp.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import motor.mechanic.finder.fyp.DataModels.UserDataModel;
import motor.mechanic.finder.fyp.DataModels.WorkshopDataModel;
import motor.mechanic.finder.fyp.Mech_PersonalInfo;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.MechanicSharedClass;
import motor.mechanic.finder.fyp.SharedData.UserSharedClass;
import motor.mechanic.finder.fyp.SharedData.WorkshopSharedClass;
import motor.mechanic.finder.fyp.User_Password;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserLogin_UI extends AppCompatActivity {

    public final String url = "https://perkier-reproductio.000webhostapp.com/wheels/register_user.php";
    RequestQueue requestQueue;
    DatabaseReference wrk_reference;
    List<UserDataModel> userData = new ArrayList<>();
    EditText etUserName, etUserPassword;
    Button btnContinue;
    TextView tvName, tv2;
    TextInputLayout textInputLayoutName;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        initWidgets();
        requestQueue = Volley.newRequestQueue(this);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/");
        myRef = mFirebaseDatabase.getReference("User_Signup");
        wrk_reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Workshop").child("All");
        buttonListener();

    }

    private void buttonListener() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (userData.size() > 0) {
                            userData.clear();
                        }
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            UserDataModel userDataModel = userSnapshot.getValue(UserDataModel.class);
                            userData.add(userDataModel);
                        }

                        if (userData.size()>0){
                            Intent intent = new Intent(UserLogin_UI.this, NearestMechanicsMap.class);
                            UserSharedClass.id = userData.get(0).getId();
                            UserSharedClass.phone = userData.get(0).getPhone();
                            UserSharedClass.username = etUserName.getText().toString();
                            UserSharedClass.password = etUserPassword.getText().toString();
                            startActivity(intent);
                        }
                        /*register_me();*/

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // WORKSHOPS

        wrk_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (WorkshopSharedClass.workshopsData.size() > 0) {
                    WorkshopSharedClass.workshopsData.clear();
                }
                for (DataSnapshot workshops : dataSnapshot.getChildren()) {
                    WorkshopDataModel dataModel = workshops.getValue(WorkshopDataModel.class);
                    WorkshopSharedClass.workshopsData.add(dataModel);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initWidgets() {
        etUserName = findViewById(R.id.etUserName);
        etUserPassword = findViewById(R.id.etUserPassword);
        btnContinue = findViewById(R.id.btnContinue);
        tvName = findViewById(R.id.tvName);
        tv2 = findViewById(R.id.tv2);
        textInputLayoutName = findViewById(R.id.textInputFullName);
    }

    public void register_me() {
        final String token = FirebaseInstanceId.getInstance().getToken();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                for (int i = 0; i < userData.size(); i++) {
                    if (userData.get(i).getUsername().equals(etUserName.getText().toString()) &&
                            userData.get(i).getPassword().equals(etUserPassword.getText().toString())) {
//                                Intent intent = new Intent(UserLogin_UI.this, Book_UI.class);
                        Intent intent = new Intent(UserLogin_UI.this, NearestMechanicsMap.class);
                        UserSharedClass.id = userData.get(0).getId();
                        UserSharedClass.phone = userData.get(0).getPhone();
                        UserSharedClass.username = etUserName.getText().toString();
                        UserSharedClass.password = etUserPassword.getText().toString();
                        startActivity(intent);
                    } else {
                        Toast.makeText(UserLogin_UI.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserLogin_UI.this, "Error in connection insertion", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("token", token);
                parameters.put("email", etUserName.getText().toString());
                return parameters;
            }
        };
        requestQueue.add(request);
    }

}
