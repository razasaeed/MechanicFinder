package motor.mechanic.finder.fyp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import motor.mechanic.finder.fyp.DataModels.MechanicDataModel;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.MechanicSharedClass;
import motor.mechanic.finder.fyp.UI.MechanicDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MechanicsAdapter extends RecyclerView.Adapter<MechanicsAdapter.MyAdapter> {

    Context context;
    List<MechanicDataModel> data;
    private LayoutInflater inflater;

    public MechanicsAdapter(Context context, List<MechanicDataModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.custom_mechanics_layout, parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        Picasso.with(context).load(data.get(position).getEmpImg()).into(holder.imgMechanic);
        holder.mechanicNameTxt.setText(data.get(position).getFullname());
        holder.mechanicPhoneTxt.setText(data.get(position).getPhone_no());
        holder.mechanicExperienceTxt.setText(data.get(position).getWork_exp() + " years");
        holder.workshopNameTxt.setText("Working in " + data.get(position).getWorkshop_fullname());
        if (data.get(position).getOnline_status().equals("1")) {
            holder.activeIcon.setVisibility(View.VISIBLE);
        } else {
            holder.activeIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {

        CircleImageView imgMechanic;
        TextView mechanicNameTxt, mechanicPhoneTxt, mechanicExperienceTxt, workshopNameTxt;
        ImageView activeIcon;

        public MyAdapter(View itemView) {
            super(itemView);

            imgMechanic = itemView.findViewById(R.id.imgMechanic);
            mechanicNameTxt = itemView.findViewById(R.id.mechanicNameTxt);
            mechanicPhoneTxt = itemView.findViewById(R.id.mechanicPhoneTxt);
            mechanicExperienceTxt = itemView.findViewById(R.id.mechanicExperienceTxt);
            workshopNameTxt = itemView.findViewById(R.id.workshopNameTxt);
            activeIcon = itemView.findViewById(R.id.activeIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    MechanicDataModel model = new MechanicDataModel();
//                    model.setId(data.get(getAdapterPosition()).getId());
//                    model.setCity(data.get(getAdapterPosition()).getCity());
//                    model.setEmail(data.get(getAdapterPosition()).getEmail());
//                    model.setPhone_no(data.get(getAdapterPosition()).getPhone_no());
//                    model.setPassword(data.get(getAdapterPosition()).getPassword());
//                    model.setEmpImg(data.get(getAdapterPosition()).getEmpImg());
//                    model.setFullname(data.get(getAdapterPosition()).getFullname());
//                    model.setCnic(data.get(getAdapterPosition()).getCnic());
//                    model.setContact(data.get(getAdapterPosition()).getContact());
//                    model.setWork_exp(data.get(getAdapterPosition()).getWork_exp());
//                    model.setWorkshop_fullname(data.get(getAdapterPosition()).getWorkshop_fullname());
//                    model.setWorkshop_address(data.get(getAdapterPosition()).getWorkshop_address());
//                    model.setWorkshop_email(data.get(getAdapterPosition()).getWorkshop_email());
//                    model.setWorkshop_password(data.get(getAdapterPosition()).getWorkshop_password());
//                    model.setWorkshop_phone(data.get(getAdapterPosition()).getWorkshop_phone());
//                    model.setNum_of_workers(data.get(getAdapterPosition()).getNum_of_workers());
//                    model.setWorkshop_img(data.get(getAdapterPosition()).getWorkshop_img());
//                    model.setOnline_status(data.get(getAdapterPosition()).getOnline_status());
//                    model.setLocation(data.get(getAdapterPosition()).getLocation());
//                    model.setAddress(data.get(getAdapterPosition()).getAddress());
//                    MechanicSharedClass.mechanicsData.add(model);

                    MechanicSharedClass.selected_mechanic_position = getAdapterPosition();
                    MechanicSharedClass.mechanicDetailCallCheck = "list";
                    context.startActivity(new Intent(context, MechanicDetails.class));
                }
            });

        }
    }
}
