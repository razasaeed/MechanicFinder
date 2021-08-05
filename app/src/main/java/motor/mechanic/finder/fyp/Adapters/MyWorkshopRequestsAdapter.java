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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import motor.mechanic.finder.fyp.DataModels.MechanicDataModel;
import motor.mechanic.finder.fyp.DataModels.WorkshopRequestDataModel;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyWorkshopRequestsAdapter extends RecyclerView.Adapter<MyWorkshopRequestsAdapter.MyAdapter> {

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

    public MyWorkshopRequestsAdapter(Context context, List<WorkshopRequestDataModel> data) {
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

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {

        MapView mMapView;
        LinearLayout secondLayout;
        Button approveBtn;
        ImageButton locationBtn;
        TextView requestUserNameTxt, requestUserPhoneTxt, requestTimeTxt, requestAddressTxt;

        public MyAdapter(View itemView) {
            super(itemView);

            secondLayout = itemView.findViewById(R.id.secondLayout);
            mMapView = (MapView) itemView.findViewById(R.id.requestMap);
            requestUserNameTxt = itemView.findViewById(R.id.requestUserNameTxt);
            approveBtn = itemView.findViewById(R.id.approveBtn);
            requestUserPhoneTxt = itemView.findViewById(R.id.requestUserPhoneTxt);
            requestTimeTxt = itemView.findViewById(R.id.requestTimeTxt);
            requestAddressTxt = itemView.findViewById(R.id.requestAddressTxt);
            locationBtn = itemView.findViewById(R.id.locationBtn);

            approveBtn.setVisibility(View.GONE);
            locationBtn.setVisibility(View.GONE);
            mMapView.setVisibility(View.GONE);
            secondLayout.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mechanicDetailDialog(getAdapterPosition());
                }
            });
        }
    }

    public void mechanicDetailDialog(final int pos) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.assigned_mechanic_detail);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        final CircleImageView img_avatar = (CircleImageView) dialog.findViewById(R.id.img_avatar);
        final TextView tv_username = (TextView) dialog.findViewById(R.id.tv_username);
        final TextView cnicEdt = (TextView) dialog.findViewById(R.id.cnicEdt);
        final TextView usercontactEdt = (TextView) dialog.findViewById(R.id.usercontactEdt);
        final TextView emailEdt = (TextView) dialog.findViewById(R.id.emailEdt);
        final TextView addresstxt = (TextView) dialog.findViewById(R.id.addresstxt);
        final Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);

        Picasso.with(context).load(data.get(pos).getEmp_pic()).into(img_avatar);
        tv_username.setText(data.get(pos).getEmp_name());
        cnicEdt.setText(data.get(pos).getEmp_cnic());
        usercontactEdt.setText(data.get(pos).getEmp_contact());
        emailEdt.setText(data.get(pos).getMechanic_email());
        addresstxt.setText(data.get(pos).getEmp_address());

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
