package com.example.inventorymanagementsystem.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Products;
import com.example.inventorymanagementsystem.models.UnitId;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FixUnitDiscrepanciesUtility extends AppCompatActivity {

    private String mWarehouse;
    private Users mCurrentUser;
    private FirebaseFirestore db;
    private Button fixButton;

    CollectionReference productRef, unitIdRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_unit_discrepancies_utility);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Locations</font>"));

        //getting intent from ItemsSubMenu class along with User object
        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");// prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // db
        db = FirebaseFirestore.getInstance();

        fixButton = findViewById(R.id.unitDiscrepanciesUtilityFixButton);

        productRef = db.collection("Warehouses/"+mWarehouse+"/Products");
        unitIdRef = db.collection("Warehouses/"+mWarehouse+"/UnitId");

        fixButton.setOnClickListener(v -> {
            updateProductAvailableUnits();
        });
    }

    private void updateProductAvailableUnits() {
        productRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d : list) {
                        Products p = d.toObject(Products.class);
                        p.setAvailableUnits("0");
                        DocumentReference pReference = d.getReference();

                        unitIdRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> list2 = queryDocumentSnapshots.getDocuments();
                                    for(DocumentSnapshot d : list2) {
                                        UnitId u = d.toObject(UnitId.class);
                                        if(u.getProductId().equals(p.getProductId())) {
                                            p.setAvailableUnits(String.valueOf(Integer.parseInt(p.getAvailableUnits()) + Integer.parseInt(u.getPiecesAvailable())));
                                        }
                                    }
                                }
                                pReference.update("availableUnits", p.getAvailableUnits());
                            }
                        });
                    }
                }
            }
        });
    }
}