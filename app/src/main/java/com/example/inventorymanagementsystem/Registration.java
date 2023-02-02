package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class Registration extends AppCompatActivity {

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;

    private EditText edtFirstname, edtLastName, edtEmail, edtPassword, edtConfirm;

    private Button btnRegister;

    private String mFirstName, mLastName, mEmail, mPassword, mConfirm;

    private static final String TAG = "Registration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //hide action bar
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        edtFirstname = findViewById(R.id.idEdtFirstName);
        edtLastName = findViewById(R.id.idEdtLastName);
        edtEmail = findViewById(R.id.idEdtEmail);
        edtPassword = findViewById(R.id.idEdtPassword);
        edtConfirm = findViewById(R.id.idEdtConfirmPassword);
        btnRegister = findViewById(R.id.idBtnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFirstName = edtFirstname.getText().toString();
                mLastName = edtLastName.getText().toString();
                mEmail = edtEmail.getText().toString();
                mPassword = edtPassword.getText().toString();
                mConfirm = edtConfirm.getText().toString();

                if(TextUtils.isEmpty(mEmail))
                {
                    edtEmail.setError("Please enter a valid email address");
                }
                if(!mEmail.contains("@"))
                {
                    edtEmail.setError("Please enter a valid email address");
                }
                if (TextUtils.isEmpty(mPassword))
                {
                    edtPassword.setError("Please enter a password");
                }
                if (!Objects.equals(mPassword, mConfirm))
                {
                    edtConfirm.setError("The passwords do not match");
                }
                if(!mEmail.isEmpty() && !mPassword.isEmpty() && mPassword.equals(mConfirm))
                {
                    CreateUser(mEmail, mPassword);
                }

            }
        });
    }

    private void addDataToFireStore(String _firstName, String _lastName, String _userKey, String _email, boolean _isAdmin) {

        // creating a collection reference
        // for our Firebase Firestore database
        CollectionReference dbUsers = db.collection("Users");
        // adding our data to our users object class.
        Users user = new Users();
        user.setFirstName(_firstName);
        user.setLastName(_lastName);
        user.setUserKey(_userKey);
        user.setEmail(_email);
        user.setIsAdmin(_isAdmin);
        dbUsers.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                user.setRank("Employee");
                user.setIsAdmin(false);
                // below method is use to add data to Firebase Firestore
                dbUsers.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // after the data addition is successful
                        // we are displaying a success toast message
                        Toast.makeText(Registration.this, "User was registered successfully.", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // this method is called when the data addition process is failed.
                        // displaying a toast message when data addition is failed.
                        Toast.makeText(Registration.this, "Fail to add user \n" + e, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Failed", ": task");
            }
        });
    }



    private void CreateUser(String _email, String _password)
    {
        mAuth.createUserWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                            user.sendEmailVerification();
                            addDataToFireStore(mFirstName,mLastName,user.getUid(),mEmail, true);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Registration.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}