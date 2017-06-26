<?php 
$Con=mysqli_connect("localhost","root","","instagram");
if(mysqli_connect_errno())
{
	echo"failed to connect".mysqli_connect_error();
	
}

else
{
	$userEmail = $_POST["email"];
	$userpass = $_POST["pass"];
    $username = $_POST["username"];
	$query="INSERT INTO users (email,password,username,profilepic,status) VALUES ('$userEmail','$userpass','$username','pp.png','welcome to instagram')";
	  
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