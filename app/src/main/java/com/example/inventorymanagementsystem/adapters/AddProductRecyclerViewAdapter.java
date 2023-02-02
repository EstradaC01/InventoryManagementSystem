package com.example.inventorymanagementsystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Products;


import java.util.ArrayList;

public class AddProductRecyclerViewAdapter extends RecyclerView.Adapter<AddProductRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Products> mProductArrayList;
    private Context mContext;
    private Activity mActivity;

    public AddProductRecyclerViewAdapter(ArrayList<Products> mProductArrayList, Context mContext, Activity mActivity) {
        this.mProductArrayList = mProductArrayList;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public AddProductRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.item, parent, false);

        return new AddProductRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddProductRecyclerViewAdapter.ViewHolder holder, int position) {
        Products product = mProductArrayList.get(position);
        holder.productId.setText(product.getProductId());
        holder.productDescription.setText(product.getProductDescription());
        holder.productUpc.setText(product.getProductUpc());

        Glide.with(mContext).load(product.getImageUri()).into(holder.itemImageView);

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));

        holder.itemView.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("Product", product.getProductId());
            mActivity.setResult(Activity.RESULT_OK, resultIntent);
            mActivity.finish();
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
        private final ImageView itemImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productId = itemView.findViewById(R.id.tvItemProductId);
            productDescription = itemView.findViewById(R.id.tvItemProductDescription);
            productUpc = itemView.findViewById(R.id.tvItemProductUPC);
            itemImageView = itemView.findViewById(R.id.imageViewItem);
        }
    }

    public void setSearchList(ArrayList<Products> searchList) {
        mProductArrayList = searchList;
        notifyDataSetChanged();
    }
}
