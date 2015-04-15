<?php
/*
 * The following code will retrieve a list of usernames
 * from the lobby specified in lobbyId from the database
 * and return the list in a JSON
 */

// connect to the database
require("db_connect.php");

// array for JSON response and set success to FALSE
$response = array();
$response["success"] = 0;
$userArray = array();
$rowCount = 0;

if (!empty($_POST))
{
	// check for lobbyId and store
	$lobbyId = $_POST["lobbyId"];

	// Retrieve a list of usernames from the DB
	try
	{       
		$stmt = $db->prepare("SELECT user.user_id, fname, lname, username, email, major, gender, age, thumb_url FROM user INNER JOIN lobby_users ON user.user_id = lobby_users.user_id AND lobby_id = ?");
		$stmt->bind_param("i", $lobbyId);	 
		$stmt->execute();
		$stmt->bind_result($userId, $fName, $lName, $username, $email, $major, $gender, $age, $thumbUrl); 
		while ($stmt->fetch())
		{    
			$userArray[] = array("userId"=>$userId, "fName"=>$fName, 
							"lName"=>$lName, "username"=>$username, "email"=>$email,
							"major"=>$major, "gender"=>$gender, "age"=>$age, "thumbUrl"=>$thumbUrl);
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
	$response["message"] = "Retrieve Usernames Successful!";
	$response["users"] = $userArray;
	$response["numUsers"] = $rowCount;
	
	echo json_encode($response);

}
else
{	
?>
		<h1>Get Lobby_Users</h1> 
		<form action="get_lobby_users.php" method="post"> 
		    LobbyID:<br /> 
		    <input type="text" name="lobbyId" placeholder="Lobby ID" value="" /> 
		    <br /><br /> 
		    <input type="submit" value="Get Users" /> 
		</form> 
		<a href="register.php">Register</a>
	<?php
}

?> 
