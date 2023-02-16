package com.example.inventorymanagementsystem.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inventorymanagementsystem.views.ProductDetails;
import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Products;

import java.util.ArrayList;

public class ItemRVAdapter extends RecyclerView.Adapter<ItemRVAdapter.ViewHolder> {
    // creating variables for our ArrayList and context
    private ArrayList<Products> mProductsArrayList;
    private Context context;
    private Intent theIntent;

    public ItemRVAdapter(ArrayList<Products> productsArrayList, Context context, Intent in) {
        this.mProductsArrayList = productsArrayList;
        this.context = context;
        this.theIntent = in;
    }

    @NonNull
    @Override
    public ItemRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ItemRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our model class
        String mWarehouse = (String) theIntent.getSerializableExtra("Warehouse");

        Products products = mProductsArrayList.get(position);

        holder.productIdTv.setText(products.getProductId());
        holder.productDescriptionTv.setText(products.getProductDescription());
        holder.productUPCTv.setText(products.getProductUpc());
        holder.productAvailableUnitsTv.setText(products.getAvailableUnits());
        Glide.with(context).load(products.getImageUri()).into(holder.itemImageView);

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(v.getContext(), ProductDetails.class);
                Intent i = new Intent(v.getContext(), ProductDetails.class);
                i.putExtra("Object", products);
                i.putExtra("Warehouse", mWarehouse);
                v.getContext().startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mProductsArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views
        private final TextView productIdTv;
        private final TextView productDescriptionTv;
        private final TextView productUPCTv;
        private final TextView productAvailableUnitsTv;
        private final ImageView itemImageView;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            // initializing our text views
            productIdTv = itemView.findViewById(R.id.tvItemProductId);
            productDescriptionTv = itemView.findViewById(R.id.tvItemProductDescription);
            productUPCTv = itemView.findViewById(R.id.tvItemProductUPC);
            productAvailableUnitsTv = itemView.findViewById(R.id.tvItemProductAvailableUnits);
            itemImageView = itemView.findViewById(R.id.imageViewItem);
        }
    }

    public void setFilteredList(ArrayList<Products> filteredList) {
        mProductsArrayList = filteredList;
        notifyDataSetChanged();
    }
}
