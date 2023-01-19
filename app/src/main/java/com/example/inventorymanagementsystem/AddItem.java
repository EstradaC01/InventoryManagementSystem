package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class AddItem extends AppCompatActivity {

    // creating variables for firebase firestore, editTexts and Button
    private Products mProduct;
    private FirebaseFirestore db;
    private EditText productIdEdt, productDescriptionEdt, productUpcEdt, productPcsPerBoxEdt;
    private Button addItemBtn;
    private Spinner categoriesSpinner;
    private ImageView uploadImage;

    // member fields for logged user
    private static Users currentUser;

    private String mCompanyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Create Product</font>"));

        // initializing variables
        productIdEdt = findViewById(R.id.idEdtProductId);
        productDescriptionEdt = findViewById(R.id.idEdtProductDescription);
        productUpcEdt = findViewById(R.id.idEdtProductUpc);
        productPcsPerBoxEdt = findViewById(R.id.idEdtPiecesPerBox);
        addItemBtn = findViewById(R.id.idBtnCreateProduct);
        categoriesSpinner = findViewById(R.id.idCategoriesSpinner);
        uploadImage = findViewById(R.id.idIVUploadPhoto);

        // setting default string for spinner
        String defaultTextForSpinner = "Category";
        String[] arrayForSpinner = {"Electronic", "Medicine", "Misc", "Other"};
        // creating ArrayAdapter for dropdown menu
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.product_categories, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//        categoriesSpinner.setAdapter(adapter);

        categoriesSpinner.setAdapter(new CustomSpinnerAdapter(this, R.layout.spinner_row, arrayForSpinner, defaultTextForSpinner));

        // initializng variable firebase
        // firestore and getting its instance
        db = FirebaseFirestore.getInstance();

        // getting intent from ItemsSubMenu class along with User object
        Intent i = getIntent();
        currentUser = (Users)i.getSerializableExtra("User");
        mCompanyCode = (String) i.getSerializableExtra("CompanyCode");
        // getting instance for Cloud Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // add on lick listener to upload image to FirebaseStorage and show in the screen
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // add on click listener to create item button
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create product
                mProduct = new Products();
                // getting data from editText fields.
                mProduct.setProductId(productIdEdt.getText().toString());
                mProduct.setProductDescription(productDescriptionEdt.getText().toString());
                mProduct.setProductUpc(productUpcEdt.getText().toString());
                mProduct.setProductPcsPerBox(productPcsPerBoxEdt.getText().toString());
                mProduct.setUserKey(currentUser.getUserKey());
                mProduct.setProductOwner(currentUser.getSystemId());
                mProduct.setProductTimeAdded();
                // validating the text fields if empty or not
                if (TextUtils.isEmpty(mProduct.getProductId())) {
                    productIdEdt.setError("Please enter product id");
                    return;
                }
                if (TextUtils.isEmpty(mProduct.getProductDescription())) {
                    productDescriptionEdt.setError("Please enter product description");
                    return;
                }
                if (TextUtils.isEmpty(mProduct.getProductUpc())) {
                    productUpcEdt.setError("Please enter product upc");
                    return;
                }
                if (TextUtils.isEmpty(mProduct.getProductPcsPerBox())) {
                    productPcsPerBoxEdt.setError("Please enter product pieces per box");
                    return;
                }
                // calling method to add data to Firebase Firestore
                addDataToFireStore(mProduct);
            }
        });
    }

    private void addDataToFireStore(Products products) {
        //Log.d("FIREBASE-ADD", "Inside");
        // creating a collection reference
        // for our Firebase Firestore database

        db.collection(mCompanyCode + "/WarehouseOne/Products").document(products.getProductId()).set(products).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddItem.this, "Product updated", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddItem.this, "Fail to add product \n" + e, Toast.LENGTH_LONG).show();
            }
        });
    }
}

