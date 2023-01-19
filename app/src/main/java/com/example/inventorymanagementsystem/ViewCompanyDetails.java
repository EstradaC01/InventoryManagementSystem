package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewCompanyDetails extends AppCompatActivity {

    private TextView edtCompanyName, edtCompanyAddress, edtCompanyCountry, edtCompanyState,
            edtCompanyZipcode;

    private FirebaseFirestore db;
    private static Users currentUser;
    private String mCompanyCode;
    private Company ourCompany;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Success ", "Inside the layout");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_company_details);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Company Details</font>"));

        Intent i = getIntent();

        currentUser = (Users) i.getSerializableExtra("User");
        mCompanyCode = (String) i.getSerializableExtra("CompanyCode");
        db = FirebaseFirestore.getInstance();
        CollectionReference details = db.collection(mCompanyCode + "/WarehouseOne/CompanyDetails");
        Log.d("Success ", "collection " + details);
        ArrayList<String> ar = new ArrayList<String>();

        edtCompanyName = findViewById(R.id.idTvCompanyName);
        edtCompanyAddress = findViewById(R.id.idTvCompanyAddress);
        edtCompanyCountry = findViewById(R.id.idTvCompanyCountry);
        details.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> myDocs = task.getResult().getDocuments();

                    Map<String, Object> d = myDocs.get(0).getData();
                    for (Object entry : d.values()) {
                        Log.d("Success ", "data " + entry.toString());
                        ar.add(entry.toString());
                    }

                    edtCompanyAddress.setText(ar.get(1) + " " + ar.get(3) + " " + ar.get(4));
                    edtCompanyName.setText(ar.get(2));
                    edtCompanyCountry.setText(ar.get(5));
                }
            }
        });

    }

}