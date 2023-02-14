package com.example.inventorymanagementsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Products;

import java.util.ArrayList;

public class PurchaseOrderRecyclerViewAdapter extends RecyclerView.Adapter<PurchaseOrderRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Products> mProductArrayList;
    private Context mContext;

    public PurchaseOrderRecyclerViewAdapter(ArrayList<Products> mProductArrayList, Context mContext) {
        this.mProductArrayList = mProductArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PurchaseOrderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.product_line, parent, false);

        return new PurchaseOrderRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseOrderRecyclerViewAdapter.ViewHolder holder, int position) {
        Products product = mProductArrayList.get(position);
        holder.productId.setText(product.getProductId());
        holder.totalUnits.setText(product.getExpectedUnits()+" units");

    }

    @Override
    public int getItemCount() {
        return mProductArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView productId;
        private final TextView totalUnits;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productId = itemView.findViewById(R.id.productLineProductTitle);
            totalUnits = itemView.findViewById(R.id.productLineUnits);
        }
    }
}
