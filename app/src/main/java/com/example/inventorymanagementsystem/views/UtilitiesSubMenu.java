package com.example.inventorymanagementsystem.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageButton;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Users;
import com.google.firebase.firestore.FirebaseFirestore;

public class UtilitiesSubMenu extends AppCompatActivity {

    private String mWarehouse;
    private Users mCurrentUser;
    private ImageButton fixDiscrepanciesImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities_sub_menu);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Locations</font>"));

        //getting intent from ItemsSubMenu class along with User object
        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        fixDiscrepanciesImageButton = findViewById(R.id.utilitiesSubMenuFixUnitDiscrepancies);

        fixDiscrepanciesImageButton.setOnClickListener(v -> {
            Intent fixDiscrepanciesIntent = new Intent(UtilitiesSubMenu.this, FixUnitDiscrepanciesUtility.class);
            fixDiscrepanciesIntent.putExtra("Warehouse", mWarehouse);
            fixDiscrepanciesIntent.putExtra("User", mCurrentUser);
            startActivity(fixDiscrepanciesIntent);
        });
    }
}