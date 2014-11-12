package com.example.localdb;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LocalDB {
	
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public LocalDB(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    
    //Method for closing connection with database.
    public void close() {
        dbHelper.close();
    }

    //Saves a user with userID and username.
    public void saveUser(int userID, String username) {
    	ContentValues values = new ContentValues();
    	values.put(MySQLiteHelper.COLUMN_ID, userID);
    	values.put(MySQLiteHelper.COLUMN_NAME, username);
    	database.insert(MySQLiteHelper.TABLE_USER, null, values);
    }
    
    //Prints all stored users contained in the database via System.out.
    public String[] viewDatabase(){
    	String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_USER;
    	Cursor cursor = database.rawQuery(selectQuery, null);
    	
    	int n = 10;
    	int i = 0;
    	String[] user = new String[n];
    	System.out.println("Hela databasen:");
    	
    	if(cursor.moveToFirst()) {
    		do {
    			user[i] = cursor.getString(0) + " "
    					+ cursor.getString(1);
    			i++;
    			System.out.println(cursor.getString(0) + " "
    							 + cursor.getString(1));
    		} while (cursor.moveToNext() && i < n);
    	}
    	return user;
    }

    //Deletes all users from the database.
    public void clearTable() {
        String CLEAR_TABLE = "DELETE FROM " + MySQLiteHelper.TABLE_USER;
        database.execSQL(CLEAR_TABLE);
    }

}
