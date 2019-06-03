var allelements;
window.onload=function (){
	$.ajax({
		  url: '/smartspace/elements'+'/'+getQueryVariable('usersmartspace')+'/'+ getQueryVariable('useremail'),
		  type: 'GET',
		  data:'',
		  success: function(data) {
			//called when successful
			  allelements=data;
			  loadelements(data);	 
		  },
		  error: function(e) {			  
			  }
		
		});
}




function loadelements(data){
	var temp;
	 var arrayVariable = [];
	 for(i=0;i<data.length;i++){
		 arrayVariable.push(data[i].name);
	 }
	  var  arrayLength = arrayVariable.length;
	

	 for (i = 0; i < arrayLength; i++) {
	   
	 
		 document.getElementById('elementselect').innerHTML += "<option class="+"delq id="+data[i].key.id+">"+arrayVariable[i]+"</option>";
	  
	  
	 }
		
}


function send(){
	var id = $("#elementselect option:selected").attr("id")
	var element ;
	for(i=0;i<allelements.length;i++){
		if(allelements[i].key.id==id){
			element=allelements[i];
		}
	}
	console.log(element);
	
	 var person = { 
		        type: "MailToGetMoreInformation",
		     element:{id:element.key.id,smartspace:element.key.smartspace},
		        player: {smartspace:getQueryVariable('usersmartspace'),email:getQueryVariable('useremail')},
		          properties:{message:$("#textArea").val()}
		    }
	  
	  
	  

	  
	  $.ajax({
	        url: '/smartspace/actions',
	        type: 'post',
	        dataType: 'json',
	        contentType: 'application/json',
	        success: function (data) {
	        
	        		 document.location.href="http://localhost:8089/indexHome.html?"+"&usersmartspace="
	        	   	 +getQueryVariable('usersmartspace')+"&useremail="+getQueryVariable('useremail');

	            $('#target').html(data.msg);
	        },
	        data: JSON.stringify(person)
	    });  
	
	
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