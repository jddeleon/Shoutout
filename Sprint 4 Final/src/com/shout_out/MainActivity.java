package com.shout_out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import com.shout_out.libraries.LobbyFunctions;

public class MainActivity extends FragmentActivity implements LocationListener {
	
	int userId;
	int temp;
    int lobbyId;
    
    boolean isGPSEnabled = false;
    LocationManager locationManager;
    Location location;
    
    LobbyFunctions lobbyFunction = new LobbyFunctions();
	    
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
    private static final String TAG_POPULATION = "numUsers";
    
    private static final double latPrev = 0;
    private static final double lonPrev = 0;
    
    
    // ArrayList contains a Hashmap of lobbies
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
		
		Intent extras = getIntent();
		userId = extras.getIntExtra("userId", 0);
		temp = extras.getIntExtra("temp", 0);
		if(temp == 0)
			LobbyID.setLobbyID(-1);
		Log.d("Main Activity", "Inside On Create");
		
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
        	
        	try {
        		
        		// get lobbies from JSON and parse them. Wait until async task is complete before continuing
				new GetLobby().execute().get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	Log.d("Lobby", "Returned from GetLobby");
			// Getting reference to the SupportMapFragment of activity_main.xml
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			
			// Getting GoogleMap object from the fragment
			googleMap = fm.getMap();
			final Map<Marker, Integer> allMarkersMap = new HashMap<Marker, Integer>();
			
        	// class used for switching to new activity when info window is clicked
			//Class[] c = new Class[48];
			// e.g. c[1] = LogonActivity.class;
			
	
			
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

				lobbyId = Integer.parseInt(lobbyMap.get(TAG_ID));
				String name = lobbyMap.get(TAG_NAME);
				double lat = Double.parseDouble(lobbyMap.get(TAG_LAT));
				double lng = Double.parseDouble(lobbyMap.get(TAG_LNG));
				int rad = Integer.parseInt(lobbyMap.get(TAG_RAD));
				String thumb = lobbyMap.get(TAG_THUMB);
				String population = lobbyMap.get(TAG_POPULATION);
				

				Log.d("Lobby", "Creating Markers");
				// Add Marker
				marker[i] = googleMap.addMarker(new MarkerOptions()  
				.position(new LatLng(lat, lng))  
				.title(name)
				.snippet("Lobby Population: " + population));

				allMarkersMap.put(marker[i], lobbyId);

				Log.d("Lobby", "Adding Circles");
				// Add Circle around the map marker to indicate lobby boundary
				circle[i] = googleMap.addCircle(new CircleOptions()
				.center(new LatLng(lat, lng))
				.radius(rad)
				.fillColor(0x40ff0000)
				.strokeColor(Color.BLUE)
				.strokeWidth(1));
			}
			googleMap.setOnInfoWindowClickListener(
				new OnInfoWindowClickListener(){
					public void onInfoWindowClick(Marker marker){
						int markerLobbyId = allMarkersMap.get(marker);
						String markerLobbyName = marker.getTitle();
						if (markerLobbyId == LobbyID.getLobbyID())
						{
							Intent lobby = new Intent(getApplicationContext(), UserLazyList.class);
							lobby.putExtra("lobbyId", markerLobbyId);
							lobby.putExtra("userId", userId);
							lobby.putExtra("lobbyName", markerLobbyName);
							startActivity(lobby);    
							// closing this screen
							finish();
						}
						else
						{
							Toast.makeText(MainActivity.this, "Sorry, you must be within this lobby's area", Toast.LENGTH_LONG).show();
						}
					}
				}
			);	
        }
			
        // Enabling MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);				


        // Getting LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        
        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // if GPS Enabled get lat/long using GPS Services
        if (isGPSEnabled) {
        	Log.d("GPS", "Using GPS as Provider");
        	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
        	location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        else {
        	Log.d("GPS", "Not using GPS as Provider");
        	// Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();
        	// Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);
            location = locationManager.getLastKnownLocation(provider);
        	
        }

        // Creating a criteria object to retrieve provider
        //Criteria criteria = new Criteria();

        // Getting the name of the best provider
       // String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        //Location location = locationManager.getLastKnownLocation(provider);
        
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

        if(location!=null){
        	Log.d("GPS", "Calling onLocationChanged()");
        	onLocationChanged(location);
        }

        //locationManager.requestLocationUpdates(provider, 5000, 0, this);
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
            JSONObject json = lobbyFunction.GetLobbies();            

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
                    
                    
                    // getting JSON Object
                    JSONObject json2 = lobbyFunction.GetLobbyPop(id);
                    String population = json2.getString(TAG_POPULATION);
                    
                    // creating new HashMap
                    HashMap<String, String> lobbyHashMap = new HashMap<String, String>();
                    
                    // adding each child node to HashMap key => value
                    lobbyHashMap.put(TAG_ID, id);
                    lobbyHashMap.put(TAG_NAME, name);
                    lobbyHashMap.put(TAG_LAT, lat);
                    lobbyHashMap.put(TAG_LNG, lng);
                    lobbyHashMap.put(TAG_RAD, rad);
                    lobbyHashMap.put(TAG_THUMB, thumb);
                    lobbyHashMap.put(TAG_POPULATION, population);
                    
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
		Log.d("GPS", "Location Changed");
		
		TextView tvLocation = (TextView) findViewById(R.id.tv_location);
		
		// Getting latitude of the current location
		double latitude = location.getLatitude();
		
		// Getting longitude of the current location
		double longitude = location.getLongitude();		
		
		// Creating a LatLng object for the current location
		LatLng latLng = new LatLng(latitude, longitude);		
		
		
		// Setting latitude and longitude in the TextView tv_location
		tvLocation.setText("Latitude:" +  latitude  + ", Longitude:"+ longitude );		
				// Enter Library
				if (LobbyID.getLobbyID() != 2 &&((longitude >= -121.846973 && longitude <= -121.845529) && (latitude >= 39.727611 && latitude <= 39.728616))){
					LobbyID.setLobbyID(2);
					AlertDialog.Builder newlobby=new AlertDialog.Builder(this);
			         
					newlobby.setTitle("Meriam Library");
					newlobby.setMessage("Do you want to enter the lobby?");
					newlobby.setIcon(android.R.drawable.ic_dialog_alert);
					newlobby.setPositiveButton("Join", new DialogInterface.OnClickListener() {
			 
			            @Override
			            public void onClick(DialogInterface dialog, int i) {
							Intent lobby = new Intent(getApplicationContext(), UserLazyList.class);
							
							lobby.putExtra("lobbyId", LobbyID.getLobbyID());
							lobby.putExtra("userId", userId);
							lobby.putExtra("lobbyName", "Meriam Library");
							startActivity(lobby);  
							finish();
			            }
			        });
					newlobby.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int i) {
			                dialog.dismiss();
			            }
			        });
					newlobby.create();
					newlobby.show();
					Log.d("lobby","lobby1");
				}
				// Exit Library
				else if (LobbyID.getLobbyID() == 2 && ((longitude <= -121.846973 || longitude >= -121.845529) && (latitude <= 39.727611 || latitude >= 39.728616))){
					LobbyID.setLobbyID(-1);
					Log.d("lobby","lobby2");
				}
				
				// Enter O'Connell
				if (LobbyID.getLobbyID() != 1 && ((longitude >= -122.848123 && longitude <= -120.847143) && (latitude >= 38.727887 && latitude <= 40.726972))){
					LobbyID.setLobbyID(1);
					AlertDialog.Builder newlobby=new AlertDialog.Builder(this);
			         
					newlobby.setTitle("O'Connell");
					newlobby.setMessage("Do you want to enter the lobby?");
					newlobby.setIcon(android.R.drawable.ic_dialog_alert);
					newlobby.setPositiveButton("Join", new DialogInterface.OnClickListener() {
			 
			            @Override
			            public void onClick(DialogInterface dialog, int i) {
							Intent lobby = new Intent(getApplicationContext(), UserLazyList.class);
							
							lobby.putExtra("lobbyId", LobbyID.getLobbyID());
							lobby.putExtra("userId", userId);
							lobby.putExtra("lobbyName", "O'Connell Center");
							startActivity(lobby);  
			            }
			        });
					newlobby.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int i) {
			                dialog.dismiss();
			            }
			        });
					newlobby.create();
					newlobby.show();
				}
				// Exit O'Connell
				else if (LobbyID.getLobbyID() == 1 && ((longitude <= -121.848123 || longitude >= -121.847143) && (latitude <= 39.727887 || latitude >= 39.726972))){
					LobbyID.setLobbyID(-1);
				}
				
				// Enter Bear
				if (LobbyID.getLobbyID() != 3 && ((longitude >= -121.842877 && longitude <= -121.842373) && (latitude >= 39.729387 && latitude <= 39.728957))){
					LobbyID.setLobbyID(3);
					AlertDialog.Builder newlobby=new AlertDialog.Builder(this);
			         
					newlobby.setTitle("Madison Bear Garden");
					newlobby.setMessage("Do you want to enter the lobby?");
					newlobby.setIcon(android.R.drawable.ic_dialog_alert);
					newlobby.setPositiveButton("Join", new DialogInterface.OnClickListener() {
			 
			            @Override
			            public void onClick(DialogInterface dialog, int i) {
							Intent lobby = new Intent(getApplicationContext(), UserLazyList.class);
							
							lobby.putExtra("lobbyId", LobbyID.getLobbyID());
							lobby.putExtra("userId", userId);
							lobby.putExtra("lobbyName", "Madison Bear Garden");
							startActivity(lobby);  
			            }
			        });
					newlobby.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int i) {
			                dialog.dismiss();
			            }
			        });
					newlobby.create();
					newlobby.show();
				}
				// Exit Bear
				else if (LobbyID.getLobbyID() == 3 && ((longitude <= -121.842877 || longitude >= -121.842373) && (latitude <= 39.729387 || latitude >= 39.728957))){
					LobbyID.setLobbyID(-1);
				}
				// Enter Sylvester's
				if (LobbyID.getLobbyID() != 4 && ((longitude >= -121.845757 && longitude <= -121.844526) && (latitude >= 39.729483 && latitude <= 39.730464))){
					LobbyID.setLobbyID(4);
					AlertDialog.Builder newlobby=new AlertDialog.Builder(this);
			         
					newlobby.setTitle("Sylvester's Cafe");
					newlobby.setMessage("Do you want to enter the lobby?");
					newlobby.setIcon(android.R.drawable.ic_dialog_alert);
					newlobby.setPositiveButton("Join", new DialogInterface.OnClickListener() {
			 
			            @Override
			            public void onClick(DialogInterface dialog, int i) {
							Intent lobby = new Intent(getApplicationContext(), UserLazyList.class);
							
							lobby.putExtra("lobbyId", LobbyID.getLobbyID());
							lobby.putExtra("userId", userId);
							lobby.putExtra("lobbyName", "Sylvester's Cafe");
							startActivity(lobby);  
			            }
			        });
					newlobby.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int i) {
			                dialog.dismiss();
			            }
			        });
					newlobby.create();
					newlobby.show();
				}
				// Exit Sylvester's
				else if (LobbyID.getLobbyID() == 4 && ((longitude <= -121.845757 || longitude >= -121.844526) && (latitude <= 39.729483 || latitude >= 39.730464))){
					LobbyID.setLobbyID(-1);
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
		
}