<?php

/**
 * File to handle all API requests
 * Accepts GET and POST
 *
 * Each request will be identified by TAG
 * Response will be JSON data

  /**
 * check for POST request
 */
if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // get tag
    $tag = $_POST['tag'];

    // include db handler
    require_once 'include/DB_Function.php';
	$db = new DB_Functions();

    // response Array
    $response = array("tag" => $tag, "error" => FALSE);

    if($tag == 'GetListBank'){
		 $db->GetListBank();
	}




//end IF Paling Atas
	else {
    // user failed to store
    $response["error"] = TRUE;
    $response["error_msg"] = "Request Tidak Valid";
    echo json_encode($response);
    }
}else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter 'tag' is missing!";
    echo json_encode($response);
}

?>
