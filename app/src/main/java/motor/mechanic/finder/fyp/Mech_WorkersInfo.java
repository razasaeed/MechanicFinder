package motor.mechanic.finder.fyp;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

public class Mech_WorkersInfo extends AppCompatActivity implements Worker_RecyclerAdapter.OnItemClickListener{

    Toolbar toolbar;
    ImageButton btn_Add;
    Button btnContinue;
    private RecyclerView mRecyclerView;
    private Worker_RecyclerAdapter mAdapter;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    FirebaseDatabase mFirebaseDatabase;
    private ValueEventListener mDBListener;
    private List<Worker_Model> mWorkers;
    FirebaseAuth mAuth;

    private void openDetailActivity(String[] data){
        Intent intent = new Intent(this, Worker_Detail.class);
        intent.putExtra("IMAGE_KEY",data[0]);
        intent.putExtra("NAME_KEY",data[1]);
        intent.putExtra("CNIC_KEY",data[2]);
        intent.putExtra("PHONE_KEY", data[3]);
        intent.putExtra("EXP_KEY", data[4]);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mech__workers_info);

        initWidgets();
        buttonListener();
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/");

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWorkers = new ArrayList<>();
        mAdapter = new Worker_RecyclerAdapter(Mech_WorkersInfo.this, mWorkers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(Mech_WorkersInfo.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = mFirebaseDatabase.getReference();

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mWorkers.clear();

                for (DataSnapshot workerSnapshot : dataSnapshot.getChildren()) {
                    Worker_Model upload = workerSnapshot.getValue(Worker_Model.class);
                    // assert upload != null;
                    upload.setKey(workerSnapshot.getKey());
                    mWorkers.add(upload);
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Mech_WorkersInfo.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        //TapTarget

        final Display display = getWindowManager().getDefaultDisplay();
        final Drawable droid = ContextCompat.getDrawable(this, R.drawable.ic_person_black_24dp);
        final Rect droidTarget = new Rect(0, 0, droid.getIntrinsicWidth() * 2, droid.getIntrinsicHeight() * 2);
        droidTarget.offset(display.getWidth() / 2, display.getHeight() / 2);

        // You don't always need a sequence, and for that there's a single time tap target
        final SpannableString spannedDesc = new SpannableString("It will allow you to add the details of the workers of your shop");
       // spannedDesc.setSpan(new UnderlineSpan(), spannedDesc.length() - "TapTargetView".length(), spannedDesc.length(), 0);
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.btnAdd), "This is an Add Person button!", spannedDesc)
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

    public void onItemClick(int position) {
        Worker_Model clickedWorker = mWorkers.get(position);
        String[] workerData={clickedWorker.getWorker_image(),clickedWorker.getEtWorkerName(),clickedWorker.getEtWorkerCNIC(),
                clickedWorker.getEtPhoneNo(), clickedWorker.getEtWExp()};
        openDetailActivity(workerData);
    }

    @Override
    public void onShowItemClick(int position) {
        Worker_Model clickedWorker = mWorkers.get(position);
        String[] workerData={clickedWorker.getWorker_image(),clickedWorker.getEtWorkerName(), clickedWorker.getEtWorkerCNIC(),
                clickedWorker.getEtPhoneNo(), clickedWorker.getEtWExp()};
        openDetailActivity(workerData);
    }

    @Override
    public void onDeleteItemClick(final int position) {
        final Worker_Model selectedItem = mWorkers.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getWorker_image());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();

                Toast.makeText(Mech_WorkersInfo.this, "Item Deleted!!!", Toast.LENGTH_SHORT).show();
            }
        });
//       mDatabaseRef = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference().child("Worker_Personal_Information").child(String.valueOf(position));
//       mDatabaseRef.removeValue();
//
//
//
//        Toast.makeText(getApplicationContext(), "Worker Information Deleted!!!", Toast.LENGTH_LONG).show();

    }


    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }


    private void buttonListener() {
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Mech_WorkersInfo.this, Worker_Upload.class);
                startActivity(intent);
                }

        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mech_WorkersInfo.this, Mech_ServicesDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private void initWidgets() {
        btn_Add = findViewById(R.id.btnAdd);
        toolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        btnContinue = findViewById(R.id.btnContinue);
    }
}
