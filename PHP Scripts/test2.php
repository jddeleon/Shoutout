<?php

require ("db_connect.php");
	
//create short variable names
$name = trim($_POST['fullName']);

//if posted data is not empty
if (!empty($_POST))
{

    if (empty($_POST['fullName']))
	{

        // Create some data that will be the JSON response 
        $response["success"] = 0;
        $response["message"] = "Please Enter a Name";
        
        //die will kill the page and not execute any code below, it will also
        //display the parameter... in this case the JSON data our Android
        //app will parse
        die(json_encode($response));
    }

    // Check to see if username is already in the DB. 
    try 
	{
		$stmt = $db->prepare("SELECT 1 FROM test WHERE name = ?");
		$stmt->bind_param("s", $name);
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

    // If we have made it here without dying, then we are in the clear to 
    // create a new user
    
    // try to create the user
    try 
	{
		$stmt = $db->prepare("INSERT INTO test VALUES (?)");
		$stmt->bind_param("s", $name);
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
    $response["message"] = "Username Successfully Added!";
    echo json_encode($response);
 	
// The first time coming to the page should display the form, since the POST array will be empty   
} else {
?>
	<h1>Register</h1> 
	<form action="test2.php" method="post"> 
	    Username:<br /> 
	    <input type="text" name="fullName" value="" /> 
	    <br /><br /> 
	    <input type="submit" value="Register New User" /> 
	</form>
	<?php
}

?>