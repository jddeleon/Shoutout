<?php 
	// Contains code to connect to the database. Include this file in all other scripts that need
	// to connect to our DB. 
    // Replace <USERNAME>, <DBNAME>, <DBHOSTNAME>, <DBPASS> with actual values
	$dbhostname = "<DBHOSTNAME>";
	$dbusername = "<USERNAME>";
	$dbname = "<USERNAME>";
	$dbpass = "<DBPASS>"; 

     
	// try to connect to the database
    try 
    { 
        // try to create a new connection
        $db = new mysqli($host, $username, $password, $dbname); 
    } 
    catch(mysqli_sql_exception $ex) 
    { 
		// If an error occurred connecting to database, it is caught here. 
        die("Failed to connect to the database: " . $ex->getMessage()); 
    } 
     
    session_start(); 

?>
