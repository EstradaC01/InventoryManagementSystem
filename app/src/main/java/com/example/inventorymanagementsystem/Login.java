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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    // creating variables for the EditText
    private EditText edtLoginEmail, edtLoginPassword;

    private String mEmail, mPassword;

    // creating variables for the buttons
    private Button btnLogin, btnRegistration;

    private FirebaseAuth mAuth;

    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hide action bar
        getSupportActionBar().hide();

        // getting instance of firebase authentication
        mAuth = FirebaseAuth.getInstance();

        // instantiating editText and button variables
        edtLoginEmail = findViewById(R.id.idEdtLoginEmail);
        edtLoginPassword = findViewById(R.id.idEdtLoginPassword);
        btnLogin = findViewById(R.id.idLoginBtn);
        btnRegistration = findViewById(R.id.idRegisterBtn);

        // creating on click listener to login user
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = edtLoginEmail.getText().toString();
                mPassword = edtLoginPassword.getText().toString();

                if(TextUtils.isEmpty(mEmail))
                {
                    edtLoginEmail.setError("Enter valid email address");
                }
                if(TextUtils.isEmpty(mPassword))
                {
                    edtLoginPassword.setError("Enter valid password");
                }
                if(!mEmail.isEmpty() && !mPassword.isEmpty())
                {
                    signInUser(mEmail, mPassword);
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
    }

    private void signInUser(String _email, String _password) {

        mAuth.signInWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWitheEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(Login.this, MainActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "No user found with email and password provided.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}