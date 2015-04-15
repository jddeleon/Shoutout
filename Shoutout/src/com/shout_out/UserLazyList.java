package com.shout_out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.shout_out.libraries.JSONParser;
import com.shout_out.libraries.LobbyFunctions;

public class UserLazyList extends ListActivity{
	
	// Extras passed from Main Activity
	int userId;
	int lobbyId;
	String lobbyName;

	
	LobbyFunctions lobbyFunction = new LobbyFunctions();
    
    // JSON Node names
    //private static final String TAG_LOBBY = "lobbies";
    private static final String TAG_USERS = "users";
    private static final String TAG_ID = "userId";
    private static final String TAG_NAME = "fName";
    private static final String TAG_LNAME = "lName";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_MAJOR = "major";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_AGE = "age";
    private static final String TAG_THUMBURL = "thumbUrl";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int testLobbyId;

    
    
   
    
    // new JSON Parser instance
    JSONParser jParser = new JSONParser();
    
    ArrayList<HashMap<String, String>> usersList;
    
    //JSONArray
    JSONArray users = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
    
        Intent extras = getIntent();
		userId = extras.getIntExtra("userId", 0);
		lobbyId = extras.getIntExtra("lobbyId", 0);
		lobbyName = extras.getStringExtra("lobbyName");
		
		ActionBar ab = getActionBar();
		ab.setTitle(lobbyName);
		
		// testing lobbyID
		testLobbyId = LobbyID.getLobbyID();
		Log.d("TEST", "LobbyID=" + testLobbyId);
		
         // Hashmap for ListView
        usersList = new ArrayList<HashMap<String, String>>();
        
        //Execute Loadusers AsyncTask in background for list
        try {
			new LoadUsers().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
     // Create button
        Button btnViewWall = (Button) findViewById(R.id.btnViewWall);
        
        // button click event
        btnViewWall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ShoutWallList.class);
                i.putExtra("userId", userId);
                i.putExtra("lobbyId", lobbyId);
                
                startActivity(i);
                //finish();
            }
        });
            
        

    }
    
    
    
    class LoadUsers extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
        	
        	JSONObject json = lobbyFunction.EnterLobby(Integer.toString(lobbyId), Integer.toString(userId));
            json = lobbyFunction.GetLobbyUsers(Integer.toString(lobbyId), Integer.toString(userId));
            
            // check log cat for response
            Log.d("Users:", json.toString());
            
            try {
            	int success = json.getInt(TAG_SUCCESS);

            	if (success == 1){
            		// Getting User Arrays
            		users = json.getJSONArray(TAG_USERS);

            		// looping through All Contacts
            		for(int i = 0; i < users.length(); i++){
            			JSONObject u = users.getJSONObject(i);

            			// Storing each json item in variable
            			String id = u.getString(TAG_ID);
            			String name = u.getString(TAG_NAME);
            			String major = u.getString(TAG_MAJOR);
            			String age = u.getString(TAG_AGE);
            			String username = u.getString(TAG_USERNAME);
            			String gender = u.getString(TAG_GENDER);
            			if (gender == "null"){
            				gender = "";
            			}
            				

            			// creating new HashMap
            			HashMap<String, String> map = new HashMap<String, String>();

            			// adding each child node to HashMap key => value
            			map.put(TAG_ID, id);
            			map.put(TAG_NAME, name);
            			map.put(TAG_MAJOR, major);
            			map.put(TAG_AGE, age);
            			map.put(TAG_USERNAME, username);
            			map.put(TAG_GENDER, gender);

            			//adding HashList to ArrayList
            			usersList.add(map);
            		}
            	}else {

            		// failed to login
            		Log.d("Failed to Retrieve Lobby users!", json.getString(TAG_MESSAGE));
            		return json.getString(TAG_MESSAGE);
            	}
            } catch (JSONException e) {
            	e.printStackTrace();
            }

            // TODO Auto-generated method stub
            return null;
        }

        protected void onPostExecute(String file_url) {                  
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    //Updating parsed JSON data into ListView
                    ListAdapter lazyadapter = new SimpleAdapter(UserLazyList.this, usersList,R.layout.listview,new String[] { TAG_USERNAME, TAG_MAJOR, TAG_AGE, TAG_GENDER }, new int[] {R.id.user, R.id.major, R.id.age, R.id.genderlist});
                    setListAdapter(lazyadapter);
                }
            });

        }
   
    }
    class ExitLobby extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
        	
        	JSONObject json = lobbyFunction.ExitLobby(Integer.toString(lobbyId), Integer.toString(userId));
        	
        	try {
        		int success = json.getInt(TAG_SUCCESS);

            	if (success == 1){
            		return json.getString(TAG_MESSAGE);
            	}
            	else {	
            		Log.d("Exit Lobby", "Couldn't Exit Lobby For Some Reason");
            	}
        	}catch (JSONException e) {
            	e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {                  
        	if (file_url != null){
               	Toast.makeText(UserLazyList.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
	public void onBackPressed() {
    	new ExitLobby().execute();
        Intent Main = new Intent(getApplicationContext(), MainActivity.class);
        Main.putExtra("userId", userId);
        Main.putExtra("temp", 1);
        //Log.d("Login", "About to call startActivity(Main)");
        startActivity(Main);
        super.onBackPressed();
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case R.id.refresh:
    	  Intent lobby = new Intent(getApplicationContext(), UserLazyList.class);
    	  lobby.putExtra("lobbyId", lobbyId);
    	  lobby.putExtra("userId", userId);
    	  startActivity(lobby);
    	  finish();  
        break;
      case R.id.logout:
    	  Intent login = new Intent(getApplicationContext(), LogonActivity.class);
    	  startActivity(login);
    	  finish();
        break;

      default:
        break;
      }

      return true;
    } 
             
}                      