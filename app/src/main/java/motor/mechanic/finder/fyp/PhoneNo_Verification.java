package motor.mechanic.finder.fyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class PhoneNo_Verification extends AppCompatActivity {
    String phno, mVerificationId, code ;
    Button btnverify;
    EditText etcode;
    TextView tvnumber, tvresend;
    private FirebaseAuth firebaseAuth;
    TextView tvVerify, tv2;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_no__verification);

        initWidgets();
        buttonListener();

        Bundle bundle = getIntent().getExtras();
        String value = bundle.getString("phno");
        tvnumber.setText(value);
        phno = getIntent().getStringExtra("phno");
        sendVerificationCode(phno);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void buttonListener() {
        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                code = etcode.getText().toString().trim();

                if(code.isEmpty() || code.length() < 6){
                    code = etcode.getText().toString();
                    etcode.setError("Enter code....");
                    etcode.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });

        tvresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resendVerificationCode(phno, mResendToken);
//                resendVerificationCode(tvnumber.getText().toString(), mResendToken);
                code = etcode.getText().toString().trim();

                if(code.isEmpty() || code.length() < 6){
                    code = etcode.getText().toString();
                    etcode.setError("Enter code....");
                    etcode.requestFocus();
                    return;
                }

                verifyCode(code);

            }
        });

    }

    private void initWidgets() {
        btnverify = findViewById(R.id.btnverify);
        etcode = findViewById(R.id.etverificicationcode);
        tvnumber = findViewById(R.id.tvNumber);
        tvVerify = findViewById(R.id.tvVerify);
        tv2 = findViewById(R.id.tv2);
        tvresend = findViewById(R.id.tvResend);

    }

    private void verifyCode(String code){

        code = etcode.getText().toString().trim();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Intent intent = new Intent(PhoneNo_Verification.this, User_Name.class);
                    startActivity(intent);
                }
                    else{
                    Toast.makeText(PhoneNo_Verification.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

                }
            });
        }


    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                 mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                etcode.setText(code);
                //verifying the code
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneNo_Verification.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            mResendToken = forceResendingToken;
        }
    };

    private void resendVerificationCode(String mobile,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks,
               token);
    }
}
