package com.example.inventorymanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

public class UserRVAdapter extends RecyclerView.Adapter<UserRVAdapter.ViewHolder> {

    private ArrayList<Users> mUsersArrayList;
    private Context context;

    public UserRVAdapter(ArrayList<Users> usersArrayList, Context context) {
        this.mUsersArrayList = usersArrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public UserRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.user,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserRVAdapter.ViewHolder holder, int position) {
        Users users = mUsersArrayList.get(position);
        holder.usernameIdTv.setText(users.getFirstName() + " " + users.getLastName());
        holder.emailIdTv.setText(users.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
