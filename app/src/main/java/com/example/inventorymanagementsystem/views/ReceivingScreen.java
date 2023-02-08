package com.example.inventorymanagementsystem.views;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.CustomSpinnerAdapter;
import com.example.inventorymanagementsystem.models.Location;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReceivingScreen extends AppCompatActivity {
    FirebaseFirestore db;
    Users mCurrentUser;
    String mWarehouse;

    private EditText edtLocation, edtProductId;
    private String mLocation;
    private String mZone;
    private String mProduct;
    private int mReceiptId = 1;
    private int mUnitId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving_screen);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Receiving</font>"));

        // Initialize widgets
        edtProductId = findViewById(R.id.edtReceivingScreenProductId);
        EditText edtNumberOfUnits = findViewById(R.id.edtReceivingScreenNumberOfUnits);
        EditText edtPiecesPerBox = findViewById(R.id.edtReceivingScreenPiecesPerBox);
        EditText edtNumberOfBoxes = findViewById(R.id.edtReceivingScreenTotalBoxes);
        EditText edtLoosePieces = findViewById(R.id.edtReceivingScreenLoosePieces);
        Spinner spinnerDisposition = findViewById(R.id.spinnerReceivingScreenDisposition);
        edtLocation = findViewById(R.id.edtReceivingScreenLocation);
        EditText edtWeight = findViewById(R.id.edtReceivingScreenWeight);
        EditText edtPO = findViewById(R.id.edtReceivingScreenPO);
        EditText edtShipFrom = findViewById(R.id.edtReceivingScreenShipFrom);

        Button btnSubmit = findViewById(R.id.btnReceivingScreenSubmit);
        Button btnAddProduct = findViewById(R.id.btnReceivingScreenAddProduct);
        Button btnAddPO = findViewById(R.id.btnReceivingScreenAddPO);
        Button btnAddLocation = findViewById(R.id.btnReceivingScreenAddLocation);

        // Getting intent from previous main activity
        Intent i = getIntent();
        mCurrentUser = (Users) i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        // Initializing FirebaseFirestore database
        db = FirebaseFirestore.getInstance();

        // setting default string for spinner
        String defaultTextForSpinner = "Disposition";
        String[] arrayForSpinner = {"Good", "Damaged", "Quarantine", "Other"};

        spinnerDisposition.setAdapter(new CustomSpinnerAdapter(this, R.layout.spinner_row, arrayForSpinner, defaultTextForSpinner));

        // add on click listener to open Find Location
        btnAddLocation.setOnClickListener(v -> {
            // opening Find Location activity and passing current activity intent
            Intent findLocationIntent = new Intent(ReceivingScreen.this, FindLocationScreen.class);
            findLocationIntent.putExtra("User", mCurrentUser);
            findLocationIntent.putExtra("Warehouse", mWarehouse);
            launchFindLocationActivity.launch(findLocationIntent);
        });

        // add on click listener to open Find Product
        btnAddProduct.setOnClickListener(v -> {
            Intent findProductIntent = new Intent(ReceivingScreen.this, FindProductScreen.class);
            findProductIntent.putExtra("User", mCurrentUser);
            findProductIntent.putExtra("Warehouse", mWarehouse);
            launchFindProductActivity.launch(findProductIntent);
        });

        btnSubmit.setOnClickListener(v -> {
            Receiving receiving = new Receiving();

            receiving.setProductId(edtProductId.getText().toString());
            receiving.setPiecesPerBox(edtPiecesPerBox.getText().toString());
            receiving.setTotalBoxes(edtNumberOfBoxes.getText().toString());
            receiving.setLoosePieces(edtLoosePieces.getText().toString());
            receiving.setDisposition(spinnerDisposition.getSelectedItem().toString());
            receiving.setLocation(edtLocation.getText().toString());
            receiving.setWeight(edtWeight.getText().toString());
            receiving.setPO(edtPO.getText().toString());
            receiving.setShipFrom(edtShipFrom.getText().toString());
            receiving.setReceiptId(String.valueOf(mReceiptId));
            receiving.setTotalPieces(String.valueOf(Integer.parseInt(edtPiecesPerBox.getText().toString()) * Integer.parseInt(edtNumberOfBoxes.getText().toString())));

            UnitId mUnit = new UnitId();
            mUnit.setUnitId(String.valueOf(mUnitId));
            mUnit.setProductId(receiving.getProductId());
            mUnit.setDateTimeCreated(getCurrentTime());
            mUnit.setPiecesPerBox(receiving.getPiecesPerBox());
            mUnit.setNumberOfBoxes(receiving.getTotalBoxes());

            int totalPieces = Integer.parseInt(mUnit.getPiecesPerBox()) * Integer.parseInt(mUnit.getNumberOfBoxes());

            mUnit.setTotalPieces(String.valueOf(totalPieces));
            mUnit.setReceiptId(receiving.getReceiptId());
            mUnit.setLocation(edtLocation.getText().toString());
            mUnit.setZone(mZone);
            mUnit.setDisposition(spinnerDisposition.getSelectedItem().toString());
            mUnit.setPiecesMarked("0");
            mUnit.setPiecesAvailable(String.valueOf(totalPieces));


            updateLocationStatus(mUnit.getLocation());
            addUnitIdToDatabase(mUnit);
            updateProductAvailableUnits(receiving.getProductId(), receiving.getTotalPieces(), receiving);

        });

        CollectionReference receiptId = db.collection("Warehouses/"+mWarehouse+"/Receiving");

        receiptId.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d : list) {
                        mReceiptId++;
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReceivingScreen.this, "Failed to get data", Toast.LENGTH_LONG).show();
            }
        });

        CollectionReference nextUnitId = db.collection("Warehouses/"+mWarehouse+"/UnitId");

        nextUnitId.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d : list) {
                        mUnitId++;
                    }
                }
             }
        });
    }



    private ActivityResultLauncher<Intent> launchFindLocationActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        mLocation = data.getStringExtra("Location");
                        mZone = data.getStringExtra("Zone");
                        edtLocation.setText(mLocation);
                    } else {
                        Toast.makeText(ReceivingScreen.this, "Cancelled...", Toast.LENGTH_LONG).show();
                    }
                }
            });

    private ActivityResultLauncher<Intent> launchFindProductActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        mProduct = data.getStringExtra("Product");
                        edtProductId.setText(mProduct);
                    } else {
                        Toast.makeText(ReceivingScreen.this, "Cancelled", Toast.LENGTH_LONG).show();
                    }
                }
            });


    private void addDataToFireStore(Products _product, Receiving _receiving) {
        db.collection("Warehouses/"+mWarehouse+"/Products").document(_product.getProductId()).set(_product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                _receiving.setDateTimeCreated(getCurrentTime());
                db.collection("Warehouses/"+mWarehouse+"/Receiving").document(_receiving.getReceiptId()).set(_receiving).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(ReceivingScreen.this, "Receiving confirmed", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ReceivingScreen.this, "Fail to confirm receiving \n" + e, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReceivingScreen.this, "Failed to update product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addLocationToFirestore(Location _location) {
        db.collection("Warehouses/"+mWarehouse+"/Locations").document(_location.getName()).set(_location).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }

    private void updateProductAvailableUnits(String _productId, String _numberOfUnits, Receiving _receiving) {
        CollectionReference productDocument = db.collection("Warehouses/"+mWarehouse+"/Products");

        productDocument.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d : list) {
                        if(d.getId().equals(_productId)) {
                            Products p = d.toObject(Products.class);
                            productDocument.document(d.getId()).delete();
                                int availableUnits = Integer.parseInt(p.getAvailableUnits()) + Integer.parseInt(_numberOfUnits);
                                p.setAvailableUnits(String.valueOf(availableUnits));
                            addDataToFireStore(p, _receiving);
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReceivingScreen.this, "Failed to get data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateLocationStatus(String _locationName) {
        CollectionReference location = db.collection("Warehouses/"+mWarehouse+"/Locations");

        location.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d : list) {
                        if(d.getId().equals(_locationName)) {
                            Location l = d.toObject(Location.class);
                            location.document(d.getId()).delete();
                            l.setStatus("INUSE");
                            addLocationToFirestore(l);
                        }
                    }
                }
            }
        });
    }

    private void addUnitIdToDatabase(UnitId _unit) {
        db.collection("Warehouses/"+mWarehouse+"/UnitId").document(_unit.getUnitId()).set(_unit).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                db.collection("Warehouses/"+mWarehouse+"/Products/"+_unit.getProductId()+"/UnitId").document(_unit.getUnitId()).set(_unit).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ReceivingScreen.this, "Unit created confirmed", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ReceivingScreen.this, "Fail to confirm unit \n" + e, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    private String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(("MM/dd/yyyy HH:mm:ss"));
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}