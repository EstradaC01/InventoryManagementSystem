package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductDetails extends AppCompatActivity {

    private TextView tvProductId, tvProductDescription;
    private ImageButton edtChangeProductDetails, edtDeleteProductDetails;
    private ImageView ivProductImage;
    private FirebaseFirestore db;

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
        String mCompanyCode = (String) i.getSerializableExtra("CompanyCode");
        String mWarehouse = (String) i.getSerializableExtra("Warehouse");


        tvProductId = findViewById(R.id.idTvProductId);
        tvProductDescription = findViewById(R.id.idTvProductDescription);
        ivProductImage = findViewById(R.id.productDetailsImageViewProductImage);
        edtChangeProductDetails = findViewById(R.id.productDetailsEditButton);
        edtDeleteProductDetails = findViewById(R.id.productDetailsDeleteButton);

        db = FirebaseFirestore.getInstance();

        tvProductId.setText(product.getProductId());
        tvProductDescription.setText(product.getProductDescription());
        Log.d("Test", ": " + mCompanyCode);

        if(product.getImageUri() != null) {
            Glide.with(this).load(product.getImageUri()).into(ivProductImage);
        }

        edtDeleteProductDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference d = db.collection(mCompanyCode + "/" + mWarehouse + "/Products");
                d.document(product.getProductId()).delete();
                finish();
            }
        });


    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}