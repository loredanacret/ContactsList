package com.project.lrdn.contactslist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Lrdm on 12/27/2017.
 */

public class EditDataActivity extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";

    private Button btnSave,btnDelete;
    private EditText editName, editPhone, editEmail, editAddress, editWeb;

    DatabaseHelper mDatabaseHelper;

    private String selectedName,selectedEmail, selectedPhone, selectedAddress, selectedWeb;
    private int selectedID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        editName = (EditText) findViewById(R.id.editName);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editWeb = (EditText) findViewById(R.id.editWeb);
        mDatabaseHelper = new DatabaseHelper(this);

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //now get the name we passed as an extra
        selectedName = receivedIntent.getStringExtra("name");
        selectedEmail = receivedIntent.getStringExtra("email");
        selectedPhone = receivedIntent.getStringExtra("phone");
        selectedAddress = receivedIntent.getStringExtra("address");
        selectedWeb = receivedIntent.getStringExtra("web");

        //set the text to show the current selected name
        editName.setText(selectedName);
        editEmail.setText(selectedEmail);
        editPhone.setText(selectedPhone);
        editAddress.setText(selectedAddress);
        editWeb.setText(selectedWeb);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] contact = new String[6] ;
                contact[1] = editName.getText().toString();
                contact[2] = editEmail.getText().toString();
                contact[3] = editWeb.getText().toString();
                contact[4] = editPhone.getText().toString();
                contact[5] = editAddress.getText().toString();

                if(!contact[1].equals("") && !contact[4].equals("")){
                    if (android.util.Patterns.PHONE.matcher(contact[4]).matches()) {
                        if( contact[2].equals("") || android.util.Patterns.EMAIL_ADDRESS.matcher(contact[2]).matches()) {
                            boolean updateData = mDatabaseHelper.updateContact(contact, selectedID);
                            if(updateData){
                                toastMessage("Update successfully");
                                Intent intent = new Intent(EditDataActivity.this, ListDataActivity.class);
                                startActivity(intent);
                            }else{toastMessage("Update failed");}
                        }else{toastMessage("You mast set a valid email address");}
                    }else{toastMessage("You must set a valid phone number");}
                }else{toastMessage("You must set a name and a phone number");}
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditDataActivity.this);

                builder
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                mDatabaseHelper.deleteName(selectedID,selectedName);
                                toastMessage("Removed from database");
                                Intent intent = new Intent(EditDataActivity.this, ListDataActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}