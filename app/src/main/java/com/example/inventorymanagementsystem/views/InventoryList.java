package com.example.inventorymanagementsystem.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.InventoryListRecyclerViewAdapter;
import com.example.inventorymanagementsystem.models.UnitId;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class InventoryList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<UnitId> mArrayListUnitId;
    private InventoryListRecyclerViewAdapter mRecyclerViewAdapter;
    private FirebaseFirestore db;

    private Users mCurrentUser;
    private String mWarehouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_list);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Product Inquiry</font>"));

        mRecyclerView = findViewById(R.id.rvInventoryList);

        db=FirebaseFirestore.getInstance();

        //getting intent from ItemsSubMenu class along with User object
        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        mArrayListUnitId = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerViewAdapter = new InventoryListRecyclerViewAdapter(mArrayListUnitId, this);

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        CollectionReference unitIdRef = db.collection("Warehouses/"+mWarehouse+"/UnitId");

        unitIdRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d : list) {
                        UnitId u = d.toObject(UnitId.class);
                        mArrayListUnitId.add(u);
                    }

                    mRecyclerViewAdapter.notifyDataSetChanged();
                }

            }
        });
    }
}