package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    // creating variables for button
    private Button createProductbtn, itemList;

    // member fields for logged user
    private String mEmail, mUserid;

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

        // adding onClick listener to view data in new activity
        createProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity on button click
                Intent i = new Intent(MainActivity.this, AddItem.class);
                startActivity(i);
            }
        });

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