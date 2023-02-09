package com.example.inventorymanagementsystem.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageButton;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Users;

public class LocationsSubMenu extends AppCompatActivity {

    private static Users mCurrentUser;
    private static String mWarehouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_sub_menu);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Locations</font>"));

        //getting intent from ItemsSubMenu class along with User object
        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        // initializing image buttons from item sub menu layout
        ImageButton locationsButton = findViewById(R.id.locationsSubMenuCreateLocationsButton);
        ImageButton zonesButton = findViewById(R.id.locationsSubMenuCreateZonesButton);
        ImageButton unitsOfMeasureButton = findViewById(R.id.locationsSubMenuUnitsOfMeasureButton);
        ImageButton massLocationsButton = findViewById(R.id.locationsSubMenuMassLocationsButton);

        unitsOfMeasureButton.setOnClickListener(v -> {
            Intent unitsOfMeasureIntent = new Intent (LocationsSubMenu.this, UnitsOfMeasure.class);
            unitsOfMeasureIntent.putExtra("User", mCurrentUser);
            unitsOfMeasureIntent.putExtra("Warehouse", mWarehouse);
            startActivity(unitsOfMeasureIntent);
        });

        zonesButton.setOnClickListener(v -> {
            Intent zonesIntent = new Intent (LocationsSubMenu.this, Zones.class);
            zonesIntent.putExtra("User", mCurrentUser);
            zonesIntent.putExtra("Warehouse", mWarehouse);
            startActivity(zonesIntent);

        });

        locationsButton.setOnClickListener(v -> {
            Intent locationsIntent = new Intent (LocationsSubMenu.this, LocationList.class);
            locationsIntent.putExtra("User", mCurrentUser);
            locationsIntent.putExtra("Warehouse", mWarehouse);
            startActivity(locationsIntent);
        });

        massLocationsButton.setOnClickListener( v -> {
            Intent massLocationsIntent = new Intent(LocationsSubMenu.this, AddMassLocations.class);
            massLocationsIntent.putExtra("User", mCurrentUser);
            massLocationsIntent.putExtra("Warehouse", mWarehouse);
            startActivity(massLocationsIntent);
        });

    }
}