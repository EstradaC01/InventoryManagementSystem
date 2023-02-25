package com.example.inventorymanagementsystem.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inventorymanagementsystem.R;

public class FeedbackScreen extends AppCompatActivity {

    Button feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_screen);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                String text = "Forward to" + Uri.encode("TylerJenningsW@gmail.com") + "?Subject"+
                        Uri.encode("Feedback" ) + "&Body" + Uri.encode("");
                Uri yuri = Uri.parse(text);
                intent.setData(yuri);
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

    }
}