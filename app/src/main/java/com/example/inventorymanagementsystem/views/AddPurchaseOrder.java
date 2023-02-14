package com.example.inventorymanagementsystem.views;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.PurchaseOrderRecyclerViewAdapter;
import com.example.inventorymanagementsystem.models.Products;
import com.example.inventorymanagementsystem.models.PurchaseOrder;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddPurchaseOrder extends AppCompatActivity {

    FirebaseFirestore db;
    private Users mCurrentUser;
    private String mWarehouse;
    private RecyclerView mRecyclerView;
    private EditText edtAnticipatedArrivalDate, edtPoNumber, edtShippingFrom;

    private String mAnticipatedArrivalDate, mPoNumber, mShippingFrom;

    private ArrayList<Products> mProductsArrayList;
    private PurchaseOrderRecyclerViewAdapter mRecyclerViewAdapter;
    private static final String TAG = "AddPurchaseOrder";

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

        // creating our new array list

        mProductsArrayList = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        mRecyclerViewAdapter = new PurchaseOrderRecyclerViewAdapter(mProductsArrayList, this);

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        btnSubmit.setOnClickListener(v -> {

            mAnticipatedArrivalDate = edtAnticipatedArrivalDate.getText().toString();
            mPoNumber = edtPoNumber.getText().toString();
            mShippingFrom = edtShippingFrom.getText().toString();

            if(TextUtils.isEmpty(mAnticipatedArrivalDate)) {
                edtAnticipatedArrivalDate.setError("Enter Anticipated Arrival Date");
            }
            if(TextUtils.isEmpty(mPoNumber)) {
                edtPoNumber.setError("Enter PO Number");
            }
            if(TextUtils.isEmpty(mShippingFrom)) {
                edtShippingFrom.setError("Enter Shipping From");
            }
            if(!mAnticipatedArrivalDate.isEmpty() && !mPoNumber.isEmpty() &&
                !mShippingFrom.isEmpty() && !mProductsArrayList.isEmpty()) {

                PurchaseOrder purchaseOrder = new PurchaseOrder();

                purchaseOrder.setDateCreated(getCurrentTime());
                purchaseOrder.setPoNumber(mPoNumber);
                purchaseOrder.setAnticipatedArrivalDate(mAnticipatedArrivalDate);
                purchaseOrder.setStatus("OPEN");
                purchaseOrder.setShippingFrom(mShippingFrom);
                purchaseOrder.setProducts(mProductsArrayList);
                addPurchaseOrderToDatabase(purchaseOrder);
                updateProductExpectedQuantity(mProductsArrayList);
            }
        });

        btnAddRemoveProduct.setOnClickListener(v -> {
            Intent addProductIntent = new Intent(AddPurchaseOrder.this, PurchaseOrderProductList.class);
            addProductIntent.putExtra("User", mCurrentUser);
            addProductIntent.putExtra("Warehouse", mWarehouse);
            if(mProductsArrayList != null) {
                addProductIntent.putExtra("ProductList", (Serializable) mProductsArrayList);

            }
            launchAddProductsActivity.launch(addProductIntent);
        });
    }

    private ActivityResultLauncher<Intent> launchAddProductsActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        ArrayList<Products> tempProduct = (ArrayList<Products>) data.getSerializableExtra("ProductList");
                        mProductsArrayList.clear();
                        mRecyclerViewAdapter.notifyDataSetChanged();
                        for(Products p : tempProduct) {
                            mProductsArrayList.add(p);
                            mRecyclerViewAdapter.notifyDataSetChanged();
                        }

                    }

                }
            });

    private void addPurchaseOrderToDatabase(PurchaseOrder _purchaseOrder) {
        db.collection("Warehouses/"+mWarehouse+"/PurchaseOrders").document(_purchaseOrder.getPoNumber()).set(_purchaseOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddPurchaseOrder.this, "Purchase Order Added To System", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProductExpectedQuantity(List<Products> _products) {
        for(Products p : _products) {
            db.collection("Warehouses/"+mWarehouse+"/Products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list) {
                            if(d.getId().equals(p.getProductId())) {
                                Products tempProduct = d.toObject(Products.class);
                                DocumentReference productReference = db.collection("Warehouses/"+mWarehouse+"/Products").document(d.getId());
                                
                                productReference.update("expectedUnits", String.valueOf(Integer.parseInt(p.getExpectedUnits()) + Integer.parseInt(tempProduct.getExpectedUnits()))).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "DocumentSnapshot successfuly updated!");
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    }

    private String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(("MM/dd/yyyy HH:mm:ss"));
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}