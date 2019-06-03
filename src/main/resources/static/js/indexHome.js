var drake=dragula([
	document.getElementById('b1'),
	document.getElementById('b2'),
    document.getElementById('b3'),
    document.getElementById('b4')
])

// Scrollable area
var allelements;
var element = document.getElementById("boards"); // Count Boards
var numberOfBoards = element.getElementsByClassName('board').length;
var boardsWidth = numberOfBoards*316 // Width of all Boards
element.style.width = boardsWidth+"px"; // set Width

document.getElementById("elementselectType").disabled=false;
// disable text-selection



window.onload=function (){
	
	 var but = document.getElementById("buttonsmannager");
	 var but = document.getElementById("buttonsmannager");
	 var archive = document.getElementById("board4");
	 var add = document.getElementById("contact");
	 var points = document.getElementById("board5");
	 add.style.display = 'none'; 
	 archive.style.display = 'none'; 
	 points.style.display = 'none';
	 but.style.display = 'none';
	 if(getQueryVariable('role')=="ADMIN"){
		       
		 $.ajax({
			  url: '/smartspace/admin/elements/'+getQueryVariable('usersmartspace')+'/'+getQueryVariable('useremail')+
			  "?"+'page='+0+'&size='+ 20,
			  type: 'GET',
			  data:'',
			  success: function(data) {
				//called when successful
				  allelements=data;
				  loadCards(data);
				  loadElementsSelect(data);
				  loadElementsSelectName(data);
			  },
			  error: function(e) {  }
			
			});
	}
	 else {
		if(getQueryVariable('role')=="MANAGER"){   
		 archive.style.display = 'block';
		 but.style.display = 'block';
		}
		else {points.style.display = 'block';
		loadpoints();
		
		}		
		
		 $.ajax({
			  url: '/smartspace/elements/'+getQueryVariable('usersmartspace')+'/'+getQueryVariable('useremail')+
			  "?"+'page='+0+'&size='+ 20,
			  type: 'GET',
			  data:'',
			  success: function(data) {
				//called when successful
				  console.log(data);
				  allelements=data;
				  loadCards(data);
		           loadElementsSelect(data);
		           loadElementsSelectName(data);
			  },
			  error: function(e) {			  
				  }
			
			});
		
	}

}



function loadplayerspoints(data){
	var temp;
	 var arrayVariable = [];
	 var arrayVariable2 = []; 
	 for(i=0;i<data.length;i++){
		 arrayVariable.push(data[i].name);
		 arrayVariable2.push(data[i].points);
	 }
	  var  arrayLength = arrayVariable.length;
	

	 for(i = 0; i < arrayLength; i++) {
	   temp = document.createElement('div');
	   temp.className = 'card';
	   temp.id=data[i].key.id;
	   temp.innerHTML += "<span class="+"cardtitle >"+ arrayVariable[i]+"points: "+arrayVariable2[i]+"</span>";
	   document.getElementById('b5').appendChild(temp);
	 }
}


function loadpoints(){
	
	var element;
	 $.ajax({
		  url: '/smartspace/elements/'+getQueryVariable('usersmartspace')+'/'+getQueryVariable('useremail')+'/'+ 
		  "?search=type"+'&value='+"SCORE_BOARD"+'&page='+0+'&size='+ 5,
		  type: 'GET',
		  data:'',
		  success: function(data) {
			//called when successful
				console.log(data);
		    element=data[0];
		  },
			  error: function(e) {			  
			  }
		
		});
	

	 var person = { 
		        type: "UpdateScoreBoard",
		     element:{id:element.key.id,smartspace:element.key.smartspace},
		        player: {smartspace:getQueryVariable('usersmartspace'),email:getQueryVariable('useremail')},
		          properties:{}
		    }
	  
	  
	  $.ajax({
	        url: '/smartspace/actions',
	        type: 'post',
	        dataType: 'json',
	        contentType: 'application/json',
	        success: function (data) {
	        	console.log('dd');
	        	console.log(data);
	        loadplayerspoints(data);	
	            $('#target').html(data.msg);
	        },
	        data: JSON.stringify(person)
	    });  
	
}



function loadElementsSelectName(data){
	var temp;
	 var arrayVariable = [];
	 for(i=0;i<data.length;i++){
		 arrayVariable.push(data[i].name);
	 }
	  
	

	  var arrayVariable2 = [];
	  $.each(arrayVariable, function(i, el){
	      if($.inArray(el, arrayVariable2) === -1) arrayVariable2.push(el);
	  });
	  var  arrayLength = arrayVariable2.length;
	  for (i = 0; i < arrayLength; i++) {
		 
	 
		 document.getElementById('elementselectName').innerHTML += "<option class="+"delq id="+data[i].key.id+">"+arrayVariable2[i]+"</option>";
	  
	  
	 }
}

