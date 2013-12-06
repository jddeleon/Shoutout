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
import com.shout_out.libraries.UserFunctions;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class LogonActivity extends Activity {

	private ProgressDialog pDialog;
	
	UserFunctions userFunction = new UserFunctions();

    EditText logEmail;
    EditText logPassword;
    TextView loginErrorMsg;
    
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_ID = "userId";
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logon);
		
	    logEmail = (EditText) findViewById(R.id.log_email);
	    logPassword = (EditText) findViewById(R.id.log_password);
	    loginErrorMsg = (TextView) findViewById(R.id.login_error);

	  //----------------------------------------------------------------------------------------------
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
                    
                    JSONObject json = userFunction.loginUser(str_logEmail, str_logPassword);
                    
                    // check log cat for response
                    Log.d("Create Response", json.toString());

                    // check for success tag
                    try {
                            int success = json.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                    // successfully log in
                            		int userId = json.getInt(TAG_ID);
                            		Log.d("Login", "About to call new Intent");
                                    Intent Main = new Intent(getApplicationContext(), MainActivity.class);
                                    Main.putExtra("userId", userId);
                                    Main.putExtra("temp", 0);
                                    Log.d("Login", "About to call startActivity(Main)");
                                    startActivity(Main);
                                    Log.d("Login", "About to call finish()");
                                    
                                    // closing this screen
                                    finish();
									return json.getString(TAG_MESSAGE);
                            } else {
								
                                // failed to login
								Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                                return json.getString(TAG_MESSAGE);
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
                    Log.d("Login", "File_url: "+file_url);

                    // display the toast notification containing the returned JSON message
                    if (file_url != null){
                    	loginErrorMsg.setText(file_url);
                       	Toast.makeText(LogonActivity.this, file_url, Toast.LENGTH_LONG).show();
                    }
                    
            }

    }
	    
	  //----------------------------------------------------------------------------------------------
	    // Create button
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        
        // button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

        	@Override
        	public void onClick(View view) {
        	
        		new LogOnActivity().execute();
            }
        });

	    
	    
	    
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
