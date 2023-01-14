package com.example.inventorymanagementsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // creating variables for button
    private Button createProductbtn, itemList, userList;
    // member fields for logged user
    private String mEmail, mUserid;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiating firebase firestore database object.
        // firebase firestore database object
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // initializing our buttons
        createProductbtn = findViewById(R.id.idBtnItems);
        itemList = findViewById(R.id.idBtnListItems);
        userList = findViewById(R.id.idBtnListUsers);

        // initializing FirebaseUser
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            // Name, email address, and profile photo Url
            // String name = user.getDisplayName();
            mEmail = user.getEmail();
            // Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            // boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            mUserid = user.getUid();
        }
        CollectionReference adminRef = db.collection("Users");

        adminRef.whereEqualTo("userId", mUserid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {
                    List<DocumentSnapshot> list = value.getDocuments();
                    for (DocumentSnapshot d : list) {
                        Users u = d.toObject(Users.class);
                        isAdmin = u.getIsAdmin();
                    }
                } else {
                    Log.d("Error ", "Exception: " + error);
                }
                if (!isAdmin) {
                    userList.setVisibility(View.GONE);
                } else if (isAdmin) {
                    userList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Intent i = new Intent(MainActivity.this, );
                        }
                    });
                }
            }
        });

        // adding onClick listener to view data in new activity
        createProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity on button click
                Intent i = new Intent(MainActivity.this, AddItem.class);
                startActivity(i);
            }
        }


        );

        // adding onClick listener to view data in recycler view. view list of items
        itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ItemList.class);
                startActivity(i);
            }
        });
    }
}