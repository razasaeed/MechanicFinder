package motor.mechanic.finder.fyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import motor.mechanic.finder.fyp.DataModels.UserDataModel;
import motor.mechanic.finder.fyp.SharedData.UserSharedClass;
import motor.mechanic.finder.fyp.UI.UserLogin_UI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User_Password extends AppCompatActivity {

    EditText etPwd;
    Button btnContinue;
    TextView tvPwd, tv2;
    TextInputLayout textInputLayoutPassword;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__password);

        initWidgets();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/");
        myRef = mFirebaseDatabase.getReference("User_Signup");
        buttonListener();

    }

    private void buttonListener() {

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stpwd = etPwd.getText().toString();

                if (!stpwd.equals("")) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                    UserSharedClass.password = stpwd;
                    etPwd.setText("");
                }

                String key = myRef.push().getKey();
                UserDataModel dataModel = new UserDataModel(key, UserSharedClass.username, UserSharedClass.password, UserSharedClass.phone);
                myRef.child(key).setValue(dataModel, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        Intent intent = new Intent(User_Password.this, UserLogin_UI.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    private void initWidgets() {

        etPwd = findViewById(R.id.etPwd);
        btnContinue = findViewById(R.id.btnContinue);
        tvPwd = findViewById(R.id.tvPwd);
        tv2 = findViewById(R.id.tv2);
        textInputLayoutPassword = findViewById(R.id.textInputPassword);
    }
}
