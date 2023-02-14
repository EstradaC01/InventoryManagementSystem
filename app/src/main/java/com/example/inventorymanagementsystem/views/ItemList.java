
package com.example.inventorymanagementsystem.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.ItemRVAdapter;
import com.example.inventorymanagementsystem.models.Products;
import com.example.inventorymanagementsystem.models.Users;
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

public class ItemList extends AppCompatActivity {

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore
    // and progress bar
    private RecyclerView itemRV;
    private ArrayList<Products> mProductsArrayList;
    private ItemRVAdapter mItemRVAdapter;
    private FirebaseFirestore db;
    private Button btnAddItem;
    private androidx.appcompat.widget.SearchView edtSearchItems;
    ProgressBar loadingPB;
    private String mWarehouse;

    private static final Boolean IS_ADMIN = false;
    private static final String USER_ID = "USER";

    private static Users currentUser;
    private static final String TAG = "ItemList";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Product Inquiry</font>"));

        // initializing variables
        itemRV = findViewById(R.id.idRVItems);
        loadingPB = findViewById(R.id.idItemProgressBar);
        btnAddItem = findViewById(R.id.btnItemListActivityAddProduct);
        edtSearchItems = findViewById(R.id.idSVSearchItems);

        // initializing our variable for firebase
        // firestore and getting its instance
        db = FirebaseFirestore.getInstance();

        // creating our new array list
        mProductsArrayList = new ArrayList<>();
        itemRV.setHasFixedSize(true);
        itemRV.setLayoutManager(new LinearLayoutManager(this));


        //getting intent from ItemsSubMenu class along with User object
        Intent i = getIntent();
        currentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        // adding our array list to our recycler view adapter class
        mItemRVAdapter = new ItemRVAdapter(mProductsArrayList, this, i);

        // setting adapter to our recycler view
        itemRV.setAdapter(mItemRVAdapter);

                CollectionReference itemsRef = db.collection("Warehouses/"+mWarehouse+"/Products");

                if(currentUser.getIsAdmin())
                {
                    itemsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(!queryDocumentSnapshots.isEmpty())
                            {
                                loadingPB.setVisibility(View.GONE);
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for(DocumentSnapshot d : list) {
                                    Products p = d.toObject(Products.class);
                                    mProductsArrayList.add(p);
                                }

                                mItemRVAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(ItemList.this, "No data found in Database", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ItemList.this, "Failed to get data", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {

                    itemsRef.whereEqualTo("userKey", currentUser.getUserKey()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                            Log.d(TAG, "onEvent: postedBy");
                            if(!value.isEmpty())
                            {
                                loadingPB.setVisibility(View.GONE);
                                List<DocumentSnapshot> list = value.getDocuments();
                                for(DocumentSnapshot d : list) {
                                    Products p = d.toObject(Products.class);

                                    mProductsArrayList.add(p);
                                }

                                mItemRVAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(ItemList.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }

                // adding on click listener for when the Add Item button is clicked
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating intent to open add product activity
                Intent i = new Intent(ItemList.this, AddItem.class);
                i.putExtra("User", currentUser);
                i.putExtra("Warehouse", mWarehouse);
                startActivity(i);
            }
        });

        edtSearchItems.setIconified(false);
        edtSearchItems.clearFocus();
        edtSearchItems.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        ArrayList<Products> filteredList = new ArrayList<>();
        for (Products product : mProductsArrayList) {
            if (product.getProductId().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No items found", Toast.LENGTH_SHORT);
        } else {
            mItemRVAdapter.setFilteredList(filteredList);
        }
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