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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class UserLazyList extends ListActivity{
    
    // url to make request
    private static String url = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/get_all_users.php";
    
    // JSON Node names
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
   
    
    // new JSON Parser instance
    JSONParser jParser = new JSONParser();
    
    ArrayList<HashMap<String, String>> usersList;
    
    //JSONArray
    JSONArray users = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
    
         // Hashmap for ListView
        usersList = new ArrayList<HashMap<String, String>>();

        new LoadUsers().execute();
            
        

    }
    
    
    
    class LoadUsers extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            
         // JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            
            try {
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
                       
                       // creating new HashMap
                       HashMap<String, String> map = new HashMap<String, String>();
                       
                       // adding each child node to HashMap key => value
                       map.put(TAG_ID, id);
                       map.put(TAG_NAME, name);
                       map.put(TAG_MAJOR, major);
                       map.put(TAG_AGE, age);
                       
                       //adding HashList to ArrayList
                       usersList.add(map);
                       }
                     } catch (JSONException e) {
                           e.printStackTrace();
                       }
            
            // TODO Auto-generated method stub
            return null;
        }
        
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all albums
            
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter lazyadapter = new SimpleAdapter(UserLazyList.this, usersList,R.layout.listview,new String[] { TAG_NAME, TAG_MAJOR, TAG_AGE }, new int[] {R.id.user, R.id.major, R.id.genderlist });
                    setListAdapter(lazyadapter);
                    
                }
            });

        }

    
    }
    
    
          
}
       
    
 

            