package com.example.inventorymanagementsystem.views;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.PurchaseOrderRecyclerViewAdapter;
import com.example.inventorymanagementsystem.models.Helper;
import com.example.inventorymanagementsystem.models.Orders;
import com.example.inventorymanagementsystem.models.Products;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddOrder extends AppCompatActivity {
    private Users mCurrentUser;
    private String mWarehouse;
    private RecyclerView mRecyclerView;

    private String mOrderId, mReferenceNumber, mCustomerName, mCompanyName, mFirstAddress, mSecondAddress,
            mState, mCity, mCountry, mZipcode, mPhoneNumber, mEmailAddress, mShippingMethod;

    private ArrayList<Products> mProductsArrayList;
    private PurchaseOrderRecyclerViewAdapter mRecyclerViewAdapter;
    private static final String TAG = "AddOrder";
    private Orders mOrder;
    FirebaseFirestore db;
    private EditText orderCustomerNameEdt, orderCompanyNameEdt, orderReferenceEdt, orderFirstAddressEdt,
            orderSecondAddressEdt, orderStateEdt, orderCityEdt, orderZipcodeEdt,orderCountryEdt, orderPhoneNumberEdt,
            orderEmailAddressEdt, orderShippingMethodEdt;
    private Button submitOrderBtn;
    private Button addRemoveOrderBtn;
    private CountryCodePicker ccp, ccpPhoneCode;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);


        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Create Order</font>"));

        ccp = (CountryCodePicker) findViewById(R.id.addOrderCcp);
        ccpPhoneCode = (CountryCodePicker) findViewById(R.id.addOrderCcpPhoneCode);

        orderReferenceEdt = findViewById(R.id.addOrderEdtReferenceNumber);
        orderCustomerNameEdt = findViewById(R.id.addOrderEdtCustomerName);
        orderCompanyNameEdt = findViewById(R.id.addOrderEdtCompanyName);
        orderFirstAddressEdt = findViewById(R.id.addOrderEdtFirstAddress);
        orderSecondAddressEdt = findViewById(R.id.addOrderEdtSecondAddress);
        orderStateEdt = findViewById(R.id.addOrderEdtState);
        orderCityEdt = findViewById(R.id.addOrderEdtCity);
        orderZipcodeEdt = findViewById(R.id.addOrderEdtZipcode);
        orderEmailAddressEdt = findViewById(R.id.addOrderEmailAddress);
        orderPhoneNumberEdt = findViewById(R.id.addOrderEdtPhoneNumber);
        orderShippingMethodEdt = findViewById(R.id.addOrderShippingMethod);
        submitOrderBtn = findViewById(R.id.addOrderBtnSubmit);
        addRemoveOrderBtn = findViewById(R.id.addOrderBtnAddRemoveProducts);
        mRecyclerView = findViewById(R.id.addOrderRecyclerView);

        ccpPhoneCode.registerCarrierNumberEditText(orderPhoneNumberEdt);
        // initializng variable firebase
        // firestore and getting its instance
        db = FirebaseFirestore.getInstance();

        // creating our new array list

        mProductsArrayList = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // getting intent from ItemsSubMenu class along with User object
        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        mRecyclerViewAdapter = new PurchaseOrderRecyclerViewAdapter(mProductsArrayList, this);

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        submitOrderBtn.setOnClickListener(v -> {

            boolean hasErrors = false;

            mReferenceNumber = orderReferenceEdt.getText().toString();
            mCustomerName = orderCustomerNameEdt.getText().toString();
            mCompanyName = orderCompanyNameEdt.getText().toString();
            mFirstAddress = orderFirstAddressEdt.getText().toString();
            mSecondAddress = orderSecondAddressEdt.getText().toString();
            mState = orderStateEdt.getText().toString();
            mCity = orderCityEdt.getText().toString();
            mZipcode = orderZipcodeEdt.getText().toString();
            mCountry = ccp.getSelectedCountryName();
            mPhoneNumber = orderPhoneNumberEdt.getText().toString();
            mEmailAddress = orderEmailAddressEdt.getText().toString();
            mShippingMethod = orderShippingMethodEdt.getText().toString();

            if(TextUtils.isEmpty(mReferenceNumber)) {
                orderReferenceEdt.setError("Enter Reference Number");
                hasErrors = true;
            }
            if(TextUtils.isEmpty(mCustomerName)) {
                orderCustomerNameEdt.setError("Enter Customer Name");
                hasErrors = true;
            }
            if(TextUtils.isEmpty(mCompanyName)) {
                orderCompanyNameEdt.setError("Enter Company Name");
                hasErrors = true;
            }
            if(TextUtils.isEmpty(mFirstAddress)) {
                orderCustomerNameEdt.setError("Enter an Address");
                hasErrors = true;
            }
            if(TextUtils.isEmpty(mState)) {
                orderStateEdt.setError("Enter State");
                hasErrors = true;
            }
            if(TextUtils.isEmpty(mCity)) {
                orderCityEdt.setError("Enter City");
                hasErrors = true;
            }
            if(TextUtils.isEmpty(mZipcode)) {
                orderZipcodeEdt.setError("Enter Zipcode");
                hasErrors = true;
            }
            if(TextUtils.isEmpty(mCountry)) {
                orderCountryEdt.setError("Enter Country");
                hasErrors = true;
            }
            if(TextUtils.isEmpty(mPhoneNumber) || !ccp.isValidFullNumber()) {
                orderPhoneNumberEdt.setError("Enter Phone Number");
                hasErrors = true;
            }
            if(TextUtils.isEmpty(mEmailAddress) || !Helper.isValidEmail(mEmailAddress)) {
                orderEmailAddressEdt.setError("Enter an Email Address");
                hasErrors = true;
            }
            if(TextUtils.isEmpty(mShippingMethod)) {
                orderShippingMethodEdt.setError("Enter a Shipping Method");
                hasErrors = true;
            }
            if(mProductsArrayList.isEmpty()) {
                Toast.makeText(getBaseContext(), "Add products to order", Toast.LENGTH_LONG)
                        .show();
                hasErrors = true;
            }

            if (!hasErrors) {
                CollectionReference ordersRef = db.collection("OrderCount");
                ordersRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot d : docs) {
                            Object id = d.get("count");
                            mOrderId = id.toString();
                            count = Integer.parseInt(mOrderId);
                        }
                    }
                }).addOnCompleteListener(task -> {
                    mOrder = new Orders();

                    mOrder.setOrderID(mOrderId);
                    mOrder.setOrderDate(getCurrentTime());
                    mOrder.setOrderReference(mReferenceNumber);
                    mOrder.setOrderCustomer(mCustomerName);
                    mOrder.setOrderCompany(mCompanyName);
                    mOrder.setOrderFirstAddress(mFirstAddress);
                    mOrder.setOrderSecondAddress(mSecondAddress);
                    mOrder.setOrderState(mState);
                    mOrder.setOrderCity(mCity);
                    mOrder.setOrderZipcode(mZipcode);
                    mOrder.setOrderCountry(mCountry);
                    mOrder.setOrderPhoneNumber(mPhoneNumber);
                    mOrder.setOrderEmailAddress(mEmailAddress);
                    mOrder.setOrderShippingMethod(mShippingMethod);
                    mOrder.setOrderStatus("Pending");
                    addDataToFireStore(mOrder);
                });
            }
        });
        addRemoveOrderBtn.setOnClickListener(v1 -> {
            Intent addProductIntent = new Intent(AddOrder.this, PurchaseOrderProductList.class);
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(("MM/dd/yyyy"));
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
    private void addDataToFireStore(Orders orders) {
        //Log.d("FIREBASE-ADD", "Inside");
        // creating a collection reference
        // for our Firebase Firestore database
        ++count;
        db.collection("OrderCount").document("counter").update("count", count);
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