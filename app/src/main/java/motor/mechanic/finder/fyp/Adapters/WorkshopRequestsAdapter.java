package motor.mechanic.finder.fyp.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import motor.mechanic.finder.fyp.Customization.RecyclerTouchListener;
import motor.mechanic.finder.fyp.DataModels.MechanicDataModel;
import motor.mechanic.finder.fyp.DataModels.MechanicRequestDataModel;
import motor.mechanic.finder.fyp.DataModels.WorkshopDataModel;
import motor.mechanic.finder.fyp.DataModels.WorkshopRequestDataModel;
import motor.mechanic.finder.fyp.Mech_PersonalInfo;
import motor.mechanic.finder.fyp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class WorkshopRequestsAdapter extends RecyclerView.Adapter<WorkshopRequestsAdapter.MyAdapter>
        implements RoutingListener, GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleApiClient.ConnectionCallbacks {

    final String picurl = "https://images.pexels.com/photos/1004109/pexels-photo-1004109.jpeg?auto=format%2Ccompress&cs=tinysrgb&dpr=1&w=500";
    List<MechanicDataModel> mechanicsData = new ArrayList<>();
    final String push_url = "https://perkier-reproductio.000webhostapp.com/wheels/push_notifs_user.php";
    DatabaseReference reference;
    DatabaseReference emp_reference, workshop_emp_reference;
    Dialog progressDialog;
    protected LatLng start;
    KProgressHUD hud;
    protected LatLng end;
    GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    Context context;
    List<WorkshopRequestDataModel> data;
    private LayoutInflater inflater;

    public WorkshopRequestsAdapter(Context context, List<WorkshopRequestDataModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.custom_requests_layout, parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, final int position) {
        holder.requestUserNameTxt.setText(data.get(position).getUsername());
        holder.requestUserPhoneTxt.setText(data.get(position).getUser_phone());
        holder.requestTimeTxt.setText(data.get(position).getRequest_time());
        holder.requestAddressTxt.setText(data.get(position).getAddress());
        Picasso.with(context).load(picurl).into(holder.img_avatar);

        if (data.get(position).getRequest_status().equals("0")) {
            holder.approveBtn.setText("Proceed");
        } else {
            holder.approveBtn.setText("Assigned");
            holder.approveBtn.setTextColor(Color.rgb(0, 200, 0));
        }

        if (holder.mMapView != null) {
            // Initialise the MapView
            holder.mMapView.onCreate(null);
            holder.mMapView.onResume();  //Probably U r missing this
            // Set the map ready callback to receive the GoogleMap object

            holder.mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    mMap = googleMap;
                    try {
                        boolean success = mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        context, R.raw.style_json_day));
                        if (!success) {
                            Log.e("MapsActivityRaw", "Style parsing failed.");
                        }
                    } catch (Resources.NotFoundException e) {
                        Log.e("MapsActivityRaw", "Can't find style.", e);
                    }

                    String[] splitLoc = data.get(position).getUser_location().split(",");
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng currentLatLng = new LatLng(Double.parseDouble(splitLoc[0]), Double.parseDouble(splitLoc[1]));
                    markerOptions.position(currentLatLng);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    googleMap.addMarker(markerOptions);

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(Double.parseDouble(splitLoc[0]), Double.parseDouble(splitLoc[1])))     // Sets the center of the map to location user
                            .zoom(14)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//                    splitLocation = mechanicsData.get(i).getLocation().split(",");
//                    LatLng mechanics = new LatLng(Double.parseDouble(splitLocation[0]), Double.parseDouble(splitLocation[1]));
//                    mMap.addMarker(new MarkerOptions().position(mechanics).title(mechanicsData.get(i).getEmail() + ", Mechanic")
//                            .icon(BitmapDescriptorFactory.fromBitmap(empMarker)));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(mechanics));

                }
            });

        }

    }

    public void route() {
        progressDialog = ProgressDialog.show(context, "Please wait.",
                "Fetching route information.", true);
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(start, end)
                .build();
        routing.execute();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class MyAdapter extends RecyclerView.ViewHolder {

        CircleImageView img_avatar;
        MapView mMapView;
        Button approveBtn;
        ImageButton locationBtn;
        TextView requestUserNameTxt, requestUserPhoneTxt, requestTimeTxt, requestAddressTxt;

        public MyAdapter(View itemView) {
            super(itemView);

            MapsInitializer.initialize(context);

            start = new LatLng(-57.965341647205726, 144.9987719580531);
            end = new LatLng(72.77492067739843, -9.998857788741589);
            mMapView = (MapView) itemView.findViewById(R.id.requestMap);
//            mMapView.onCreate(itemView.onSaveInstanceState());
//            mMapView.onResume();// needed to get the map to display immediately
            img_avatar=itemView.findViewById(R.id.img_avatar);
            requestUserNameTxt = itemView.findViewById(R.id.requestUserNameTxt);
            approveBtn = itemView.findViewById(R.id.approveBtn);
            requestUserPhoneTxt = itemView.findViewById(R.id.requestUserPhoneTxt);
            requestTimeTxt = itemView.findViewById(R.id.requestTimeTxt);
            requestAddressTxt = itemView.findViewById(R.id.requestAddressTxt);
            locationBtn = itemView.findViewById(R.id.locationBtn);

            locationBtn.setVisibility(View.GONE);
            locationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] splitLoc = data.get(getAdapterPosition()).getUser_location().split(",");
                    LatLng pos = new LatLng(Double.parseDouble(splitLoc[0]), Double.parseDouble(splitLoc[1]));
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(pos, 12);
                    mMap.moveCamera(update);
                }
            });

            approveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emp_reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Mechanic");
                    workshop_emp_reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Workshop").child("Employees");
                    if (data.get(getAdapterPosition()).getRequest_status().equals("0")) {
                        /*hud = KProgressHUD.create(context)
                                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                                .setLabel("Please wait")
                                .setMaxProgress(100)
                                .show();
                        sendNotification(getAdapterPosition());*/
                        workshopsDialog(getAdapterPosition());
                    } else {
                        Toast.makeText(context, "already assigned", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void workshopsDialog(final int pos) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_choose_emp_cell);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        final RecyclerView chooseEmployeeRV = (RecyclerView) dialog.findViewById(R.id.chooseEmployeeRV);
        final TextView cancelBtn = (TextView) dialog.findViewById(R.id.cancelBtn);
        chooseEmployeeRV.setLayoutManager(new LinearLayoutManager(context));

        emp_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mechanicsData.size() > 0) {
                    mechanicsData.clear();
                }
                for (DataSnapshot mechanic_snapshot : dataSnapshot.getChildren()) {
                    MechanicDataModel dataModel = mechanic_snapshot.getValue(MechanicDataModel.class);
                    mechanicsData.add(dataModel);
                }
                if (mechanicsData.size() > 0) {
                    workshop_emp_reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot mechanic_snapshot : dataSnapshot.getChildren()) {
                                MechanicDataModel dataModel = mechanic_snapshot.getValue(MechanicDataModel.class);
                                mechanicsData.add(dataModel);
                            }
                            ChooseEmployeeAdapter employeeAdapter = new ChooseEmployeeAdapter(context, mechanicsData, data, dialog, pos);
                            chooseEmployeeRV.setAdapter(employeeAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                Toast.makeText(context, mechanicsData.size() + "", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }

//        mLocationRequest = createLocationRequest();
//        mGoogleApiClient.connect();
    }

}
