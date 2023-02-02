package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ReceivingScreen extends AppCompatActivity {

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
        EditText edtLocation = findViewById(R.id.edtReceivingScreenLocation);
        EditText edtWeight = findViewById(R.id.edtReceivingScreenWeight);
        EditText edtPO = findViewById(R.id.edtReceivingScreenPO);
        EditText edtShipFrom = findViewById(R.id.edtReceivingScreenShipFrom);

        Button btnSubmit = findViewById(R.id.btnReceivingScreenSubmit);
        Button btnAddProduct = findViewById(R.id.btnReceivingScreenAddProduct);
        Button btnAddPO = findViewById(R.id.btnReceivingScreenAddPO);
        Button btnAddLocaiton = findViewById(R.id.btnReceivingScreenAddLocation);
    }
}