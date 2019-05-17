function toggleSignup(){
   document.getElementById("login-toggle").style.backgroundColor="#fff";
    document.getElementById("login-toggle").style.color="#222";
    document.getElementById("signup-toggle").style.backgroundColor="#57b846";
    document.getElementById("signup-toggle").style.color="#fff";
    document.getElementById("login-form").style.display="none";
    document.getElementById("signup-form").style.display="block";
    
}

function login() {
	 var person = {
			 userSmartspace: $("#userSmartspace").val(),
			 userEmail:$("#userEmailogin").val(),
	            
	        }

	 
	 $.ajax({
		  url: '/smartspace/users/login'+'/'+$("#userSmartspace").val()+'/'+ $("#userEmailogin").val(),
		  type: 'GET',
		  data:'',
		  success: function(data) {
			//called when successful
	
		 document.location.href="http://localhost:8089/indexHome.html?"+"username="+data.username+"&usersmartspace="
		 +$("#userSmartspace").val()+"&role="+data.role+"&useremail="+$("#userEmailogin").val();
			//$('#btnlogin').html(data);
		  },
		  error: function(e) {
			  $('#btnlogin').val="ff";
			  }
		
		});

	 
	 
	 
	 
}


function Signup() {
	 var person = {
	            email: $("#emailSign").val(),
	            username:$("#userNameSign").val(),
	            role:$("#role").val(),
	              avatar:$("#avatarSign").val()
	        }

    $.ajax({
        url: '/smartspace/users',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            $('#target').html(data.msg);
        },
        data: JSON.stringify(person)
    });
}


function toggleLogin(){
    document.getElementById("login-toggle").style.backgroundColor="#57B846";
    document.getElementById("login-toggle").style.color="#fff";
    document.getElementById("signup-toggle").style.backgroundColor="#fff";
    document.getElementById("signup-toggle").style.color="#222";
    document.getElementById("signup-form").style.display="none";
    document.getElementById("login-form").style.display="block";
}