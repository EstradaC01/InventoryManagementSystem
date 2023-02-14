package com.example.inventorymanagementsystem.adapters;

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

public class UnitIdRecyclerViewAdapter extends RecyclerView.Adapter<UnitIdRecyclerViewAdapter.ViewHolder> {
    private ArrayList<UnitId> mUnitIdArrayList;
    private Context mContext;

    public UnitIdRecyclerViewAdapter(ArrayList<UnitId> mUnitIdArrayList, Context mContext) {
        this.mUnitIdArrayList = mUnitIdArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context  context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.unit_id_card, parent, false);

        return new UnitIdRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UnitId u = mUnitIdArrayList.get(position);

        holder.productId.setText(u.getProductId());
        holder.unitId.setText(u.getUnitId());
        holder.piecesPerBox.setText(u.getPiecesPerBox());
        holder.numberOfBoxes.setText(u.getNumberOfBoxes());
        holder.totalPieces.setText(u.getTotalPieces());
        holder.availableUnits.setText(u.getPiecesAvailable());
        holder.markedUnits.setText(u.getPiecesMarked());

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));
    }

    @Override
    public int getItemCount() {
        return mUnitIdArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView productId;
        private final TextView unitId;
        private final TextView piecesPerBox;
        private final TextView numberOfBoxes;
        private final TextView totalPieces;
        private final TextView availableUnits;
        private final TextView markedUnits;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productId = itemView.findViewById(R.id.unitIdCardProductId);
            unitId = itemView.findViewById(R.id.unitIdCardUnitId);
            piecesPerBox = itemView.findViewById(R.id.unitIdCardPiecesPerBox);
            numberOfBoxes = itemView.findViewById(R.id.unitIdCardNumberOfBoxes);
            totalPieces = itemView.findViewById(R.id.unitIdCardTotalPieces);
            availableUnits = itemView.findViewById(R.id.unitIdCardAvailableUnits);
            markedUnits = itemView.findViewById(R.id.unitIdCardMarkedUnits);
        }
    }
}
