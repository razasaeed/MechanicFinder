package motor.mechanic.finder.fyp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import motor.mechanic.finder.fyp.SharedData.MechanicSharedClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class Mechanic_Login extends AppCompatActivity {
    EditText etEmail, etPwd;
    Button btnlogin;
    String stEmail, stpwd;
    FirebaseAuth firebaseAuth;
    TextView tvemail, tvpwd, tvLOGIN, tvDont, tvSignupHere, tvforgotPassword;
    ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic__login);

        initWidgets();
        firebaseAuth = FirebaseAuth.getInstance();
        buttonListener();
    }

    private void buttonListener() {
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stEmail = etEmail.getText().toString();
                stpwd = etPwd.getText().toString();
                if (isallFieldsFilled(stEmail, stpwd)) {
                    if (isEmailMatches(stEmail)) {
                        firebaseAuth.signInWithEmailAndPassword(stEmail, stpwd).
                                addOnCompleteListener(Mechanic_Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(Mechanic_Login.this, Mechanic_Requests.class);
                                            MechanicSharedClass.email = etEmail.getText().toString();
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(Mechanic_Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    } else {
                        Toast.makeText(Mechanic_Login.this, "Please type valid Email address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Mechanic_Login.this, "All fields are not filled", Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mechanic_Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mechanic_Login.this, Mechanic_Signup.class);
                startActivity(intent);
            }
        });
        tvSignupHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mechanic_Login.this, Mechanic_Signup.class);
                startActivity(intent);
            }
        });
    }


    public boolean isallFieldsFilled(String stEmail, String stpwd) {
        if (TextUtils.isEmpty(stEmail) || TextUtils.isEmpty(stpwd)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isEmailMatches(String stEmail) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            if (Patterns.EMAIL_ADDRESS.matcher(stEmail).matches()) {
                return true;
            } else

            {
                return false;
            }
        }
        return false;
    }

    private void initWidgets() {
        imgback = findViewById(R.id.imgback);
        etEmail = findViewById(R.id.etEmail);
        etPwd = findViewById(R.id.etPassword);
        btnlogin = findViewById(R.id.btn_login);
        tvemail = findViewById(R.id.tvemail);
        tvpwd = findViewById(R.id.tvPwd);
        tvLOGIN = findViewById(R.id.tvLOGIN);
        tvDont = findViewById(R.id.tvDont);
        tvSignupHere = findViewById(R.id.tvSignupHere);
        tvforgotPassword = findViewById(R.id.tvforgotPassword);
    }

}
