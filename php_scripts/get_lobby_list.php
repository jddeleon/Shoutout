<?php
/*
 * Following code will retrieve a list of lobbies
 * from the database and return the list in a JSON
 */

// connect to the database
require("db_connect.php");

// array for JSON response and set success to FALSE
$response = array();
$response["success"] = 0;
$lobbyArray = array();
$rowCount = 0;

// Check to see if username is already in the DB. 
try 
{	
	$stmt = $db->prepare("SELECT * FROM lobby");
	$stmt->bind_result($lobbyId, $lobbyName, $lobbyLat, $lobbyLng, $lobbyRad, $lobbyThumbUrl);
	$stmt->execute();
	while ($stmt->fetch())
	{	
		$lobbyArray[] = array("lobby_id"=>$lobbyId, "lobby_name"=>$lobbyName, 
								"lat"=>$lobbyLat, "lng"=>$lobbyLng, "rad"=>$lobbyRad,
								"thumb_url"=>$lobbyThumbUrl);
		$rowCount += 1;
	}
	$stmt->close();
}
catch (mysqli_exception $ex) 
{
	$response["success"] = 0;
	$response["message"] = "Database Error1. Please Try Again!";
	die(json_encode($response));
}

$response["success"] = 1;
$response["message"] = "Retrieve Lobbies Successful!";
$response["numLobbies"] = $rowCount;
$response["lobbies"] = $lobbyArray;
echo json_encode($response);


?>