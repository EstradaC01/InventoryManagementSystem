package com.example.inventorymanagementsystem.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.UnitId;
import com.example.inventorymanagementsystem.models.Users;
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
                            }
                        }
                    }
                }
            });
        });
    }

    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}