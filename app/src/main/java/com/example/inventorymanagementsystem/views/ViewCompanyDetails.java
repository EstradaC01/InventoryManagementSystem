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

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Company;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ViewCompanyDetails extends AppCompatActivity {

    private TextView edtCompanyName, edtCompanyAddress, edtCompanyCountry, edtCompanyState,
            edtCompanyZipcode;

    private Button btnEditCompanyDetails;

    private FirebaseFirestore db;
    private static Users currentUser;
    private Company ourCompany;

    private static final String COMPANY_CODE = "NULL";

    private static final String TAG = "ViewCompanyDetails";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Success ", "Inside the layout");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_company_details);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Company Details</font>"));

        Intent i = getIntent();

        currentUser = (Users) i.getSerializableExtra("User");

        db = FirebaseFirestore.getInstance();

        CollectionReference details = db.collection("CompanyDetails");

        Log.d("Success ", "collection " + details);

        edtCompanyName = findViewById(R.id.idTvCompanyName);
        edtCompanyAddress = findViewById(R.id.idTvCompanyAddress);
        edtCompanyCountry = findViewById(R.id.idTvCompanyCountry);
        btnEditCompanyDetails = findViewById(R.id.idBtnCompanyDetailsEditCompanyDetails);

        btnEditCompanyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewCompanyDetails.this, CompanySetup.class);
                i.putExtra("User", currentUser);
                startActivity(i);
            }
        });

        details.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> myDocs = task.getResult().getDocuments();
                    ourCompany = myDocs.get(0).toObject(Company.class);

                    edtCompanyAddress.setText(ourCompany.getCompanyAddress() + " "
                            + ourCompany.getCompanyCity() + " " +ourCompany.getCompanyState()
                            + " " + ourCompany.getCompanyZipcode());
                    edtCompanyName.setText(ourCompany.getCompanyName());
                    edtCompanyCountry.setText(ourCompany.getCompanyCountry());
                }
            }
        });

    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}