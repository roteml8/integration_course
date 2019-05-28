function addelement(){

	var person = { 
            elementType: $("#elementtype").val(),
            name:$("#elementname").val(),
            expired:false,
            created:new Date(),
            creator: {email:getQueryVariable('manageremail'),smartspace:getQueryVariable('managersmartspace')},
            latlng:{lat:0.0,lng:$("#y").val()},
              elementProperties:{lastkey:$("#attribute").val()}
        }
 
$.ajax({
    url: '/smartspace/elements'+'/'+getQueryVariable('managersmartspace')+'/'+ getQueryVariable('manageremail'),
    type: 'post',
    dataType: 'json',
    contentType: 'application/json',
    success: function (data) {
    	console.log(data);
        $('#target').html(data.msg);
        document.location.href="http://localhost:8089/indexHome.html?"+"username="+data.username+"&usersmartspace="
   	 +getQueryVariable('managersmartspace')+"&role="+"MANAGER"+"&useremail="+getQueryVariable('manageremail');
    },
    data: JSON.stringify(person)
});
	
	
	
}





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
