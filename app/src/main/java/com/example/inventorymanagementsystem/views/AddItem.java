package com.example.inventorymanagementsystem.views;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.views.adapters.CustomSpinnerAdapter;
import com.example.inventorymanagementsystem.models.Products;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AddItem extends AppCompatActivity {

    // creating variables for firebase firestore, editTexts and Button
    private Products mProduct;
    private FirebaseFirestore db;
    private EditText productIdEdt, productDescriptionEdt, productUpcEdt, productPcsPerBoxEdt;
    private Button addItemBtn;
    private Spinner categoriesSpinner;
    private ImageView uploadImage;
    private ProgressBar pbLoad;

    // member fields for logged user
    private static Users currentUser;

    private String imageURL;
    private Uri uri;

    private String mWarehouse;


    private static final String TAG = "AddItem";
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
        pbLoad = findViewById(R.id.addItemLayoutPB);

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
        mWarehouse = (String) i.getSerializableExtra("Warehouse");


        // add on click listener to upload image to FirebaseStorage and show in the screen
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent i = result.getData();
                            uri = i.getData();
                            imageURL = uri.toString();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(AddItem.this, "No Image Selected", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                activityResultLauncher.launch(i);
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
                        mProduct.setAvailableUnits("0");
                        mProduct.setExpectedUnits("0");

                        // validating the text fields if empty or not
                        if (TextUtils.isEmpty(mProduct.getProductId())) {
                            productIdEdt.setError("Please enter product id");
                            return;
                        }
                        if (TextUtils.isEmpty(mProduct.getProductDescription())) {
                            productDescriptionEdt.setError("Please enter product description");
                            return;
                        }
                        if (TextUtils.isEmpty(mProduct.getProductUpc()) || mProduct.getProductUpc().length() != 12) {
                            productUpcEdt.setError("Please enter product upc");
                            return;
                        }
                        if (TextUtils.isEmpty(mProduct.getProductPcsPerBox())) {
                            productPcsPerBoxEdt.setError("Please enter product pieces per box");
                            return;
                        }
                        // calling method to add data to Firebase Firestore
                        saveProduct(mProduct);
                    }
                });
    }

    private void addDataToFireStore(Products products) {
        //Log.d("FIREBASE-ADD", "Inside");
        // creating a collection reference
        // for our Firebase Firestore database

        db.collection("Warehouses/"+mWarehouse+"/Products").document(products.getProductId()).set(products).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(AddItem.this, "Product updated", Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddItem.this, "Fail to add product \n" + e, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveProduct(Products _product){
        if(!(uri == null)) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("productImages")
                    .child(uri.getLastPathSegment());

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                    while(!uriTask.isComplete())pbLoad.setVisibility(View.VISIBLE);
                    Uri urlImage = uriTask.getResult();
                    _product.setImageUri(urlImage.toString());
                    addDataToFireStore(_product);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    addDataToFireStore(_product);
                }
            });
        } else {
            addDataToFireStore(_product);
        }

    }

}

