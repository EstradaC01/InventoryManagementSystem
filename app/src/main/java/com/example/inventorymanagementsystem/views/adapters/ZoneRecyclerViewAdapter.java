package com.example.inventorymanagementsystem.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
    @NonNull
    @Override
    public ZoneRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.zone, parent, false);
        // we need to pass the listener
        return new ZoneRecyclerViewAdapter.ViewHolder(v, listener);
    }


    @Override
    public void onBindViewHolder(@NonNull ZoneRecyclerViewAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our model class
        Zone zone = mZoneArrayList.get(position);
        holder.tvZoneId.setText(zone.getZoneId());
        holder.tvZoneDescription.setText(zone.getDescription().toString());
        holder.tvZoneTypeOfUnit.setText(zone.getTypeOfUnit().toString());

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));
    }

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
