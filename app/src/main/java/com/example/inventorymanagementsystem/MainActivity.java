package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    // creating variables for button
    private Button createProductbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiating firebase firestore database object.
        // firebase firestore database object
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // initializing our buttons
        createProductbtn = findViewById(R.id.idBtnItems);

        // adding onClick listener to view data in new activity
        createProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity on button click
                Intent i = new Intent(MainActivity.this, AddItem.class);
                startActivity(i);
            }
        });
    }
}