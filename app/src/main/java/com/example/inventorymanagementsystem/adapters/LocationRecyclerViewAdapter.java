package com.example.inventorymanagementsystem.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Location;
import com.example.inventorymanagementsystem.views.LocationDetails;

import java.util.ArrayList;

public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<LocationRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Location> mLocationArrayList;
    private Context mContext;
    private Intent mIntent;

    public LocationRecyclerViewAdapter(ArrayList<Location> locationArrayList, Context context) {
        mLocationArrayList = locationArrayList;
        mContext = context;
    }

    public LocationRecyclerViewAdapter(ArrayList<Location> mLocationArrayList, Context mContext, Intent mIntent) {
        this.mLocationArrayList = mLocationArrayList;
        this.mContext = mContext;
        this.mIntent = mIntent;
    }

    @NonNull
    @Override
    public LocationRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context  context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.location, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationRecyclerViewAdapter.ViewHolder holder, int position) {
        String mWarehouse = (String) mIntent.getSerializableExtra("Warehouse");

        Location location = mLocationArrayList.get(position);
        holder.tvLocationName.setText(location.getName());
        holder.tvZone.setText(location.getZone());
        holder.tvStatus.setText(location.getStatus());

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), LocationDetails.class);
            i.putExtra("Object", location);
            i.putExtra("Warehouse", mWarehouse);
            v.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return mLocationArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvLocationName;
        private final TextView tvZone;
        private final TextView tvStatus;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocationName = itemView.findViewById(R.id.tvLocationName);
            tvZone = itemView.findViewById(R.id.tvLocationZone);
            tvStatus = itemView.findViewById(R.id.tvLocationStatus);
        }
    }

    public void setSearchList(ArrayList<Location> searchList) {
        mLocationArrayList = searchList;
        notifyDataSetChanged();
    }

    public void setFilteredList(ArrayList<Location> filteredList) {
        mLocationArrayList = filteredList;
        notifyDataSetChanged();
    }

}
