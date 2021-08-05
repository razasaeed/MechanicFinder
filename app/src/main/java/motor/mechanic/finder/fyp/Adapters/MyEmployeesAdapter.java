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

public class MyEmployeesAdapter extends RecyclerView.Adapter<MyEmployeesAdapter.MyAdapter> {

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
    List<MechanicDataModel> data;
    private LayoutInflater inflater;

    public MyEmployeesAdapter(Context context, List<MechanicDataModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.my_employees_layout, parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, final int position) {
        holder.requestUserNameTxt.setText(data.get(position).getFullname());
        holder.requestUserPhoneTxt.setText(data.get(position).getPhone_no());
        holder.requestTimeTxt.setText(data.get(position).getEmail());
        holder.requestAddressTxt.setText(data.get(position).getAddress());
        Picasso.with(context).load(data.get(position).getEmpImg()).into(holder.img_avatar);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {

        CircleImageView img_avatar;
        Button approveBtn;
        TextView requestUserNameTxt, requestUserPhoneTxt, requestTimeTxt, requestAddressTxt;

        public MyAdapter(View itemView) {
            super(itemView);

            img_avatar = itemView.findViewById(R.id.img_avatar);
            requestUserNameTxt = itemView.findViewById(R.id.requestUserNameTxt);
            approveBtn = itemView.findViewById(R.id.deleteBtn);
            requestUserPhoneTxt = itemView.findViewById(R.id.requestUserPhoneTxt);
            requestTimeTxt = itemView.findViewById(R.id.requestTimeTxt);
            requestAddressTxt = itemView.findViewById(R.id.requestAddressTxt);

            approveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    workshop_emp_reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Workshop").child("Employees");
                    workshop_emp_reference.child(data.get(getAdapterPosition()).getId()).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

}
