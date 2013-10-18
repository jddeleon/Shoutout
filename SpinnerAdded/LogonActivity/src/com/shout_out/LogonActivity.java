package com.shout_out;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import android.app.ProgressDialog;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.shout_out.LogonActivity;
import com.shout_out.RegisterActivity;
import com.shout_out.MainActivity;
import com.shout_out.R;
//import com.shout_out.RegisterActivity.CreateNewUser;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
//import android.widget.Toast;

public class LogonActivity extends Activity {

	private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText logEmail;
    EditText logPassword;

    private static String url_log_on = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/login.php";
    
    private static final String TAG_SUCCESS = "success";
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logon);
		
	    logEmail = (EditText) findViewById(R.id.log_email);
	    logPassword = (EditText) findViewById(R.id.log_password);

//--------------------------------------------------
	    class LogOnActivity extends AsyncTask<String, String, String> {

            /**
             * Before starting background thread Show Progress Dialog
             * */
            @Override
            protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(LogonActivity.this);
                    pDialog.setMessage("Logging in...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
            }

            /**
             * Log in user
             * */
            protected String doInBackground(String... args) {
                    
                    String str_logEmail = logEmail.getText().toString();
                    String str_logPassword = logPassword.getText().toString();
                    

                    // Building Parameters
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                   
                    params.add(new BasicNameValuePair("logEmail", str_logEmail));
                    params.add(new BasicNameValuePair("logPassword", str_logPassword));
                    
                    // getting JSON Object
                    // Note that create product URL accepts POST method
                    JSONObject json = jsonParser.makeHttpRequest(url_log_on,
                                    "POST", params);
                    
                    // check log cat for response
                    Log.d("Create Response", json.toString());

                    // check for success tag
                    try {
                            int success = json.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                    // successfully created product
                                    Intent Main = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(Main);
                                    
                                    // closing this screen
                                    finish();
                            } else {
                                    // failed to create product
                            }
                    } catch (JSONException e) {
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
            }

    }
	    
	  //--------------------------------------------------
		TextView registerScreen = (TextView) findViewById(R.id.btnlink_to_register);
	
        registerScreen.setOnClickListener(new View.OnClickListener() {
        	    public void onClick(View v) {
                //Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logon, menu);
		return true;
	}

}
