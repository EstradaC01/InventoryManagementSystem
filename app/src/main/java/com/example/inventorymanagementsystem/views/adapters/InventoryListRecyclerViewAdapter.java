package com.example.inventorymanagementsystem.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.UnitId;

import java.util.ArrayList;

public class InventoryListRecyclerViewAdapter extends RecyclerView.Adapter<InventoryListRecyclerViewAdapter.ViewHolder> {

    private ArrayList<UnitId> mUnitIdArrayList;
    private Context context;

    private String currentProductId = "";
    private int productCount = 0;

    public InventoryListRecyclerViewAdapter(ArrayList<UnitId> mUnitIdArrayList, Context context) {
        this.mUnitIdArrayList = mUnitIdArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public InventoryListRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if(viewType == 0) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_tag, parent, false);
//            return new ViewHolder(view);
//        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_card, parent, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryListRecyclerViewAdapter.ViewHolder holder, int position) {

        UnitId unit = mUnitIdArrayList.get(position);


            holder.tvProductId.setText("Product ID: " + unit.getProductId());

            holder.tvLocationName.setText(unit.getLocation());
            holder.tvLocationZone.setText(unit.getZone());
            holder.tvPiecesPerBox.setText(unit.getPiecesPerBox());
            holder.tvNoOfBoxes.setText(unit.getNumberOfBoxes());
            holder.tvTotalPieces.setText(unit.getTotalPieces());
            holder.tvAvailableUnits.setText(unit.getPiecesAvailable());
            holder.tvMarkedUnits.setText(unit.getPiecesMarked());
            holder.tvUnitId.setText(unit.getUnitId());


        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));
    }

    @Override
    public int getItemCount() {
        return mUnitIdArrayList.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//
//        int returnvalue = 0;
//        for (int i = 0; i < mUnitIdArrayList.size(); i++) {
//            if(mUnitIdArrayList.get(i).getProductId().equals(mUnitIdArrayList.get(position).getProductId()) && productCount == 0) {
//                currentProductId = mUnitIdArrayList.get(i).getProductId();
//                for(int j = 0; j < mUnitIdArrayList.size(); j++) {
//                    if(currentProductId.equals(mUnitIdArrayList.get(j).getProductId())) {
//                        productCount++;
//                    }
//                }
//                returnvalue = 0;
//                break;
//            } else {
//                productCount--;
//                returnvalue = 1;
//                break;
//            }
//        }
//        return returnvalue;
//    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvLocationName;
        private final TextView tvLocationZone;
        private final TextView tvPiecesPerBox;
        private final TextView tvNoOfBoxes;
        private final TextView tvTotalPieces;
        private final TextView tvAvailableUnits;
        private final TextView tvMarkedUnits;
        private final TextView tvProductId;
        private final TextView tvUnitId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

                tvProductId = itemView.findViewById(R.id.tvProductNameLocationCard);
                tvLocationName = itemView.findViewById(R.id.tvLocationNameLocationCard);
                tvLocationZone = itemView.findViewById(R.id.tvLocationZoneLocationCard);
                tvPiecesPerBox = itemView.findViewById(R.id.tvLocationPiecesPerBoxLocationCard);
                tvNoOfBoxes = itemView.findViewById(R.id.tvLocationNoBoxesLocationCard);
                tvTotalPieces = itemView.findViewById(R.id.tvLocationTotalPiecesLocationCard);
                tvAvailableUnits = itemView.findViewById(R.id.tvLocationAvailableUnitsLocationCard);
                tvMarkedUnits = itemView.findViewById(R.id.tvLocationMarkedUnitsLocationCard);
                tvUnitId = itemView.findViewById(R.id.tvLocationUnitIdLocationCard);
        }
    }

    public void setFilteredList(ArrayList<UnitId> filteredList) {
        mUnitIdArrayList = filteredList;
        notifyDataSetChanged();
    }

}
