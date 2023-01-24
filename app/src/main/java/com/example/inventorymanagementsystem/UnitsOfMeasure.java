package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UnitsOfMeasure extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<UnitType> mUnitTypeArrayList;
    private UnitTypeRecyclerViewAdapter mUnitTypeRecyclerViewAdapter;
    private FirebaseFirestore db;
    private ImageButton addButton;
    private Button deleteButton;
    private EditText edUnitofMeasure;

    private String mCompanyCode;
    private String mWarehouse;

    private static Users mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units_of_measure);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Units of Measure</font>"));

        // initializing widgets
        mRecyclerView = findViewById(R.id.unitsOfMeasureRecyclerView);
        edUnitofMeasure = findViewById(R.id.edUnitOfMeasureDescription);
        addButton = findViewById(R.id.unitsOfMeasureAddButton);

        // creating our new array list
        mUnitTypeArrayList = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initializing firebase
        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mCompanyCode = (String) i.getSerializableExtra("CompanyCode");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        // adding our array list to our recycler view adapter class
        mUnitTypeRecyclerViewAdapter = new UnitTypeRecyclerViewAdapter(mUnitTypeArrayList, this);

        // setting adapter to our recycler view
        mRecyclerView.setAdapter(mUnitTypeRecyclerViewAdapter);

        CollectionReference itemsRef = db.collection(mCompanyCode + "/"+mWarehouse+"/Units of Measure");

        if(mCurrentUser.getIsAdmin())
        {
            itemsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty())
                    {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list) {
                            UnitType u = d.toObject(UnitType.class);
                            mUnitTypeArrayList.add(u);
                        }
                        mUnitTypeRecyclerViewAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(UnitsOfMeasure.this, "No data found in Database", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UnitsOfMeasure.this, "Failed to get data", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(UnitsOfMeasure.this, "Access Denied", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}