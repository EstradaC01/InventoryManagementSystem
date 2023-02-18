package com.example.inventorymanagementsystem.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.PurchaseOrderRecyclerViewAdapter;
import com.example.inventorymanagementsystem.models.Orders;
import com.example.inventorymanagementsystem.models.Products;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {

    private TextView orderCustomerNameTV, orderCompanyNameTV, orderReferenceTV, orderFirstAddressTV,
            orderSecondAddressTV, orderStateTV, orderCityTV, orderZipcodeTV,orderCountryTV, orderPhoneNumberTV,
            orderEmailAddressTV, orderShippingMethodTV;
    private FirebaseFirestore db;
    private static Orders mOrder;
    private static String mWarehouse;
    private RecyclerView mRecyclerView;
    private PurchaseOrderRecyclerViewAdapter mRecyclerViewAdapter;
    private ArrayList<Products> mProductsArrayList;


    private static final String TAG = "OrderDetails";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Order Details</font>"));
        }


        Intent i = getIntent();
        mOrder = (Orders) i.getSerializableExtra("Order object");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");
        HashMap<String, String> productList = mOrder.getProductList();
        db = FirebaseFirestore.getInstance();
        mProductsArrayList = new ArrayList<>();
        for(Map.Entry<String, String> e: productList.entrySet()) {
            Products product = new Products();
            product.setProductId(e.getKey());
            product.setExpectedUnits(e.getValue());
            mProductsArrayList.add(product);
        }

        orderCustomerNameTV = findViewById(R.id.orderDetailsEdtCustomerName);
        orderCompanyNameTV = findViewById(R.id.orderDetailsEdtCompanyName);
        orderReferenceTV = findViewById(R.id.orderDetailsEdtReferenceNumber);
        orderFirstAddressTV = findViewById(R.id.orderDetailsEdtFirstAddress);
        orderSecondAddressTV = findViewById(R.id.orderDetailsEdtSecondAddress);
        orderStateTV = findViewById(R.id.orderDetailsEdtState);
        orderCityTV = findViewById(R.id.orderDetailsEdtCity);
        orderZipcodeTV = findViewById(R.id.orderDetailsEdtZipcode);
        orderCountryTV = findViewById(R.id.orderDetailsCcp);
        orderPhoneNumberTV = findViewById(R.id.orderDetailsEdtPhoneNumber);
        orderEmailAddressTV = findViewById(R.id.orderDetailsEmailAddress);
        orderShippingMethodTV = findViewById(R.id.orderDetailsShippingMethod);
        mRecyclerView = findViewById(R.id.orderDetailsRecyclerView);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerViewAdapter = new PurchaseOrderRecyclerViewAdapter(mProductsArrayList, this);

        mRecyclerView.setAdapter(mRecyclerViewAdapter);


        orderCustomerNameTV.setText(mOrder.getOrderCustomer());
        orderCompanyNameTV.setText(mOrder.getOrderCompany());
        orderReferenceTV.setText(mOrder.getOrderReference());
        orderFirstAddressTV.setText(mOrder.getOrderFirstAddress());
        orderSecondAddressTV.setText(mOrder.getOrderSecondAddress());
        orderStateTV.setText(mOrder.getOrderState());
        orderCityTV.setText(mOrder.getOrderCity());
        orderZipcodeTV.setText(mOrder.getOrderZipcode());
        orderCountryTV.setText(mOrder.getOrderCountry());
        orderPhoneNumberTV.setText(mOrder.getOrderPhoneNumber());
        orderEmailAddressTV.setText(mOrder.getOrderEmailAddress());
        orderShippingMethodTV.setText(mOrder.getOrderShippingMethod());
    }
}