package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductDetails extends AppCompatActivity {

    private TextView tvProductId, tvProductDescription;
    private ImageButton edtChangeProductDetails, edtDeleteProductDetails;
    private ImageView ivProductImage;
    private FirebaseFirestore db;
    private static Products mProduct;
    private static String mCompanyCode;
    private static String mWarehouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Product Details</font>"));

        Intent i = getIntent();
        mProduct = (Products)i.getSerializableExtra("Object");
        mCompanyCode = (String) i.getSerializableExtra("CompanyCode");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        tvProductId = findViewById(R.id.idTvProductId);
        tvProductDescription = findViewById(R.id.idTvProductDescription);
        ivProductImage = findViewById(R.id.productDetailsImageViewProductImage);
        edtChangeProductDetails = findViewById(R.id.productDetailsEditButton);
        edtDeleteProductDetails = findViewById(R.id.productDetailsDeleteButton);

        db = FirebaseFirestore.getInstance();

        tvProductId.setText(mProduct.getProductId());
        tvProductDescription.setText(mProduct.getProductDescription());

        if(mProduct.getImageUri() != null) {
            Glide.with(this).load(mProduct.getImageUri()).into(ivProductImage);
        }

        edtDeleteProductDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
    }

    void showCustomDialog() {
        final Dialog dialog = new Dialog(ProductDetails.this);

        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_confirmation);

        final TextView title = dialog.findViewById(R.id.dialogConfirmationTitle);
        final TextView description = dialog.findViewById(R.id.dialogConfirmationDescription);
        Button noButton = dialog.findViewById(R.id.dialogConfirmationNoButton);
        Button yesButton = dialog.findViewById(R.id.dialogConfirmationYesButton);

        dialog.show();
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference d = db.collection(mCompanyCode + "/" + mWarehouse + "/Products");
                d.document(mProduct.getProductId()).delete();
                dialog.dismiss();
                finish();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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