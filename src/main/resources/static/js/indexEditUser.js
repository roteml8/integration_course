

var user;
function edituser(){
	var username;
	var person = { 
	        key:{ smartspace:getQueryVariable('usersmartspace'),email:getQueryVariable('useremail')},
	        role:$("#role").val(),
	        username:$("#username").val(),
	        avatar:$("#avatar").val(),
	        points: $("#points").val()
	    }

	 $.ajax({
	     url: '/smartspace/users/login'+'/'+getQueryVariable('usersmartspace')+'/'+ getQueryVariable('useremail'),
	     type: 'PUT',
	     dataType: 'json',
	     contentType: 'application/json',
	     success: function (data) {
	    	// $('#target').html(data.msg);
	    username=data.username;
	     },
	     data: JSON.stringify(person)
	 });
	if(!username)
	 document.location.href="http://localhost:8089/indexHome.html?"+"username="+username+"&usersmartspace="
   	 +getQueryVariable('usersmartspace')+"&role="+"MANAGER"+"&useremail="+getQueryVariable('useremail');

}



window.onload=function (){

	 var points = document.getElementById("points");
	 points.style.display = 'none';
	$.ajax({
		  url: '/smartspace/users/login'+'/'+getQueryVariable('usersmartspace')+'/'+ getQueryVariable('useremail'),
		  type: 'GET',
		  data:'',
		  success: function(data) {
			//called when successful
			  user=data;
			  loaduser(data);	 
		  },
		  error: function(e) {			  
			  }
		
		});
}


function loaduser(data){
	document.getElementById('useremail').value=data.key.email;

	document.getElementById('username').value=data.username;

	document.getElementById('role').value=data.role;

	document.getElementById('avatar').value=data.avatar;
	document.getElementById('points').value=data.points;

}



function getQueryVariable(variable){
       var query = window.location.search.substring(1);
       var vars = query.split("&");
       for (var i=0;i<vars.length;i++) {
               var pair = vars[i].split("=");
               if(pair[0] == variable){return pair[1];}
       }
       return(false);
}
