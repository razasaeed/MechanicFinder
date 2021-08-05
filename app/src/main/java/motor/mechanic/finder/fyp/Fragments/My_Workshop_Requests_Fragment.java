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

import motor.mechanic.finder.fyp.Adapters.MyWorkshopRequestsAdapter;
import motor.mechanic.finder.fyp.Adapters.WorkshopRequestsAdapter;
import motor.mechanic.finder.fyp.DataModels.WorkshopRequestDataModel;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.UserSharedClass;
import motor.mechanic.finder.fyp.UI.Workshop_Requests;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class My_Workshop_Requests_Fragment extends Fragment {

    private View rootview;
    private RecyclerView requestsRV;
    private TextView noReqTxt;
    DatabaseReference req_reference;
    List<WorkshopRequestDataModel> requestData = new ArrayList<>();

    public My_Workshop_Requests_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //addHelper = new Adds();
        rootview = inflater.inflate(R.layout.activity_myworkshop_requests, container, false);

        req_reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Mechanic_Requests").child("Workshops");
        requestsRV = rootview.findViewById(R.id.requestsRV);
        requestsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        noReqTxt = rootview.findViewById(R.id.noReqTxt);

        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        req_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (requestData.size() > 0) {
                    requestData.clear();
                }
                for (DataSnapshot reqSnapshot : dataSnapshot.getChildren()) {
                    WorkshopRequestDataModel dataModel = reqSnapshot.getValue(WorkshopRequestDataModel.class);
                    if (dataModel.getUsername().equals(UserSharedClass.username)) {
                        requestData.add(dataModel);
                    }
                }
                if (requestData.size() > 0) {
                    noReqTxt.setVisibility(View.GONE);
                    requestsRV.setVisibility(View.VISIBLE);
                } else {
                    noReqTxt.setVisibility(View.VISIBLE);
                    requestsRV.setVisibility(View.GONE);
                }
                MyWorkshopRequestsAdapter requestsAdapter = new MyWorkshopRequestsAdapter(getActivity(), requestData);
                requestsRV.setAdapter(requestsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
