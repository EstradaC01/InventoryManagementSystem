package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductDetails extends AppCompatActivity {

    private static EditText tvProductId, tvProductDescription;
    private static ImageButton edtChangeProductDetails, edtDeleteProductDetails;
    private static Button saveButton;
    private static ImageView ivProductImage;
    private FirebaseFirestore db;
    private static Products mProduct;
    private static String mCompanyCode;
    private static String mWarehouse;

    private static final String TAG = "ProductDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Product Details</font>"));
        }


        Intent i = getIntent();
        mProduct = (Products)i.getSerializableExtra("Object");
        mCompanyCode = (String) i.getSerializableExtra("CompanyCode");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        tvProductId = findViewById(R.id.idTvProductId);
        tvProductDescription = findViewById(R.id.idTvProductDescription);
        ivProductImage = findViewById(R.id.productDetailsImageViewProductImage);
        edtChangeProductDetails = findViewById(R.id.productDetailsEditButton);
        edtDeleteProductDetails = findViewById(R.id.productDetailsDeleteButton);
        saveButton = findViewById(R.id.productDetailsSaveButton);

        db = FirebaseFirestore.getInstance();

        tvProductId.setText(mProduct.getProductId());
        tvProductDescription.setText(mProduct.getProductDescription());

        tvProductId.setEnabled(false);
        tvProductDescription.setEnabled(false);

        if(mProduct.getImageUri() != null) {
            Glide.with(this).load(mProduct.getImageUri()).into(ivProductImage);
        }

        edtDeleteProductDetails.setOnClickListener(v -> showCustomDialog());

        edtChangeProductDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setVisibility(View.VISIBLE);
                tvProductId.setEnabled(true);
                tvProductId.requestFocus();
                tvProductDescription.setEnabled(true);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference d = db.collection(mCompanyCode + "/" + mWarehouse + "/Products");

                d.document(mProduct.getProductId()).delete();
                // getting data from editText fields.
                mProduct.setProductId(tvProductId.getText().toString());
                mProduct.setProductDescription(tvProductDescription.getText().toString());
                addDataToFireStore(mProduct);
            }
        });
    }

    void showCustomDialog() {
        final Dialog dialog = new Dialog(ProductDetails.this);

        saveButton.setVisibility(View.GONE);
        tvProductId.setEnabled(false);
        tvProductDescription.setEnabled(false);

        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_confirmation);

        Button noButton = dialog.findViewById(R.id.dialogConfirmationNoButton);
        Button yesButton = dialog.findViewById(R.id.dialogConfirmationYesButton);

        dialog.show();
        yesButton.setOnClickListener(v -> {
            CollectionReference d = db.collection(mCompanyCode + "/" + mWarehouse + "/Products");
            d.document(mProduct.getProductId()).delete();
            dialog.dismiss();
            finish();
        });

        noButton.setOnClickListener(v -> dialog.dismiss());
    }

    private void addDataToFireStore(Products _product) {
        // creating a collection reference
        // for our Firebase Firestore database

        db.collection(mCompanyCode + "/"+mWarehouse+"/Products").document(_product.getProductId()).set(_product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ProductDetails.this, "Product updated", Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductDetails.this, "Fail to add product \n" + e, Toast.LENGTH_LONG).show();
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