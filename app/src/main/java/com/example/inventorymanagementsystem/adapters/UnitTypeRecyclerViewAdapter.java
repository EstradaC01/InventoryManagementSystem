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
import com.example.inventorymanagementsystem.models.UnitType;

import java.util.ArrayList;


public class UnitTypeRecyclerViewAdapter extends RecyclerView.Adapter<UnitTypeRecyclerViewAdapter.ViewHolder> {

    private ArrayList<UnitType> mUnitTypeArrayList;
    private Context mContext;
    private OnItemClickListener listener;

    // we need interface for this
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    // now we need a method
    public void setOnItemClickListener(OnItemClickListener clickListener) {
        listener = clickListener;
    }

    public UnitTypeRecyclerViewAdapter(ArrayList<UnitType> _unitTypeArrayList, Context _context) {
        mUnitTypeArrayList = _unitTypeArrayList;
        mContext = _context;
    }
    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
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
     * @see #getItemViewType(int)
     *  #onBindViewHolder(ViewHolder, int)
     */
    @NonNull
    @Override
    public UnitTypeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.unit_type, parent, false);
        // we need to pass the listener
        return new ViewHolder(v, listener);
    }


    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getBindingAdapterPosition()} which
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
    public void onBindViewHolder(@NonNull UnitTypeRecyclerViewAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our model class
        UnitType unitType = mUnitTypeArrayList.get(position);
        holder.tvUnitTypeDescription.setText(unitType.getUnitType());
        holder.tvUnitTypeActive.setText(unitType.getEnabled().toString());

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mUnitTypeArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views
        private final TextView tvUnitTypeDescription;
        private final TextView tvUnitTypeActive;
        private final Button btnUnitTypeDelete;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener)
        {
            super(itemView);
            // initializing our text views
            tvUnitTypeDescription = itemView.findViewById(R.id.tvUnitTypeDescription);
            tvUnitTypeActive = itemView.findViewById(R.id.tvUnitTypeActive);
            btnUnitTypeDelete = itemView.findViewById(R.id.btnUnitTypeDelete);

            btnUnitTypeDelete.setOnClickListener(v -> {
                listener.onItemClick(getAbsoluteAdapterPosition());
            });
        }
    }
}
