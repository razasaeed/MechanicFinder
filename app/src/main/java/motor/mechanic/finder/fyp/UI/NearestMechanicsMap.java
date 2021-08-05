package motor.mechanic.finder.fyp.UI;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import motor.mechanic.finder.fyp.BuildConfig;
import motor.mechanic.finder.fyp.DataModels.WorkshopDataModel;
import motor.mechanic.finder.fyp.SharedData.UserSharedClass;
import motor.mechanic.finder.fyp.SharedData.WorkshopSharedClass;
import motor.mechanic.finder.fyp.settings.SettingFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import motor.mechanic.finder.fyp.Adapters.MechanicsAdapter;
import motor.mechanic.finder.fyp.DataModels.MechanicDataModel;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.MechanicSharedClass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import im.delight.android.location.SimpleLocation;

public class NearestMechanicsMap extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer_layout;
    String[] splitMarklerTitle;
    ActionBarDrawerToggle mDrawerToggle;
    int height = 100;
    int width = 100;
    double cityLat, cityLng = 0.0;
    private SimpleLocation location;
    double latitude, longitude = 0.0;
    ImageButton mechanicsListActivity;
    private GoogleMap mMap;
    String[] splitLocation, splitWLocation;
    DatabaseReference reference, wrk_reference;
    AutocompleteSupportFragment autocompleteFragment;
    List<MechanicDataModel> mechanicsData = new ArrayList<>();
    List<String> lat = new ArrayList<>();
    List<String> lng = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_ui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.map_toolbar);
        toolbar.setTitle("Nearest Mechanics");
        setSupportActionBar(toolbar);

        Places.initialize(getApplicationContext(), "AIzaSyC4qZqhC02THhGhER4D3aCBGbYgEkN3Bus");
//        Places.initialize(getApplicationContext(), "AIzaSyB2re7cODKfF3oTwDYqRgcKwEppYPNUStk");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mechanicsListActivity = findViewById(R.id.mechanicsListActivity);
        reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Mechanic");
        wrk_reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Workshop").child("All");

        mechanicsListActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NearestMechanicsList.class));
                finish();
            }
        });

        location = new SimpleLocation(this);
        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();


        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("msg", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("msg", "An error occurred: " + status);
            }
        });

        //AutoCompleteFragment
        /*mAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mAutocompleteFragment.setHint(getResources().getString(R.string.home_search_hint));
        ((EditText) mAutocompleteFragment.getView().
                findViewById(R.id.place_autocomplete_search_input))
                .setTextSize(Integer.parseInt(getResources().getString(R.string.text_size_home_search)));
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        mAutocompleteFragment.setFilter(typeFilter);*/


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mechanicsMap);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json_day));
            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mechanicsData.size() > 0) {
                    mechanicsData.clear();
                }
                for (DataSnapshot mechanicSnapshot : dataSnapshot.getChildren()) {
                    MechanicDataModel dataModel = mechanicSnapshot.getValue(MechanicDataModel.class);
                    mechanicsData.add(dataModel);
                }

                BitmapDrawable bitmapdraw_emp = (BitmapDrawable) getResources().getDrawable(R.drawable.emp_marker);
                Bitmap b1 = bitmapdraw_emp.getBitmap();
                Bitmap empMarker = Bitmap.createScaledBitmap(b1, width, height, false);

                for (int i = 0; i < mechanicsData.size(); i++) {
                    if (mechanicsData.get(i).getOnline_status().equals("1")) {
                        Toast.makeText(NearestMechanicsMap.this, mechanicsData.get(i).getFullname(), Toast.LENGTH_SHORT).show();
                        splitLocation = mechanicsData.get(i).getLocation().split(",");
                        lat.add(splitLocation[0]);
                        lng.add(splitLocation[1]);
                        LatLng mechanics = new LatLng(Double.parseDouble(splitLocation[0]), Double.parseDouble(splitLocation[1]));
                        mMap.addMarker(new MarkerOptions().position(mechanics).title(mechanicsData.get(i).getEmail() + ", Mechanic")
                                .icon(BitmapDescriptorFactory.fromBitmap(empMarker)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mechanics));
                    }
                }

                BitmapDrawable bitmapdraw_wrk = (BitmapDrawable) getResources().getDrawable(R.drawable.workshop_marker);
                Bitmap b2 = bitmapdraw_wrk.getBitmap();
                Bitmap wrkMarker = Bitmap.createScaledBitmap(b2, 75, height, false);

                for (int i = 0; i < WorkshopSharedClass.workshopsData.size(); i++) {
                    Toast.makeText(NearestMechanicsMap.this, WorkshopSharedClass.workshopsData.get(i).getWorkshop_fullname(), Toast.LENGTH_SHORT).show();
                    splitWLocation = WorkshopSharedClass.workshopsData.get(i).getLocation().split(",");
                    lat.add(splitWLocation[0]);
                    lng.add(splitWLocation[1]);
                    LatLng workshops = new LatLng(Double.parseDouble(splitWLocation[0]), Double.parseDouble(splitWLocation[1]));
                    mMap.addMarker(new MarkerOptions().position(workshops).title(WorkshopSharedClass.workshopsData.get(i).getWorkshop_email() + ", Workshop")
                            .icon(BitmapDescriptorFactory.fromBitmap(wrkMarker)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(workshops));
                }

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(Double.parseDouble(lat.get(0)), Double.parseDouble(lng.get(0))))     // Sets the center of the map to location user
                        .zoom(10)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                autocompleteFragment.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        Toast.makeText(NearestMechanicsMap.this, place.getLatLng() + "", Toast.LENGTH_SHORT).show();
                        final String cityId = String.valueOf(place.getId());
                        final String cityName = String.valueOf(place.getName());
                        final String cityLat = String.valueOf(place.getLatLng().latitude);
                        final String cityLng = String.valueOf(place.getLatLng().longitude);

                        mMap.clear();

                        temp(place.getLatLng().latitude, place.getLatLng().longitude);

