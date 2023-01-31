package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddZone extends AppCompatActivity {

    private FirebaseFirestore db;
    private static Users mCurrentUser;
    private String mWarehouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zone);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Zones</font>"));

        //Initializing firebase
        db = FirebaseFirestore.getInstance();

        // Initializing widgets
        Button addButton = findViewById(R.id.btnAddZonesCreateZone);
        EditText edtZoneId = findViewById(R.id.edtAddZoneZoneID);
        EditText edtZoneDescription = findViewById(R.id.edtAddZoneDescription);
        Spinner spinnerUnitType = findViewById(R.id.spinnerAddZones);

        // Spinner properties
        String defaultTextForSpinner = "Unit Type Stored In This Zone";
        List<String> typeOfUnits = new ArrayList<>();



        // getting intent from previous activities
        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        addButton.setOnClickListener(v -> {

            Zone zone = new Zone();
            if(edtZoneId.getText().toString().length() > 6) {
                edtZoneId.setError("Zone Id must be 6 characters or less");
            } else {
                zone.setZoneId(edtZoneId.getText().toString());
                zone.setDescription(edtZoneDescription.getText().toString());
                zone.setTypeOfUnit(spinnerUnitType.getSelectedItem().toString());
                zone.setCanBeDeleted(true);

                if (!zone.getZoneId().isEmpty()) {

                    CollectionReference collectionReference = db.collection("Warehouses/" + mWarehouse + "/Zones");

                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Boolean unitTypeExists = false;
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                if (d.getId().equals(zone.getZoneId())) {
                                    unitTypeExists = true;
                                    Toast.makeText(AddZone.this, "Zone already exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (!unitTypeExists) {
                                addDataToFireStore(zone);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddZone.this, "Unable to read data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        CollectionReference unitTypeRef = db.collection("Warehouses/"+mWarehouse+"/Units of Measure");

        unitTypeRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty())
                {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d : list) {
                        String typeOfUnit = d.getId();
                        typeOfUnits.add(typeOfUnit);
                    }

                    String[] arrayForSpinner = new String[typeOfUnits.size()];
                    for(int i = 0; i < typeOfUnits.size(); i++) {
                        arrayForSpinner[i] = typeOfUnits.get(i);
                    }
                    spinnerUnitType.setAdapter(new CustomSpinnerAdapter(AddZone.this, R.layout.spinner_row, arrayForSpinner, defaultTextForSpinner));
                } else {
                    Toast.makeText(AddZone.this, "No data found in Database", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddZone.this, "Failed to get data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addDataToFireStore(Zone _zone) {
        //Log.d("FIREBASE-ADD", "Inside");
        // creating a collection reference
        // for our Firebase Firestore database

        db.collection("Warehouses/"+mWarehouse+"/Zones").document(_zone.getZoneId()).set(_zone).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddZone.this, "Zone Added", Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddZone.this, "Fail to add zone \n" + e, Toast.LENGTH_LONG).show();
            }
        });
    }
}