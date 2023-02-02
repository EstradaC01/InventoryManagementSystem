package com.example.inventorymanagementsystem.views;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.inventorymanagementsystem.models.Users;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReceivingScreen extends AppCompatActivity {
    private static int STATIC_INTEGER_VALUE;
    FirebaseFirestore db;
    Users mCurrentUser;
    String mWarehouse;

    private EditText edtLocation;
    private String mLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving_screen);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Receiving</font>"));

        // Initialize widgets
        EditText edtProductId = findViewById(R.id.edtReceivingScreenProductId);
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
        String[] arrayForSpinner = {"Damaged", "Quarantine", "Other"};

        spinnerDisposition.setAdapter(new CustomSpinnerAdapter(this, R.layout.spinner_row, arrayForSpinner, defaultTextForSpinner));

        // add on click listener to open Find Location
        btnAddLocation.setOnClickListener(v -> {
            // opening Find Location activity and passing current activity intent
            Intent findLocationIntent = new Intent(ReceivingScreen.this, FindLocationScreen.class);
            findLocationIntent.putExtra("User", mCurrentUser);
            findLocationIntent.putExtra("Warehouse", mWarehouse);
            launchSecondActivity.launch(findLocationIntent);
        });
    }

    private ActivityResultLauncher<Intent> launchSecondActivity = registerForActivityResult(
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
}