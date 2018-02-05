package com.project.lrdn.contactslist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lrdm on 12/27/2017.
 */

public class ViewDataActivity extends AppCompatActivity {

    private static final String TAG = "ViewDataActivity";

    private Button btnEdit, btnListView;
    private TextView viewName, viewPhone, viewEmail, viewAddress, viewWeb;

    DatabaseHelper mDatabaseHelper;

    private String selectedName, selectedEmail, selectedPhone, selectedAddress, selectedWeb;
    private int selectedID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_data_layout);
        btnEdit = findViewById(R.id.btnEdit);
        btnListView = findViewById(R.id.btnListView);
        viewName = findViewById(R.id.viewName);
        viewPhone = findViewById(R.id.viewPhone);
        viewEmail = findViewById(R.id.viewEmail);
        viewAddress = findViewById(R.id.viewAddress);
        viewWeb = findViewById(R.id.viewWeb);
        mDatabaseHelper = new DatabaseHelper(this);

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id", -1); //NOTE: -1 is just the default value

        //now get the name we passed as an extra
        selectedName = receivedIntent.getStringExtra("name");
        selectedEmail = receivedIntent.getStringExtra("email");
        selectedPhone = receivedIntent.getStringExtra("phone");
        selectedAddress = receivedIntent.getStringExtra("address");
        selectedWeb = receivedIntent.getStringExtra("web");

        //set the text to show the current selected contact
        viewName.setText(selectedName);
        viewEmail.setText(selectedEmail);
        viewPhone.setText(selectedPhone);
        viewAddress.setText(selectedAddress);
        viewWeb.setText(selectedWeb);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedID > -1) {
                    Log.d(TAG, "onItemClick: The ID is: " + selectedID);
                    Intent editScreenIntent = new Intent(ViewDataActivity.this, EditDataActivity.class);
                    editScreenIntent.putExtra("id", selectedID);
                    editScreenIntent.putExtra("name", selectedName);
                    editScreenIntent.putExtra("email", selectedEmail);
                    editScreenIntent.putExtra("web", selectedWeb);
                    editScreenIntent.putExtra("phone", selectedPhone);
                    editScreenIntent.putExtra("address", selectedAddress);
                    startActivity(editScreenIntent);
                } else {
                    toastMessage("No ID associated with that name");
                }
            }

        });
        btnListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewDataActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });
    }
    public void phoneButton(View v) {
        View.OnClickListener listSet = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + selectedPhone));
                if (ActivityCompat.checkSelfPermission(ViewDataActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    toastMessage("You do not have permissions");
                    return;
                }
                    startActivity(callIntent);
            }
        };
        ImageButton phoneBtn = (ImageButton) findViewById(R.id.phoneImageButton);
        phoneBtn.setOnClickListener(listSet);
    }


    public void emailButton(View v) {
        View.OnClickListener listSet = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:"
                        + selectedEmail
                        + "?subject=" + "test" + "&body=" + "test");
                intent.setData(data);
                startActivity(intent);
            }
        };
        ImageButton emailBtn = (ImageButton) findViewById(R.id.emailImageButton);
        emailBtn.setOnClickListener(listSet);
    }

    public void addressButton(View v) {
        View.OnClickListener listSet = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(selectedAddress));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        };
        ImageButton addressBtn = (ImageButton) findViewById(R.id.addressImageButton);
        addressBtn.setOnClickListener(listSet);
    }

    public void webButton(View v) {
        View.OnClickListener listSet = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedWeb));
                startActivity(browserIntent);
            }
        };
        ImageButton webBtn = (ImageButton) findViewById(R.id.webImageButton);
        webBtn.setOnClickListener(listSet);
    }


    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}