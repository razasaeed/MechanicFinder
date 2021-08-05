package motor.mechanic.finder.fyp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import motor.mechanic.finder.fyp.Adapters.MyMechRequestAdapter;
import motor.mechanic.finder.fyp.Adapters.RequestsAdapter;
import motor.mechanic.finder.fyp.DataModels.MechanicDataModel;
import motor.mechanic.finder.fyp.DataModels.MechanicRequestDataModel;
import motor.mechanic.finder.fyp.Mechanic_Requests;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.UserSharedClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class My_Mechanics_Requests_Fragment extends Fragment {

    private View rootview;
    private RecyclerView requestsRV;
    private TextView noReqTxt;
    DatabaseReference reference, mechanicRef;
    List<MechanicRequestDataModel> requestData = new ArrayList<>();
    List<MechanicDataModel> mechanicsData = new ArrayList<>();

    public My_Mechanics_Requests_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //addHelper = new Adds();
        rootview = inflater.inflate(R.layout.activity_mymech_requests, container, false);

        reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Mechanic_Requests").child("Mechanics");
        mechanicRef = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Mechanic");

        requestsRV = rootview.findViewById(R.id.requestsRV);
        requestsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        noReqTxt = rootview.findViewById(R.id.noReqTxt);

        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (requestData.size() > 0) {
                    requestData.clear();
                }
                for (DataSnapshot reqSnapshot : dataSnapshot.getChildren()) {
                    MechanicRequestDataModel dataModel = reqSnapshot.getValue(MechanicRequestDataModel.class);
                    if (dataModel.getUsername().equals(UserSharedClass.username)) {
                        requestData.add(dataModel);
                    }
                }

                /*mechanicRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (mechanicsData.size() > 0) {
                            mechanicsData.clear();
                        }
                        for (DataSnapshot mechanicSnapshot : dataSnapshot.getChildren()) {
                            MechanicDataModel dataModel = mechanicSnapshot.getValue(MechanicDataModel.class);
                            mechanicsData.add(dataModel);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

                if (requestData.size() > 0) {
                    noReqTxt.setVisibility(View.GONE);
                    requestsRV.setVisibility(View.VISIBLE);
                } else {
                    noReqTxt.setVisibility(View.VISIBLE);
                    requestsRV.setVisibility(View.GONE);
                }

                MyMechRequestAdapter requestsAdapter = new MyMechRequestAdapter(getActivity(), requestData);
                requestsRV.setAdapter(requestsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
