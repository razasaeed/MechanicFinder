package motor.mechanic.finder.fyp.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import motor.mechanic.finder.fyp.Adapters.MechanicsAdapter;
import motor.mechanic.finder.fyp.DataModels.MechanicDataModel;
import motor.mechanic.finder.fyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NearestMechanicsList extends AppCompatActivity {

    ImageButton mechanicsMapActivity;
    RecyclerView mechanicsRV;
    DatabaseReference reference;
    List<MechanicDataModel> mechanicsData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_mechanics_list);

        mechanicsMapActivity = findViewById(R.id.mechanicsMapActivity);
        reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Mechanic");
        mechanicsRV = findViewById(R.id.mechanicsRV);
        mechanicsRV.setLayoutManager(new LinearLayoutManager(this));

        mechanicsMapActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NearestMechanicsMap.class));
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
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
                MechanicsAdapter adapter = new MechanicsAdapter(NearestMechanicsList.this, mechanicsData);
                mechanicsRV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
