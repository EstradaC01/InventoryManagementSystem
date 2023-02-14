package com.example.inventorymanagementsystem.views.adapters;

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
import com.example.inventorymanagementsystem.views.UserDetails;
import com.example.inventorymanagementsystem.models.Users;

import java.util.ArrayList;

public class UserRVAdapter extends RecyclerView.Adapter<UserRVAdapter.ViewHolder> {

    private ArrayList<Users> mUsersArrayList;
    private Context context;
    private Intent theIntent;
    public UserRVAdapter(ArrayList<Users> usersArrayList, Context context, Intent in) {
        this.mUsersArrayList = usersArrayList;
        this.context = context;
        this.theIntent = in;
    }
    @NonNull
    @Override
    public UserRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.user,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserRVAdapter.ViewHolder holder, int position) {
        String mCompanyCode = (String) theIntent.getSerializableExtra("CompanyCode");
        String mWarehouse = (String) theIntent.getSerializableExtra("Warehouse");
        Users users = mUsersArrayList.get(position);
        holder.usernameIdTv.setText(users.getFirstName() + " " + users.getLastName());
        holder.emailIdTv.setText(users.getEmail());

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), UserDetails.class);
                i.putExtra("UserObject", users);
                i.putExtra("CompanyCode", mCompanyCode);
                i.putExtra("Warehouse", mWarehouse);
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsersArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views
        private final TextView usernameIdTv;
        private final TextView emailIdTv;

        public ViewHolder(@NonNull View userView)
        {
            super(userView);
            // initializing our text views
            usernameIdTv = itemView.findViewById(R.id.idTVUsername);
            emailIdTv = itemView.findViewById(R.id.idTVEmail);
        }
    }
}