function loadElementsSelect(data){
	
	
		var temp;
		 var arrayVariable = [];
		 for(i=0;i<data.length;i++){
			 
			 arrayVariable.push(data[i].elementType);
		 }
		 
		 
		 
		 var arrayVariable2 = [];
		  $.each(arrayVariable, function(i, el){
		      if($.inArray(el, arrayVariable2) === -1) arrayVariable2.push(el);
		  });
		  var  arrayLength = arrayVariable2.length;
		 
		

		 for (i = 0; i < arrayLength; i++) {
			 console.log(arrayVariable[i]);
		 
			 document.getElementById('elementselectType').innerHTML += "<option class="+"delq id="+data[i].key.id+">"+arrayVariable[i]+"</option>";
		  
		  
		 }
			
	
}



function loadCards(data){
	var temp;
	 var arrayVariable = [];
	 for(i=0;i<data.length;i++){
		 arrayVariable.push(data[i].name);
	 }
	  var  arrayLength = arrayVariable.length;
	

	 for (i = 0; i < arrayLength; i++) {
	   temp = document.createElement('div');
	   temp.className = 'card';
	  
	   temp.id=data[i].key.id;
	   temp.innerHTML += "<span class="+"cardtitle >"+ arrayVariable[i]+"</span>";
	   temp.ondragover='false'
	   if(data[i].latlng.lat==0)
	   document.getElementById('b1').appendChild(temp);
	   else if(data[i].latlng.lat==1)
		   document.getElementById('b2').appendChild(temp);
	   else if(data[i].latlng.lat==2)
		   document.getElementById('b3').appendChild(temp);
	   else if(getQueryVariable('role')=="MANAGER"&&data[i].latlng.lat==3)
			   document.getElementById('b4').appendChild(temp);
	 }
}


function remove(myNode){
	
while (myNode.firstChild) {
    myNode.removeChild(myNode.firstChild);
}
}




function startSelect(){
	var smart;
	
	 var selector = document.getElementById('elementselectType');
	 if(selector[selector.selectedIndex])
	 var value = selector[selector.selectedIndex].text;
	   
	    var arrayVariable = [];
	    var myNode = document.getElementById('b1');
	    remove(myNode);
	    var myNode2 = document.getElementById('b2'); 
	    remove(myNode2);
	    var myNode3 = document.getElementById('b3');
	    remove(myNode3);
	    var myNode4 = document.getElementById('b4');
	    remove(myNode4);
	    
	    if(value=="All--type"){
	    	loadCards(allelements);
	    }else{
			 $.ajax({
				  url: '/smartspace/elements/'+getQueryVariable('usersmartspace')+'/'+getQueryVariable('useremail')+'/'+ 
				  "?search=type"+'&value='+value+'&page='+0+'&size='+ 20,
				  type: 'GET',
				  data:'',
				  success: function(data) {
					//called when successful
	   				 
					  var temp;
						console.log(data);
					 
					

					 for (i = 0; i < data.length; i++) {
					   temp = document.createElement('div');
					   temp.className = 'card';
					  
					   temp.id=data[i].key.id;
					   temp.innerHTML += "<span class="+"cardtitle >"+ data[i].name+"</span>";
					   temp.ondragover='false'
					   if(data[i].latlng.lat==0)
					   document.getElementById('b1').appendChild(temp);
					   else if(data[i].latlng.lat==1)
						   document.getElementById('b2').appendChild(temp);
					   else if(data[i].latlng.lat==2)
						   document.getElementById('b3').appendChild(temp);
					   else if(getQueryVariable('role')=="MANAGER"&&data[i].latlng.lat==3)
							   document.getElementById('b4').appendChild(temp);
					 }
		           
			    
					 
				  },
	    			  error: function(e) {			  
					  }
				
				});

}

}



