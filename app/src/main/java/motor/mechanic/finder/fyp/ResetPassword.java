package motor.mechanic.finder.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResetPassword extends AppCompatActivity {
    TextView tvlinkreset, tvcheck, tvbacklogin;
    ImageView imageView, imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initWidgets();
        buttonListener();
    }

    private void buttonListener() {
        tvbacklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPassword.this, Mechanic_Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPassword.this, ForgotPassword.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void initWidgets() {
        imageView = findViewById(R.id.imageView);
        tvlinkreset = findViewById(R.id.tvlinkreset);
        tvcheck = findViewById(R.id.tvcheck);
        tvbacklogin = findViewById(R.id.tvbacklogin);
        imgback = findViewById(R.id.imgback);
    }

}
