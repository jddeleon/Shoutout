<?php

/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['regFirst']) && isset($_POST['regLast']) && isset($_POST['regUsername']) && isset($_POST['regPassword']) 
		&& isset($_POST['regEmail'])) {
    
    $regFirst = $_POST['regFirst'];
	$regLast = $_POST['regLast'];
	$regUsername = $_POST['regUsername'];
	$regPassword = $_POST['regPassword'];
    $regEmail = $_POST['regEmail'];
	$regMajor = $_POST['regMajor'];
	$regGender = $_POST['regGender'];
	$regAge = $_POST['regAge'];
	$date_created = date('Y-m-d');
	$date_modified = date('Y-m-d');
	
    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql inserting a new row
    $result = mysql_query("INSERT INTO user(regFirst, regLast, regUsername, regPassword, regEmail, regMajor, regGender, age, date_created, date_modified) VALUES('$regFirst', '$regLast', '$regUsername', '$regPassword', '$regEmail', '$regMajor','$age', '$date_created', '$date_modified')");

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
        
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>
