package com.example.localdb;

import com.example.localdb.R;

import android.support.v7.app.ActionBarActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Create the local database.
		final LocalDB localDB = new LocalDB(this);
        
		//A button used to fetch username and password from textfields.
		Button login = (Button)findViewById(R.id.login);
		//The username textfield.
        final EditText username = (EditText)findViewById(R.id.username);
        
        //This happens when the button is pressed.
        // later on this should happen after the user has been confirmed to
        // exist on the online database.
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	//Fetch the username from textfield.
                String displayName = username.getText().toString();
                
                
                
                //Both userID and displayName should arrive 
                // from the online server after a user/password check.
                int userID = 1337;
                
                //Open the database connection. 
                localDB.open();
                //Clear the database from old users.
                localDB.clearTable();
                //Save the new user with userID (int) and displayName (String). 
                localDB.saveUser(userID, displayName);
                //Shows the current database in System.out.
                localDB.viewDatabase();
                //Close the database connection. 
                localDB.close();
            }
        });
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
