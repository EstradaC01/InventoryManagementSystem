package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
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

public class AddLocation extends AppCompatActivity {

    private FirebaseFirestore db;
    private static Users mCurrentUser;
    private String mCompanyCode;
    private String mWarehouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Locations</font>"));

        //Initializing firebase
        db = FirebaseFirestore.getInstance();

        // Initializing widgets
        EditText edtAisle = findViewById(R.id.edtAddLocationAisle);
        EditText edtRack = findViewById(R.id.edtAddLocationRack);
        EditText edtLevel = findViewById(R.id.edtAddLocationLevel);
        EditText edtLpLimit = findViewById(R.id.edtAddLocationLPLimit);
        EditText edtWeight = findViewById(R.id.edtAddLocationWeight);
        EditText edtHeight = findViewById(R.id.edtAddLocationHeight);
        EditText edtWidth = findViewById(R.id.edtAddLocationWidth);
        EditText edtLength = findViewById(R.id.edtAddLocationLength);

        Spinner spinnerZones = findViewById(R.id.spinnerAddLocationZone);

        Button btnAddLocation = findViewById(R.id.btnAddLocationAddLocation);

        // Spinner properties
        String defaultTextForSpinner = "Zone";
        List<String> listZones= new ArrayList<>();


        // getting intent from previous activities
        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mCompanyCode = (String) i.getSerializableExtra("CompanyCode");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        btnAddLocation.setOnClickListener(v -> {

            Location location = new Location();

            if(edtAisle.getText().toString().isEmpty()) {
                edtAisle.setError("Location must contain at least Aisle property");
            }
            if(edtRack.getText().toString().isEmpty()) {
                edtRack.setError("Location must contain at least Rack property");
            }
            if(edtLevel.getText().toString().isEmpty()) {
                    edtLevel.setError("Location must contain at least Level property");
            }
            if(spinnerZones.getSelectedItem().toString().equals("Zone")) {
                Toast.makeText(AddLocation.this, "Zone is empty", Toast.LENGTH_SHORT).show();
            }
            if(!edtAisle.getText().toString().isEmpty()
            && !edtRack.getText().toString().isEmpty()
            && !edtLevel.getText().toString().isEmpty()
            && !spinnerZones.getSelectedItem().toString().equals("Zone")) {
                location.setAisle(edtAisle.getText().toString());
                location.setRack(edtRack.getText().toString());
                location.setLevel(edtLevel.getText().toString());
                location.setZone(spinnerZones.getSelectedItem().toString());
                location.setLpLimit(edtLpLimit.getText().toString());
                location.setWeight(edtWeight.getText().toString());
                location.setHeight(edtHeight.getText().toString());
                location.setWidth(edtWidth.getText().toString());
                location.setLength(edtLength.getText().toString());
                location.setCanBeDeleted(true);
                location.setHasInventory(false);
                location.setStatus("OPEN");
                location.setName(location.getAisle()+"-"+location.getRack()+"-"+location.getLevel());

                if (!location.getAisle().isEmpty()) {

                    CollectionReference collectionReference = db.collection(mCompanyCode + "/" + mWarehouse + "/Locations");

                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Boolean unitTypeExists = false;
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                if (d.getId().equals(location.getName())) {
                                    unitTypeExists = true;
                                    Toast.makeText(AddLocation.this, "Location already exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (!unitTypeExists) {
                                addDataToFireStore(location);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddLocation.this, "Unable to read data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        CollectionReference unitTypeRef = db.collection(mCompanyCode + "/"+mWarehouse+"/Zones");

        unitTypeRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty())
                {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d : list) {
                        String zoneName = d.getId();
                        listZones.add(zoneName);
                    }

                    String[] arrayForSpinner = new String[listZones.size()];
                    for(int i = 0; i < listZones.size(); i++) {
                        arrayForSpinner[i] = listZones.get(i);
                    }
                    spinnerZones.setAdapter(new CustomSpinnerAdapter(AddLocation.this, R.layout.spinner_row, arrayForSpinner, defaultTextForSpinner));
                } else {
                    Toast.makeText(AddLocation.this, "No data found in Database", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddLocation.this, "Failed to get data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addDataToFireStore(Location _location) {
        //Log.d("FIREBASE-ADD", "Inside");
        // creating a collection reference
        // for our Firebase Firestore database

        db.collection(mCompanyCode + "/"+mWarehouse+"/Locations").document(_location.getName()).set(_location).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddLocation.this, "Location Added", Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddLocation.this, "Fail to add zone \n" + e, Toast.LENGTH_LONG).show();
            }
        });
    }

}