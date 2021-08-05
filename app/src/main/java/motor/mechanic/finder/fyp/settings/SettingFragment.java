package motor.mechanic.finder.fyp.settings;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import motor.mechanic.finder.fyp.ChoiceActivity;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.UserSharedClass;
import motor.mechanic.finder.fyp.UI.NearestMechanicsMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hieuapp on 10/12/2017.
 */

public class SettingFragment extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference reference;
    Button updateBtn;
    EditText passwordEdt, usernameEdt, usercontactEdt;
    LinearLayout logoutBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);

        reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("User_Signup");

        usercontactEdt = findViewById(R.id.usercontactEdt);
        updateBtn = findViewById(R.id.updateBtn);
        passwordEdt = findViewById(R.id.passwordEdt);
        usernameEdt = findViewById(R.id.usernameEdt);
        logoutBtn = findViewById(R.id.logoutBtn);
        updateBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);

        usercontactEdt.setText(UserSharedClass.phone);
        usernameEdt.setText(UserSharedClass.username);
        passwordEdt.setText(UserSharedClass.password);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateBtn:
                reference.child(UserSharedClass.id).child("password").setValue(passwordEdt.getText().toString());
                reference.child(UserSharedClass.id).child("phone").setValue(usercontactEdt.getText().toString());
                reference.child(UserSharedClass.id).child("username").setValue(usernameEdt.getText().toString(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        Toast.makeText(SettingFragment.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.logoutBtn:
                startActivity(new Intent(getApplicationContext(), ChoiceActivity.class));
                finish();
                break;
        }
    }
}
