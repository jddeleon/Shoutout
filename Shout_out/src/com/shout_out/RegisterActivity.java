package com.shout_out;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends Activity {
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.register);
      
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

