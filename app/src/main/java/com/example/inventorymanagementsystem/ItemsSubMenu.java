package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ItemsSubMenu extends AppCompatActivity {

    // create variables for buttons
    private ImageButton btnCreateProduct;
    private ImageButton btnProductList;

    private static Users currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_sub_menu);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // instantiating buttons
        btnCreateProduct = findViewById(R.id.idImageButtonItemMenuCreateProduct);
        btnProductList = findViewById(R.id.idImageButtonItemMenuProductList);

        // change action support bar title
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Products</font>"));

        // getting intent from MainActivity with serialize user object
        Intent i = getIntent();
        currentUser = (Users)i.getSerializableExtra("User");

        // adding on click listener for Create Product button
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity using an intent
                Intent i = new Intent(ItemsSubMenu.this, AddItem.class);
                i.putExtra("User", currentUser);
                startActivity(i);
            }
        });

        // adding on click listener to view products on recycler view
        btnProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity using an intent
                Intent i = new Intent(ItemsSubMenu.this, ItemList.class);
                i.putExtra("User", currentUser);
                startActivity(i);
            }
        });
    }
}