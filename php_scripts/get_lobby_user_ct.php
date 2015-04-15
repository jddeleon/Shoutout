<?php
/*
 * The following code will retrieve the number of
 * users in the lobby specified in lobbyId
 * from the database and return the list in a JSON
 */

// connect to the database
require("db_connect.php");

// array for JSON response and set success to FALSE
$response = array();
$response["success"] = 0;
$rowCount = 0;

// check for lobbyId and store
if (!empty($_POST))
{
        $lobbyId = $_POST["lobbyId"];


// Count users in lobby
try
{        
        $stmt = $db->prepare("SELECT * FROM lobby_users WHERE lobby_id = ?");
        $stmt->bind_param("s", $lobbyId);
        $stmt->execute();
        while ($stmt->fetch())
        {        
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
$response["message"] = "Retrieve User Count Successful!";
$response["numUsers"] = $rowCount;
echo json_encode($response);
}
else
{
?>
		<h1>Get Lobby Population</h1> 
		<form action="get_lobby_user_ct.php" method="post"> 
		    LobbyID:<br /> 
		    <input type="text" name="lobbyId" placeholder="Lobby ID" value="" /> 
		    <br /><br /> 
		    <input type="submit" value="Get Users" /> 
		</form> 
		<a href="register.php">Register</a>
<?php
}

?> 

