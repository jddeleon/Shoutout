package com.shout_out;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
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
        Button btnCreateUser = (Button) findViewById(R.id.btnLogout);
        
        // button click event
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
        	@Override
            public void onClick(View view) {

            Intent gologin = new Intent(getApplicationContext(), LogonActivity.class);
            
            startActivity(gologin);
            }
        	});
	}
}