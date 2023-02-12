package com.example.inventorymanagementsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Location;
import com.example.inventorymanagementsystem.models.UnitId;

import java.util.ArrayList;

public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<LocationRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Location> mLocationArrayList;
    private Context mContext;

    public LocationRecyclerViewAdapter(ArrayList<Location> locationArrayList, Context context) {
        mLocationArrayList = locationArrayList;
        mContext = context;
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
        Location location = mLocationArrayList.get(position);
        holder.tvLocationName.setText(location.getName());
        holder.tvZone.setText(location.getZone());
        holder.tvStatus.setText(location.getStatus());

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));
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
