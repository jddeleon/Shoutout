package com.shout_out;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class LogonActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logon);
        
		TextView registerScreen = (TextView) findViewById(R.id.btnlink_to_register);
	
        registerScreen.setOnClickListener(new View.OnClickListener() {
        	    public void onClick(View v) {
                //Switching to Register screen
                Intent gotoreg = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(gotoreg);
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
