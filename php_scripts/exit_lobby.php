<?php

//load and connect to DB
require("db_connect.php");

if (!empty($_POST))
{
	if (empty($_POST['userId']) || empty($_POST['lobbyId']))
	{	
		$response["success"] = 0;
		$response["message"] = "Please complete all required fields";
		die(json_encode($response));
	}
	
	// else all required fields have been entered
	$userId = $_POST['userId'];
	$lobbyId = $_POST['lobbyId'];

	// Check to see if email and password match a row in the DB
    try 
	{
		$stmt = $db->prepare("DELETE FROM lobby_users WHERE user_id = ?");
		$stmt->bind_param("s", $userId);
		$stmt->execute();
		$stmt = $db->prepare("SELECT lobby_name FROM lobby WHERE lobby_id = ?");
		$stmt->bind_param("s", $lobbyId);
		$stmt->execute();
		$stmt->store_result();
		$stmt->bind_result($lobbyName);
		$stmt->fetch();

		
    }
    catch (mysqli_exception $ex) 
	{
        $response["success"] = 0;
        $response["message"] = "Database Error1. Please Try Again!";
        die(json_encode($response));
    }

	$response["success"] = 1;
	$response["message"] = "You have exited ".$lobbyName." Lobby";

	echo json_encode($response);
	
} else {
?>
		<h1>Exit Lobby</h1> 
		<form action="exit_lobby.php" method="post"> 
		    UserID:<br /> 
		    <input type="text" name="userId" placeholder="User ID" /> 
		    <br /><br /> 
		    LobbyID:<br /> 
		    <input type="text" name="lobbyId" placeholder="Lobby ID" value="" /> 
		    <br /><br /> 
		    <input type="submit" value="Exit Lobby" /> 
		</form> 
		<a href="register.php">Register</a>
	<?php
}

?> 
