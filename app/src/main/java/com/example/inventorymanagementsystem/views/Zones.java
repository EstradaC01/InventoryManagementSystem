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
import com.example.inventorymanagementsystem.views.adapters.ZoneRecyclerViewAdapter;
import com.example.inventorymanagementsystem.models.Users;
import com.example.inventorymanagementsystem.models.Zone;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Zones extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Zone> mZoneArrayList;
    private ZoneRecyclerViewAdapter mZoneRecyclerViewAdapter;
    private FirebaseFirestore db;
    private Button addButton;

    private String mWarehouse;

    private static Users mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zones);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Zones</font>"));

        // initializing widgets
        mRecyclerView = findViewById(R.id.recyclerViewZonesListOfZones);
        addButton = findViewById(R.id.btnZonesAddZone);

        // creating our new array list
        mZoneArrayList = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initializing firebase
        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        // adding our array list to our recycler view adapter class
        mZoneRecyclerViewAdapter = new ZoneRecyclerViewAdapter(mZoneArrayList, this);

        // setting adapter to our recycler view
        mRecyclerView.setAdapter(mZoneRecyclerViewAdapter);

        mZoneRecyclerViewAdapter.setOnItemClickListener(new ZoneRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if(mZoneArrayList.get(position).getCanBeDeleted() == true)
                {
                    CollectionReference d = db.collection("Warehouses/" + mWarehouse + "/Zones");
                    d.document(mZoneArrayList.get(position).getZoneId()).delete();
                    mZoneArrayList.remove(position);
                    //then notify...
                    mZoneRecyclerViewAdapter.notifyItemRemoved(position);
                } else {
                    Toast.makeText(Zones.this, "Data cannot be deleted.", Toast.LENGTH_LONG).show();
                }
            }
        });

        CollectionReference itemsRef = db.collection("Warehouses/" + mWarehouse + "/Zones");

        if(mCurrentUser.getIsAdmin())
        {
            itemsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty())
                    {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list) {
                            Zone z = d.toObject(Zone.class);
                            mZoneArrayList.add(z);
                        }
                        mZoneRecyclerViewAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(Zones.this, "No data found in Database", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Zones.this, "Failed to get data", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(Zones.this, "Access Denied", Toast.LENGTH_LONG).show();
            finish();
        }

        addButton.setOnClickListener(v -> {
            // Intent
            Intent addZoneIntent = new Intent(Zones.this, AddZone.class);
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