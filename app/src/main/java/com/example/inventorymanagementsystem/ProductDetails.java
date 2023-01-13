package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ProductDetails extends AppCompatActivity {

    private TextView tvProductId, tvProductDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent i = getIntent();
        Products product = (Products)i.getSerializableExtra("Object");

        tvProductId = findViewById(R.id.idTvProductId);
        tvProductDescription = findViewById(R.id.idTvProductDescription);

        tvProductId.setText(product.getProductId());
        tvProductDescription.setText(product.getProductDescription());
    }
}