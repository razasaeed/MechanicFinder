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

import motor.mechanic.finder.fyp.DataModels.ProductsDataModel;
import motor.mechanic.finder.fyp.R;
import motor.mechanic.finder.fyp.SharedData.MechanicSharedClass;
import motor.mechanic.finder.fyp.UI.NearestMechanicsList;
import motor.mechanic.finder.fyp.UI.ServiceDetail_UI;
import motor.mechanic.finder.fyp.UI.SubServiceDetail_UI;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ServiceProductsAdapter extends RecyclerView.Adapter<ServiceProductsAdapter.MyAdapter> {

    Context context;
    List<ProductsDataModel> data;
    private LayoutInflater inflater;

    public ServiceProductsAdapter(Context context, List<ProductsDataModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.products_custom_cell, parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        Picasso.with(context).load(data.get(position).getImage()).into(holder.product_image);
        holder.productNameTxt.setText(data.get(position).getName());
        holder.productPriceTxt.setText(data.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        CircleImageView product_image;
        TextView productNameTxt, productPriceTxt;

        public MyAdapter(View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_image);
            productNameTxt = itemView.findViewById(R.id.productNameTxt);
            productPriceTxt = itemView.findViewById(R.id.productPriceTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MechanicSharedClass.product_name = data.get(getAdapterPosition()).getName();
                    MechanicSharedClass.product_price = data.get(getAdapterPosition()).getPrice();
                    MechanicSharedClass.product_pic = data.get(getAdapterPosition()).getImage();

                    context.startActivity(new Intent(context, SubServiceDetail_UI.class));
                }
            });
        }
    }
}
