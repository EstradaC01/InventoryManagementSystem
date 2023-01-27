package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserDetails extends AppCompatActivity {

    private static EditText tvUserName, tvUserEmail, tvUserRank;
    private static ImageButton edtChangeUserDetails, edtDeleteUserDetails;
    private static Button saveButton;
    private FirebaseFirestore db;
    private static Users mUser;
    private static String mCompanyCode;
    private static String mWarehouse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>User Details</font>"));
        }

        Intent i = getIntent();
        mUser = (Users)i.getSerializableExtra("UserObject");
        mCompanyCode = (String) i.getSerializableExtra("CompanyCode");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        tvUserName = findViewById(R.id.idTVUsername);
        tvUserEmail = findViewById(R.id.idTvUserEmail);
        tvUserRank = findViewById(R.id.idTVUserRank);
        saveButton = findViewById(R.id.userDetailsSaveButton);
        edtDeleteUserDetails = findViewById(R.id.userDetailsDeleteButton);
        edtChangeUserDetails = findViewById(R.id.userDetailsEditButton);

        db = FirebaseFirestore.getInstance();

        tvUserName.setText(mUser.getFirstName() + " " + mUser.getLastName());
        tvUserEmail.setText(mUser.getEmail());
        tvUserRank.setText(mUser.getRank());

        tvUserName.setEnabled(false);
        tvUserEmail.setEnabled(false);
        tvUserRank.setEnabled(false);

        edtDeleteUserDetails.setOnClickListener(v -> showCustomDialog());

        edtChangeUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setVisibility(View.VISIBLE);
                tvUserName.setEnabled(true);
                tvUserName.requestFocus();
                tvUserRank.setEnabled(true);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference d = db.collection(mCompanyCode + "/CompanyUsers/" + "Users");

                d.document(mUser.getUserKey()).delete();
                String[] split = tvUserName.getText().toString().split("\\s+");

                // getting data from editText fields.
                mUser.setFirstName(split[0]);
                mUser.setLastName(split[1]);
                mUser.setEmail(tvUserEmail.getText().toString());
                mUser.setRank(tvUserRank.getText().toString());
                addDataToFireStore(mUser);
            }
        });
    }

    void showCustomDialog() {
        final Dialog dialog = new Dialog(UserDetails.this);

        saveButton.setVisibility(View.GONE);
        tvUserName.setEnabled(false);
        tvUserEmail.setEnabled(false);
        tvUserRank.setEnabled(false);

        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_confirmation);

        Button noButton = dialog.findViewById(R.id.dialogConfirmationNoButton);
        Button yesButton = dialog.findViewById(R.id.dialogConfirmationYesButton);

        dialog.show();
        yesButton.setOnClickListener(v -> {
            CollectionReference d = db.collection(mCompanyCode + "/CompanyUsers/"+"Users");
            d.document(mUser.getUserKey()).delete();
            d.whereEqualTo("userKey", mUser.getUserKey()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (!value.isEmpty()) {
                        List<DocumentSnapshot> list = value.getDocuments();
                        for (DocumentSnapshot d : list) {
                            d.getReference().delete();
                        }
                    }
                }
            });
            dialog.dismiss();
            finish();
        });

        noButton.setOnClickListener(v -> dialog.dismiss());
    }

    private void addDataToFireStore(Users _user) {
        // creating a collection reference
        // for our Firebase Firestore database
        CollectionReference d = db.collection(mCompanyCode + "/CompanyUsers/"+"Users");
        d.whereEqualTo("userKey", mUser.getUserKey()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {

                    List<DocumentSnapshot> list = value.getDocuments();
                    for (DocumentSnapshot d : list) {
                        d.getReference().set(_user);
                    }
                    Toast.makeText(UserDetails.this, "User updated", Toast.LENGTH_LONG).show();

                    finish();
                }
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