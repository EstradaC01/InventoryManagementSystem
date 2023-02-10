package com.example.inventorymanagementsystem.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.OrderRVAdapter;
import com.example.inventorymanagementsystem.models.Orders;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrdersList extends AppCompatActivity {

    private RecyclerView orderRV;
    private ArrayList<Orders> mOrdersArrayList;
    private OrderRVAdapter mOrderRVAdapter;
    private FirebaseFirestore db;
    private Button btnAddOrders;
    private ImageButton btnOrderFilter;
    public PopupMenu DropDownMenu;
    public Menu menu;
    private androidx.appcompat.widget.SearchView edtSearchOrders;
    private ProgressBar loadingOrdersPB;
    private static Users currentUser;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Order Inquiry</font>"));

        orderRV = findViewById(R.id.idRVOrders);
        loadingOrdersPB = findViewById(R.id.idOrderProgressBar);
        btnAddOrders = findViewById(R.id.btnOrderListActivityAddOrder);
        btnOrderFilter = findViewById(R.id.btnOrderListFilter);
        edtSearchOrders = findViewById(R.id.idSVSearchOrders);
        db = FirebaseFirestore.getInstance();
        Intent i = getIntent();

        mOrdersArrayList = new ArrayList<>();
        orderRV.setHasFixedSize(true);
        orderRV.setLayoutManager(new LinearLayoutManager(this));
        mOrderRVAdapter = new OrderRVAdapter(mOrdersArrayList, this, i);

        orderRV.setAdapter(mOrderRVAdapter);

        DropDownMenu = new PopupMenu(getApplicationContext(), btnOrderFilter);
        menu = DropDownMenu.getMenu();
        menu.add(0,0,0,"Name");
        menu.add(0,2,0,"Status");
        menu.add(0,3,0,"Order Date");

        currentUser = (Users)i.getSerializableExtra("User");

        CollectionReference ordersRef = db.collection("Orders");

        ordersRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                loadingOrdersPB.setVisibility(View.GONE);
                List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                for (DocumentSnapshot d : docs) {
                    Orders o = d.toObject(Orders.class);
                    mOrdersArrayList.add(o);
                    Log.d("Orders", ": " + o);
                }

                mOrderRVAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrdersList.this, "Failed to read data", Toast.LENGTH_LONG);
            }
        });

        btnAddOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating intent to open add order activity
                Intent i = new Intent(OrdersList.this, AddOrder.class);
                i.putExtra("User", currentUser);
                startActivity(i);
            }
        });

        edtSearchOrders.setIconified(false);
        edtSearchOrders.clearFocus();

        edtSearchOrders.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        DropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 0: //Name

                        ArrayList<String> FilterName = new ArrayList();
                        for (Orders order : mOrdersArrayList){
                            Object FName = order.getOrderCustomer();
                            FilterName.add(FName.toString());
                             Collections.sort(FilterName);
                        }

                        return true;

                    case 2: //Status
                        ArrayList<String> FilterStatus = new ArrayList();
                        for (Orders order : mOrdersArrayList){
                            Object Status = order.getOrderStatus();
                            FilterStatus.add(Status.toString());
                            Collections.sort(FilterStatus);
                        }

                        return true;
                    case 3: //Date
                        ArrayList<String> FilterDate = new ArrayList();
                        for (Orders order : mOrdersArrayList){
                            Object Date = order.getOrderDate();
                            FilterDate.add(Date.toString());
                            Collections.sort(FilterDate);
                        }

                        return true;
                }
                return false;
            }
        });

        btnOrderFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropDownMenu.show();
            }
        });
    }

    //search bar
    private void filterList(String text) {
        ArrayList<Orders> filteredList = new ArrayList<>();
        for (Orders order : mOrdersArrayList) {
            if (order.getOrderID().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(order);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No items found", Toast.LENGTH_SHORT);
        } else {
            mOrderRVAdapter.setFilteredList(filteredList);
        }
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