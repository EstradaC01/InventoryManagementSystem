package com.example.inventorymanagementsystem;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;





public class ForgotPassword extends AppCompatActivity {

    private String mEmail;
    private EditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInitializations();
    }

    void viewInitializations() {
        edtEmail = findViewById(R.id.FPEmail);
mEmail = edtEmail.getText().toString();
        // To show back button in actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Checking if the input in form is valid
    boolean validateInput() {

        if (mEmail.equals("")) {
            edtEmail.setError("Please Enter Email");
            return false;
        }

        // checking the proper email format
        if (!isEmailValid(mEmail)) {
            edtEmail.setError("Please Enter Valid Email");
            return false;
        }


        return true;
    }

    boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
