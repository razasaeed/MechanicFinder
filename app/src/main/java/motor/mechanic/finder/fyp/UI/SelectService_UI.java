package motor.mechanic.finder.fyp.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import motor.mechanic.finder.fyp.Adapters.ServicesAdapter;
import motor.mechanic.finder.fyp.R;

public class SelectService_UI extends AppCompatActivity {

    RecyclerView servicesRV;
    Integer[] pics = {R.drawable.oil_change, R.drawable.brakes, R.drawable.tuning, R.drawable.battery, R.drawable.ac_services,
            R.drawable.belt_replacement, R.drawable.radiator, R.drawable.computer_scanning};
    String[] services = {"Oil Change", "Brake Service", "Tuning", "Battery Replacement", "AC Service", "Belt Replacement",
            "Radiator Service", "Computer Scanning"};
    String[] details = {"Engine lubricants and filters for routine maintenance",
            "Brake pads replacement and service", "Engine tuning, throttle body clean-up and more",
            "Get your car's battery replaced at your place", "Checkup, general service, gas refilling",
            "Replacing fan belt, generator belt and others", "Cleaning, fixing leakages and replacing bends",
            "Complete computerized OBD scanning and fixing errors"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service__ui);

        servicesRV = findViewById(R.id.servicesRV);
        servicesRV.setLayoutManager(new LinearLayoutManager(this));
        ServicesAdapter servicesAdapter = new ServicesAdapter(SelectService_UI.this, pics, services, details);
        servicesRV.setAdapter(servicesAdapter);
    }
}
