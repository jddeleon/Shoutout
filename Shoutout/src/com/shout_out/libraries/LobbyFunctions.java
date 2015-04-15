package com.shout_out.libraries;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import com.shout_out.libraries.JSONParser;

public class LobbyFunctions {
	
	private JSONParser jsonParser;
	
	private static String urlGetLobbyList = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/get_lobby_list.php";
	private static String urlGetLobbyPopulation =  "http://www.ecst.csuchico.edu/~jdeleon/shoutout/get_lobby_user_ct.php";
	private static String urlGetLobbyUsers = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/get_lobby_users.php";
	private static String urlGetShouts = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/get_shouts.php";
    private static String urlPostShout = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/post_shout.php";
    private static String urlEnterLobby = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/enter_lobby.php";
    private static String urlExitLobby = "http://www.ecst.csuchico.edu/~jdeleon/shoutout/exit_lobby.php";


	
	// constructor
    public LobbyFunctions(){
        jsonParser = new JSONParser();
    }
     
    /**
     * function get list of Lobbies
     * */
    public JSONObject GetLobbies(){
        
        JSONObject json = jsonParser.getJSONFromUrl(urlGetLobbyList);
        return json;
    }
    
    /**
     * function get Lobby population
     * @param lobbyId
     * */
    public JSONObject GetLobbyPop(String lobbyId){
    	// Building Parameters for getting lobby population
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("lobbyId", lobbyId));
    
        JSONObject json = jsonParser.makeHttpRequest(urlGetLobbyPopulation,
                        "POST", params);
        return json;
    }
    
    /**
     * function  get lobby users
     * @param lobbyId
     * @param userId
     * */
    public JSONObject GetLobbyUsers(String lobbyId, String userId){
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();

    	params.add(new BasicNameValuePair("userId", userId));
    	params.add(new BasicNameValuePair("lobbyId", lobbyId));
    	
    	JSONObject json = jsonParser.makeHttpRequest(urlGetLobbyUsers,
                "POST", params);
    	return json;
    }
    
    /**
     * function  get shouts
     * @param lobbyId
     * */
    public JSONObject GetShouts(String lobbyId){
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
       
        params.add(new BasicNameValuePair("lobbyId", lobbyId));
    	
    	JSONObject json = jsonParser.makeHttpRequest(urlGetShouts,
                "POST", params);
    	return json;
    }
    
    /**
     * function post shout message
     * @param lobbyId
     * @param shout
     * @param userId
     * */
    public JSONObject PostShout(String lobbyId, String userId, String shout){
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("lobbyId", lobbyId));
    	params.add(new BasicNameValuePair("userId", userId));
    	params.add(new BasicNameValuePair("shout", shout));
    	
    	JSONObject json = jsonParser.makeHttpRequest(urlPostShout,
                "POST", params);
    	return json;
    }
    
    /**
     * function post shout message
     * @param lobbyId
     * @param userId
     * */
    public JSONObject EnterLobby(String lobbyId, String userId){
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("lobbyId", lobbyId));
    	params.add(new BasicNameValuePair("userId", userId));
    	
    	JSONObject json = jsonParser.makeHttpRequest(urlEnterLobby,
                "POST", params);
    	return json;
    }
    
    /**
     * function post shout message
     * @param lobbyId
     * @param userId
     * */
    public JSONObject ExitLobby(String lobbyId, String userId){
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("lobbyId", lobbyId));
    	params.add(new BasicNameValuePair("userId", userId));
    	
    	JSONObject json = jsonParser.makeHttpRequest(urlExitLobby,
                "POST", params);
    	return json;
    }
	
}
