package com.example.inventorymanagementsystem.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.UnitIdRecyclerViewAdapter;
import com.example.inventorymanagementsystem.models.Location;
import com.example.inventorymanagementsystem.models.UnitId;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

public class LocationDetails extends AppCompatActivity {

    private String mWarehouse;
    private RecyclerView mRecyclerView;
    private ArrayList<UnitId> mUnitIdArrayList;
    private UnitIdRecyclerViewAdapter mAdapter;
    private Location mLocation;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Location Details</font>"));

        TextView tvAisle = findViewById(R.id.locationDetailsAisle);
        TextView tvRack = findViewById(R.id.locationDetailsRack);
        TextView tvLevel = findViewById(R.id.locationDetailsLevel);
        TextView tvZone = findViewById(R.id.locationDetailsZone);
        TextView tvUnitType = findViewById(R.id.locationDetailsUnitType);
        TextView tvHeight = findViewById(R.id.locationDetailsHeight);
        TextView tvWidth = findViewById(R.id.locationDetailsWidth);
        TextView tvLength = findViewById(R.id.locationDetailsLength);
        mRecyclerView = findViewById(R.id.locaationDetailsRecyclerView);

        // creating array list
        mUnitIdArrayList = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initialize firebase
        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        mLocation = (Location) i.getSerializableExtra("Object");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");



        tvAisle.setText(mLocation.getAisle());
        tvRack.setText(mLocation.getRack());
        tvLevel.setText(mLocation.getLevel());
        tvZone.setText(mLocation.getZone());
        // tvUnitType.setText
        tvHeight.setText(mLocation.getHeight());
        tvWidth.setText(mLocation.getWidth());
        tvLength.setText(mLocation.getLength());

        mAdapter = new UnitIdRecyclerViewAdapter(mUnitIdArrayList, this);

        mRecyclerView.setAdapter(mAdapter);

        CollectionReference unitIdRef = db.collection("Warehouses/" + mWarehouse + "/UnitId");

        unitIdRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        UnitId u = d.toObject(UnitId.class);
                        if(u.getLocation().equals(mLocation.getName())) {
                            mUnitIdArrayList.add(u);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(LocationDetails.this, "No data found in Database", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LocationDetails.this, "Failed to get data", Toast.LENGTH_LONG).show();
            }
        });
    }

}