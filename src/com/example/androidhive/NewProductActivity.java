package com.example.androidhive;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewProductActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText inputName;
	EditText inputPrice;
	EditText inputDesc;
	EditText inputFname;
	EditText inputLname;
	EditText inputUsername;
	EditText inputPassword;
	EditText inputEmail;
	EditText inputGender;
	EditText inputMajor;
	EditText inputAge;

	// url to create new product
	private static String url_create_product = "http://www.ecst.csuchico.edu/~jdeleon/create_user.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_product);

		// Edit Text
		//inputName = (EditText) findViewById(R.id.inputName);
		//inputPrice = (EditText) findViewById(R.id.inputPrice);
		//inputDesc = (EditText) findViewById(R.id.inputDesc);
		inputFname = (EditText) findViewById(R.id.inputFname);
		inputLname = (EditText) findViewById(R.id.inputLname);
		inputUsername = (EditText) findViewById(R.id.inputUsername);
		inputPassword = (EditText) findViewById(R.id.inputPassword);
		inputEmail = (EditText) findViewById(R.id.inputEmail);
		inputGender = (EditText) findViewById(R.id.inputGender);
		inputMajor = (EditText) findViewById(R.id.inputMajor);
		inputAge = (EditText) findViewById(R.id.inputAge);

		// Create button
		Button btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);

		// button click event
		btnCreateProduct.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				new CreateNewProduct().execute();
			}
		});
	}

	/**
	 * Background Async Task to Create new product
	 * */
	class CreateNewProduct extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(NewProductActivity.this);
			pDialog.setMessage("Creating User..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			//String name = inputName.getText().toString();
			//String price = inputPrice.getText().toString();
			//String description = inputDesc.getText().toString();
			String fname = inputFname.getText().toString();
			String lname = inputLname.getText().toString();
			String username = inputUsername.getText().toString();
			String password = inputPassword.getText().toString();
			String email = inputEmail.getText().toString();
			String gender = inputGender.getText().toString();
			String age = inputAge.getText().toString();
			String major = inputMajor.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			//params.add(new BasicNameValuePair("name", name));
			//params.add(new BasicNameValuePair("price", price));
			//params.add(new BasicNameValuePair("description", description));
			params.add(new BasicNameValuePair("fname", fname));
			params.add(new BasicNameValuePair("lname", lname));
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("gender", gender));
			params.add(new BasicNameValuePair("age", age));
			params.add(new BasicNameValuePair("major", major));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_product,
					"POST", params);
			
			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
					startActivity(i);
					
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
}
