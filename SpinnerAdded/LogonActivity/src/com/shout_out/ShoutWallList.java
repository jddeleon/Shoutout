package com.shout_out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ShoutWallList extends ListActivity{
    
    // Progress Dialog
    private ProgressDialog pDialog;

    // url to make request
    private static String url_get = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/get_shout";
    //url to post shouts
    private static String url_post = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/...";
    
    private static final String TAG_USERS = "users";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_SHOUT = "shout";
    private static final String TAG_TIME = "time";
    
    EditText shoutPost;
    
    //new JSON Parser for posting message
    JSONParser jParser_post = new JSONParser();
    
    // new JSON Parser instance for wall
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
        new LoadWall().execute();
        

        shoutPost = (EditText) findViewById(R.id.shout_post);
             
        
        
        // Create button
        Button btnPost = (Button) findViewById(R.id.sendBtn);
        
        // button click event
        btnPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new CreateShout().execute();
                Intent i = new Intent(getApplicationContext(), ShoutWallList.class);
                startActivity(i);
            }
        });

    }
    
    class LoadWall extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            
         // JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(url_get);
            
            try {
                // Getting User Arrays
                   wall_users = json.getJSONArray(TAG_USERS);
                   
                // looping through All Contacts
                   for(int i = 0; i < wall_users.length(); i++){
                       JSONObject w = wall_users.getJSONObject(i);
                        
                       // Storing each json item in variable
                       String username = w.getString(TAG_USERNAME);
                       String shout = w.getString(TAG_SHOUT);
                       
                       
                       // creating new HashMap
                       HashMap<String, String> Shout_map = new HashMap<String, String>();
                       
                       // adding each child node to HashMap key => value
                       Shout_map.put(TAG_USERNAME, username);
                       Shout_map.put(TAG_SHOUT, shout);
                       
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
                    ListAdapter walladapter = new SimpleAdapter(ShoutWallList.this,wallList,R.layout.wallview,new String[] { TAG_USERNAME, TAG_SHOUT }, new int[] {R.id.wall_user, R.id.shout_get });
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
                pDialog.setMessage("Creating Shout..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
        }

        /**
         * Creating Shout
         * */
        protected String doInBackground(String... args) {
            
            String str_shoutPost = shoutPost.getText().toString();
            
         // Building Parameters
            List<NameValuePair> shout_params = new ArrayList<NameValuePair>();
            shout_params.add(new BasicNameValuePair("LobbyId", null));
            shout_params.add(new BasicNameValuePair("UserId", null));
            shout_params.add(new BasicNameValuePair("shoutPost", str_shoutPost));
            
            Log.d("Gender", "Value : " + str_shoutPost);
            
            JSONObject json_wall = jParser_post.makeHttpRequest(url_post,
                    "POST", shout_params);

            
            
            
            return null;
            
        }
        
    }
    

}
