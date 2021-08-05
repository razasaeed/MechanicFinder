package motor.mechanic.finder.fyp;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import motor.mechanic.finder.fyp.Adapters.RequestsAdapter;
import motor.mechanic.finder.fyp.DataModels.MechanicRequestDataModel;
import motor.mechanic.finder.fyp.UI.My_Requests;
import motor.mechanic.finder.fyp.UI.NearestMechanicsList;
import motor.mechanic.finder.fyp.settings.SettingFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Mechanic_Requests extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout drawer_layout;

    TextView noReqTxt;
    DatabaseReference req_reference;
    RecyclerView requestsRV;
    ImageButton requestsMapActivity;
    List<MechanicRequestDataModel> requestData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_ui_employee);
        Toolbar toolbar = (Toolbar) findViewById(R.id.map_toolbar);
        toolbar.setTitle("Mechanic Requests");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        req_reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Mechanic_Requests").child("Mechanics");
        initWidgets();

    }

    @Override
    protected void onStart() {
        super.onStart();
        req_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (requestData.size() > 0) {
                    requestData.clear();
                }
                for (DataSnapshot reqSnapshot : dataSnapshot.getChildren()) {
                    MechanicRequestDataModel dataModel = reqSnapshot.getValue(MechanicRequestDataModel.class);
                    requestData.add(dataModel);
                }
                if (requestData.size() > 0) {
                    noReqTxt.setVisibility(View.GONE);
                    requestsRV.setVisibility(View.VISIBLE);
                } else {
                    noReqTxt.setVisibility(View.VISIBLE);
                    requestsRV.setVisibility(View.GONE);
                }
                RequestsAdapter requestsAdapter = new RequestsAdapter(Mechanic_Requests.this, requestData);
                requestsRV.setAdapter(requestsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initWidgets() {
        noReqTxt = findViewById(R.id.noReqTxt);
        requestsMapActivity = findViewById(R.id.requestsMapActivity);
        requestsRV = findViewById(R.id.requestsRV);
        requestsRV.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.requestsMapActivity:

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favorites) {
//            Intent favIntent = new Intent(this, FavoritesActivity.class);
//            favIntent.putExtra("mUid", mUid);
//            startActivity(favIntent);
            return false;
        }

        if (id == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(), SettingFragment.class));
            /*Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_desc));
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getString(R.string.intent_desc_share)));*/

            return true;
        } else if (id == R.id.nav_requests) {
            startActivity(new Intent(getApplicationContext(), My_Requests.class));
            return true;

        } else if (id == R.id.nav_mech_list) {

            startActivity(new Intent(getApplicationContext(), NearestMechanicsList.class));
            return true;
        }
//      else if (id == R.id.nav_info) {
//            startActivity(new Intent(this, InfoActivity.class));
//            return true;
//        }
        else if (id == R.id.nav_signout) {
//            confirmSignOut(this, getString(R.string.alert_title_signout),
//                    getString(R.string.alert_title_signout_desc),
//                    getString(R.string.alert_choice_positive_signOut));

            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
