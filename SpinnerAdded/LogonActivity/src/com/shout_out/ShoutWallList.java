package com.shout_out;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ShoutWallList extends ListActivity{
    
    // url to make request
    private static String url = "";
    
    private static final String TAG_USERS = "users";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_MSG="messages";

    // new JSON Parser instance
    JSONParser jParser = new JSONParser();
    
    ArrayList<HashMap<String, String>> wallList;
    
    //JSONArray
    JSONArray wall_users = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoutwall);
    
         // Hashmap for ListView
        wallList = new ArrayList<HashMap<String, String>>();
        
        //Execute Loadusers in wall AsyncTask in background for list
        new LoadW_Users().execute();
        
        // Create button
        Button btnPost = (Button) findViewById(R.id.sendBtn);
        
        // button click event
        btnPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new LoadW_Users().execute();
                Intent i = new Intent(getApplicationContext(), ShoutWallList.class);
                startActivity(i);
            }
        });

    }
    
    class LoadW_Users extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            
         // JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            
            try {
                // Getting User Arrays
                   wall_users = json.getJSONArray(TAG_USERS);
                   
                // looping through All Contacts
                   for(int i = 0; i < wall_users.length(); i++){
                       JSONObject w = wall_users.getJSONObject(i);
                        
                       // Storing each json item in variable
                       String username = w.getString(TAG_USERNAME);
                       String message = w.getString(TAG_MSG);
                       
                       
                       // creating new HashMap
                       HashMap<String, String> map = new HashMap<String, String>();
                       
                       // adding each child node to HashMap key => value
                       map.put(TAG_USERNAME, username);
                       map.put(TAG_MSG, message);
                       
                       //adding HashList to ArrayList
                       wallList.add(map);
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
                    ListAdapter walladapter = new SimpleAdapter(ShoutWallList.this,wallList,R.layout.wallview,new String[] { TAG_USERNAME, TAG_MSG }, new int[] {R.id.userwall, R.id.msg });
                    setListAdapter(walladapter);                    
                }
            });

        }
   
    }
    
    
    

}
