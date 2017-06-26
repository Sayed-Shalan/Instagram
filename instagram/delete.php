<?php 
$Con=mysqli_connect("localhost","root","","instagram");
if(mysqli_connect_errno())
{
	echo"failed to connect".mysqli_connect_error();
	
}

else
{
	$imgName = $_POST["imgName"];

	$query="DELETE FROM posts WHERE postimage ='$imgName'";
	  
		if($stmt=$Con->prepare($query))
		{
	
		if($stmt->execute())
		{
				$stmt->store_result();
				$userinfo['response']="Done";
				echo json_encode($userinfo);
			}
			else
			{
				$userinfo['response']="not Executed";
				echo json_encode($userinfo);
			}
		}
	
	else
	{
		echo"prepare failed";
	}
}
?>