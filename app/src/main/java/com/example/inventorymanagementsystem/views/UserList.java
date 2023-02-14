package com.example.inventorymanagementsystem.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.views.adapters.UserRVAdapter;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {
    private RecyclerView userRV;
    private ArrayList<Users> mUsersArrayList;
    private UserRVAdapter mUserRVAdapter;
    private FirebaseFirestore db;
    private ProgressBar loadingUsersPB;
    private static Users currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>User Inquiry</font>"));

        userRV = findViewById(R.id.idRVUsers);
        loadingUsersPB = findViewById(R.id.idUserProgressBar);

        db = FirebaseFirestore.getInstance();
        Intent i = getIntent();

        mUsersArrayList = new ArrayList<>();
        userRV.setHasFixedSize(true);
        userRV.setLayoutManager(new LinearLayoutManager(this));

        mUserRVAdapter = new UserRVAdapter(mUsersArrayList, this, i);

        userRV.setAdapter(mUserRVAdapter);


        currentUser = (Users)i.getSerializableExtra("User");

        CollectionReference usersRef = db.collection("Users");

        usersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    loadingUsersPB.setVisibility(View.GONE);
                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : docs) {
                        Users u = d.toObject(Users.class);
                        mUsersArrayList.add(u);
                        Log.d("Users", ": " + u);
                    }

                    mUserRVAdapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserList.this, "Failed to read data", Toast.LENGTH_LONG);
            }
        });
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}