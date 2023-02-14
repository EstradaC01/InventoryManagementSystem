package com.example.inventorymanagementsystem.adapters;

import android.app.Activity;
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
import com.example.inventorymanagementsystem.models.PurchaseOrder;

import java.util.ArrayList;

public class FindPurchaseOrderRecyclerViewAdapter extends RecyclerView.Adapter<FindPurchaseOrderRecyclerViewAdapter.ViewHolder> {

    private ArrayList<PurchaseOrder> mArrayPurchaseOrder;
    private Context mContext;
    private Activity mActivity;

    public FindPurchaseOrderRecyclerViewAdapter(ArrayList<PurchaseOrder> mArrayPurchaseOrder, Context mContext) {
        this.mArrayPurchaseOrder = mArrayPurchaseOrder;
        this.mContext = mContext;
    }

    public FindPurchaseOrderRecyclerViewAdapter(ArrayList<PurchaseOrder> mArrayPurchaseOrder, Context mContext, Activity mActivity) {
        this.mArrayPurchaseOrder = mArrayPurchaseOrder;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.purchase_order_card, parent, false);

        return new FindPurchaseOrderRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PurchaseOrder purchaseOrder = mArrayPurchaseOrder.get(position);
        holder.poNumber.setText(purchaseOrder.getPoNumber());
        holder.arrivalDate.setText(purchaseOrder.getAnticipatedArrivalDate());
        holder.shipFrom.setText(purchaseOrder.getShippingFrom());

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));

        holder.itemView.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("PoNumber", purchaseOrder.getPoNumber());
            mActivity.setResult(Activity.RESULT_OK, resultIntent);
            mActivity.finish();
        });
    }

    @Override
    public int getItemCount() {
        return mArrayPurchaseOrder.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView poNumber;
        private final TextView arrivalDate;
        private final TextView shipFrom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            poNumber = itemView.findViewById(R.id.purchaseOrderCardPo);
            arrivalDate = itemView.findViewById(R.id.purchaseOrderCardArrivalDate);
            shipFrom = itemView.findViewById(R.id.purchaseOrderCardShipFrom);
        }
    }

    public void setSearchList(ArrayList<PurchaseOrder> _searchList) {
        mArrayPurchaseOrder = _searchList;
        notifyDataSetChanged();
    }

    public void setFilteredList(ArrayList<PurchaseOrder> _filteredList) {
        mArrayPurchaseOrder = _filteredList;
        notifyDataSetChanged();
    }
}
