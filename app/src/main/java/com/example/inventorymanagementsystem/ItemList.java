
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

        // initializing our variable for firebase
        // firestore and getting its instance
        db = FirebaseFirestore.getInstance();

        // creating our new array list
        mProductsArrayList = new ArrayList<>();
        itemRV.setHasFixedSize(true);
        itemRV.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class
        mItemRVAdapter = new ItemRVAdapter(mProductsArrayList, this);

        // setting adapter to our recycler view
        itemRV.setAdapter(mItemRVAdapter);

        //getting intent from ItemsSubMenu class along with User object
        Intent i = getIntent();
        currentUser = (Users)i.getSerializableExtra("User");

        CollectionReference adminRef = db.collection("Users");

//        adminRef.whereEqualTo("userKey", currentUser.getUserKey()).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                if(!value.isEmpty())
//                {
//
//                    loadingPB.setVisibility(View.GONE);
//                    List<DocumentSnapshot> list = value.getDocuments();
//                    for(DocumentSnapshot d : list) {
//                        currentUser = d.toObject(Users.class);
//                    }
//
//                } else {
//                    Log.d(TAG, "onEvent value is empty " + value.getDocuments());
//
//                }
//
                CollectionReference itemsRef = db.collection("Products");

                if(currentUser.getIsAdmin())
                {
                    db.collection("Products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

                    itemsRef.whereEqualTo("postedBy", currentUser.getUserKey()).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
//            }
//        });
    }

    /**
     * This method is called after {@link #onStart} when the activity is
     * being re-initialized from a previously saved state, given here in
     * <var>savedInstanceState</var>.  Most implementations will simply use {@link #onCreate}
     * to restore their state, but it is sometimes convenient to do it here
     * after all of the initialization has been done or to allow subclasses to
     * decide whether to use your default implementation.  The default
     * implementation of this method performs a restore of any view state that
     * had previously been frozen by {@link #onSaveInstanceState}.
     *
     * <p>This method is called between {@link #onStart} and
     * {@link #onPostCreate}. This method is called only when recreating
     * an activity; the method isn't invoked if {@link #onStart} is called for
     * any other reason.</p>
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     * @see #onCreate
     * @see #onPostCreate
     * @see #onResume
     * @see #onSaveInstanceState
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}