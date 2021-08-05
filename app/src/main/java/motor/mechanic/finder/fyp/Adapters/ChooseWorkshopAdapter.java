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
import motor.mechanic.finder.fyp.DataModels.WorkshopDataModel;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.MechanicSharedClass;
import motor.mechanic.finder.fyp.UI.MechanicDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChooseWorkshopAdapter extends RecyclerView.Adapter<ChooseWorkshopAdapter.MyAdapter> {

    Context context;
    List<WorkshopDataModel> data;
    private LayoutInflater inflater;

    public ChooseWorkshopAdapter(Context context, List<WorkshopDataModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.custom_choose_workshop, parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        Picasso.with(context).load(data.get(position).getWorkshop_img()).into(holder.imageWorkshop);
        holder.textWName.setText(data.get(position).getWorkshop_fullname());
        holder.textWPhone.setText(data.get(position).getWorkshop_phone());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {

        CircleImageView imageWorkshop;
        TextView textWName, textWPhone;

        public MyAdapter(View itemView) {
            super(itemView);

            imageWorkshop = itemView.findViewById(R.id.imageWorkshop);
            textWName = itemView.findViewById(R.id.textWName);
            textWPhone = itemView.findViewById(R.id.textWPhone);

        }
    }
}
