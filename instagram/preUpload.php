<?php
    //this function returns a random 5-char filename with the jpg extension
	

	$FileName = $_POST["FileNme"];
	$target = 'instaimage/';
    $target = $target . $FileName. ".jpg";
	
 if(move_uploaded_file($_FILES['image']['tmp_name'], $target))

 {
	 
	 echo json_encode(array ("response"=>"sucsses"));
	
}
else {
echo json_encode(array ("response"=>"false"));

}

?>