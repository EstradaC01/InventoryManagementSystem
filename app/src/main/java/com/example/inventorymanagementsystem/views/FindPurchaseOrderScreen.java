package com.example.inventorymanagementsystem.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.PurchaseOrder;
import com.example.inventorymanagementsystem.models.Users;
import com.example.inventorymanagementsystem.adapters.FindPurchaseOrderRecyclerViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FindPurchaseOrderScreen extends AppCompatActivity {

    private ArrayList<PurchaseOrder> mArrayPurchaseOrder;
    private RecyclerView mRecyclerView;
    private FindPurchaseOrderRecyclerViewAdapter mAdapter;
    private FirebaseFirestore db;
    private String mWarehouse;
    private Users mCurrentUser;

    private androidx.appcompat.widget.SearchView searchView;
    private ImageButton ProductFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_purchase_order_screen);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Find PO</font>"));

        // initializing widgets
        searchView = findViewById(R.id.findPurchaseOrderSearchView);
        mRecyclerView = findViewById(R.id.findPurchaseOrderRecyclerView);

        // creating our new array list
        mArrayPurchaseOrder = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initialize firebase
        db = FirebaseFirestore.getInstance();

        // getting intent from previous class
        Intent i = getIntent();
        mCurrentUser = (Users) i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        // adding our array list to our recycler view adapter class
        mAdapter = new FindPurchaseOrderRecyclerViewAdapter(mArrayPurchaseOrder, this, FindPurchaseOrderScreen.this);

        // setting adapter to our recycler view
        mRecyclerView.setAdapter(mAdapter);

        // creating firebasefirestore reference to locations path
        CollectionReference locationsRef = db.collection("Warehouses/"+mWarehouse+"/PurchaseOrders");

        if(mCurrentUser.getIsAdmin()) {
            locationsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list) {
                            PurchaseOrder p = d.toObject(PurchaseOrder.class);
                            mArrayPurchaseOrder.add(p);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(FindPurchaseOrderScreen.this, "No data found in Database", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(FindPurchaseOrderScreen.this, "Failed to get data", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(FindPurchaseOrderScreen.this, "Access Denied", Toast.LENGTH_LONG).show();
            finish();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });


    }

    private void filterList(String text) {
        ArrayList<PurchaseOrder> filteredList = new ArrayList<>();

        for(PurchaseOrder p : mArrayPurchaseOrder) {
            if(p.getPoNumber().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(p);
            }
        }

        if (filteredList.isEmpty()) {
            mAdapter.setFilteredList(filteredList);
            Toast.makeText(this, "No items found", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.setFilteredList(filteredList);
        }
    }

    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}