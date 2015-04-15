<?php

//load and connect to DB
require("db_connect.php");

if (!empty($_POST))
{
	if (empty($_POST['shout']))
	{	
		$response["success"] = 0;
		$response["message"] = "Message cannot be blank!";
		die(json_encode($response));
	}
	
	// else all required fields have been entered
	$userId = $_POST['userId'];
	$lobbyId = $_POST['lobbyId'];
	$shout = $_POST['shout'];
	$time = date('H:i');
	// Check to see if email and password match a row in the DB
    try 
	{	
		$stmt = $db->prepare("INSERT INTO shout values (null, ?, ?, ?, curtime())");
		$stmt->bind_param("sss", $shout, $lobbyId, $userId);
		$stmt->execute();
    }
    catch (mysqli_exception $ex) 
	{
        $response["success"] = 0;
        $response["message"] = "Database Error1. Please Try Again!";
        die(json_encode($response));
    }
        

	$response["success"] = 1;
	$response["message"] = "Post Successful!";
	
	// return either success or failure
	die(json_encode($response));
	
} else {
?>
		<h1>Post Shout</h1> 
		<form action="post_shout.php" method="post"> 
		    Shout:<br /> 
		    <input type="text" name="shout" placeholder="Shout" /> 
		    <br /><br /> 
		    lobbyId:<br /> 
		    <input type="text" name="lobbyId" placeholder="LobbyId" value="" /> 
		    <br /><br /> 
            UserId:<br /> 
		    <input type="text" name="userId" placeholder="UserId" value="" /> 
            <br /><br />
		    <input type="submit" value="Post" /> 
		</form> 
		<a href="register.php">Register</a>
	<?php
}

?> 