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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    private EditText edtUnitofMeasure;
    private String mWarehouse;
    private static Users mCurrentUser;

    private static final String TAG = "UnitsOfMeasure";

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
        edtUnitofMeasure = findViewById(R.id.edUnitOfMeasureDescription);
        addButton = findViewById(R.id.unitsOfMeasureAddButton);

        // creating our new array list
        mUnitTypeArrayList = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initializing firebase
        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        // adding our array list to our recycler view adapter class
        mUnitTypeRecyclerViewAdapter = new UnitTypeRecyclerViewAdapter(mUnitTypeArrayList, this);

        // setting adapter to our recycler view
        mRecyclerView.setAdapter(mUnitTypeRecyclerViewAdapter);

        mUnitTypeRecyclerViewAdapter.setOnItemClickListener(new UnitTypeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // now delete...
                if(mUnitTypeArrayList.get(position).getCanBeDeleted() == true)
                {
                    CollectionReference d = db.collection("Warehouses/" + mWarehouse + "/Units of Measure");
                    d.document(mUnitTypeArrayList.get(position).getUnitType()).delete();
                    mUnitTypeArrayList.remove(position);
                    //then notify...
                    mUnitTypeRecyclerViewAdapter.notifyItemRemoved(position);
                } else {
                    Toast.makeText(UnitsOfMeasure.this, "Data cannot be deleted.", Toast.LENGTH_LONG).show();
                }
            }
        });

        CollectionReference itemsRef = db.collection("Warehouses/"+mWarehouse+"/Units of Measure");

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

            addButton.setOnClickListener(v -> {
                // Adding unit of measure to firebase

                UnitType unitType = new UnitType();
                unitType.setUnitType(edtUnitofMeasure.getText().toString());
                unitType.setActive(true);
                unitType.setCanBeDeleted(true);

                if(!unitType.getUnitType().isEmpty()) {

                    CollectionReference collectionReference = db.collection("Warehouses/" + mWarehouse + "/Units of Measure");

                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Boolean unitTypeExists = false;
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d: list) {
                                if(d.getId().equals(unitType.getUnitType())) {
                                    unitTypeExists = true;
                                    Toast.makeText(UnitsOfMeasure.this, "Unit type already exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if(!unitTypeExists) {
                                addDataToFireStore(unitType);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UnitsOfMeasure.this, "Unable to read data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

    }

    private void addDataToFireStore(UnitType _unitType) {
        //Log.d("FIREBASE-ADD", "Inside");
        // creating a collection reference
        // for our Firebase Firestore database

        db.collection("Warehouses/"+mWarehouse+"/Units of Measure").document(_unitType.getUnitType()).set(_unitType).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                mUnitTypeArrayList.add(_unitType);
                mUnitTypeRecyclerViewAdapter.notifyDataSetChanged();
                Toast.makeText(UnitsOfMeasure.this, "Unit Type Added", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UnitsOfMeasure.this, "Fail to add product \n" + e, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}