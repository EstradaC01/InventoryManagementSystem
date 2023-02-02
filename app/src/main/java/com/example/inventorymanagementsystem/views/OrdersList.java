package com.example.inventorymanagementsystem.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.OrderRVAdapter;
import com.example.inventorymanagementsystem.models.Orders;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class OrdersList extends AppCompatActivity {

    private RecyclerView orderRV;
    private ArrayList<Orders> mOrdersArrayList;
    private OrderRVAdapter mOrderRVAdapter;
    private FirebaseFirestore db;
    private ProgressBar loadingOrdersPB;
    private static Users currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Order Inquiry</font>"));

        orderRV = findViewById(R.id.idRVOrders);
        loadingOrdersPB = findViewById(R.id.idOrderProgressBar);
        db = FirebaseFirestore.getInstance();
        Intent i = getIntent();

        mOrdersArrayList = new ArrayList<>();
        orderRV.setHasFixedSize(true);
        orderRV.setLayoutManager(new LinearLayoutManager(this));
        mOrderRVAdapter = new OrderRVAdapter(mOrdersArrayList, this, i);

        orderRV.setAdapter(mOrderRVAdapter);


        currentUser = (Users)i.getSerializableExtra("User");

        CollectionReference ordersRef = db.collection("Orders");

        ordersRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                loadingOrdersPB.setVisibility(View.GONE);
                List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                for (DocumentSnapshot d : docs) {
                    Orders o = d.toObject(Orders.class);
                    mOrdersArrayList.add(o);
                    Log.d("Orders", ": " + o);
                }

                mOrderRVAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrdersList.this, "Failed to read data", Toast.LENGTH_LONG);
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