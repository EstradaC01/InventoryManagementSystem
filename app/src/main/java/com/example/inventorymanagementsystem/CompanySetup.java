package com.example.inventorymanagementsystem;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class CompanySetup extends AppCompatActivity {
    private String mCompanyName, mCompanyCode, mCompanyOwner,
            mCompanyAddress, mCompanyCountry, mCompanyState, mCompanyZipcode;


    private TextView edtCompanyName, edtCompanyAddress, edtCompanyCountry, edtCompanyState,
            edtCompanyZipcode;


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
        mCompanyCode = (String) i.getSerializableExtra("CompanyCode");
        db = FirebaseFirestore.getInstance();
        setupCompanyBtn = findViewById(R.id.idBtnSetCompanyDetails);
        edtCompanyName = findViewById(R.id.idEdtCompanyName);
        edtCompanyAddress = findViewById(R.id.idEdtCompanyAddress);
        edtCompanyCountry = findViewById(R.id.idEdtCompanyCountry);
        edtCompanyState = findViewById(R.id.idEdtCompanyState);
        edtCompanyZipcode = findViewById(R.id.idEdtCompanyZipcode);
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
                newCompany.setCompanyCode(mCompanyCode);

                addCompany(newCompany);

            }
        });
    }
    private void addCompany(Company company) {

        db.collection(mCompanyCode + "/WarehouseOne/CompanyDetails")
                .document(newCompany.getCompanyName()).set(company)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CompanySetup.this, "Details updated", Toast.LENGTH_LONG).show();
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