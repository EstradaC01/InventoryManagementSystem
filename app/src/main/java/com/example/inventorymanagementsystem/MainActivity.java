package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // member fields for logged user
    private static Users currentUser;

    private static String mWarehouse = "";

    private FirebaseFirestore db;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // getting intent from Login screen with Users object and companyCode string
        Intent i = getIntent();
        currentUser = (Users) i.getSerializableExtra("User");

        db = FirebaseFirestore.getInstance();

        // Changing title of the action bar and color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>"+mWarehouse+"</font>"));

        // Shows dialog box if warehouse is not selected or null
        if (mWarehouse.isEmpty()) {
            showCustomDialog();
        }
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     *
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     *
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     *
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     *
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.idMenuProducts);
        menu.findItem(R.id.idMenuInventory);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.idMenuCompanyDetails:
                CollectionReference details = db.collection("CompanyDetails");
                details.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot document = task.getResult();
                            if (!document.isEmpty()) {
                                Intent newIntent = new Intent(MainActivity.this, ViewCompanyDetails.class);
                                newIntent.putExtra("User", currentUser);
                                startActivity(newIntent);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to read database", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            case R.id.idMenuUsers:
                Intent u = new Intent(MainActivity.this, UserList.class);
                u.putExtra("User", currentUser);
                startActivity(u);
                break;
            case R.id.idMenuSystems:
                break;
            case R.id.idMenuProducts:
                Intent i = new Intent(MainActivity.this, ItemList.class);
                i.putExtra("User", currentUser);
                i.putExtra("Warehouse", mWarehouse);
                startActivity(i);
                break;
            case R.id.idMenuOrders:
                break;
            case R.id.idMenuReceiving:
                break;
            case R.id.idMenuInventory:
                break;
            case R.id.idMenuLocations:
                Intent locationsIntent = new Intent(MainActivity.this, LocationsSubMenu.class);
                locationsIntent.putExtra("User", currentUser);
                locationsIntent.putExtra("Warehouse", mWarehouse);
                startActivity(locationsIntent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    void showCustomDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_choose_warehouse);

        final TextView title = dialog.findViewById(R.id.dialogChooseWarehouseTitle);
        final Spinner spinner = dialog.findViewById(R.id.dialogChooseWarehouseSpinner);
        Button submitButton = dialog.findViewById(R.id.dialogChooseWarehouseSubmitButton);

        // setting default string for spinner
        String defaultTextForSpinner = "Warehouse";

        List<String> warehouses = new ArrayList<>();

        db.collection("Warehouses").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty())
                {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d : list) {
                        if(d.getId().contains("Warehouse")){
                            String warehouse = d.getId();
                            warehouses.add(warehouse);
                        }
                    }
                    String[] arrayForSpinner = new String[warehouses.size()];
                    for(int i = 0; i < warehouses.size(); i++) {
                        arrayForSpinner[i] = warehouses.get(i);
                    }

                    spinner.setAdapter(new CustomSpinnerAdapter(MainActivity.this, R.layout.spinner_row, arrayForSpinner, defaultTextForSpinner));
                    dialog.show();
                } else {
                    Toast.makeText(MainActivity.this, "No data found in Database", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to get data", Toast.LENGTH_LONG).show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWarehouse = spinner.getSelectedItem().toString();
                for(int i = 0; i < warehouses.size(); i++) {
                    if (spinner.getSelectedItem() == warehouses.get(i)) {
                        dialog.dismiss();
                        break;
                    }
                }
                getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>"+mWarehouse+"</font>"));

            }
        });
    }
}