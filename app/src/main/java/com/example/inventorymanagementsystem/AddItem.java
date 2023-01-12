package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddItem extends AppCompatActivity {

    // creating strings for storing
    // our values from editText fields
    private String mProductId, mProductDescription, mProductUpc, mProductQty, mProductPcsPerBox, mProductTimeAdded;
    // creating variables for firebase firestore, editTexts and Button
    private Products mProduct;
    private FirebaseFirestore db;
    private EditText productIdEdt, productDescriptionEdt, productUpcEdt, productQtyEdt, productPcsPerBoxEdt;
    private Button addItemBtn;
    LocalDateTime myDateObj = LocalDateTime.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // initializing variables
        productIdEdt = findViewById(R.id.idEdtProductId);
        productDescriptionEdt = findViewById(R.id.idEdtProductDescription);
        productUpcEdt = findViewById(R.id.idEdtProductUpc);
        productQtyEdt = findViewById(R.id.idEdtQty);
        productPcsPerBoxEdt = findViewById(R.id.idEdtPiecesPerBox);
        addItemBtn = findViewById(R.id.idBtnCreateProduct);

        // initializng variable firebase
        // firestore and getting its instance
        db = FirebaseFirestore.getInstance();

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
                mProduct.setProductQty(productQtyEdt.getText().toString());
                mProduct.setProductPcsPerBox(productPcsPerBoxEdt.getText().toString());
                mProduct.setProductTimeAdded(getCurrentTime());
                // validating the text fields if empty or not
                if(TextUtils.isEmpty(mProductId))
                {
                    productIdEdt.setError("Please enter product id");
                } else if (TextUtils.isEmpty(mProductDescription))
                {
                    productDescriptionEdt.setError("Please enter product description");
                } else if (TextUtils.isEmpty(mProductUpc)) {
                    productUpcEdt.setError("Please enter product upc");
                } else if (TextUtils.isEmpty(mProductPcsPerBox)) {
                    productPcsPerBoxEdt.setError("Please enter product pieces per box");
                } else {
                    // calling method to add data to Firebase Firestore
                    if(mProductQty.isEmpty()) {
                        mProductQty = "0";
                    }
                    addDataToFireStore(mProduct);
                }

            }
        });

    }

    private void addDataToFireStore(Products products) {

        // creating a collection reference
        // for our Firebase Firestore database
        CollectionReference dbProducts = db.collection("Products");

        // adding our data to our courses object class.
        // below method is use to add data to Firebase Firestore
        dbProducts.add(products).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message
                Toast.makeText(AddItem.this, "Your product has been added to the system.", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast mesage when data addition is failed.
                Toast.makeText(AddItem.this, "Fail to add product \n" + e, Toast.LENGTH_LONG).show();
            }
        });
    }
    private String getCurrentTime() {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }
}

