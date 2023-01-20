package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Login extends AppCompatActivity {

    // creating variables for the EditText
    private EditText edtLoginEmail, edtLoginPassword, edtCompanyCode;

    private String mEmail, mPassword, mCompanyCode, mUserKey;

    // creating variables for the buttons
    private Button btnLogin, btnRegistration, btnForgotPassword;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static Users currentUser;

    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hide action bar
        getSupportActionBar().hide();

        // getting instance of firebase authentication
        mAuth = FirebaseAuth.getInstance();

        // getting instance of firebase firestore db
        db = FirebaseFirestore.getInstance();
        // instantiating editText and button variables
        edtLoginEmail = findViewById(R.id.idEdtLoginEmail);
        edtLoginPassword = findViewById(R.id.idEdtLoginPassword);
        btnLogin = findViewById(R.id.idLoginBtn);
        btnRegistration = findViewById(R.id.idRegisterBtn);
        btnForgotPassword = findViewById(R.id.idforgotpasswordbtn);
        edtCompanyCode = findViewById(R.id.idEdtLoginScreenCompanyCode);

        // creating on click listener to login user
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = edtLoginEmail.getText().toString();
                mPassword = edtLoginPassword.getText().toString();
                mCompanyCode = edtCompanyCode.getText().toString();

                if(TextUtils.isEmpty(mEmail))
                {
                    edtLoginEmail.setError("Enter valid email address");
                }
                if(TextUtils.isEmpty(mPassword))
                {
                    edtLoginPassword.setError("Enter valid password");
                }
                if(TextUtils.isEmpty(mCompanyCode))
                {
                    edtCompanyCode.setError("Enter valid password");
                }
                if(!mEmail.isEmpty() && !mPassword.isEmpty() && !mCompanyCode.isEmpty())
                {
                    CollectionReference companyCodeRef = db.collection(mCompanyCode);

                    if (companyCodeRef.getId().isEmpty()) {
                        Toast.makeText(Login.this, "Company Code Invalid", Toast.LENGTH_LONG).show();
                    } else {
                        signInUser(mEmail, mPassword, mCompanyCode);
                    }
                }
            }
        });

        // creating on click listener to register user
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Registration.class);
                startActivity(i);
            }
        });

        //creating on click listener to forgot password
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, ForgotPassword.class);
                startActivity(i);
            }
        });
    }

    private void signInUser(String _email, String _password, String _companyCode) {

        mAuth.signInWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            mUserKey = user.getUid();
                            CollectionReference adminRef = db.collection(_companyCode + "/CompanyUsers/Users");

                            adminRef.whereEqualTo("userKey", mUserKey).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (!value.isEmpty()) {
                                        List<DocumentSnapshot> list = value.getDocuments();
                                        for (DocumentSnapshot d : list) {
                                            currentUser = d.toObject(Users.class);
                                        }
                                        Intent i = new Intent (Login.this, MainActivity.class);
                                        i.putExtra("User", currentUser);
                                        i.putExtra("CompanyCode", _companyCode);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(Login.this, "Company code invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "No user found with email and password provided.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}