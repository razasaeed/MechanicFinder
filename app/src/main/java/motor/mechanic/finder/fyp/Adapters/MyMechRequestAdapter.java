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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import motor.mechanic.finder.fyp.DataModels.FeedbackModel;
import motor.mechanic.finder.fyp.DataModels.MechanicDataModel;
import motor.mechanic.finder.fyp.DataModels.MechanicRequestDataModel;
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
import com.stepstone.apprating.AppRatingDialog;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMechRequestAdapter extends RecyclerView.Adapter<MyMechRequestAdapter.MyAdapter> {

    final String push_url = "https://perkier-reproductio.000webhostapp.com/wheels/push_notifs_user.php";
    DatabaseReference reference;
    Dialog progressDialog;
    protected LatLng start;
    KProgressHUD hud;
    protected LatLng end;
    float actualrating = 0;
    GoogleMap mMap;
    String[] arr;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    Context context;
    List<MechanicRequestDataModel> data;
    private LayoutInflater inflater;

    public MyMechRequestAdapter(Context context, List<MechanicRequestDataModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.custom_mymech_requests_layout, parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, final int position) {
        arr = data.get(position).getMechanic_email().split("@");
        holder.requestUserNameTxt.setText(arr[0] + "\n" + data.get(position).getMechanic_email());
        holder.requestUserPhoneTxt.setText(data.get(position).getUser_phone());
        holder.requestTimeTxt.setText(data.get(position).getRequest_time());
        holder.requestAddressTxt.setText(data.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {

        LinearLayout secondLayout;
        MapView mMapView;
        Button approveBtn;
        ImageButton locationBtn, rateBtn;
        TextView requestUserNameTxt, requestUserPhoneTxt, requestTimeTxt, requestAddressTxt;

        public MyAdapter(View itemView) {
            super(itemView);

            rateBtn = itemView.findViewById(R.id.rateBtn);
            secondLayout = itemView.findViewById(R.id.secondLayout);
            locationBtn = itemView.findViewById(R.id.locationBtn);
            mMapView = itemView.findViewById(R.id.requestMap);
            approveBtn = itemView.findViewById(R.id.approveBtn);
            requestUserNameTxt = itemView.findViewById(R.id.requestUserNameTxt);
            requestUserPhoneTxt = itemView.findViewById(R.id.requestUserPhoneTxt);
            requestTimeTxt = itemView.findViewById(R.id.requestTimeTxt);
            requestAddressTxt = itemView.findViewById(R.id.requestAddressTxt);

            rateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    workshopsDialog(getAdapterPosition());
                }
            });

            secondLayout.setVisibility(View.GONE);
            locationBtn.setVisibility(View.GONE);
            mMapView.setVisibility(View.GONE);
            approveBtn.setVisibility(View.GONE);

        }
    }

    public void workshopsDialog(final int pos) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_feedback);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        final EditText commentEdt = (EditText) dialog.findViewById(R.id.commentEdt);
        final RatingBar ratingbar = (RatingBar) dialog.findViewById(R.id.ratingbar);
        final TextView btnLater = (TextView) dialog.findViewById(R.id.btnLater);
        final TextView btnCancel = (TextView) dialog.findViewById(R.id.btnCancel);
        final TextView btnSubmit = (TextView) dialog.findViewById(R.id.btnSubmit);

        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                actualrating = rating;
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Feedback");
                String key = reference.push().getKey();
                FeedbackModel feedbackModel = new FeedbackModel(data.get(pos).getUsername(), data.get(pos).getMechanic_email(),
                        commentEdt.getText().toString(), String.valueOf(actualrating), getDateTime());
                reference.child(key).setValue(feedbackModel, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    public String getDateTime() {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        return dateToStr;
    }

}
