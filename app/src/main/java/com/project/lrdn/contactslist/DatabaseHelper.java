package com.project.lrdn.contactslist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 * Created by Lrdm on 12/27/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "people_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";
    private static final String COL3 = "email";
    private static final String COL4 = "web";
    private static final String COL5 = "phone";
    private static final String COL6 = "address";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 +" TEXT,"  + COL3 +" TEXT,"  + COL4 +" TEXT," + COL5 +" TEXT,"+ COL6 +" TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String[] contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, contact[1]);
        contentValues.put(COL3, contact[2]);
        contentValues.put(COL4, contact[3]);
        contentValues.put(COL5, contact[4]);
        contentValues.put(COL6, contact[5]);

        Log.d(TAG, "addData: Adding " + contact[1] + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if data is inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(String q){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + q;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns all the data from corresponding to a contact
     * @return
     */
    public Cursor getContactData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +" WHERE "+ COL1 + " = " + id;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */
    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param newContact
     * @param id
     */
    public boolean updateContact(String[] newContact, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newContact[1] + "',"+ COL3 +
                " = '" + newContact[2] + "',"+ COL4 +
                " = '" + newContact[3] + "',"+ COL5 +
                " = '" + newContact[4] + "',"+ COL6 +
                " = '" + newContact[5] + "' WHERE " + COL1 + " = " + id;
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newContact[1]);
        try{db.execSQL(query);}
        catch (SQLException e){
            return false;
        }
        return true;
    }

    /**
     * Delete from database
     * @param id
     * @param name
     */
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }

}