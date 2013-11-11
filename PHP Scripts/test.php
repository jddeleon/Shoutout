<?php 
	
	//create short variable names
	$name = trim($_POST['fullName']);
	
	
	//variables for connecting to db
	$dbhostname = "db.ecst.csuchico.edu";
	$dbusername = "jdeleon";
	$dbname = "shoutout";
	$dbpass = "qWpeQGPh";
	
	// Connecting to your database
	$db = mysql_connect($dbhostname,$dbusername, $dbpass) or die ("<html><script language='JavaScript'>alert('Unable to
	 connect to database! Please try again later.'),history.go(-1)</script></html>");
	mysql_select_db($dbname);


//if posted data is not empty
if (!empty($_POST)) {
    //If the username or password is empty when the user submits
    //the form, the page will die.
    //Using die isn't a very good practice, you may want to look into
    //displaying an error message within the form instead.  
    //We could also do front-end form validation from within our Android App,
    //but it is good to have a have the back-end code do a double check.
    if (empty($_POST['fullName'])) {
        
        // Create some data that will be the JSON response 
        $response["success"] = 0;
        $response["message"] = "Please Enter a Name";
        
        //die will kill the page and not execute any code below, it will also
        //display the parameter... in this case the JSON data our Android
        //app will parse
        die(json_encode($response));
    }
	
    //if the page hasn't died, we will check with our database to see if there is
    //already a user with the username specificed in the form. 
    $query  = " SELECT 1 FROM test WHERE name = '$name'";
   

    //Now let's make run the query:
    try {
        $result   = mysql_query($query);
    }
    catch (PDOException $ex) {
        // For testing, you could use a die and message. 
        //die("Failed to run query: " . $ex->getMessage());
        //or just use this use this one to product JSON data:
        $response["success"] = 0;
        $response["message"] = "Database Error1. Please Try Again!";
        die(json_encode($response));
    }
    //fetch is an array of returned data.  If any data is returned,
    //we know that the username is already in use, so we murder our
    //page
    $row = mysql_fetch_array($result);
	
    if ($row) {
        // For testing, you could use a die and message. 
        //die("This username is already in use");
        //You could comment out the above die and use this one:
        $response["success"] = 0;
        $response["message"] = "I'm sorry, this username is already in use";
        die(json_encode($response));
    }
    //If we have made it here without dying, then we are in the clear to 
    //create a new user.  Let's setup our new query to create a user.  
    //Again, to protect against sql injects, user tokens such as :user and :pass
    $query = "INSERT INTO test VALUES ('$name')";
    
    //time to run our query, and create the user
    try {
        $result = mysql_query($query);
    }
    catch (PDOException $ex) {
        // For testing, you could use a die and message. 
        //die("Failed to run query: " . $ex->getMessage());
        
        //or just use this use this one:
        $response["success"] = 0;
        $response["message"] = "Database Error2. Please Try Again!";
        die(json_encode($response));
    }
    
    //If we have made it this far without dying, we have successfully added
    //a new user to our database.  We could do a few things here, such as 
    //redirect to the login page.  Instead we are going to echo out some
    //json data that will be read by the Android application, which will login
    //the user (or redirect to a different activity, I'm not sure yet..)
    $response["success"] = 1;
    $response["message"] = "Username Successfully Added!";
    echo json_encode($response);
    
    //for a php webservice you could do a simple redirect and die.
    //header("Location: login.php"); 
    //die("Redirecting to login.php");
    
} else {
?>
	<h1>Register</h1> 
	<form action="test.php" method="post"> 
	    Username:<br /> 
	    <input type="text" name="fullName" value="" /> 
	    <br /><br /> 
	    <input type="submit" value="Register New User" /> 
	</form>
	<?php
}
?>
