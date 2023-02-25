package com.example.inventorymanagementsystem.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Users;

import java.net.PasswordAuthentication;
import java.util.Properties;

public class FeedbackScreen extends AppCompatActivity {

    private Button feedback;
    private EditText email;
    private EditText feedbackContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_screen);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Send Feedback</font>"));

        feedback = findViewById(R.id.btnFeedbackSubmit);
        feedbackContent = findViewById(R.id.idEdtFeedbackMessage);
        email = findViewById(R.id.idEdtFeedbackEmail);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(
                        "mailto:TylerJenningsW@gmail.com"));
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });
    }
}