package com.example.inventorymanagementsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

public class UserRVAdapter extends RecyclerView.Adapter<ItemRVAdapter.ViewHolder> {

    private ArrayList<Users> mUsersArrayList;
    private Context context;

    public UserRVAdapter(ArrayList<Users> usersArrayList, Context context) {
        this.mUsersArrayList = usersArrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public ItemRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemRVAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.user,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRVAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
