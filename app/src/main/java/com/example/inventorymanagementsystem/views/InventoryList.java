package com.example.inventorymanagementsystem.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.InventoryListRecyclerViewAdapter;
import com.example.inventorymanagementsystem.models.UnitId;
import com.example.inventorymanagementsystem.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InventoryList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<UnitId> mArrayListUnitId;
    private InventoryListRecyclerViewAdapter mRecyclerViewAdapter;
    private FirebaseFirestore db;
    private androidx.appcompat.widget.SearchView searchView;
    private ImageButton InventoryFilter;
    public PopupMenu DropDownMenu;
    public Menu menu;
    private Users mCurrentUser;
    private String mWarehouse;
    private enum Sort {
        LocationName,
        LocationZone,
        NumBox,
        PiecePerBox,
        TotalPieces,
        Availability,
        PiecesMarked,
        UnitID;
    }
    private Sort sorter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_list);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // change action support bar title and font color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Inventory</font>"));
        searchView = findViewById(R.id.svInventoryList);
        mRecyclerView = findViewById(R.id.rvInventoryList);

        db=FirebaseFirestore.getInstance();

        //getting intent from ItemsSubMenu class along with User object
        Intent i = getIntent();
        mCurrentUser = (Users)i.getSerializableExtra("User");
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

        mArrayListUnitId = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerViewAdapter = new InventoryListRecyclerViewAdapter(mArrayListUnitId, this);

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        CollectionReference unitIdRef = db.collection("Warehouses/"+mWarehouse+"/UnitId");

        InventoryFilter = findViewById(R.id.ibInventoryListFilter);
        sorter = Sort.UnitID;

        DropDownMenu = new PopupMenu(getApplicationContext(), InventoryFilter);
        menu = DropDownMenu.getMenu();
        menu.add(0,0,0,"Location Name");
        menu.add(0,1,0,"Location Zone");
        menu.add(0,2,0,"Number of Boxes");
        menu.add(0,3,0,"Pieces Per Box");
        menu.add(0,4,0,"Total Pieces");
        menu.add(0,5,0,"Availability");
        menu.add(0,6,0,"Pieces Marked");

        unitIdRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d : list) {
                        UnitId u = d.toObject(UnitId.class);
                        mArrayListUnitId.add(u);
                    }

                    mRecyclerViewAdapter.notifyDataSetChanged();
                }

            }
        });

        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText, sorter);
                return false;
            }

        });

        DropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 0: //ProductID
                        sorter = Sort.LocationName;
                        return true;
                    case 1:
                        sorter = Sort.LocationZone;
                        return true;
                    case 2:
                        sorter = Sort.PiecePerBox;
                        return true;
                    case 3:
                        sorter = Sort.NumBox;
                        return true;
                    case 4:
                        sorter = Sort.TotalPieces;
                        return true;
                    case 5:
                        sorter = Sort.Availability;
                        return true;
                    case 6:
                        sorter = Sort.PiecesMarked;
                        return true;
                }
                return false;
            }
        });

        InventoryFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropDownMenu.show();
            }
        });
    }

    private void filterList(String text, Sort sorter) {
        ArrayList<UnitId> filteredList = new ArrayList<>();
        for (UnitId list : mArrayListUnitId) {
            switch (sorter) {
                case LocationName:
                    if (list.getLocation().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(list);
                    }
                    break;
                case LocationZone:
                    if (list.getZone().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(list);
                    }
                    break;
                case PiecePerBox:
                    if (list.getPiecesPerBox().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(list);
                    }
                    break;
                case NumBox:
                    if (list.getNumberOfBoxes().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(list);
                    }
                    break;
                case TotalPieces:
                    if (list.getTotalPieces().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(list);
                    }
                    break;
                case Availability:
                    if (list.getPiecesAvailable().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(list);
                    }
                    break;
                case PiecesMarked:
                    if (list.getPiecesMarked().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(list);
                    }
                    break;
                default:
                    if (list.getUnitId().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(list);
                    }
                    break;
            }
        }
        if (filteredList.isEmpty()) {
            mRecyclerViewAdapter.setFilteredList(filteredList);
            Toast.makeText(this, "No items found", Toast.LENGTH_SHORT).show();
        } else {
            mRecyclerViewAdapter.setFilteredList(filteredList);
        }
    }
}