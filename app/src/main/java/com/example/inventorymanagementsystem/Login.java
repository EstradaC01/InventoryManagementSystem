package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    // creating variables for the EditText
    private EditText edtLoginEmail, edtLoginPassword;

    // creating variables for the buttons
    private Button btnLogin, btnRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // instantiating editText and button variables
        edtLoginEmail = findViewById(R.id.idEdtLoginEmail);
        edtLoginPassword = findViewById(R.id.idEdtLoginPassword);
        btnLogin = findViewById(R.id.idLoginBtn);
        btnRegistration = findViewById(R.id.idRegisterBtn);

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Registration.class);
                startActivity(i);
            }
        });
    }
}