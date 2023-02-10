package com.example.inventorymanagementsystem.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Products;
import com.example.inventorymanagementsystem.models.Users;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddPurchaseOrder extends AppCompatActivity {

    FirebaseFirestore db;
    private Users mCurrentUser;
    private String mWarehouse;
    private RecyclerView mRecyclerView;
    private EditText edtAnticipatedArrivalDate, edtPoNumber, edtShippingFrom;

    String mAnticipatedArrivalDate, mPoNumber, mShippingFrom;

    ArrayList<Products> mProductsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase_order);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Purchase Order</font>"));

        // initialize firebase
        db = FirebaseFirestore.getInstance();

        Button btnSubmit = findViewById(R.id.addPurchaseOrderBtnSubmit);
        Button btnAddRemoveProduct = findViewById(R.id.addPurchaseOrderBtnAddRemoveProducts);
        edtAnticipatedArrivalDate = findViewById(R.id.addPurchaseOrderEdtAnticipatedArrivalDate);
        edtPoNumber = findViewById(R.id.addPurchaseOrderEdtPONumber);
        edtShippingFrom = findViewById(R.id.addPurchaseOrderEdtShippingFrom);
        mRecyclerView = findViewById(R.id.addPurchaseOrderRecyclerView);


        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        btnSubmit.setOnClickListener(v -> {

            if(TextUtils.isEmpty(mAnticipatedArrivalDate)) {
                edtAnticipatedArrivalDate.setError("Enter Anticipated Arrival Date");
            }
            if(TextUtils.isEmpty(mPoNumber)) {
                edtPoNumber.setError("Enter PO Number");
            }
            if(TextUtils.isEmpty(mShippingFrom)) {
                edtShippingFrom.setError("Enter Shipping From");
            }
        });

        btnAddRemoveProduct.setOnClickListener(v -> {

        });
    }
}