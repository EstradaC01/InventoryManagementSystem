package com.example.inventorymanagementsystem.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Products;
import com.example.inventorymanagementsystem.models.Receiving;
import com.example.inventorymanagementsystem.models.UnitId;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UnitAdjustment extends AppCompatActivity {

    private FirebaseFirestore db;

    private EditText edtUnitId, edtLocation, edtZone, edtNumberOfBoxes, edtPiecesPerBox, edtTotalPieces, edtUnitIdSearch, edtProductId;
    private Button btnSearch, btnSubmit;
    private String mWarehouse;
    private Users mCurrentUser;

    private UnitId mUnitId;
    private Products mProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_adjustment);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Adjustment</font>"));

        //Initializing firebase
        db = FirebaseFirestore.getInstance();

        // getting intent from previous activities
        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        edtUnitId = findViewById(R.id.edtUnitAdjustmentUnitId);
        edtProductId = findViewById(R.id.edtUnitAdjustmentProductId);
        edtLocation = findViewById(R.id.edtUnitAdjustmentLocationName);
        edtZone = findViewById(R.id.edtUnitAdjustmentZone);
        edtNumberOfBoxes = findViewById(R.id.edtUnitAdjustmentNumberOfBoxes);
        edtPiecesPerBox = findViewById(R.id.edtUnitAdjustmentPiecesPerBox);
        edtTotalPieces = findViewById(R.id.edtUnitAdjustmentTotalPieces);
        edtUnitIdSearch = findViewById(R.id.edtUnitAdjustmentUnitIdSearch);

        btnSearch = findViewById(R.id.btnUnitAdjustmentSearch);
        btnSubmit = findViewById(R.id.btnUnitAdjustmentSubmit);

        edtUnitId.setEnabled(false);
        edtProductId.setEnabled(false);
        edtLocation.setEnabled(false);
        edtZone.setEnabled(false);
        edtNumberOfBoxes.setEnabled(false);
        edtPiecesPerBox.setEnabled(false);
        edtTotalPieces.setEnabled(false);
        btnSubmit.setEnabled(false);



        btnSearch.setOnClickListener(v -> {

            CollectionReference unitIdRef = db.collection("Warehouses/"+mWarehouse+"/UnitId");

            unitIdRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    boolean unitIdExists = false;
                    if(!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list) {
                            if(d.getId().equals(edtUnitIdSearch.getText().toString())) {
                                mUnitId = d.toObject(UnitId.class);
                                edtUnitId.setText(mUnitId.getUnitId());
                                edtProductId.setText(mUnitId.getProductId());
                                edtLocation.setText(mUnitId.getLocation());
                                edtZone.setText(mUnitId.getZone());
                                edtNumberOfBoxes.setText(mUnitId.getNumberOfBoxes());
                                edtPiecesPerBox.setText(mUnitId.getPiecesPerBox());
                                edtNumberOfBoxes.setEnabled(true);
                                edtPiecesPerBox.setEnabled(true);
                                btnSubmit.setEnabled(true);
                                unitIdExists = true;

                                CollectionReference productRef = db.collection("Warehouses/"+mWarehouse+"/Products");
                                productRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if(!queryDocumentSnapshots.isEmpty()) {
                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                            for(DocumentSnapshot d : list) {
                                                if(d.getId().equals(mUnitId.getProductId())) {
                                                    mProduct = d.toObject(Products.class);
                                                    int tempAvailableUnits = Integer.parseInt(mProduct.getAvailableUnits()) - Integer.parseInt(mUnitId.getTotalPieces());
                                                    mProduct.setAvailableUnits(String.valueOf(tempAvailableUnits));
                                                }
                                            }
                                        }
                                    }
                                });

                            }
                        }
                        if(!unitIdExists) {
                            Toast.makeText(UnitAdjustment.this, "Unit ID Not Found in System", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        });

        btnSubmit.setOnClickListener(v ->{
            mUnitId.setNumberOfBoxes(edtNumberOfBoxes.getText().toString());
            mUnitId.setPiecesPerBox(edtPiecesPerBox.getText().toString());
            int numberOfBoxes = Integer.parseInt(mUnitId.getNumberOfBoxes());
            int piecesPerBox = Integer.parseInt(mUnitId.getPiecesPerBox());
            mUnitId.setTotalPieces(String.valueOf(numberOfBoxes*piecesPerBox));
            mProduct.setAvailableUnits(String.valueOf(Integer.parseInt(mProduct.getAvailableUnits()) + Integer.parseInt(mUnitId.getTotalPieces())));
            updateDatabase();
        });
    }

    private void updateDatabase() {
        CollectionReference productDocument = db.collection("Warehouses/"+mWarehouse+"/Products");
        CollectionReference unitIdRef = db.collection("Warehouses/"+mWarehouse+"/UnitId");
        productDocument.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d : list) {
                        if(d.getId().equals(mUnitId.getProductId())) {
                            productDocument.document(d.getId()).delete();
                            addProductToFirestore();
                        }
                    }
                }
                unitIdRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d : list) {
                                if(d.getId().equals(mUnitId.getUnitId())) {
                                    unitIdRef.document(d.getId()).delete();
                                    addUnitIdToFirestore();
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    private void addUnitIdToFirestore() {
        db.collection("Warehouses/"+mWarehouse+"/UnitId").document(mUnitId.getUnitId()).set(mUnitId).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UnitAdjustment.this, "Unit ID Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void addProductToFirestore() {
        db.collection("Warehouses/"+mWarehouse+"/Products").document(mProduct.getProductId()).set(mProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }

    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}