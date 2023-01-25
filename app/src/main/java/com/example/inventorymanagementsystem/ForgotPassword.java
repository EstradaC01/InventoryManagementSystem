package com.example.inventorymanagementsystem;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.chromium.base.Callback;



public class ForgotPassword extends AppCompatActivity {

    private String mEmail;
    private EditText edtEmail;
    private Button btnSubmit;


    void viewInitializations() {

        edtEmail = findViewById(R.id.idEdtForgotPasswordUI);
        mEmail = edtEmail.getText().toString();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

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
                if(TextUtils.isEmpty(mEmail))
                {
                    edtEmail.setError("Enter valid email address");
                }
            }

        });
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

    protected final void resetPassword(String emailId,final Callback callback){

        FirebaseAuth.getInstance().sendPasswordResetEmail(emailId)

                .addOnCompleteListener(new OnCompleteListener() {

                    @Override

                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();

                        }

                    }

                });

    }


}
