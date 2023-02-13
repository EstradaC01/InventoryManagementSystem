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
import android.widget.Button;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.ItemRVAdapter;
import com.example.inventorymanagementsystem.adapters.PurchaseOrderProductListRecyclerViewAdapter;
import com.example.inventorymanagementsystem.models.Products;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderProductList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PurchaseOrderProductListRecyclerViewAdapter mAdapter;
    private static ArrayList<Products> mArrayListProducts;
    private FirebaseFirestore db;
    private String mWarehouse;
    private Users mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order_product_list);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Purchase Order Products</font>"));

        mRecyclerView = findViewById(R.id.purchaseOrderProductListRecyclerView);
        Button btnAddProduct = findViewById(R.id.purchaseOrderProductListBtnAddProduct);
        Button btnSave = findViewById(R.id.purchaseOrderProductListBtnSave);

        // initializing our variable for firebase
        // firestore and getting its instance
        db = FirebaseFirestore.getInstance();

        // creating our new array list
        mArrayListProducts= new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        //getting intent from ItemsSubMenu class along with User object
        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");
        if((ArrayList<Products>) i.getSerializableExtra("ProductList") != null) {
            mArrayListProducts = (ArrayList<Products>) i.getSerializableExtra("ProductList");
        }


        // adding our array list to our recycler view adapter class
        mAdapter = new PurchaseOrderProductListRecyclerViewAdapter(mArrayListProducts, this, PurchaseOrderProductList.this);

        // setting adapter to our recycler view
        mRecyclerView.setAdapter(mAdapter);

        btnAddProduct.setOnClickListener(v -> {
            Intent findProductIntent = new Intent(PurchaseOrderProductList.this, FindProductScreen.class);
            findProductIntent.putExtra("User", mCurrentUser);
            findProductIntent.putExtra("Warehouse", mWarehouse);
            launchFindProductActivity.launch(findProductIntent);
        });

        btnSave.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("ProductList", (Serializable) mArrayListProducts);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

    }

    private ActivityResultLauncher<Intent> launchFindProductActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        searchProductInDatabase(data.getStringExtra("Product"));
                    }
                }
            }
    );

    private void searchProductInDatabase(String _productId) {
        CollectionReference productCollection = db.collection("Warehouses/"+mWarehouse+"/Products");
        productCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d : list) {
                        if(d.getId().equals(_productId)) {
                            Products p = new Products();
                            p = d.toObject(Products.class);
                            mArrayListProducts.add(p);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }
}
