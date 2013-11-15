package com.shout_out;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.shout_out.LogonActivity;
import com.shout_out.MainActivity;
import com.shout_out.R;

public class MainActivity extends FragmentActivity implements LocationListener {
	final JSONParser jsonParser = new JSONParser();
	// used to be like "private static final" need research about it
	final String url_get_data = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/register.php";

	//private ProgressDialog pDialog;
    final String TAG_SUCCESS = "success";
    final String TAG_MESSAGE = "message";
    final String TAG_LOBBIES = "lobbies";
	

	GoogleMap googleMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
        	
        	int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
            
        }else {	// Google Play Services are available	
		
/*			for (int i = 1; i <= 47; i++){
				// Add Marker
				googleMap.addMarker(new MarkerOptions()  
	            .position(new LatLng(lat[i], lng[i]))  
	            .title(lobby[i])
	            .snippet("Population: 137,400")); 
	                    
	                    
	                    
	            

	        	}

	            googleMap.setOnInfoWindowClickListener(
	           	new OnInfoWindowClickListener(){
	           		public void onInfoWindowClick(Marker marker){
	           		// need pass the lobby_id to new activity 
	               	Intent Logon = new Intent(getApplicationContext(), LogonActivity.class);
	               	startActivity(Logon);    
	               	// closing this screen
	              	finish();
	           		});

*/
			}
        


			
			// Enabling MyLocation Layer of Google Map
			googleMap.setMyLocationEnabled(true);				
					
			
			 // Getting LocationManager object from System Service LOCATION_SERVICE
	        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	
	        // Creating a criteria object to retrieve provider
	        Criteria criteria = new Criteria();
	
	        // Getting the name of the best provider
	        String provider = locationManager.getBestProvider(criteria, true);
	
	        // Getting Current Location
	        Location location = locationManager.getLastKnownLocation(provider);
	
	        if(location!=null){
	                onLocationChanged(location);
	        }
	
	        locationManager.requestLocationUpdates(provider, 20000, 0, this);
        }
	

	@Override
	public void onLocationChanged(Location location) {
		
		TextView tvLocation = (TextView) findViewById(R.id.tv_location);
		
		// Getting latitude of the current location
		double latitude = location.getLatitude();
		
		// Getting longitude of the current location
		double longitude = location.getLongitude();		
		
		// Creating a LatLng object for the current location
		LatLng latLng = new LatLng(latitude, longitude);
		
		// Showing the current location in Google Map
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		
		// Zoom in the Google Map
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));		
		
		
		// Setting latitude and longitude in the TextView tv_location
		tvLocation.setText("Latitude:" +  latitude  + ", Longitude:"+ longitude );		
				
	}
	
	class GetLobby extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
/*                @Override
        protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Getting Data..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
        }*/

        /**
         * Getting data
         * */
        protected String doInBackground(String... args) {

                // getting JSON Object
                JSONObject json = jsonParser.getJSONFromUrl(url_get_data);

                
                // check log cat for response
                Log.d("Create Response", json.toString());

                // check for success tag
                try {
                        int success = json.getInt(TAG_SUCCESS);
                        
                        JSONArray lobbyList = json.getJSONArray(TAG_LOBBIES);
                		
            			// Getting reference to the SupportMapFragment of activity_main.xml
            			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            			
            			// Getting GoogleMap object from the fragment
            			googleMap = fm.getMap();

                        int [] lobby_id = new int[lobbyList.length()];
            			double [] lat = new double[lobbyList.length()];
            			double [] lng = new double[lobbyList.length()];
                        double [] rad = new double[lobbyList.length()];
                        String [] lobby_name = new String[lobbyList.length()];
                        String [] thumb_url = new String[lobbyList.length()];
            			
                        for (int i = 1; i <= lobbyList.length(); i++){
                        	JSONObject l = lobbyList.getJSONObject(i);
                            lobby_id[i] = l.getInt("lobby_id");
                            lat[i] = l.getDouble("lat");
                            lng[i] = l.getDouble("lng");
                            rad[i] = l.getDouble("rad");
                            lobby_name[i] = l.getString("lobby_name");
                            thumb_url[i] = l.getString("thumb_url");
                            
                            googleMap.addMarker(new MarkerOptions()
            	            .position(new LatLng(lat[i], lng[i]))
            	            .title(lobby_name[i])
            	            .snippet("Population: 137,400"));
                        }
   
                        
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
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
}