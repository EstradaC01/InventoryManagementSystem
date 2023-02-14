package com.example.inventorymanagementsystem.views.adapters;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Location;

import java.util.ArrayList;
public class AddLocationRecyclerViewAdapter extends RecyclerView.Adapter<AddLocationRecyclerViewAdapter.ViewHolder>{
    private ArrayList<Location> mLocationArrayList;
    private Context mContext;

    private Activity mActivity;

    public AddLocationRecyclerViewAdapter(ArrayList<Location> locationArrayList, Context context, Activity activity) {
        mLocationArrayList = locationArrayList;
        mContext = context;
        mActivity = activity;
    }

    @NonNull
    @Override
    public AddLocationRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context  context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.location, parent, false);

        return new AddLocationRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddLocationRecyclerViewAdapter.ViewHolder holder, int position) {
        Location location = mLocationArrayList.get(position);
        holder.tvLocationName.setText(location.getName());
        holder.tvZone.setText(location.getZone());
        holder.tvStatus.setText(location.getStatus());
        holder.linearLayout.setVisibility(View.GONE);
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent
                Intent resultIntent = new Intent();
                resultIntent.putExtra("Location", location.getName());
                resultIntent.putExtra("Zone", location.getZone());
                mActivity.setResult(Activity.RESULT_OK, resultIntent);
                mActivity.finish();
            }
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

        private final LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocationName = itemView.findViewById(R.id.tvLocationName);
            tvZone = itemView.findViewById(R.id.tvLocationZone);
            tvStatus = itemView.findViewById(R.id.tvLocationStatus);
            linearLayout = itemView.findViewById(R.id.linearLayoutLocationTapForDetails);
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
