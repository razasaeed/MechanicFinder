package motor.mechanic.finder.fyp.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import motor.mechanic.finder.fyp.R;

public class Book_UI extends AppCompatActivity implements View.OnClickListener {

    ImageView bookAppointmentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);
        bookAppointmentBtn = findViewById(R.id.bookAppointmentBtn);

        bookAppointmentBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bookAppointmentBtn:
                startActivity(new Intent(getApplicationContext(), SelectService_UI.class));
                break;
        }
    }
}
