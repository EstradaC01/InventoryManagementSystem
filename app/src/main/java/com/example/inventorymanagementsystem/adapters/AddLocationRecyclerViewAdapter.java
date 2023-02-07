package com.example.inventorymanagementsystem.adapters;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
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

    /**
     * Called when RecyclerView needs a new {@link LocationRecyclerViewAdapter.ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * onBindViewHolder(LocationRecyclerViewAdapter.ViewHolder, int)
     */
    @NonNull
    @Override
    public AddLocationRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context  context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.location, parent, false);

        return new AddLocationRecyclerViewAdapter.ViewHolder(v);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link LocationRecyclerViewAdapter.ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link LocationRecyclerViewAdapter.ViewHolder#getBindingAdapterPosition()} which
     * will have the updated adapter position.
     * <p>
     * Override {link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
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
                mActivity.setResult(Activity.RESULT_OK, resultIntent);
                mActivity.finish();
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
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
}
