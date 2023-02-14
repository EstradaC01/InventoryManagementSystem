package com.example.inventorymanagementsystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Products;

import java.util.ArrayList;

public class PurchaseOrderProductListRecyclerViewAdapter extends RecyclerView.Adapter<PurchaseOrderProductListRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Products> mProductArrayList;
    private Context mContext;
    private Activity mActivity;


    public PurchaseOrderProductListRecyclerViewAdapter(ArrayList<Products> mProductArrayList, Context mContext, Activity mActivity) {
        this.mProductArrayList = mProductArrayList;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public PurchaseOrderProductListRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.purchase_order_product_card, parent, false);

        return new PurchaseOrderProductListRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseOrderProductListRecyclerViewAdapter.ViewHolder holder, int position) {
        Products product = mProductArrayList.get(position);
        holder.productId.setText(product.getProductId());
        holder.productDescription.setText(product.getProductId());
        holder.productUpc.setText(product.getProductUpc());
        holder.totalUnits.setText(product.getExpectedUnits());

        Glide.with(mContext).load(product.getImageUri()).into(holder.productImage);

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));

        holder.btnUpdate.setOnClickListener(v -> {
            if(!holder.totalUnits.getText().toString().isEmpty()) {
                product.setExpectedUnits(holder.totalUnits.getText().toString());
                notifyDataSetChanged();
            }
        });

        holder.btnRemove.setOnClickListener(v -> {
            mProductArrayList.remove(product);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mProductArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView productId;
        private final TextView productDescription;
        private final TextView productUpc;
        private final ImageView productImage;
        private final Button btnRemove;
        private final Button btnUpdate;
        private final EditText totalUnits;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productId = itemView.findViewById(R.id.purchaseOrderProductCardTvProductId);
            productDescription = itemView.findViewById(R.id.purchaseOrderProductCardTvProductDescription);
            productUpc = itemView.findViewById(R.id.purchaseOrderProductCardTvUPC);
            productImage = itemView.findViewById(R.id.purchaseOrderProductCardImage);
            btnRemove = itemView.findViewById(R.id.purchaseOrderProductCardBtnRemove);
            btnUpdate = itemView.findViewById(R.id.purchaseOrderProductCardBtnUpdate);
            totalUnits = itemView.findViewById(R.id.purchaseOrderProductCardUnits);

        }
    }
}
