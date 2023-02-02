package com.example.inventorymanagementsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Zone;

import java.util.ArrayList;

public class ZoneRecyclerViewAdapter extends RecyclerView.Adapter<ZoneRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Zone> mZoneArrayList;
    private Context mContext;
    private ZoneRecyclerViewAdapter.OnItemClickListener listener;

    // we need interface for this
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    // now we need a method
    public void setOnItemClickListener(ZoneRecyclerViewAdapter.OnItemClickListener clickListener) {
        listener = clickListener;
    }

    public ZoneRecyclerViewAdapter(ArrayList<Zone> _zoneArrayList, Context _context) {
        mZoneArrayList = _zoneArrayList;
        mContext = _context;
    }
    /**
     * Called when RecyclerView needs a new {@link UnitTypeRecyclerViewAdapter.ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * { #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     *
     *  #onBindViewHolder(ViewHolder, int)
     */
    @NonNull
    @Override
    public ZoneRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.zone, parent, false);
        // we need to pass the listener
        return new ZoneRecyclerViewAdapter.ViewHolder(v, listener);
    }


    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link UnitTypeRecyclerViewAdapter.ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link UnitTypeRecyclerViewAdapter.ViewHolder#getBindingAdapterPosition()} which
     * will have the updated adapter position.
     * <p>
     * Override { #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ZoneRecyclerViewAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our model class
        Zone zone = mZoneArrayList.get(position);
        holder.tvZoneId.setText(zone.getZoneId());
        holder.tvZoneDescription.setText(zone.getDescription().toString());
        holder.tvZoneTypeOfUnit.setText(zone.getTypeOfUnit().toString());

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mZoneArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views
        private final TextView tvZoneId;
        private final TextView tvZoneDescription;
        private final TextView tvZoneTypeOfUnit;
        private final Button btnDeleteZone;

        public ViewHolder(@NonNull View itemView, ZoneRecyclerViewAdapter.OnItemClickListener listener)
        {
            super(itemView);
            // initializing our text views
            tvZoneId = itemView.findViewById(R.id.tvZoneZoneId);
            tvZoneDescription = itemView.findViewById(R.id.tvZoneName);
            tvZoneTypeOfUnit = itemView.findViewById(R.id.tvZoneTypeOfUnit);
            btnDeleteZone = itemView.findViewById(R.id.btnZoneDelete);

            btnDeleteZone.setOnClickListener(v -> {
                listener.onItemClick(getAbsoluteAdapterPosition());
            });
        }
    }
}
