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

import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.MechanicSharedClass;
import motor.mechanic.finder.fyp.SharedData.ProductsSharedClass;
import motor.mechanic.finder.fyp.UI.NearestMechanicsList;
import motor.mechanic.finder.fyp.UI.ServiceDetail_UI;
import com.squareup.picasso.Picasso;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.MyAdapter> {

    Context context;
    Integer[] pics;
    String[] services;
    String[] details;
    private LayoutInflater inflater;

    public ServicesAdapter(Context context, Integer[] pics, String[] services, String[] details) {
        this.context = context;
        this.pics = pics;
        this.services = services;
        this.details = details;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.services_custom_cell, parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        Picasso.with(context).load(pics[position]).into(holder.serviceImg);
        holder.serviceNameTxt.setText(services[position]);
        holder.serviceDetailTxt.setText(details[position]);
    }

    @Override
    public int getItemCount() {
        return services.length;
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        ImageView serviceImg;
        TextView serviceNameTxt, serviceDetailTxt;

        public MyAdapter(View itemView) {
            super(itemView);
            serviceImg = itemView.findViewById(R.id.serviceImg);
            serviceNameTxt = itemView.findViewById(R.id.serviceNameTxt);
            serviceDetailTxt = itemView.findViewById(R.id.serviceDetailTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MechanicSharedClass.my_request_title = services[getAdapterPosition()];
                    MechanicSharedClass.my_request_detail = details[getAdapterPosition()];

                    ProductsSharedClass.product_type = services[getAdapterPosition()];
                    context.startActivity(new Intent(context, ServiceDetail_UI.class));

                    if (getAdapterPosition() == 0) {
                        ProductsSharedClass.product_type = "Oil";
                        context.startActivity(new Intent(context, ServiceDetail_UI.class));
                    } else if (getAdapterPosition() == 1) {
                        ProductsSharedClass.product_type = "Brake";
                        context.startActivity(new Intent(context, ServiceDetail_UI.class));
                    } else if (getAdapterPosition() == 3) {
                        ProductsSharedClass.product_type = "Battery";
                        context.startActivity(new Intent(context, ServiceDetail_UI.class));
                    }
                    else {
                        ProductsSharedClass.product_type = "Brake";
                        context.startActivity(new Intent(context, ServiceDetail_UI.class));
                    }
                }
            });
        }
    }
}
