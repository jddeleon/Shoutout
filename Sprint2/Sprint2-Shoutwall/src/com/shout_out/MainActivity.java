package com.shout_out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements LocationListener {
	
	boolean MUTEX_LOCK = false;
    
    // url to make request
    private static String url = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/get_lobby_list.php";
    
    // JSON Node names
    private static final String TAG_LOBBIES = "lobbies";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_COUNT = "numLobbies";
    private static final String TAG_ID = "lobby_id";
    private static final String TAG_NAME = "lobby_name";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LNG = "lng";
    private static final String TAG_RAD = "rad";
    private static final String TAG_THUMB = "thumb_url";
    
    // new JSON Parser instance
    JSONParser jParser = new JSONParser();
    
    // might not need this. come back to it later
    ArrayList<HashMap<String, String>> lobbyList;
    
    //JSONArray
    JSONArray lobbies = null;

    // New Google Map instance
	GoogleMap googleMap;
	
	private ProgressDialog pDialog;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Hashmap for Google Map Lobby List
        lobbyList = new ArrayList<HashMap<String, String>>();
		
		// Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
        	
        	int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
            
        }else {	// Google Play Services are available
        	
        	new GetLobby().execute();
        	Log.d("Lobby", "Returned from GetLobby");
			// Getting reference to the SupportMapFragment of activity_main.xml
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			
			// Getting GoogleMap object from the fragment
			googleMap = fm.getMap();
			final Map<Marker, Class> allMarkersMap = new HashMap<Marker, Class>();
			
        	// class used for switching to new activity when info window is clicked
			//Class[] c = new Class[48];
			// e.g. c[1] = LogonActivity.class;
			
			// CRITICAL SECTION
			// WAIT UNTIL AYSNC TASK IS COMPLETE BEFORE CONTINUING
			while (MUTEX_LOCK == false){
				Log.d("MUTEX", "Waiting for async task to finish");
			}
			
			// display markers and circles around markers
			Marker marker[] = new Marker[lobbyList.size()];
			Circle circle[] = new Circle[lobbyList.size()];
			
			// iterate through lobbyList and place markers on map
			//Iterator itList = lobbyList.iterator();
			Log.d("Lobby", "About to Enter for loop");
			int count = lobbyList.size();
			
			for (int i=0; i < count; i++){
				Log.d("Lobby", "Inside for loop");
				HashMap<String, String> lobbyMap = lobbyList.get(i);

				String id = lobbyMap.get(TAG_ID);
				String name = lobbyMap.get(TAG_NAME);
				double lat = Double.parseDouble(lobbyMap.get(TAG_LAT));
				double lng = Double.parseDouble(lobbyMap.get(TAG_LNG));
				int rad = Integer.parseInt(lobbyMap.get(TAG_RAD));
				String thumb = lobbyMap.get(TAG_THUMB);

				Log.d("Lobby", "Creating Markers");
				// Add Marker
				marker[i] = googleMap.addMarker(new MarkerOptions()  
				.position(new LatLng(lat, lng))  
				.title(name)
				.snippet("Lobby Population: 0"));

				//allMarkersMap.put(marker[k], LogonActivity.class);

				Log.d("Lobby", "Adding Circles");
				// Add Circle around the map marker to indicate lobby boundary
				circle[i] = googleMap.addCircle(new CircleOptions()
				.center(new LatLng(lat, lng))
				.radius(rad)
				.fillColor(0x40ff0000)
				.strokeColor(Color.BLUE)
				.strokeWidth(1));
				Log.d("Lobby", "Finished inner for loop");
				
				Log.d("Lobby", "Finished OUTER for loop");
			}
			googleMap.setOnInfoWindowClickListener(
					new OnInfoWindowClickListener(){
						public void onInfoWindowClick(Marker marker){
							Class cls = allMarkersMap.get(marker);
							Intent Logon = new Intent(getApplicationContext(), cls);
							startActivity(Logon);    
							// closing this screen
							finish();
						}
					});
				
			
        
	
        	
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
		
	class GetLobby extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Fetching Lobbies..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
        }

        /**
         * Getting data
         * */
        protected String doInBackground(String... args) {
        	

        	// JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(url);

            // check log cat for response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
            	// Getting lobby Arrays
                int success = json.getInt(TAG_SUCCESS);
                int numLobbies = json.getInt(TAG_COUNT);
                String message = json.getString(TAG_MESSAGE);
                
                // Lobbies element is an array of JSON objects
                //JSONObject lobbyList = json.getJSONObject(TAG_LOBBIES);
                lobbies = json.getJSONArray(TAG_LOBBIES);
                
                
                // looping through All Lobbies
                for(int i = 0; i < lobbies.length(); i++){
                	JSONObject l = lobbies.getJSONObject(i);
                	
                    // Storing each JSON item in a variable
                	String id = l.getString(TAG_ID);
                    String name = l.getString(TAG_NAME);
                    String lat = l.getString(TAG_LAT);
                    String lng = l.getString(TAG_LNG);
                    String rad = l.getString(TAG_RAD);
                    String thumb = l.getString(TAG_THUMB);
                    
                    // creating new HashMap
                    HashMap<String, String> lobbyHashMap = new HashMap<String, String>();
                    
                    // adding each child node to HashMap key => value
                    lobbyHashMap.put(TAG_ID, id);
                    lobbyHashMap.put(TAG_NAME, name);
                    lobbyHashMap.put(TAG_LAT, lat);
                    lobbyHashMap.put(TAG_LNG, lng);
                    lobbyHashMap.put(TAG_RAD, rad);
                    lobbyHashMap.put(TAG_THUMB, thumb);
                    
                    //adding HashList to ArrayList
                    lobbyList.add(lobbyHashMap);
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
                MUTEX_LOCK = true;
                Log.d("Login", "Just dismissed dialog box");
                Log.d("Login", "File_url: "+file_url);

                // display the toast notification containing the returned JSON message
                if (file_url != null){
                	Log.d("Login", "About to display toast");
                   	Toast.makeText(MainActivity.this, file_url, Toast.LENGTH_LONG).show();
                }
                
        }
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