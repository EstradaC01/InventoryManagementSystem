package com.example.inventorymanagementsystem;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * { onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @NonNull
    @Override
    public ItemRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item,parent,false));
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
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override { #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ItemRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our model class

        String mCompanyCode = (String) theIntent.getSerializableExtra("CompanyCode");
        String mWarehouse = (String) theIntent.getSerializableExtra("Warehouse");
        Products products = mProductsArrayList.get(position);

        holder.productIdTv.setText(products.getProductId());
        holder.productDescriptionTv.setText(products.getProductId());

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(v.getContext(), ProductDetails.class);
                Intent i = new Intent(v.getContext(), ProductDetails.class);
                i.putExtra("Object", products);
                i.putExtra("CompanyCode", mCompanyCode);
                i.putExtra("Warehouse", mWarehouse);
                v.getContext().startActivity(i);
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
        return mProductsArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views
        private final TextView productIdTv;
        private final TextView productDescriptionTv;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            // initializing our text views
            productIdTv = itemView.findViewById(R.id.idTVItemCardProductId);
            productDescriptionTv = itemView.findViewById(R.id.idTVItemCardProductDescription);
        }
    }

    public void setFilteredList(ArrayList<Products> filteredList) {
        mProductsArrayList = filteredList;
        notifyDataSetChanged();
    }
}
