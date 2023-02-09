package com.example.inventorymanagementsystem.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageButton;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Users;

public class InventorySubMenu extends AppCompatActivity {

    private ImageButton btnInventory, btnAdjustments, btnMoveUnit;
    private String mWarehouse;
    private Users mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_sub_menu);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Locations</font>"));

        //getting intent from ItemsSubMenu class along with User object
        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");
        btnInventory = findViewById(R.id.inventorySubMenuShowInventory);
        btnAdjustments = findViewById(R.id.inventorySubMenuAdjustment);
        btnMoveUnit = findViewById(R.id.inventorySubMenuMoveUnit);

        btnInventory.setOnClickListener(v -> {
            Intent showInventoryIntent = new Intent(InventorySubMenu.this, InventoryList.class);
            showInventoryIntent.putExtra("Warehouse", mWarehouse);
            showInventoryIntent.putExtra("User", mCurrentUser);
            startActivity(showInventoryIntent);
        });

        btnAdjustments.setOnClickListener(v -> {
            Intent adjustmentIntent = new Intent(InventorySubMenu.this, UnitAdjustment.class);
            adjustmentIntent.putExtra("Warehouse", mWarehouse);
            adjustmentIntent.putExtra("User", mCurrentUser);
            startActivity(adjustmentIntent);
        });

    }
}