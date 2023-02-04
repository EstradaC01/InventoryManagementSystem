package com.example.inventorymanagementsystem.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.LocationRecyclerViewAdapter;
import com.example.inventorymanagementsystem.models.Location;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LocationList extends AppCompatActivity {

    private Users mCurrentUser;
    private String mWarehouse;
    private RecyclerView mRecyclerView;
    private ArrayList<Location> mLocationArrayList;
    private LocationRecyclerViewAdapter mLocationRecyclerViewAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Locations</font>"));

        //initializing widgets
        Button btnAddLocation = findViewById(R.id.btnLocationListAddLocation);
        mRecyclerView = findViewById(R.id.recyclerViewLocationList);

        // creating our new array list
        mLocationArrayList = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initialize firebase
        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        // adding our array list to our recycler view adapter class
        mLocationRecyclerViewAdapter = new LocationRecyclerViewAdapter(mLocationArrayList, this);

        // setting adapter to our recycler view
        mRecyclerView.setAdapter(mLocationRecyclerViewAdapter);

        CollectionReference itemsRef = db.collection("Warehouses/"+mWarehouse+"/Locations");

        if(mCurrentUser.getIsAdmin())
        {
            itemsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty())
                    {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list) {
                            Location l = d.toObject(Location.class);
                            mLocationArrayList.add(l);
                        }
                        mLocationRecyclerViewAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(LocationList.this, "No data found in Database", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LocationList.this, "Failed to get data", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(LocationList.this, "Access Denied", Toast.LENGTH_LONG).show();
            finish();
        }

        btnAddLocation.setOnClickListener(v -> {
            // Intent
            Intent addZoneIntent = new Intent(LocationList.this, AddLocation.class);
            addZoneIntent.putExtra("User", mCurrentUser);
            addZoneIntent.putExtra("Warehouse", mWarehouse);
            startActivity(addZoneIntent);
        });
    }

    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}