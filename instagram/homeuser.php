<?php 
$Con=mysqli_connect("localhost","root","","instagram");
if(mysqli_connect_errno())
{
	echo"failed to connect".mysqli_connect_error();
	
}

else
{
$feed = array();
	$query="SELECT * FROM users ";
	  
		if($stmt=$Con->prepare($query))
		{
	
		if($stmt->execute())
		{
				$stmt->store_result();
			$count = $stmt->num_rows();
		
			
				$stmt->bind_result($id,$user_email,$user_password,$user_name,$profilepic,$status);
				while($stmt->fetch())
				{
					$temp = array ('id'=>$id, 'useremail'=>$user_email , 'userpassword'=>$user_password , 'username'=>$user_name,'profilepic'=>$profilepic,'status'=>$status);
					array_push($feed,$temp);
				}
			$userinfo = array('response'=>'success', 'user'=>$feed);
			echo json_encode($userinfo);
			
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