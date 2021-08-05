package motor.mechanic.finder.fyp;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class Service_RecyclerAdapter extends RecyclerView.Adapter<Service_RecyclerAdapter.ServiceViewHolder>{
    private Context mContext;
    private List<Service_Model> servicesList;
    private Service_RecyclerAdapter.OnItemClickListener mSerListener;
   // private Worker_RecyclerAdapter.OnItemClickListener mListener;

    Service_RecyclerAdapter(Mech_ServicesDetails service_items, List<Service_Model> mServices) {
        mContext = service_items;
        servicesList = mServices;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.service_rowmodel, viewGroup, false);
        return new ServiceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder serviceViewHolder, int i) {
        Service_Model currentService = servicesList.get(i);
        serviceViewHolder.tvSerID.setText(currentService.getEtSerID());
        serviceViewHolder.tvSerName.setText(currentService.getEtSerName());
        serviceViewHolder.tvSerPrice.setText(currentService.getEtSerPrice());
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }


    public class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView tvSerID, tvSerName, tvSerPrice;


         ServiceViewHolder(@NonNull View itemView) {
            super(itemView);


            tvSerID = itemView.findViewById(R.id.tvSerID);
            tvSerName =itemView.findViewById (R.id.tvSerName);
            tvSerPrice = itemView.findViewById(R.id.tvSerPrice);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View v) {
        if (mSerListener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                mSerListener.onItemClick(position);
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select Action");
        MenuItem showItem = menu.add( Menu.NONE, 1, 1, "Show");
        MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");

        showItem.setOnMenuItemClickListener(this);
        deleteItem.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (mSerListener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {

                switch (item.getItemId()) {
                    case 1:
                        mSerListener.onShowItemClick(position);
                        return true;
                    case 2:
                        mSerListener.onDeleteItemClick(position);
                        return true;
                }
            }
        }
        return false;
    }
}

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onShowItemClick(int position);
        void onDeleteItemClick(int position);
    }

    public void setOnItemClickListener(Mech_ServicesDetails listener) {
        mSerListener = listener;
    }
}

