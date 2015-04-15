<?php

//load and connect to DB
require("db_connect.php");

if (!empty($_POST))
{
	if (empty($_POST['logEmail']) || empty($_POST['logPassword']))
	{	
		$response["success"] = 0;
		$response["message"] = "Please complete all required fields";
		die(json_encode($response));
	}
	
	// else all required fields have been entered
	$logPassword = $_POST['logPassword'];
	$logEmail = $_POST['logEmail'];

	// Check to see if email and password match a row in the DB
    try 
	{
		$stmt = $db->prepare("SELECT user_id, fname FROM user WHERE email = ? AND password = ? ");
		$stmt->bind_param("ss", $logEmail, $logPassword);
		$stmt->execute();
		$stmt->store_result();
		$stmt->bind_result($id, $name);
		$row = $stmt->fetch();
    }
    catch (mysqli_exception $ex) 
	{
        $response["success"] = 0;
        $response["message"] = "Database Error1. Please Try Again!";
        die(json_encode($response));
    }
        
	
	// if there is a row in the table then we know there was a valid match
    if ($row) 
	{
        $response["success"] = 1;
        $response["message"] = "Welcome, ".$name."!";
		$response["userId"] = $id;
		
    }
    else 
	{
        $response["success"] = 0;
        $response["message"] = "Invalid Email or Password";
	}
	
	// return either success or failure
	die(json_encode($response));
	
} else {
?>
		<h1>Login</h1> 
		<form action="login.php" method="post"> 
		    Email:<br /> 
		    <input type="text" name="logEmail" placeholder="username" /> 
		    <br /><br /> 
		    Password:<br /> 
		    <input type="password" name="logPassword" placeholder="password" value="" /> 
		    <br /><br /> 
		    <input type="submit" value="Login" /> 
		</form> 
		<a href="register.php">Register</a>
	<?php
}

?> 
