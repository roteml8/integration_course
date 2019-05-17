var allelements;
function editelement(){
	var person = { 
            elementType: $("#elementtype").val(),
            name:$("#elementname").val(),
            expired:false,
            created:new Date(),
            creator: {email:getQueryVariable('manageremail'),smartspace:getQueryVariable('managersmartspace')},
            latlng:{lat:0.0,lng:$("#y").val()},
              elementProperties:{lastkey:$("#attribute").val()}
        }
 
	

	
}


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
	document.getElementById('elementname').value=data.name;

	document.getElementById('elementtype').value=data.elementType;

	document.getElementById('x').value=data.latlng.lat;

	document.getElementById('y').value=data.latlng.lng;

	document.getElementById('expired').value=data.expired;



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


$('#elementselect').change(function() {
	var smart;
	 var selector = document.getElementById('elementselect');
	    var value = selector[selector.selectedIndex].id;
	for(i=0;i<allelements.length;i++){
		if(value==allelements[i].key.id)
			st=i;;
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
