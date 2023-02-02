package com.example.inventorymanagementsystem.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.AddLocationRecyclerViewAdapter;
import com.example.inventorymanagementsystem.adapters.LocationRecyclerViewAdapter;
import com.example.inventorymanagementsystem.models.Location;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FindLocationScreen extends AppCompatActivity {

    private RecyclerView mLocationsRecyclerView;
    private ArrayList<Location> mLocationArrayList;
    private AddLocationRecyclerViewAdapter mLocationRecyclerViewAdapter;
    private FirebaseFirestore db;
    private androidx.appcompat.widget.SearchView searchView;
    private String mWarehouse;
    private static Users mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location_screen);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Find Location</font>"));

        // initializing widgets
        searchView = findViewById(R.id.svFindLocationScreen);
        mLocationsRecyclerView = findViewById(R.id.rvFindLocationScreen);

        // creating our new array list
        mLocationArrayList = new ArrayList<>();
        mLocationsRecyclerView.setHasFixedSize(true);
        mLocationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initialize firebase
        db = FirebaseFirestore.getInstance();

        // getting intent from previous class
        Intent i = getIntent();
        mCurrentUser = (Users) i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        // adding our array list to our recycler view adapter class
        mLocationRecyclerViewAdapter = new AddLocationRecyclerViewAdapter(mLocationArrayList, this, FindLocationScreen.this);

        // setting adapter to our recycler view
        mLocationsRecyclerView.setAdapter(mLocationRecyclerViewAdapter);

        // creating firebasefirestore reference to locations path
        CollectionReference locationsRef = db.collection("Warehouses/"+mWarehouse+"/Locations");

        if(mCurrentUser.getIsAdmin()) {
            locationsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list) {
                            Location l = d.toObject(Location.class);
                            mLocationArrayList.add(l);
                        }
                        mLocationRecyclerViewAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(FindLocationScreen.this, "No data found in Database", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(FindLocationScreen.this, "Failed to get data", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(FindLocationScreen.this, "Access Denied", Toast.LENGTH_LONG).show();
            finish();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });
    }

    private void searchList(String text) {
        ArrayList<Location> searchedList = new ArrayList<>();
        for(Location l : mLocationArrayList) {
            if(l.getName().toLowerCase().contains(text.toLowerCase())) {
                searchedList.add(l);
            } else {
                mLocationRecyclerViewAdapter.setSearchList(searchedList);
            }
        }
            mLocationRecyclerViewAdapter.setSearchList(searchedList);

    }

    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}