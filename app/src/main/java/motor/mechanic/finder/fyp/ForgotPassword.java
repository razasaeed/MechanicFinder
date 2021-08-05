package motor.mechanic.finder.fyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    Button btnResetLink;
    EditText etResetEmail;
     TextView tv2, tvEmail;
     FirebaseAuth mAuth;
     ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        initWidgets();
        buttonListener();
        mAuth = FirebaseAuth.getInstance();
    }

    private void buttonListener() {
        btnResetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etResetEmail.getText().toString();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                           // Toast.makeText(ForgotPassword.this, "A link to reset your password has been sent to you.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ForgotPassword.this, ResetPassword.class);
                            startActivity(i);

                        }else{
                            Toast.makeText(ForgotPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, ForgotPassword.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void initWidgets() {
        btnResetLink = findViewById(R.id.btnResetLink);
        etResetEmail = findViewById(R.id.etResetEmail);
        tv2 = findViewById(R.id.tv2);
        tvEmail = findViewById(R.id.tvEmail);
        imgback = findViewById(R.id.imgback);
    }
}
