<?php 
$Con=mysqli_connect("localhost","root","","instagram");
if(mysqli_connect_errno())
{
	echo"failed to connect".mysqli_connect_error();
	
}

else
{
	$feed = array();
	$query="SELECT * FROM posts ORDER BY post_id DESC";
	 		if($stmt=$Con->prepare($query))
		{
	
		if($stmt->execute())
		{
				$stmt->bind_result($post_id,$id,$postimage,$posttext,$dates);
				while($stmt->fetch())
				{
					$temp = array ('post_id'=>$post_id,
					'id'=>$id ,
					'post_image'=>$postimage,
					'post_text'=>$posttext,
					'date'=>$dates);
					array_push($feed,$temp);
				}
				$userinfo = array('response'=>'success', 'user'=>$feed);
			echo json_encode($userinfo);
				
				}
			else
			{
				$userinfo = array ('execute failed');
				echo json_encode($userinfo);
			}
		}
		
		else{
			echo "prepare failed";
		}

}
?>