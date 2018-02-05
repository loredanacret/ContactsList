package com.project.lrdn.contactslist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Lrdm on 12/27/2017.
 */

public class ListDataActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    DatabaseHelper mDatabaseHelper;
    ListAdapter adapter;
    String searchText;
    private SearchView searchView;
    private ListView mListView;
    String query;
    Boolean asc = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mListView = (ListView) findViewById(R.id.listView);
        searchView = (SearchView) findViewById(R.id.search);
        mDatabaseHelper = new DatabaseHelper(this);
        query = "";
        populateListView(query);

       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query = " WHERE name LIKE '%"+s+"%'";
                populateListView(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals(""))  {
                    query = "";
                    populateListView(query);
                }
                return false;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDataActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button sort = findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String q;
                asc = !asc;
                if(asc){
                    q = query + " ORDER BY name ASC";
                }else{
                    q = query + " ORDER BY name DESC";
                }
                populateListView(q);
            }
        });


    }

     void populateListView(String query) {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData(query);
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
        }
        //create the list adapter and set the adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);

                Cursor data = mDatabaseHelper.getItemID(name); //get the id associated with that name

                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }

                Cursor contact = mDatabaseHelper.getContactData(itemID);

                String email = "", phone = "", web = "", address = "";
                while(contact.moveToNext()){
                    name = contact.getString(1);
                    email = contact.getString(2);
                    web = contact.getString(3);
                    phone = contact.getString(4);
                    address = contact.getString(5);

                }
                if(itemID > -1){
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent(ListDataActivity.this, ViewDataActivity.class);
                    editScreenIntent.putExtra("id", itemID);
                    editScreenIntent.putExtra("name", name);
                    editScreenIntent.putExtra("email", email);
                    editScreenIntent.putExtra("web", web);
                    editScreenIntent.putExtra("phone", phone);
                    editScreenIntent.putExtra("address",address);
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("No ID associated with that name");
                }

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