//                        CameraPosition cameraPosition = new CameraPosition.Builder()
//                                .target(new LatLng(Double.parseDouble(cityLat), Double.parseDouble(cityLng)))     // Sets the center of the map to location user
//                                .zoom(10)                   // Sets the zoom
//                                .bearing(90)                // Sets the orientation of the camera to east
//                                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
//                                .build();                   // Creates a CameraPosition from the builder
//                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }

                    @Override
                    public void onError(Status status) {

                    }
                });

                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        mMap.clear();
                        mMap.addCircle(new CircleOptions()
                                .center(latLng)
                                .radius(10)
                                .strokeWidth(2f)
                                .strokeColor(0x700a420b)
                                .fillColor(0x700a420b));
                        temp(latLng.latitude, latLng.longitude);
                    }
                });

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        final String title = marker.getTitle();
                        splitMarklerTitle = title.trim().split(",");

                        if (splitMarklerTitle[1].trim().equals("Mechanic")) {
                            UserSharedClass.user_type = "Mechanic";
                            if (MechanicSharedClass.mechanicsDataTwo.size() > 0) {
                                MechanicSharedClass.mechanicsDataTwo.clear();
                            }
                            for (int i = 0; i < mechanicsData.size(); i++) {
                                if (mechanicsData.get(i).getEmail().equals(splitMarklerTitle[0])) {
                                    MechanicDataModel mechanicDataModel = new MechanicDataModel();
                                    mechanicDataModel.setId(mechanicsData.get(i).getId());
                                    mechanicDataModel.setCity(mechanicsData.get(i).getCity());
                                    mechanicDataModel.setEmail(mechanicsData.get(i).getEmail());
                                    mechanicDataModel.setPhone_no(mechanicsData.get(i).getPhone_no());
                                    mechanicDataModel.setPassword(mechanicsData.get(i).getPassword());
                                    mechanicDataModel.setEmpImg(mechanicsData.get(i).getEmpImg());
                                    mechanicDataModel.setFullname(mechanicsData.get(i).getFullname());
                                    mechanicDataModel.setCnic(mechanicsData.get(i).getCnic());
                                    mechanicDataModel.setContact(mechanicsData.get(i).getContact());
                                    mechanicDataModel.setWork_exp(mechanicsData.get(i).getWork_exp());
                                    mechanicDataModel.setWorkshop_fullname(mechanicsData.get(i).getWorkshop_fullname());
                                    mechanicDataModel.setWorkshop_address(mechanicsData.get(i).getWorkshop_address());
                                    mechanicDataModel.setWorkshop_email(mechanicsData.get(i).getWorkshop_email());
                                    mechanicDataModel.setWorkshop_password(mechanicsData.get(i).getWorkshop_password());
                                    mechanicDataModel.setWorkshop_phone(mechanicsData.get(i).getWorkshop_phone());
                                    mechanicDataModel.setNum_of_workers(mechanicsData.get(i).getNum_of_workers());
                                    mechanicDataModel.setWorkshop_img(mechanicsData.get(i).getWorkshop_img());
                                    mechanicDataModel.setOnline_status(mechanicsData.get(i).getOnline_status());
                                    mechanicDataModel.setLocation(mechanicsData.get(i).getLocation());
                                    mechanicDataModel.setAddress(mechanicsData.get(i).getAddress());
                                    MechanicSharedClass.mechanicsDataTwo.add(mechanicDataModel);

                                    MechanicSharedClass.mechanicDetailCallCheck = "mechanic_map";
                                    startActivity(new Intent(getApplicationContext(), MechanicDetails.class));
                                    finish();
                                }
                            }
                        } else {
                            UserSharedClass.user_type = "Workshop";
                            if (WorkshopSharedClass.workshopsDataSingle.size() > 0) {
                                WorkshopSharedClass.workshopsDataSingle.clear();
                            }
                            for (int i = 0; i < WorkshopSharedClass.workshopsData.size(); i++) {
                                if (WorkshopSharedClass.workshopsData.get(i).getWorkshop_email().equals(splitMarklerTitle[0])) {
                                    WorkshopDataModel workshopDataModel = new WorkshopDataModel();
                                    workshopDataModel.setId(WorkshopSharedClass.workshopsData.get(i).getId());
                                    workshopDataModel.setWorkshop_fullname(WorkshopSharedClass.workshopsData.get(i).getWorkshop_fullname());
                                    workshopDataModel.setWorkshop_address(WorkshopSharedClass.workshopsData.get(i).getWorkshop_address());
                                    workshopDataModel.setWorkshop_email(WorkshopSharedClass.workshopsData.get(i).getWorkshop_email());
                                    workshopDataModel.setWorkshop_password(WorkshopSharedClass.workshopsData.get(i).getWorkshop_password());
                                    workshopDataModel.setWorkshop_phone(WorkshopSharedClass.workshopsData.get(i).getWorkshop_phone());
                                    workshopDataModel.setNum_of_workers(WorkshopSharedClass.workshopsData.get(i).getNum_of_workers());
                                    workshopDataModel.setWorkshop_img(WorkshopSharedClass.workshopsData.get(i).getWorkshop_img());
                                    workshopDataModel.setOnline_status(WorkshopSharedClass.workshopsData.get(i).getOnline_status());
                                    workshopDataModel.setLocation(WorkshopSharedClass.workshopsData.get(i).getLocation());
                                    workshopDataModel.setAddress(WorkshopSharedClass.workshopsData.get(i).getAddress());
                                    WorkshopSharedClass.workshopsDataSingle.add(workshopDataModel);

                                    MechanicSharedClass.mechanicDetailCallCheck = "workshop_map";
                                    startActivity(new Intent(getApplicationContext(), MechanicDetails.class));
                                    finish();
                                }
                            }
                        }

                        return false;
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /*@Override
    protected void onStart() {
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mechanicsData.size() > 0) {
                    mechanicsData.clear();
                }
                for (DataSnapshot mechanicSnapshot : dataSnapshot.getChildren()) {
                    MechanicDataModel dataModel = mechanicSnapshot.getValue(MechanicDataModel.class);
                    mechanicsData.add(dataModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    public void temp(final Double newCityLat, final Double newCityLng) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mechanicsData.size() > 0) {
                    mechanicsData.clear();
                }
                for (DataSnapshot mechanicSnapshot : dataSnapshot.getChildren()) {
                    MechanicDataModel dataModel = mechanicSnapshot.getValue(MechanicDataModel.class);
                    mechanicsData.add(dataModel);
                }

                BitmapDrawable bitmapdraw_emp = (BitmapDrawable) getResources().getDrawable(R.drawable.emp_marker);
                Bitmap b1 = bitmapdraw_emp.getBitmap();
                Bitmap empMarker = Bitmap.createScaledBitmap(b1, width, height, false);

                for (int i = 0; i < mechanicsData.size(); i++) {

                    Location loc1 = new Location("");
                    loc1.setLatitude(newCityLat);
                    loc1.setLongitude(newCityLng);

                    splitLocation = mechanicsData.get(i).getLocation().split(",");
                    Location loc2 = new Location("");
                    loc2.setLatitude(Double.parseDouble(splitLocation[0]));
                    loc2.setLongitude(Double.parseDouble(splitLocation[1]));

                    float distanceInMeters = loc1.distanceTo(loc2);

                    if (mechanicsData.get(i).getOnline_status().equals("1") && ((distanceInMeters / 1000) <= 10)) {
                        Toast.makeText(NearestMechanicsMap.this, mechanicsData.get(i).getFullname(), Toast.LENGTH_SHORT).show();
                        splitLocation = mechanicsData.get(i).getLocation().split(",");
                        lat.add(splitLocation[0]);
                        lng.add(splitLocation[1]);
                        LatLng mechanics = new LatLng(Double.parseDouble(splitLocation[0]), Double.parseDouble(splitLocation[1]));
                        mMap.addMarker(new MarkerOptions().position(mechanics).title(mechanicsData.get(i).getEmail() + ", Mechanic")
                                .icon(BitmapDescriptorFactory.fromBitmap(empMarker)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mechanics));
                    }
                }

                BitmapDrawable bitmapdraw_wrk = (BitmapDrawable) getResources().getDrawable(R.drawable.workshop_marker);
                Bitmap b2 = bitmapdraw_wrk.getBitmap();
                Bitmap wrkMarker = Bitmap.createScaledBitmap(b2, 75, height, false);

                for (int i = 0; i < WorkshopSharedClass.workshopsData.size(); i++) {
                    Toast.makeText(NearestMechanicsMap.this, WorkshopSharedClass.workshopsData.get(i).getWorkshop_fullname(), Toast.LENGTH_SHORT).show();
                    splitWLocation = WorkshopSharedClass.workshopsData.get(i).getLocation().split(",");
                    lat.add(splitWLocation[0]);
                    lng.add(splitWLocation[1]);
                    LatLng workshops = new LatLng(Double.parseDouble(splitWLocation[0]), Double.parseDouble(splitWLocation[1]));
                    mMap.addMarker(new MarkerOptions().position(workshops).title(WorkshopSharedClass.workshopsData.get(i).getWorkshop_email() + ", Workshop")
                            .icon(BitmapDescriptorFactory.fromBitmap(wrkMarker)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(workshops));
                }

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(newCityLat, newCityLng))     // Sets the center of the map to location user
                        .zoom(10)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        final String title = marker.getTitle();
                        splitMarklerTitle = title.trim().split(",");

                        if (splitMarklerTitle[1].trim().equals("Mechanic")) {
                            UserSharedClass.user_type = "Mechanic";
                            if (MechanicSharedClass.mechanicsDataTwo.size() > 0) {
                                MechanicSharedClass.mechanicsDataTwo.clear();
                            }
                            for (int i = 0; i < mechanicsData.size(); i++) {
                                if (mechanicsData.get(i).getEmail().equals(splitMarklerTitle[0])) {
                                    MechanicDataModel mechanicDataModel = new MechanicDataModel();
                                    mechanicDataModel.setId(mechanicsData.get(i).getId());
                                    mechanicDataModel.setCity(mechanicsData.get(i).getCity());
                                    mechanicDataModel.setEmail(mechanicsData.get(i).getEmail());
                                    mechanicDataModel.setPhone_no(mechanicsData.get(i).getPhone_no());
                                    mechanicDataModel.setPassword(mechanicsData.get(i).getPassword());
                                    mechanicDataModel.setEmpImg(mechanicsData.get(i).getEmpImg());
                                    mechanicDataModel.setFullname(mechanicsData.get(i).getFullname());
                                    mechanicDataModel.setCnic(mechanicsData.get(i).getCnic());
                                    mechanicDataModel.setContact(mechanicsData.get(i).getContact());
                                    mechanicDataModel.setWork_exp(mechanicsData.get(i).getWork_exp());
                                    mechanicDataModel.setWorkshop_fullname(mechanicsData.get(i).getWorkshop_fullname());
                                    mechanicDataModel.setWorkshop_address(mechanicsData.get(i).getWorkshop_address());
                                    mechanicDataModel.setWorkshop_email(mechanicsData.get(i).getWorkshop_email());
                                    mechanicDataModel.setWorkshop_password(mechanicsData.get(i).getWorkshop_password());
                                    mechanicDataModel.setWorkshop_phone(mechanicsData.get(i).getWorkshop_phone());
                                    mechanicDataModel.setNum_of_workers(mechanicsData.get(i).getNum_of_workers());
                                    mechanicDataModel.setWorkshop_img(mechanicsData.get(i).getWorkshop_img());
                                    mechanicDataModel.setOnline_status(mechanicsData.get(i).getOnline_status());
                                    mechanicDataModel.setLocation(mechanicsData.get(i).getLocation());
                                    mechanicDataModel.setAddress(mechanicsData.get(i).getAddress());
                                    MechanicSharedClass.mechanicsDataTwo.add(mechanicDataModel);

                                    MechanicSharedClass.mechanicDetailCallCheck = "mechanic_map";
                                    startActivity(new Intent(getApplicationContext(), MechanicDetails.class));
                                    finish();
                                }
                            }
                        } else {
                            UserSharedClass.user_type = "Workshop";
                            if (WorkshopSharedClass.workshopsDataSingle.size() > 0) {
                                WorkshopSharedClass.workshopsDataSingle.clear();
                            }
                            for (int i = 0; i < WorkshopSharedClass.workshopsData.size(); i++) {
                                if (WorkshopSharedClass.workshopsData.get(i).getWorkshop_email().equals(splitMarklerTitle[0])) {
                                    WorkshopDataModel workshopDataModel = new WorkshopDataModel();
                                    workshopDataModel.setId(WorkshopSharedClass.workshopsData.get(i).getId());
                                    workshopDataModel.setWorkshop_fullname(WorkshopSharedClass.workshopsData.get(i).getWorkshop_fullname());
                                    workshopDataModel.setWorkshop_address(WorkshopSharedClass.workshopsData.get(i).getWorkshop_address());
                                    workshopDataModel.setWorkshop_email(WorkshopSharedClass.workshopsData.get(i).getWorkshop_email());
                                    workshopDataModel.setWorkshop_password(WorkshopSharedClass.workshopsData.get(i).getWorkshop_password());
                                    workshopDataModel.setWorkshop_phone(WorkshopSharedClass.workshopsData.get(i).getWorkshop_phone());
                                    workshopDataModel.setNum_of_workers(WorkshopSharedClass.workshopsData.get(i).getNum_of_workers());
                                    workshopDataModel.setWorkshop_img(WorkshopSharedClass.workshopsData.get(i).getWorkshop_img());
                                    workshopDataModel.setOnline_status(WorkshopSharedClass.workshopsData.get(i).getOnline_status());
                                    workshopDataModel.setLocation(WorkshopSharedClass.workshopsData.get(i).getLocation());
                                    workshopDataModel.setAddress(WorkshopSharedClass.workshopsData.get(i).getAddress());
                                    WorkshopSharedClass.workshopsDataSingle.add(workshopDataModel);

                                    MechanicSharedClass.mechanicDetailCallCheck = "workshop_map";
                                    startActivity(new Intent(getApplicationContext(), MechanicDetails.class));
                                    finish();
                                }
                            }
                        }

                        return false;
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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


//        if (id == R.id.action_settings) {
//            Intent email = new Intent(Intent.ACTION_SENDTO);
//            email.setType("text/email");
//            email.setData(Uri.parse("mailto:" + getString(R.string.email_admin)));
//            email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_feedback));
//            email.putExtra(Intent.EXTRA_TEXT, "\n \n" + getResources().getString(R.string.desc_app_version) + BuildConfig.VERSION_NAME +
//                    "\n" + getResources().getString(R.string.desc_device_info) + Build.BRAND.toUpperCase() + " " + Build.MODEL + ", OS : " + Build.VERSION.RELEASE);
//            email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(Intent.createChooser(email, getString(R.string.intent_desc_link)));
//            return true;
//        }

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
