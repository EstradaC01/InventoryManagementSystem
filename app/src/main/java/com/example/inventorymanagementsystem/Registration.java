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

public class Registration extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText edtFirstname, edtLastName, edtEmail, edtPassword;

    private Button btnRegister;

    private String mFirstName, mLastName, mEmail, mPassword;

    private static final String TAG = "Registration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        edtFirstname = findViewById(R.id.idEdtFirstName);
        edtLastName = findViewById(R.id.idEdtLastName);
        edtEmail = findViewById(R.id.idEdtEmail);
        edtPassword = findViewById(R.id.idEdtPassword);
        btnRegister = findViewById(R.id.idBtnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstName = edtFirstname.getText().toString();
                mLastName = edtLastName.getText().toString();
                mEmail = edtEmail.getText().toString();
                mPassword = edtPassword.getText().toString();

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

    private void CreateUser(String _email, String _password)
    {
        mAuth.createUserWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createuserWithEmail: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(Registration.this, MainActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Registration.this, "Authentication failed.", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}