package com.example.inventorymanagementsystem.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Company;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class CompanySetup extends AppCompatActivity {

    private TextView edtCompanyName, edtCompanyAddress, edtCompanyCountry, edtCompanyState,
            edtCompanyZipcode, edtCompanyCity;


    private static Users currentUser;
    private Button setupCompanyBtn;
    private FirebaseFirestore db;
    private Company newCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Inside setup: ", "true");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_setup);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Setup Company details</font>"));

        Intent i = getIntent();
        currentUser = (Users) i.getSerializableExtra("User");

        db = FirebaseFirestore.getInstance();

        setupCompanyBtn = findViewById(R.id.idBtnSetCompanyDetails);
        edtCompanyName = findViewById(R.id.idEdtCompanyName);
        edtCompanyAddress = findViewById(R.id.idEdtCompanyAddress);
        edtCompanyCountry = findViewById(R.id.idEdtCompanyCountry);
        edtCompanyState = findViewById(R.id.idEdtCompanyState);
        edtCompanyZipcode = findViewById(R.id.idEdtCompanyZipcode);
        edtCompanyCity = findViewById(R.id.idEdtCompanyCity);
        setupCompanyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCompany = new Company();
                newCompany.setCompanyName(edtCompanyName.getText().toString());
                newCompany.setCompanyAddress(edtCompanyAddress.getText().toString());
                newCompany.setCompanyCountry(edtCompanyCountry.getText().toString());
                newCompany.setCompanyState(edtCompanyState.getText().toString());
                newCompany.setCompanyZipcode(edtCompanyZipcode.getText().toString());
                newCompany.setCompanyOwner(currentUser.getUserKey());
                newCompany.setCompanyCity((edtCompanyCity.getText().toString()));

                addCompany(newCompany);

            }
        });
    }
    private void addCompany(Company company) {

        db.collection( "CompanyDetails")
                .document("CompanyDetails").set(company)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CompanySetup.this, "Details updated", Toast.LENGTH_LONG).show();
                Intent i = new Intent(CompanySetup.this, ViewCompanyDetails.class);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CompanySetup.this, "Details failed to update", Toast.LENGTH_LONG).show();
                    }
                });
    }
}