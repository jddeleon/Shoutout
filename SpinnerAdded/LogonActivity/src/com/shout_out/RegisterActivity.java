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

import com.shout_out.LogonActivity;
import com.shout_out.RegisterActivity;
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

public class RegisterActivity extends Activity {

        // Progress Dialog
        private ProgressDialog pDialog;

        JSONParser jsonParser = new JSONParser();
        EditText regFirst;
        EditText regLast;
        EditText regUsername;
        EditText regAge;
        EditText regEmail;
        EditText regPassword;
        EditText regConfirmPass;
        EditText regMajor;
        Spinner regGender;

        // URL to create new user
        private static String url_create_user = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/register.php";

        // JSON Node names
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.register);
      //----------------------------------------------------------------------------------------------
        Spinner genderselect= (Spinner) findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        genderselect.setAdapter(adapter);
        genderselect.setOnItemSelectedListener(new OnItemSelectedListener()  {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, 
                            int pos, long id) {
                        // An item was selected. You can retrieve the selected item using
                         parent.getItemAtPosition(pos);
                         Log.d("gendertest", (String) parent.getItemAtPosition(pos));
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                      // Another interface callback
                    }

        });
      //----------------------------------------------------------------------------------------------
        
        regAge = (EditText) findViewById(R.id.reg_age);
        regEmail = (EditText) findViewById(R.id.reg_email);
        regFirst = (EditText) findViewById(R.id.reg_first);
        regLast = (EditText) findViewById(R.id.reg_last);
        regMajor = (EditText) findViewById(R.id.reg_major);
        regPassword = (EditText) findViewById(R.id.reg_password);
        regConfirmPass = (EditText) findViewById(R.id.reg_confirmPass);
        regGender = (Spinner) findViewById(R.id.gender_spinner);
        regUsername = (EditText) findViewById(R.id.reg_username);
 
 
      //----------------------------------------------------------------------------------------------
            /**
             * Background Async Task to Register a new User
             * 
             * Actually you can define this class at very end of the program by doing this
             * import com.shout_out.RegisterActivity.CreateNewUser;
             * But I failed to do so. So I just moved everything before that call happen.
             * Simon
             * 
             * */
            class CreateNewUser extends AsyncTask<String, String, String> {

                    /**
                     * Before starting background thread Show Progress Dialog
                     * */
                    @Override
                    protected void onPreExecute() {
                            super.onPreExecute();
                            pDialog = new ProgressDialog(RegisterActivity.this);
                            pDialog.setMessage("Registering User..");
                            pDialog.setIndeterminate(false);
                            pDialog.setCancelable(true);
                            pDialog.show();
                    }

                    /**
                     * Creating user
                     * */
                    protected String doInBackground(String... args) {
                            String str_regAge = regAge.getText().toString();
                            String str_regEmail = regEmail.getText().toString();
                            String str_regFirst = regFirst.getText().toString();
                            String str_regLast = regLast.getText().toString();
                            String str_regMajor = regMajor.getText().toString();
                            String str_regPassword = regPassword.getText().toString();
                            String str_regConfirmPass = regConfirmPass.getText().toString();
                            String str_regUsername = regUsername.getText().toString();
                            String str_regGender = regGender.getSelectedItem().toString();
 


                            // Building Parameters
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("regAge", str_regAge));
                            params.add(new BasicNameValuePair("regEmail", str_regEmail));
                            params.add(new BasicNameValuePair("regFirst", str_regFirst));
                            params.add(new BasicNameValuePair("regLast", str_regLast));
                            params.add(new BasicNameValuePair("regMajor", str_regMajor));
                            params.add(new BasicNameValuePair("regPassword", str_regPassword));
                            params.add(new BasicNameValuePair("regGender",str_regGender));
                            params.add(new BasicNameValuePair("regUsername", str_regUsername));
                            params.add(new BasicNameValuePair("regConfirmPass",str_regConfirmPass));

                            // getting JSON Object
                            // NOTE: The create_user URL accepts POST method
                            JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                                            "POST", params);
                            
                            // check log cat for response
                            Log.d("Create Response", json.toString());

                            // check for success tag
                            try {
                                    int success = json.getInt(TAG_SUCCESS);

                                    if (success == 1) {
                                            // successfully registered the user
                                    		Log.d("User Registered!", json.toString());
                                            Intent Logon = new Intent(getApplicationContext(), LogonActivity.class);
                                            startActivity(Logon);
                                            
                                            // closing this screen
                                            finish();
                                            return json.getString(TAG_MESSAGE);
                                    } else {
                      
                                        // failed to register the user
                                    	Log.d("Register Failure!", json.getString(TAG_MESSAGE));
                                       	return json.getString(TAG_MESSAGE);
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
                            
                            // display the toast notification containing the returned JSON message
                            if (file_url != null){
                               	Toast.makeText(RegisterActivity.this, file_url, Toast.LENGTH_LONG).show();
                            }
                    }

            }
          //----------------------------------------------------------------------------------------------
        
        // Create button
        Button btnCreateUser = (Button) findViewById(R.id.btnRegister);
        
        // button click event
             btnCreateUser.setOnClickListener(new View.OnClickListener() {

                             @Override
                             public void onClick(View view) {
                                     // creating new product in background thread
                                     new CreateNewUser().execute();
                             }
                     });

             
             
        TextView loginScreen = (TextView) findViewById(R.id.btnlink_to_login);
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {
                
            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });
    }
}