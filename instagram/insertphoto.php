<?php 
$Con=mysqli_connect("localhost","root","","instagram");
if(mysqli_connect_errno())
{
	echo"failed to connect".mysqli_connect_error();
	
}

else
{
	$useremail = $_POST["email"];
	$image = $_POST["img"];
	$current_date = $_POST["date"];
	$image = $image .".jpg";
	$query="SELECT * FROM users WHERE email='$useremail'";
	  
	if($stmt=$Con->prepare($query))
		{
	
		if($stmt->execute())
		{
				$stmt->store_result();
			$count = $stmt->num_rows();
		
			if($count==1)
			{
				$stmt->bind_result($id,$user_email,$user_password,$user_name,$profilepic,$status);
				while($stmt->fetch())
				{
					$temp = array ('id'=>$id);
					$userinfo = array('response'=>'success', 'user'=>$temp);
					echo json_encode($userinfo);
				}
			}
			else
			{
				$userinfo['response']="not found";
				echo json_encode($userinfo);
			}
			echo $id;
			
			
			
	$query="INSERT INTO posts (id,postimage,posttext,dates) VALUES ('$id','$image','hello','$current_date')";
	  
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
		else
		{
			echo"execute failed";
		}
	}
	else
	{
		echo"prepare failed";
	}
	
	}
?>