package com.shout_out;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;



import com.shout_out.JSONParser;
import com.shout_out.LogonActivity;
import com.shout_out.RegisterActivity;
import com.shout_out.MainActivity;
import com.shout_out.R;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
 
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to main.xml
        Log.d("Main", "About to call setContentView");
        setContentView(R.layout.main);
        Log.d("Main", "About to make button logout");       
    }
    
     
    
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.logon, menu);
                  return true;
      }
     
     public boolean onOptionsItemSelected(MenuItem item){
             
            int itemId = item.getItemId();
            if (itemId == R.id.logout) {
                Intent gologin = new Intent(this, LogonActivity.class);
                startActivity(gologin);
                Toast.makeText(MainActivity.this, "Signing Out", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.menu_editprofile) {
                Intent testlist = new Intent(this, UserLazyList.class);
                startActivity(testlist);
                Toast.makeText(MainActivity.this, "editprofile is Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }  
     
    

       
}