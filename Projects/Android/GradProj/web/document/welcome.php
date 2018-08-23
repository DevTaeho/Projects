<meta http-equiv="content-type" content="text/html"; charset="utf-8">

<?php

  session_start();

  $dbhost = 'localhost';
  $dbuser = 'root';
  $dbname = "cafe";
  $dbpwd = "dudrnr63";

  $conn = mysqli_connect($dbhost, $dbuser, $dbpwd, $dbname);

  if(!$conn){
    echo "Error : Unable connect to database...!";
  }

  mysqli_query($conn, "set names utf8");


  $id = $_SESSION['id'];

  $query = "SELECT * FROM login";
  $result = mysqli_query($conn, $query);

  if($result){

    while($row = mysqli_fetch_assoc($result)){
        $re_row = (string)$row["id"];
        $re_name = (string)$row["name"];
        $str = strcmp($id, $re_row);

        if($str==0){
            $showid = $re_row;
            $name = $re_name;
        }
    }
  }

  mysqli_close();
?>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title></title>
  </head>
  <body>
    환영합니다 <?php echo $name; ?> 님
  </body>
</html>
