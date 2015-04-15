package com.shout_out.libraries;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


import android.util.Log;

public class UserFunctions {

	private JSONParser jsonParser;
	private static String urlLogin = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/login.php";
	private static String urlRegister = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/register.php";
	private static String urlGetLobbyList = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/get_lobby_list.php";
	private static String urlGetLobbyPop = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/get_lobby_user_ct.php";
	
	// constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
     
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("logEmail", email));
        params.add(new BasicNameValuePair("logPassword", password));
        JSONObject json = jsonParser.makeHttpRequest(urlLogin, "POST", params);
        return json;
    }
    
    /**
     * function Register a new user
     * @param fName
     * @param lName
     * @param username
     * @param email
     * @param password
     * @param confirmPass
     * @param major
     * @param age
     * @param gender
     * */
    public JSONObject registerUser(String fName, String lName, String username, String email, String password, 
    		String confirmPass, String major, String age, String gender){
    	//Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("regAge", age));
        params.add(new BasicNameValuePair("regEmail", email));
        params.add(new BasicNameValuePair("regFirst", fName));
        params.add(new BasicNameValuePair("regLast", lName));
        params.add(new BasicNameValuePair("regMajor", major));
        params.add(new BasicNameValuePair("regPassword", password));
        params.add(new BasicNameValuePair("regGender", gender));
        params.add(new BasicNameValuePair("regUsername", username));
        params.add(new BasicNameValuePair("regConfirmPass", confirmPass));

        // getting JSON Object
        // NOTE: The create_user URL accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(urlRegister,
                        "POST", params);
        return json;
    }
	

}
