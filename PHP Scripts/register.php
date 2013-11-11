<?php

/*
 * Following code will create a new user in the DB
 * All user details except date_created and date_modified are 
 * read from HTTP Post Request
 */

// connect to the database
require("db_connect.php");

// array for JSON response and set success to FALSE
$response = array();
$response["success"] = 0;

// check for required fields
if (!empty($_POST))
{
		
	if (empty($_POST['regFirst']) || empty($_POST['regLast']) || empty($_POST['regUsername']) || 
		empty($_POST['regPassword']) || empty($_POST['regConfirmPass']) || empty($_POST['regEmail']))
	{
		
		// Create some data that will be the JSON response 
		$response["message"] = "Please complete all required fields";
		
		//die will kill the page and not execute any code below, it will also
		//display the parameter... in this case the JSON data our Android
		//app will parse
		die(json_encode($response));
	}
  	
	// else all required fields have been entered
	$regFirst = trim($_POST['regFirst']);
	$regLast = trim($_POST['regLast']);
	$regUsername = trim($_POST['regUsername']);
	$regPassword = $_POST['regPassword'];
	$regConfirmPass = $_POST['regConfirmPass'];
	$regEmail = trim($_POST['regEmail']);
	$regMajor = trim($_POST['regMajor']);
	$regGender = trim($_POST['regGender']);
	$regAge = trim($_POST['regAge']);
	$date_created = date('Y-m-d H:i:s');
	$date_modified = date('Y-m-d H:i:s');
	
	// so now check to make sure all the fields are valid
	// First and Last name must only contain alpha chars and hyphens
	if (!preg_match('/^([A-Za-z-]+)$/', $regFirst) || !preg_match('/^([A-Za-z- ]+)$/', $regLast))
	{
		$response["message"] = "Name contains invalid characters";
		die(json_encode($response));
	}
	if (strlen($regFirst) > 255)
	{
		$response["message"] = "First Name is too long";
		die(json_encode($response));
	}
	if (strlen($regLast) > 255)
	{
		$response["message"] = "Last Name is too long";
		die(json_encode($response));
	}
	if (strlen($regMajor) > 255)
	{
		$response["message"] = "Major is too long";
		die(json_encode($response));
	}
	if (strlen($regEmail) > 255)
	{
		$response["message"] = "Email is too long";
		die(json_encode($response));
	}
	if ((strlen($regUsername) > 20) || (strlen($regUsername) < 6) )
	{
		$response["message"] = "Username must be 6 to 20 characters";
		die(json_encode($response));
	}
	if (!preg_match('/^([A-Za-z0-9-_]+)$/', $regUsername) )
	{
		$response["message"] = "Username contains invalid characters";
		die(json_encode($response));
	}
    if(!preg_match('/\A[\w+\-.]+@[a-z\d\-]+(\.[a-z]+)*\.[a-z]+\z/i', $regEmail))
    {
        $response["message"] = "Email is not valid";
        die(json_encode($response));
    }
	if ((strlen($regPassword) > 255) || (strlen($regPassword) < 8))
	{
		$response["message"] = "Password must be 8 to 255 characters";
		die(json_encode($response));
	}
	if ($regPassword != $regConfirmPass)
	{
		$response["message"] = "Passwords don't match!";
		die(json_encode($response));
	}
	if (empty($regMajor))
	{
		$regMajor = NULL;
	}
	if (($regMajor != NULL) && (!preg_match('/^([A-Za-z- ]+)$/', $regMajor)))
	{
		$response["message"] = "Major contains invalid characters";
		die(json_encode($response));
	}
	if (empty($regAge))
	{
		$regAge = NULL;
	}
	if (($regAge != NULL) && ((!is_numeric($regAge)) || ($regAge > 100)))
	{
		$response["message"] = "Age is not valid";
		die(json_encode($response));
	}
	if ($regGender == "Male")
	{
		$regGender = 1;
	}
	elseif ($regGender == "Female")
	{
		$regGender = 0;
	}
	else // Decline to Say
	{
		$regGender = NULL;
	}
	$regFirst = ucwords($regFirst);
	$regLast = ucwords($regLast);

    
	// If we've made it this far, then the input must be valid. Now, check to see if there is already a 
	// matching username or email in the database
	
    // Check to see if username is already in the DB. 
    try 
	{
		$stmt = $db->prepare("SELECT 1 FROM user WHERE username = ?");
		$stmt->bind_param("s", $regUsername);
		$stmt->execute();
		$row = $stmt->fetch();
		$stmt->close();
    }
    catch (mysqli_exception $ex) 
	{
        $response["success"] = 0;
        $response["message"] = "Database Error1. Please Try Again!";
        die(json_encode($response));
    }

	// If any data is returned,
    //we know that the username is already in use	
    if ($row) 
	{
        $response["success"] = 0;
        $response["message"] = "I'm sorry, this username is already in use";
        die(json_encode($response));
    }

    // Check to see if email is already in the DB. 
    try 
	{
		$stmt = $db->prepare("SELECT 1 FROM user WHERE email = ?");
		$stmt->bind_param("s", $regEmail);
		$stmt->execute();
		$row = $stmt->fetch();
		$stmt->close();

    }
    catch (mysqli_exception $ex) 
	{
        $response["success"] = 0;
        $response["message"] = "Database Error1. Please Try Again!";
        die(json_encode($response));
    }

	// If any data is returned,
    //we know that the Email is already in use	
    if ($row) 
	{
        $response["success"] = 0;
        $response["message"] = "This Email is already registered!";
        die(json_encode($response));
    }
	
    // If we have made it here without dying, then we are in the clear to 
    // create a new user
    // try to create the user
    try 
	{	
		$stmt = $db->prepare("INSERT INTO user VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, null)");
		$stmt->bind_param("ssssssiiss", $regFirst, $regLast, $regUsername, $regPassword, 
							$regEmail, $regMajor, $regGender, $regAge, $date_created, $date_modified);
		$stmt->execute();
		$row = $stmt->fetch();
		$stmt->close();

    }
    catch (mysqli_exception $ex)
	{	
        $response["success"] = 0;
        $response["message"] = "Database Error2. Please Try Again!";
        die(json_encode($response));
    }
	
    // If we have made it this far without dying, we have successfully added
    // a new user to our database.
    $response["success"] = 1;
    $response["message"] = "Registration Successful!";
    echo json_encode($response);
  
  
} else {
?>
	<h1>Register</h1> 
	<form action="register.php" method="post"> 
	    First Name:<br /> 
	    <input type="text" name="regFirst" value="" /> 
	    <br /><br />
        Last Name:<br /> 
	    <input type="text" name="regLast" value="" /> 
	    <br /><br /> 
        Username:<br /> 
	    <input type="text" name="regUsername" value="" /> 
	    <br /><br /> 
        Password:<br /> 
	    <input type="text" name="regPassword" value="" /> 
	    <br /><br />
        Confirm Password:<br /> 
	    <input type="text" name="regConfirmPass" value="" /> 
	    <br /><br /> 
        Email:<br /> 
	    <input type="text" name="regEmail" value="" /> 
	    <br /><br /> 
        Major:<br /> 
	    <input type="text" name="regMajor" value="" /> 
	    <br /><br /> 
        Gender:<br /> 
	    <input type="radio" name="regGender" value="Male" />Male 
        <input type="radio" name="regGender" value="Female" />Female
        <input type="radio" name="regGender" value="Decline to Say" />Decline to Say
	    <br /><br />
        Age:<br /> 
	    <input type="text" name="regAge" value="" /> 
	    <br /><br /> 
	    <input type="submit" value="Register New User" /> 
	</form>
	<?php
}

?>
