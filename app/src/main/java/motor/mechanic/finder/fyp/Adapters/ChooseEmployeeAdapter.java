package motor.mechanic.finder.fyp.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import motor.mechanic.finder.fyp.DataModels.MechanicDataModel;
import motor.mechanic.finder.fyp.DataModels.WorkshopDataModel;
import motor.mechanic.finder.fyp.DataModels.WorkshopRequestDataModel;
import motor.mechanic.finder.fyp.Mech_PersonalInfo;
import motor.mechanic.finder.fyp.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChooseEmployeeAdapter extends RecyclerView.Adapter<ChooseEmployeeAdapter.MyAdapter> {

    Context context;
    final String push_url = "https://perkier-reproductio.000webhostapp.com/wheels/push_notifs_user.php";
    List<MechanicDataModel> data;
    List<WorkshopRequestDataModel> workshopRequestData;
    Dialog dialog;
    KProgressHUD hud;
    int pos;
    private LayoutInflater inflater;

    public ChooseEmployeeAdapter(Context context, List<MechanicDataModel> data, List<WorkshopRequestDataModel> workshopRequestData, Dialog dialog, int pos) {
        this.context = context;
        this.data = data;
        this.workshopRequestData = workshopRequestData;
        this.dialog = dialog;
        this.pos = pos;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.custom_employee_layout, parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        Picasso.with(context).load(data.get(position).getEmpImg()).into(holder.imgMechanic);
        holder.mechanicNameTxt.setText(data.get(position).getFullname());
        holder.mechanicPhoneTxt.setText(data.get(position).getPhone_no());
        holder.mechanicExperienceTxt.setText(data.get(position).getWork_exp() + " years");
        holder.workshopNameTxt.setText("Working in " + data.get(position).getWorkshop_fullname());
        /*if (data.get(position).getOnline_status().equals("1")) {
            holder.activeIcon.setVisibility(View.VISIBLE);
        } else {
            holder.activeIcon.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {

        Button assignBtn;
        CircleImageView imgMechanic;
        TextView mechanicNameTxt, mechanicPhoneTxt, mechanicExperienceTxt, workshopNameTxt;
        ImageView activeIcon;

        public MyAdapter(View itemView) {
            super(itemView);

            assignBtn = itemView.findViewById(R.id.assignBtn);
            imgMechanic = itemView.findViewById(R.id.imgMechanic);
            mechanicNameTxt = itemView.findViewById(R.id.mechanicNameTxt);
            mechanicPhoneTxt = itemView.findViewById(R.id.mechanicPhoneTxt);
            mechanicExperienceTxt = itemView.findViewById(R.id.mechanicExperienceTxt);
            workshopNameTxt = itemView.findViewById(R.id.workshopNameTxt);
            activeIcon = itemView.findViewById(R.id.activeIcon);

            assignBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hud = KProgressHUD.create(context)
                            .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                            .setLabel("Please wait")
                            .setMaxProgress(100)
                            .show();
                    sendNotification(getAdapterPosition());
                }
            });

        }
    }

    public void sendNotification(final int adapterpos) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, push_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DatabaseReference reference = FirebaseDatabase.getInstance("https://motormechanicfinder-default-rtdb.firebaseio.com/").getReference("Mechanic_Requests").child("Workshops");
                reference.child(workshopRequestData.get(pos).getUser_phone() + "and" + workshopRequestData.get(pos).getWorkshop_phone()).child("emp_address").setValue(data.get(adapterpos).getAddress());
                reference.child(workshopRequestData.get(pos).getUser_phone() + "and" + workshopRequestData.get(pos).getWorkshop_phone()).child("emp_cnic").setValue(data.get(adapterpos).getCnic());
                reference.child(workshopRequestData.get(pos).getUser_phone() + "and" + workshopRequestData.get(pos).getWorkshop_phone()).child("emp_contact").setValue(data.get(adapterpos).getContact());
                reference.child(workshopRequestData.get(pos).getUser_phone() + "and" + workshopRequestData.get(pos).getWorkshop_phone()).child("emp_location").setValue(data.get(adapterpos).getLocation());
                reference.child(workshopRequestData.get(pos).getUser_phone() + "and" + workshopRequestData.get(pos).getWorkshop_phone()).child("emp_name").setValue(data.get(adapterpos).getFullname());
                reference.child(workshopRequestData.get(pos).getUser_phone() + "and" + workshopRequestData.get(pos).getWorkshop_phone()).child("emp_pic").setValue(data.get(adapterpos).getEmpImg());
                reference.child(workshopRequestData.get(pos).getUser_phone() + "and" + workshopRequestData.get(pos).getWorkshop_phone()).child("request_status").setValue("1");
                reference.child(workshopRequestData.get(pos).getUser_phone() + "and" + workshopRequestData.get(pos).getWorkshop_phone()).child("mechanic_email").setValue(data.get(adapterpos).getEmail(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        dialog.dismiss();
                        hud.dismiss();
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
                parameters.put("email", workshopRequestData.get(pos).getUsername());
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
