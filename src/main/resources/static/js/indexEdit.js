var allelements;
var elementId;




window.onload=function (){
	$.ajax({
		  url: '/smartspace/elements'+'/'+getQueryVariable('managersmartspace')+'/'+ getQueryVariable('manageremail'),
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


function loadinputbyelement(data){
	console.log('ff');
	document.getElementById('elementname').value=data.name;

	document.getElementById('elementtype').value=data.elementType;

	document.getElementById('x').value=data.latlng.lat;

	document.getElementById('y').value=data.latlng.lng;

	document.getElementById('expired').value=data.expired;
	document.getElementById('useremail').value=data.elementProperties.useremail;
	document.getElementById('useremail2').value=data.elementProperties.useremail2;

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
function edit(){


	var bool ;
	var x;
	if($("#x").val()==4||$("#expired").val()=="true"){
		bool=true;
		x=4;
	}else{
		bool=false;
		x=$("#x").val();	
	}
	
	var person = { 
            elementType: $("#elementtype").val(),
            name:$("#elementname").val(),
            expired:bool,
            created:new Date(),
            creator: {email:getQueryVariable('manageremail'),smartspace:getQueryVariable('managersmartspace')},
            latlng:{lat:x,lng:$("#y").val()},
              elementProperties:{location:allelements[elementId].elementProperties.location
            ,useremail:$("#useremail").val(),useremail2:$("#useremail2").val()}
        }
 var user;
	$.ajax({
		  url: '/smartspace/elements'+'/'+getQueryVariable('managersmartspace')+'/'+getQueryVariable('manageremail')+'/'+
		  allelements[elementId].key.smartspace +'/'+allelements[elementId].key.id,
		  type: 'put',
		  dataType: 'json',
		  contentType: 'application/json',
		  success: function (data) {
			  
		   //   $('#target').html(data.msg);
		     user =data.usernme;
		  },
		  data: JSON.stringify(person)
		});
	if(!user)
	document.location.href="http://localhost:8089/indexHome.html?"+"username="+user+"&usersmartspace="
	 +getQueryVariable('managersmartspace')+"&role="+"MANAGER"+"&useremail="+getQueryVariable('manageremail');
}

$('#elementselect').change(function() {
	var smart;
	 var selector = document.getElementById('elementselect');
	    var value = selector[selector.selectedIndex].id;
	for(i=0;i<allelements.length;i++){
		if(value==allelements[i].key.id)
		{elementId=i;
			st=i;
		}
	}
	
	$.ajax({
		  url: '/smartspace/elements/'+getQueryVariable('managersmartspace')+'/'+ getQueryVariable('manageremail')+
		  '/'+allelements[st].key.smartspace +'/'+allelements[st].key.id,
		  type: 'GET',
		  data:'',
		  success: function(data) {
			//called when successful
			console.log(data);
			  loadinputbyelement(data);	 
	
		  },
		  error: function(e) {			  
			  }
		
		}); 


});


function getQueryVariable(variable){
       var query = window.location.search.substring(1);
       var vars = query.split("&");
       for (var i=0;i<vars.length;i++) {
               var pair = vars[i].split("=");
               if(pair[0] == variable){return pair[1];}
       }
       return(false);
}
