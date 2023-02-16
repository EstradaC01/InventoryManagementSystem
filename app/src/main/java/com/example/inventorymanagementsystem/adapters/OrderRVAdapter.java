package com.example.inventorymanagementsystem.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Orders;
import com.example.inventorymanagementsystem.views.OrderDetails;
import com.example.inventorymanagementsystem.views.ProductDetails;

import java.util.ArrayList;

public class OrderRVAdapter extends RecyclerView.Adapter<OrderRVAdapter.ViewHolder>{

    private ArrayList<Orders> mOrdersArrayList;
    private Context context;
    private Intent theIntent;
    public OrderRVAdapter(ArrayList<Orders> ordersArrayList, Context context, Intent in) {
        this.mOrdersArrayList = ordersArrayList;
        this.context = context;
        this.theIntent = in;
    }
    @NonNull
    @Override
    public OrderRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderRVAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRVAdapter.ViewHolder holder, int position) {
        String mWarehouse = (String) theIntent.getSerializableExtra("Warehouse");
        Orders order = mOrdersArrayList.get(position);
        holder.customerIDTV.setText(order.getOrderID());
        holder.dateTV.setText(order.getOrderDate());
        holder.referenceTV.setText(order.getOrderReference());
        holder.customerNameTV.setText(order.getOrderCustomer());
        holder.statusTV.setText(order.getOrderStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), OrderDetails.class);
                i.putExtra("Order object", order);
                i.putExtra("Warehouse", mWarehouse);
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mOrdersArrayList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views
        private final TextView customerIDTV;
        private final TextView dateTV;
        private final TextView referenceTV;
        private final TextView customerNameTV;
        private final TextView statusTV;

        public ViewHolder(@NonNull View orderView)
        {
            super(orderView);
            // initializing our text views
            customerIDTV = itemView.findViewById(R.id.idTVCustomerID);
            dateTV = itemView.findViewById(R.id.idTVOrderDate);
            referenceTV = itemView.findViewById(R.id.idTVReferenceNumber);
            customerNameTV = itemView.findViewById(R.id.idTVCustomerName);
            statusTV = itemView.findViewById(R.id.idTVOrderStatus);
        }
    }
    public void setFilteredList(ArrayList<Orders> filteredList) {
        mOrdersArrayList = filteredList;
        notifyDataSetChanged();
    }
}
