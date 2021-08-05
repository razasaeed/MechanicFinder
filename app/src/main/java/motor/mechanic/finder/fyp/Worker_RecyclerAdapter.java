package motor.mechanic.finder.fyp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static motor.mechanic.finder.fyp.R.id.tvPhoneNo;
import static motor.mechanic.finder.fyp.R.id.tvWorkerName;
import static motor.mechanic.finder.fyp.R.id.worker_img;

public class Worker_RecyclerAdapter extends RecyclerView.Adapter<Worker_RecyclerAdapter.RecyclerViewHolder>{

    private Context mContext;
    private List<Worker_Model> workersList;
    private OnItemClickListener mListener;

    Worker_RecyclerAdapter(Mech_WorkersInfo worker_items, List<Worker_Model> mWorkers) {
        mContext = worker_items;
        workersList = mWorkers;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.worker_rowmodel, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Worker_Model currentWorker = workersList.get(position);
        holder.nameTextView.setText(currentWorker.getEtWorkerName());
        holder.phoneTextView.setText(currentWorker.getEtPhoneNo());
        Picasso.with(mContext)
                .load(currentWorker.getWorker_image())
                .placeholder(R.drawable.ic_camera_alt_black_24dp)
                .centerCrop()
                .fit()
                .into(holder.workerImageView);
    }

    @Override
    public int getItemCount() {
        return workersList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {


        TextView nameTextView;
        TextView phoneTextView;
        ImageView workerImageView;

        //worker_rowmodel items are initialized

        RecyclerViewHolder(View itemView) {
            super(itemView);

            workerImageView = itemView.findViewById(worker_img);
            nameTextView =itemView.findViewById ( tvWorkerName );
            phoneTextView = itemView.findViewById(tvPhoneNo);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
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
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onShowItemClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteItemClick(position);
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
