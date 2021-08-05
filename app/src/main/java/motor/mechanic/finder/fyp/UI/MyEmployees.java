package motor.mechanic.finder.fyp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import motor.mechanic.finder.fyp.Adapters.MyEmployeesAdapter;
import motor.mechanic.finder.fyp.Adapters.WorkshopRequestsAdapter;
import motor.mechanic.finder.fyp.DataModels.MechanicDataModel;
import motor.mechanic.finder.fyp.DataModels.WorkshopRequestDataModel;
import motor.mechanic.finder.fyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyEmployees extends AppCompatActivity {

    TextView noReqTxt;
    DatabaseReference req_reference;
    RecyclerView requestsRV;
    ImageButton requestsMapActivity;
    List<MechanicDataModel> requestData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_employees);
        
        req_reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Workshop").child("All").child("Employees");
        initWidgets();

    }

    @Override
    protected void onStart() {
        super.onStart();
        req_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (requestData.size() > 0) {
                    requestData.clear();
                }
                for (DataSnapshot reqSnapshot : dataSnapshot.getChildren()) {
                    MechanicDataModel dataModel = reqSnapshot.getValue(MechanicDataModel.class);
                    requestData.add(dataModel);
                }
                if (requestData.size() > 0) {
                    noReqTxt.setVisibility(View.GONE);
                    requestsRV.setVisibility(View.VISIBLE);
                } else {
                    noReqTxt.setVisibility(View.VISIBLE);
                    requestsRV.setVisibility(View.GONE);
                }
                MyEmployeesAdapter requestsAdapter = new MyEmployeesAdapter(MyEmployees.this, requestData);
                requestsRV.setAdapter(requestsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initWidgets() {
        noReqTxt = findViewById(R.id.noReqTxt);
        requestsMapActivity = findViewById(R.id.requestsMapActivity);
        requestsRV = findViewById(R.id.empRV);
        requestsRV.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyEmployees.this, WorkshopChoiceActivity.class);
        startActivity(intent);
        finish();
    }
}
