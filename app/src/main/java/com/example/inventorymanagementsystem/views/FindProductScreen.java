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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.AddLocationRecyclerViewAdapter;
import com.example.inventorymanagementsystem.adapters.AddProductRecyclerViewAdapter;
import com.example.inventorymanagementsystem.models.Location;
import com.example.inventorymanagementsystem.models.Products;
import com.example.inventorymanagementsystem.models.UnitId;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FindProductScreen extends AppCompatActivity {
    private RecyclerView mProductsRecyclerView;
    private ArrayList<Products> mProductArrayList;
    private AddProductRecyclerViewAdapter mProductRecyclerViewAdapter;
    private FirebaseFirestore db;
    private androidx.appcompat.widget.SearchView searchView;
    private String mWarehouse;
    private static Users mCurrentUser;
    private ImageButton ProductFilter;
    public PopupMenu DropDownMenu;
    public Menu menu;
    private enum Sort {
        ProductUPC,
        ProductID;
    }
    private Sort sorter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_product_screen);
        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Find Location</font>"));

        // initializing widgets
        searchView = findViewById(R.id.svFindProductScreen);
        mProductsRecyclerView = findViewById(R.id.rvFindProductScreen);

        // creating our new array list
        mProductArrayList = new ArrayList<>();
        mProductsRecyclerView.setHasFixedSize(true);
        mProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initialize firebase
        db = FirebaseFirestore.getInstance();

        // getting intent from previous class
        Intent i = getIntent();
        mCurrentUser = (Users) i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        // adding our array list to our recycler view adapter class
        mProductRecyclerViewAdapter = new AddProductRecyclerViewAdapter(mProductArrayList, this, FindProductScreen.this);

        // setting adapter to our recycler view
        mProductsRecyclerView.setAdapter(mProductRecyclerViewAdapter);

        ProductFilter = findViewById(R.id.ibFindProductScreen);
        sorter = Sort.ProductID;
        DropDownMenu = new PopupMenu(getApplicationContext(), ProductFilter);
        menu = DropDownMenu.getMenu();
        menu.add(0,0,0,"Product ID");

        // creating firebasefirestore reference to locations path
        CollectionReference locationsRef = db.collection("Warehouses/"+mWarehouse+"/Products");

        if(mCurrentUser.getIsAdmin()) {
            locationsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list) {
                            Products p = d.toObject(Products.class);
                            mProductArrayList.add(p);
                        }
                        mProductRecyclerViewAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(FindProductScreen.this, "No data found in Database", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(FindProductScreen.this, "Failed to get data", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(FindProductScreen.this, "Access Denied", Toast.LENGTH_LONG).show();
            finish();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });

        DropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 0: //ProductID
                        sorter = Sort.ProductUPC;
                        return true;

                }
                return false;
            }
        });

        ProductFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropDownMenu.show();
            }
        });
    }

    private void searchList(String text) {
        ArrayList<Products> searchedList = new ArrayList<>();
        for(Products p : mProductArrayList) {
            if(p.getProductId().toLowerCase().contains(text.toLowerCase())) {
                searchedList.add(p);
            } else {
                mProductRecyclerViewAdapter.setSearchList(searchedList);
            }
        }
        mProductRecyclerViewAdapter.setSearchList(searchedList);

    }

    private void filterList(String text, Sort sorter) {
        ArrayList<Products> filteredList = new ArrayList<>();
        for (Products list : mProductArrayList) {
            switch (sorter) {
                case ProductUPC:
                    if (list.getProductUpc().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(list);
                    }
                    break;

                default:
                    if (list.getProductId().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(list);
                    }
                    break;
            }
        }
        if (filteredList.isEmpty()) {
            mProductRecyclerViewAdapter.setFilteredList(filteredList);
            Toast.makeText(this, "No items found", Toast.LENGTH_SHORT).show();
        } else {
            mProductRecyclerViewAdapter.setFilteredList(filteredList);
        }
    }

    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}