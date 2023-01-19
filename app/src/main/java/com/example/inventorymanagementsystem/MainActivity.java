package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    // creating variables for button
    private Button userList, getCompanyDetails;
    // member fields for logged user
    private String mEmail, mUserKey, mCompanyCode;

    private static Users currentUser;

    private FirebaseFirestore db;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // prevents users from rotating screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // initializing our buttons
        userList = findViewById(R.id.idBtnListUsers);
        getCompanyDetails = findViewById(R.id.idBtnCompanyDetails);
        Intent i = getIntent();
        currentUser = (Users) i.getSerializableExtra("User");
        mCompanyCode = (String) i.getSerializableExtra("CompanyCode");
        db = FirebaseFirestore.getInstance();

        // Changing title of the action bar and color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>WAREHOUSE 1</font>"));
        userList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        getCompanyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference details = db.collection(mCompanyCode + "/WarehouseOne/CompanyDetails");
                details.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot document = task.getResult();
                            if (!document.isEmpty()) {
                                Log.d(TAG, "Document exists.");
                                Intent newIntent = new Intent(MainActivity.this, ViewCompanyDetails.class);
                                newIntent.putExtra("CompanyCode", mCompanyCode);
                                newIntent.putExtra("User", currentUser);
                                startActivity(newIntent);
                            } else {
                                Log.d(TAG, "Document does not exist.");
                                Intent newIntent = new Intent(MainActivity.this, CompanySetup.class);
                                newIntent.putExtra("CompanyCode", mCompanyCode);
                                newIntent.putExtra("User", currentUser);
                                startActivity(newIntent);
                            }
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                });
            }
        });
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
                break;
            case R.id.idMenuUsers:
                break;
            case R.id.idMenuSystems:
                break;
            case R.id.idMenuProducts:
                Intent i = new Intent(MainActivity.this, ItemsSubMenu.class);
                i.putExtra("User", currentUser);
                i.putExtra("CompanyCode", mCompanyCode);
                startActivity(i);
                break;
            case R.id.idMenuOrders:
                break;
            case R.id.idMenuReceiving:
                break;
            case R.id.idMenuInventory:
                break;
            case R.id.idMenuLocations:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}