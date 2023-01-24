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
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.auth.AUTH;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.chromium.base.Callback;

import javax.security.auth.callback.Callback;


public class ForgotPassword extends AppCompatActivity {

    private String mEmail;
    private EditText edtEmail;
    private Button btnSubmit;



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

    public static final FirebaseUser AUTH_USER = FirebaseAuth.getInstance().getCurrentUser()!=null ? FirebaseAuth.getInstance().getCurrentUser(): null ;

    protected final void resetPassword(String emailId,final Callback callback){

        AUTH.sendPasswordResetEmail(emailId)

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
}
