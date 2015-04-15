<?php
/*
 * The following will retrieve a list of shouts
 * on the wall in the lobby, specified in
 * lobbyId.
 */

// connect to the database
require("db_connect.php");

// array for JSON response and set success to FALSE
$response = array();
$response["success"] = 0;
$shoutArray = array();
$rowCount = 0;

// check for lobbyId and store
if (!empty($_POST))
{
	if (empty($_POST["lobbyId"]))
	{
		$response["success"] = 0;
		$response["message"] = "Missing lobbyId!";
		die(json_encode($response));
	}
	
	
	$lobbyId = $_POST["lobbyId"];


	// Retrieve the list of shouts from the DB
	try
	{        
			$stmt = $db->prepare("SELECT user.username, shout.shout, shout.created_at
								  FROM user
								  INNER JOIN shout
								  ON user.user_id = shout.user_id and lobby_id = ?");
			$stmt->bind_param("s", $lobbyId);
			$stmt->bind_result($username, $shout, $time);
			$stmt->execute();
			while ($stmt->fetch())
			{        
					$shoutArray[] = array("username"=>$username, "shout"=>$shout, "time"=>$time);
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
	$response["message"] = "Retrieve Shout Outs Successful!";
	$response["numShouts"] = $rowCount;
	$response["shouts"] = $shoutArray;
	echo json_encode($response);
}
else
{
?>
		<h1>Get Shouts</h1> 
		<form action="get_shouts.php" method="post"> 
		    LobbyId:<br /> 
		    <input type="text" name="lobbyId" placeholder="lobbyId" /> 
		    <input type="submit" value="Get Shouts" /> 
		</form> 
		<a href="register.php">Register</a>
<?php
}

?> 