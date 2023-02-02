package com.example.inventorymanagementsystem.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Orders;
import com.example.inventorymanagementsystem.models.Products;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddOrder extends AppCompatActivity {

    private Orders mOrder;
    private FirebaseFirestore db;
    private EditText orderIDEdt, orderDateEdt, orderReferenceEdt, orderCustomerEdt, orderStatusEdt;
    private Button addOrderBtn;
    private static Users currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);


        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Create Order</font>"));

        orderIDEdt = findViewById(R.id.idEdtOrderId);
        orderDateEdt = findViewById(R.id.idEdtOrderDate);
        orderReferenceEdt = findViewById(R.id.idEdtReferenceNumber);
        orderCustomerEdt = findViewById(R.id.idEdtCustomerName);
        orderStatusEdt = findViewById(R.id.idEdtStatus);
        addOrderBtn = findViewById(R.id.idBtnCreateOrder);

        // initializng variable firebase
        // firestore and getting its instance
        db = FirebaseFirestore.getInstance();


        // getting intent from ItemsSubMenu class along with User object
        Intent i = getIntent();
        currentUser = (Users)i.getSerializableExtra("User");

        addOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrder = new Orders();

                mOrder.setOrderID(orderIDEdt.getText().toString());
                mOrder.setOrderDate(orderDateEdt.getText().toString());
                mOrder.setOrderReference(orderReferenceEdt.getText().toString());
                mOrder.setOrderCustomer(orderCustomerEdt.getText().toString());
                mOrder.setOrderStatus(orderStatusEdt.getText().toString());

                // validating the text fields if empty or not
                if (TextUtils.isEmpty(mOrder.getOrderID())) {
                    orderIDEdt.setError("Please enter order id");
                    return;
                }
                if (TextUtils.isEmpty(mOrder.getOrderDate())) {
                    orderDateEdt.setError("Please enter order date");
                    return;
                }
                if (TextUtils.isEmpty(mOrder.getOrderReference())) {
                    orderReferenceEdt.setError("Please enter order reference");
                    return;
                }
                if (TextUtils.isEmpty(mOrder.getOrderCustomer())) {
                    orderCustomerEdt.setError("Please enter order customer name");
                    return;
                }
                if (TextUtils.isEmpty(mOrder.getOrderStatus())) {
                    orderStatusEdt.setError("Please enter order customer name");
                    return;
                }
                // calling method to add data to Firebase Firestore
                addDataToFireStore(mOrder);
            }
        });
    }
    private void addDataToFireStore(Orders orders) {
        //Log.d("FIREBASE-ADD", "Inside");
        // creating a collection reference
        // for our Firebase Firestore database

        db.collection("Orders").document(orders.getOrderID()).set(orders).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(AddOrder.this, "Order updated", Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddOrder.this, "Failed to add order \n" + e, Toast.LENGTH_LONG).show();
            }
        });
    }
}