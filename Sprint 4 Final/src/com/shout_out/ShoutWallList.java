package com.shout_out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.shout_out.libraries.JSONParser;
import com.shout_out.libraries.LobbyFunctions;

public class ShoutWallList extends ListActivity{
	
	int userId;
	int lobbyId;
    
    // Progress Dialog
    private ProgressDialog pDialog;
    
    LobbyFunctions lobbyFunction = new LobbyFunctions();
    
    private static final String TAG_SHOUTS = "shouts";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_SHOUT = "shout";
    private static final String TAG_TIME = "time";
    private static final String TAG_LOBBY_ID = "lobbyId";
    private static final String TAG_USER_ID = "userId";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    
    EditText shoutPost;
    
    
    ArrayList<HashMap<String, String>> wallList;
    
    //JSONArray
    JSONArray wall_users = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoutwall);
        
        Intent extras = getIntent();
		userId = extras.getIntExtra("userId", 0);
		lobbyId = extras.getIntExtra("lobbyId", 0);
      
        
    
         // Hashmap for ListView
        wallList = new ArrayList<HashMap<String, String>>();
        
        //Execute LoadWall AsyncTask in background for list of shout messages
        try {
			new LoadWall().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        shoutPost = (EditText) findViewById(R.id.shout_post);
             
        
        
        // Create button
        Button btnPost = (Button) findViewById(R.id.sendBtn);
        
        // button click event
        btnPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new CreateShout().execute();
                Intent i = new Intent(getApplicationContext(), ShoutWallList.class);
                i.putExtra("lobbyId", lobbyId);
                i.putExtra("userId", userId);
                
                startActivity(i);
                finish();
            }
        });

    }
    
    class LoadWall extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
        	
        	
            
            // getting JSON Object
            // Note that login URL accepts POST method
            JSONObject json = lobbyFunction.GetShouts(Integer.toString(lobbyId));
            
            // check log cat for response
            Log.d("Shouts:", json.toString());
            
            try {


            	// Getting User Arrays
            	wall_users = json.getJSONArray(TAG_SHOUTS);

            	// looping through All Contacts
            	for(int i = 0; i < wall_users.length(); i++){
            		JSONObject w = wall_users.getJSONObject(i);

            		// Storing each json item in variable
            		String username = w.getString(TAG_USERNAME);
            		String shout = w.getString(TAG_SHOUT);
            		String time = w.getString(TAG_TIME);


            		// creating new HashMap
            		HashMap<String, String> Shout_map = new HashMap<String, String>();

            		// adding each child node to HashMap key => value
            		Shout_map.put(TAG_USERNAME, username);
            		Shout_map.put(TAG_SHOUT, shout);
            		Shout_map.put(TAG_TIME, time);

            		//adding HashList to ArrayList
            		wallList.add(Shout_map);
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
                    ListAdapter walladapter = new SimpleAdapter(ShoutWallList.this,wallList,R.layout.wallview,new String[] { TAG_USERNAME, TAG_SHOUT, TAG_TIME }, new int[] {R.id.wall_user, R.id.shout_get, R.id.timestamp });
                    setListAdapter(walladapter);                    
                }
            });

        }
   
    }
    
    class CreateShout extends AsyncTask<String, String, String>{

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(ShoutWallList.this);
                pDialog.setMessage("Posting Shout to Wall..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
        }

        /**
         * Creating Shout
         * */
        protected String doInBackground(String... args) {

        	String str_shoutPost = shoutPost.getText().toString();

        	JSONObject json_wall = lobbyFunction.PostShout(Integer.toString(lobbyId), Integer.toString(userId), str_shoutPost);

        	// check for success tag
        	try {
        		int success = json_wall.getInt(TAG_SUCCESS);

        		if (success == 1) {

        			return json_wall.getString(TAG_MESSAGE);
        		} else {

        			// failed to post shout
        			Log.d("Post Shout", json_wall.getString(TAG_MESSAGE));
        			return json_wall.getString(TAG_MESSAGE);
        		}
        	} 
        	catch (JSONException e) {
        		e.printStackTrace();
        	}

        	return null;
        }
        	/**
             * After completing background task Dismiss the progress dialog
             * **/
            protected void onPostExecute(String file_url) {
                    // dismiss the dialog once done
                    pDialog.dismiss();

                    // display the toast notification containing the returned JSON message
                    if (file_url != null){
              
                       	Toast.makeText(ShoutWallList.this, file_url, Toast.LENGTH_LONG).show();
                    }
                    
            }






        

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
    		Intent wall = new Intent(getApplicationContext(), ShoutWallList.class);
    		wall.putExtra("lobbyId", lobbyId);
    		wall.putExtra("userId", userId);
    		startActivity(wall);
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
