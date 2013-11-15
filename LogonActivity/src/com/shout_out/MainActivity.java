package com.shout_out;

import java.util.HashMap;
import java.util.Map;

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


	

	GoogleMap googleMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Getting Google Play availability status
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
        	
        	int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
            
        }else {	// Google Play Services are available	
        	
        	// used to be like "private static final" need research about it
        	final String url_get_data = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/register.php";
        	final JSONParser jsonParser = new JSONParser();

        	double latitude;
        	double longitude;
        	//private ProgressDialog pDialog;
            final String TAG_SUCCESS = "success";
            final String TAG_MESSAGE = "message";
            final String TAG_LOBBIES = "lobbies";
            
        	
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
                	JSONParser jsonParser = new JSONParser();
                        // getting JSON Object
                        JSONObject json = jsonParser.getHttpRequest(url_get_data);
                        
                        
                        // check log cat for response
                        Log.d("Create Response", json.toString());
                        
                        

                        // check for success tag
                        try {
                                int success = json.getInt(TAG_SUCCESS);
                                JSONArray lobbyList = json.getJSONArray(TAG_LOBBIES);
                                
                                double [] lat = new double [lobbyList.length()];
                                double [] lng = new double [lobbyList.length()];
                                int [] lobby_id = new int [lobbyList.length()];
                                String [] lobby_name = new String [lobbyList.length()];
                                double [] rad = new double [lobbyList.length()];
                                String [] thumb_url = new String [lobbyList.length()];
                                
                                for (int i = 0; i < lobbyList.length(); i++){
                                	JSONObject c = lobbyList.getJSONObject(i);
                                    lat [i] = c.getDouble("lat");
                                    lng [i] = c.getDouble("lng");
                                    lobby_id [i] = c.getInt("lobby_id");
                                    lobby_name [i] = c.getString("lobby_name");
                                    rad [i] = c.getDouble("rad");
                                    thumb_url [i] = c.getString("thumb_url");
                                    
                                    System.out.println("Hello " + lat[i] + ". You are " + lng[i] + " years old" + lobby_id[i] + lobby_name[i] + rad[i] + thumb_url[i]);
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
                public int getlength()
                {
					return status;
                };
        	}
        	
        	
        	
		
			// Getting reference to the SupportMapFragment of activity_main.xml
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			
			// Getting GoogleMap object from the fragment
			googleMap = fm.getMap();
			final Map<Marker, Class> allMarkersMap = new HashMap<Marker, Class>();
			GetLobby a;
			double[] lat = new double[48];
			double[] lng = new double[48];
			String[] lobby = new String[48];
			Class[] c = new Class[48];
			
			lobby[1] = "Softball Diamond";
			lat[1] =  39.731170;
			lng [1] = -121.854257;
			c[1] = LogonActivity.class;
			lobby[2] = "Soccer Stadium";
			lat[2] =   39.731988;
			lng [2] = -121.853761;
			c[2] = RegisterActivity.class;
			lobby[3] = "Residence Halls: Esken, Konkow, Mechoopda";
			lat[3] =  39.732346;
			lng [3] = -121.852902;
			lobby[4] = "Nettleton Stadium";
			lat[4] =  39.730442;
			lng [4] = -121.853501;
			lobby[5] = "Sports Stadium";
			lat[5] = 39.730129;
			lng [5] = -121.850981;
			lobby[6] = "Student Health Center";
			lat[6] =  39.730772;
			lng [6] = -121.850340;
			lobby[7] = "Acker Gym";
			lat[7] =  39.729820;
			lng [7] = -121.849789;
			lobby[8] = "Yolo Hall";
			lat[8] =  39.728807;
			lng [8] = -121.850133;
			lobby[9] = "Shurmer Gym";
			lat[9] =  39.729101;
			lng [9] = -121.849172;
			lobby[10] = "O'Connell Technology Center";
			lat[10] =  39.727658;
			lng [10] = -121.847620;
			lobby[11] = "Langdon Engineering Center";
			lat[11] =  39.727130;
			lng [11] = -121.847401;
			lobby[12] = "Parking Structure";
			lat[12] =  39.726540;
			lng [12] = -121.846739;
			lobby[13] = "Wildcat Recreation";
			lat[13] =  39.725699;
			lng [13] = -121.847954;
			lobby[14] = "Whitney";
			lat[14] =  39.730510;
			lng [14] = -121.849051;
			lobby[15] = "Sutter Hall";
			lat[15] =  39.730870;
			lng [15] = -121.848612;
			lobby[16] = "Tehama Hall";
			lat[16] =  39.729857;
			lng [16] = -121.848509;
			lobby[17] = "Plumas Hall";
			lat[17] =  39.729347;
			lng [17] = -121.848201;
			lobby[18] = "Glenn Hall";
			lat[18] =  39.729145;
			lng [18] = -121.846308;
			lobby[19] = "Siskiyou Hall";
			lat[19] =  39.728418;
			lng [19] = -121.847385;
			lobby[20] = "Meriam Library";
			lat[20] =  39.728084;
			lng [20] = -121.846182;
			lobby[21] = "Student Services Center";
			lat[21] = 39.727149;
			lng [21] = -121.845764;
			lobby[22] = "Bell Memorial Student Union";
			lat[22] = 39.728039;
			lng [22] = -121.844798;
			lobby[23] = "Housing & Food Service";
			lat[23] = 39.731404;
			lng [23] = -121.847510;
			lobby[24] = "Shasta Hall";
			lat[24] = 39.731177;
			lng [24] = -121.847807;
			lobby[25] = "Lassen Hall";
			lat[25] = 39.730542;
			lng [25] = -121.847436;
			lobby[26] = "Butte Hall";
			lat[26] = 39.729998;
			lng [26] = -121.847289;
			lobby[27] = "Center for Continuing Education";
			lat[27] = 39.729796;
			lng [27] = -121.846018;
			lobby[28] = "Colusa Hall";
			lat[28] = 39.729628;
			lng [28] = -121.845688;
			lobby[29] = "Trinity Hall";
			lat[29] = 39.728956;
			lng [29] = -121.845296;
			lobby[30] = "Warrens Center";
			lat[30] = 39.730901;
			lng [30] = -121.846315;
			lobby[31] = "Holt Hall";
			lat[31] = 39.731063;
			lng [31] = -121.845311;
			lobby[32] = "Kendall Hall";
			lat[32] = 39.729629;
			lng [32] = -121.844766;
			lobby[33] = "Laxson Auditorium";
			lat[33] = 39.729811;
			lng [33] = -121.843840;
			lobby[34] = "Performing Arts Center";
			lat[34] = 39.728513;
			lng [34] = -121.844016;
			lobby[35] = "Sierra Hall";
			lat[35] = 39.727456;
			lng [35] = -121.843206;
			lobby[36] = "Alumni House/ Sapp Hall";
			lat[36] = 39.727736;
			lng [36] = -121.842760;
			lobby[37] = "University Police, info & Parking";
			lat[37] = 39.727805;
			lng [37] = -121.843221;
			lobby[38] = "Taylor Hall";
			lat[38] = 39.729310;
			lng [38] = -121.843256;
			lobby[39] = "Ayres Hall";
			lat[39] = 39.730031;
			lng [39] = -121.843563;
			lobby[40] = "Selvester's Cafe";
			lat[40] = 39.730015;
			lng [40] = -121.845122;
			lobby[41] = "Physical Science Bldg.";
			lat[41] = 39.730956;
			lng [41] = -121.843307;
			lobby[42] = "25 Main";
			lat[42] = 39.731748;
			lng [42] = -121.841470;
			lobby[43] = "35 Main";
			lat[43] = 39.731420;
			lng [43] = -121.841110;
			lobby[44] = "Gateway Science Museum";
			lat[44] = 39.733598;
			lng [44] = -121.843711;
			lobby[45] = "Bidwell Mansion";
			lat[45] = 39.732275;
			lng [45] = -121.843486;
			lobby[46] = "Modoc Hall";
			lat[46] = 39.732242;
			lng [46] = -121.844716;
			lobby[47] = "Aymer J. Hamilton Bldg.";
			lat[47] = 39.733145;
			lng [47] = -121.845217;
			Marker marker[] = new Marker[48];
			
			for (int i = 1; i <= 2; i++){
				// Add Marker
				marker[i] = googleMap.addMarker(new MarkerOptions()  
	            .position(new LatLng(lat[i], lng[i]))  
	            .title(lobby[i])
	            .snippet("Population: 137,400")); 
				allMarkersMap.put(marker[i], c[i]);        
	                    
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