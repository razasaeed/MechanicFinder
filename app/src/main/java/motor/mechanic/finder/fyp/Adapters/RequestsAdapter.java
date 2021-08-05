package motor.mechanic.finder.fyp.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import motor.mechanic.finder.fyp.Customization.Util;
import motor.mechanic.finder.fyp.DataModels.MechanicDataModel;
import motor.mechanic.finder.fyp.DataModels.MechanicRequestDataModel;
import motor.mechanic.finder.fyp.Mech_PersonalInfo;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.MechanicSharedClass;
import motor.mechanic.finder.fyp.UI.MechanicDetails;
import motor.mechanic.finder.fyp.UI.NearestMechanicsMap;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.MyAdapter>
        implements RoutingListener, GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleApiClient.ConnectionCallbacks {

    //    String link = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
    final String push_url = "https://perkier-reproductio.000webhostapp.com/wheels/push_notifs_user.php";
    DatabaseReference reference;
    Dialog progressDialog;
    protected LatLng start;
    KProgressHUD hud;
    protected LatLng end;
    GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    Context context;
    List<MechanicRequestDataModel> data;
    private LayoutInflater inflater;

    public RequestsAdapter(Context context, List<MechanicRequestDataModel> data) {
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

        if (data.get(position).getRequest_status().equals("0")) {
            holder.approveBtn.setText("Approve");
        } else {
            holder.approveBtn.setText("Approved");
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
            requestUserNameTxt = itemView.findViewById(R.id.requestUserNameTxt);
            approveBtn = itemView.findViewById(R.id.approveBtn);
            requestUserPhoneTxt = itemView.findViewById(R.id.requestUserPhoneTxt);
            requestTimeTxt = itemView.findViewById(R.id.requestTimeTxt);
            requestAddressTxt = itemView.findViewById(R.id.requestAddressTxt);
            locationBtn = itemView.findViewById(R.id.locationBtn);

//            locationBtn.setVisibility(View.GONE);
            locationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] splitLoc = data.get(getAdapterPosition()).getUser_location().split(",");
                    LatLng pos = new LatLng(Double.parseDouble(splitLoc[0]), Double.parseDouble(splitLoc[1]));
                    String link = "http://maps.google.com/maps?q=loc:" + splitLoc[0] + "," + splitLoc[1];
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    context.startActivity(browserIntent);
//                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(pos, 12);
//                    mMap.moveCamera(update);
                }
            });

            approveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.get(getAdapterPosition()).getRequest_status().equals("0")) {
                        hud = KProgressHUD.create(context)
                                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                                .setLabel("Please wait")
                                .setMaxProgress(100)
                                .show();
                        sendNotification(getAdapterPosition());
                    } else {
                        Toast.makeText(context, "already approved", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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

    public void sendNotification(final int pos) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, push_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Mechanic_Requests").child("Mechanics");
                reference.child(data.get(pos).getKey()).child("request_status").setValue("1", new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        hud.dismiss();
                        Toast.makeText(context, data.get(pos).getUsername(), Toast.LENGTH_SHORT).show();
                    }
                });
//                Toast.makeText(context, "Request Sent", Toast.LENGTH_SHORT).show();
//                context.startActivity(new Intent(context, NearestMechanicsMap.class));
//                ((Activity) context).finish();
//                Toast.makeText(context, "User Added Successfully", Toast.LENGTH_SHORT).show();
                Log.d("notif", "sent");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, "Communication Error!", Toast.LENGTH_SHORT).show();
                    hud.dismiss();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context, "Authentication Error!", Toast.LENGTH_SHORT).show();
                    hud.dismiss();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context, "Server Side Error!", Toast.LENGTH_SHORT).show();
                    hud.dismiss();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error!", Toast.LENGTH_SHORT).show();
                    hud.dismiss();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Parse Error!", Toast.LENGTH_SHORT).show();
                    hud.dismiss();
                }
//                Toast.makeText(MainActivity.this, "Error in connection insertion"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", data.get(pos).getUsername());
//                parameters.put("Email", "")
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

}
