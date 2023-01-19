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

public class Registration extends AppCompatActivity {

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;

    private EditText edtFirstname, edtLastName, edtEmail, edtPassword, edtCompanyCode;

    private Button btnRegister;

    private String mFirstName, mLastName, mEmail, mPassword, mCompanyCode;

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
        edtCompanyCode = findViewById(R.id.idEdtCompanyGroup);
        btnRegister = findViewById(R.id.idBtnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstName = edtFirstname.getText().toString();
                mLastName = edtLastName.getText().toString();
                mEmail = edtEmail.getText().toString();
                mPassword = edtPassword.getText().toString();
                mCompanyCode = edtCompanyCode.getText().toString();
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
                if(!mEmail.isEmpty() && !mPassword.isEmpty())
                {
                    CreateUser(mEmail, mPassword);
                }
            }
        });
    }


    private void addDataToFireStore(String _firstName, String _lastName, String _userKey, String _email, boolean _isAdmin) {

        // creating a collection reference
        // for our Firebase Firestore database
        CollectionReference dbProducts = db.collection("Users");

        // adding our data to our users object class.
        Users user = new Users(_firstName,_lastName,_userKey,_email, _isAdmin);

        // below method is use to add data to Firebase Firestore
        dbProducts.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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



    private void CreateUser(String _email, String _password)
    {
        mAuth.createUserWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            addDataToFireStore(mFirstName,mLastName,user.getUid().toString(),mEmail, false);

                            Intent i = new Intent(Registration.this, Login.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Registration.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendEmailVerification() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

}