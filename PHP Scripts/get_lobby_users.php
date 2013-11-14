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

// check for lobbyId and store
if (!empty($_POST))
{
        $lobbyId = $_POST["lobbyId"];
}


// Retrieve a list of usernames from the DB
try
{        
        $stmt = $db->prepare("SELECT username FROM lobby_users WHERE lobby_id = ?");
        $stmt->bind_param("s", $lobbyId);
        $stmt->execute();
        while ($stmt->fetch())
        {        
                $userArray[] = array("username"=>$username);
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
echo json_encode($response);


?>
