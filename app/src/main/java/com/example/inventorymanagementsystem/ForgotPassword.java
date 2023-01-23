package com.example.inventorymanagementsystem;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;





public class ForgotPassword extends AppCompatActivity {

    private String mEmail;
    private EditText edtEmail;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        viewInitializations();

        //Submit Button
        btnSubmit = findViewById(R.id.idSubmitbtn);
        btnSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mEmail = edtEmail.getText().toString();
            }

        });
    }

    void viewInitializations() {

        edtEmail = findViewById(R.id.idEdtForgotPasswordUI);
        mEmail = edtEmail.getText().toString();

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
