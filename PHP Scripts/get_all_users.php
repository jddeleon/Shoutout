<?php
/*
 * Following code will retrieve a list of users
 * from the database and return the list in a JSON
 */

// connect to the database
require("db_connect.php");

// array for JSON response and set success to FALSE
$response = array();
$response["success"] = 0;
$userArray = array();
$rowCount = 0;

// Retrieve a list of lobbies from the DB
try 
{	
	$stmt = $db->prepare("SELECT user_id, fname, lname, username, email, major, gender, age, thumb_url FROM user");
	$stmt->bind_result($userId, $fName, $lName, $username, $email, $major, $gender, $age, $thumbUrl);
	$stmt->execute();
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
$response["message"] = "Retrieve Users Successful!";
$response["numUsers"] = $rowCount;
$response["users"] = $userArray;
echo json_encode($response);


?>