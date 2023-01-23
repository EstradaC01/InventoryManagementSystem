package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;

import java.net.URI;

public class ProductDetails extends AppCompatActivity {

    private TextView tvProductId, tvProductDescription;
    private ImageView ivProductImage;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Product Details</font>"));



        Intent i = getIntent();
        Products product = (Products)i.getSerializableExtra("Object");

        tvProductId = findViewById(R.id.idTvProductId);
        tvProductDescription = findViewById(R.id.idTvProductDescription);
        ivProductImage = findViewById(R.id.productDetailsImageViewProductImage);

        tvProductId.setText(product.getProductId());
        tvProductDescription.setText(product.getProductDescription());

        if(product.getImageUri() != null) {
            Glide.with(this).load(product.getImageUri()).into(ivProductImage);
        }


    }
}