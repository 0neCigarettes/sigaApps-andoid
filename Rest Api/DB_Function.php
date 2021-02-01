<?php

class DB_Functions {

    private $conn;

    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // koneksi ke database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {

    }


    public function GetListBank(){
        // $mysqli = new mysqli("localhost", "root", "", "juli_sisfordik");
        //
        // /* check connection */
        // if (mysqli_connect_errno()) {
        //     printf("Connect failed: %s\n", mysqli_connect_error());
        //     exit();
        // }

        $query = "SELECT * FROM tbl_bank";
        $result = $this->conn->query($query);
        $myArray   = array();
        while($row = $result->fetch_object())
        {
            $tempArray = $row;
            array_push($myArray, $tempArray);
        }

        echo json_encode(array('result'=>$myArray));

        /* free result set */
        $result->close();

        /* close connection */
        $mysqli->close();
    }
}

?>
