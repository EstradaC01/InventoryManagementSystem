
package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemList extends AppCompatActivity {

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore
    // and progress bar
    private RecyclerView itemRV;
    private ArrayList<Products> mProductsArrayList;
    private ItemRVAdapter mItemRVAdapter;
    private FirebaseFirestore db;
    ProgressBar loadingPB;

    private String mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        // initializing variables
        itemRV = findViewById(R.id.idRVItems);
        loadingPB = findViewById(R.id.idProgressBar);

        // initializing our variable for firebase
        // firestore and getting its instance
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = user.getUid().toString();

        // creating our new array list
        mProductsArrayList = new ArrayList<>();
        itemRV.setHasFixedSize(true);
        itemRV.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class
        mItemRVAdapter = new ItemRVAdapter(mProductsArrayList, this);

        // setting adapter to our recycler view
        itemRV.setAdapter(mItemRVAdapter);

        // below line is use to get the data from Firebase Firestore
        // previously we were saving data on a reference of Courses
        // now we will be getting the data from the same reference
        CollectionReference itemsRef = db.collection("Products");

       itemsRef.whereEqualTo("postedBy", mUserId).addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

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
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if(!queryDocumentSnapshots.isEmpty()) {
//                            loadingPB.setVisibility(View.GONE);
//                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//                            for(DocumentSnapshot d : list)
//                            {
//                                Products p = d.toObject(Products.class);
//
//                                mProductsArrayList.add(p);
//                            }
//
//                            mItemRVAdapter.notifyDataSetChanged();
//                        } else
//                            Toast.makeText(ItemList.this, "No data found in Database", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(ItemList.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
//                    }
//                })
    }
}