package com.example.inventorymanagementsystem.views;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
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
import com.example.inventorymanagementsystem.models.Receiving;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ReceivingScreen extends AppCompatActivity {
    private static int STATIC_INTEGER_VALUE;
    FirebaseFirestore db;
    Users mCurrentUser;
    String mWarehouse;

    private EditText edtLocation, edtProductId;
    private String mLocation;
    private String mProduct;
    private int mReceiptId = 1;
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
            receiving.setNumberOfUnits(edtNumberOfUnits.getText().toString());
            receiving.setPiecesPerBox(edtPiecesPerBox.getText().toString());
            receiving.setTotalBoxes(edtNumberOfBoxes.getText().toString());
            receiving.setLoosePieces(edtLoosePieces.getText().toString());
            receiving.setDisposition(spinnerDisposition.getSelectedItem().toString());
            receiving.setLocation(edtLocation.getText().toString());
            receiving.setWeight(edtWeight.getText().toString());
            receiving.setPO(edtPO.getText().toString());
            receiving.setShipFrom(edtShipFrom.getText().toString());
            receiving.setReceiptId(String.valueOf(mReceiptId));

            addDataToFireStore(receiving);

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
    }

    private ActivityResultLauncher<Intent> launchFindLocationActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        mLocation = data.getStringExtra("Location");
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
                        Toast.makeText(ReceivingScreen.this, "Cancelled...", Toast.LENGTH_LONG).show();
                    }
                }
            });

    private void addDataToFireStore(Receiving _receiving) {
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
}