function startSelectName(){
	var smart;
	
	 var selector = document.getElementById('elementselectName');
	 if(selector[selector.selectedIndex])
	 var value = selector[selector.selectedIndex].text;
	   
	    var arrayVariable = [];
	    var myNode = document.getElementById('b1');
	    remove(myNode);
	    var myNode2 = document.getElementById('b2'); 
	    remove(myNode2);
	    var myNode3 = document.getElementById('b3');
	    remove(myNode3);
	    var myNode4 = document.getElementById('b4');
	    remove(myNode4);
	    
	    if(value=="All--name"){
	    	loadCards(allelements);
	    }else{
			 $.ajax({
				  url: '/smartspace/elements/'+getQueryVariable('usersmartspace')+'/'+getQueryVariable('useremail')+'/'+ 
				  "?search=name"+'&value='+value+'&page='+0+'&size='+ 20,
				  type: 'GET',
				  data:'',
				  success: function(data) {
					//called when successful
	   				 
					  var temp;
						console.log(data);
					 
					

					 for (i = 0; i < data.length; i++) {
					   temp = document.createElement('div');
					   temp.className = 'card';
           					  
					   temp.id=data[i].key.id;
					   temp.innerHTML += "<span class="+"cardtitle >"+ data[i].name+"</span>";
					   temp.ondragover='false'
					   if(data[i].latlng.lat==0)
					   document.getElementById('b1').appendChild(temp);
					   else if(data[i].latlng.lat==1)
						   document.getElementById('b2').appendChild(temp);
					   else if(data[i].latlng.lat==2)
						   document.getElementById('b3').appendChild(temp);
					   else if(getQueryVariable('role')=="MANAGER"&&data[i].latlng.lat==3)
							   document.getElementById('b4').appendChild(temp);
					 }
		           
			    
					 
				  },
	    			  error: function(e) {			  
					  }
				
				});
	    }
}

$('#elementselectType').change(startSelect);

$('#elementselectName').change(startSelectName);




function getQueryVariable(variable)
{
       var query = window.location.search.substring(1);
       var vars = query.split("&");
       for (var i=0;i<vars.length;i++) {
               var pair = vars[i].split("=");
               if(pair[0] == variable){return pair[1];}
       }
       return(false);
}





function add(){
	
	if(getQueryVariable('role')=="MANAGER")
		{
		 document.location.href="http://localhost:8089/indexAdd.html?"+"managersmartspace="
		 +getQueryVariable('usersmartspace')+"&managername="+getQueryVariable('username')+
		 "&manageremail="+getQueryVariable('useremail');
		
		}
}


function editelement(){
	if(getQueryVariable('role')=="MANAGER")
	{
	 document.location.href="http://localhost:8089/indexEdit.html?"+"managersmartspace="
	 +getQueryVariable('usersmartspace')+"&managername="+getQueryVariable('username')+
	 "&manageremail="+getQueryVariable('useremail');
	
	}
}



function edituser(){
	 document.location.href="http://localhost:8089/indexEditUser.html?"+"usersmartspace="
	 +getQueryVariable('usersmartspace')+
	 "&useremail="+getQueryVariable('useremail');
	
}


function newStatus(target) {
    switch (target.id) {
      case 'b1': return 0;
      case 'b2': return 1;
      case 'b3': return 2;
      case 'b4': return 3;
    }
  }




drake.on('drop', function(el, target) {
	 
	var element ;
	for(i=0;i<allelements.length;i++){
		if(allelements[i].key.id==el.id){
			element=allelements[i];
		}
	}
	
  if(getQueryVariable('role')=="MANAGER"){
var bool ;
if(target.id=='b4')
	bool=true;
else bool=false; 

element.latlng.lat=newStatus(target);
var person = { 
        elementType: element.elementType,
        name:element.name,
        expired:bool,
        created:new Date(),
        creator: {email:element.creator.email,smartspace:element.creator.smartspace},
        latlng:{lat:element.latlng.lat,lng:element.latlng.lng},
          elementProperties:{lastkey:element.elementProperties.lastkey}
    }



var smart=element.key.smartspace;
$.ajax({
  url: '/smartspace/elements'+'/'+getQueryVariable('usersmartspace')+'/'+getQueryVariable('useremail')+'/'+
  smart+'/'+element.key.id,
  type: 'put',
  dataType: 'json',
  contentType: 'application/json',
  success: function (data) {
      $('#target').html(data.msg);
  },
  data: JSON.stringify(person)
});
  
  }else if(getQueryVariable('role')=="PLAYER"){
	 var x= (newStatus(target)).toFixed(2);
         
	  var person = { 
		        type: "UpdateTaskStatus",
		     element:{id:element.key.id,smartspace:element.key.smartspace},
		        player: {smartspace:getQueryVariable('usersmartspace'),email:getQueryVariable('useremail')},
		          properties:{location:x,dealine:element.elementProperties.duration }
		    }
	  
	  
	  

	  
	  $.ajax({
	        url: '/smartspace/actions',
	        type: 'post',
	        dataType: 'json',
	        contentType: 'application/json',
	        success: function (data) {
	        	
	            $('#target').html(data.msg);
	        },
	        data: JSON.stringify(person)
	    });  
  }

});
