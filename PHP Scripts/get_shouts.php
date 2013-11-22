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
        $lobbyId = $_POST["lobbyId"];
}

// Retrieve the list of shouts from the DB
try
{        
        $stmt = $db->prepare("SELECT username, shout, created_at
			      FROM user
			      INNER JOIN shout
			      ON user.user_id = shout.user_id");
        $stmt->bind_param("s", $lobbyId);
        $stmt->execute();
        while ($stmt->fetch())
        {        
                $shoutArray[] = array("shout"=>$shout);
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
$response["shouts"] = $shoutArray;
echo json_encode($response);

?>
