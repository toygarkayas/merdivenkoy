var clientWebSocket = new WebSocket("ws://localhost:8090/event-emitter");
var initialized = false;
clientWebSocket.onopen = function() {
    console.log("clientWebSocket.onopen", clientWebSocket);
    console.log("clientWebSocket.readyState", "websocketstatus");
    //clientWebSocket.send("event-me-from-browser");
}
clientWebSocket.onclose = function(error) {
    console.log("clientWebSocket.onclose", clientWebSocket, error);
	document.querySelector(".events").innerHTML += "<br>Closing connection";
}
clientWebSocket.onerror = function(error) {
    console.log("clientWebSocket.onerror", clientWebSocket, error);
	document.write("<br>An error occured");
}
clientWebSocket.onmessage = function(error) {
	if(!initialized){
		init(error.data);
		initialized = true;
	}
	else{
	    console.log("clientWebSocket.onmessage", clientWebSocket, error);
	    events(error.data);		
	}
}

function init(initInfo){
	var jsonFile = JSON.parse(initInfo);
	var elementName;
	for(var i in jsonFile){
		elementName = jsonFile[i].folderName + "-" + jsonFile[i].projectName;
		document.querySelector("." + jsonFile[i].folderName).innerHTML += "<div id= " + elementName + ">" + elementName + "<div id="+ elementName + "-color></div>" + "</div>";
	}
}

function events(responseEvent) {
	var jsonFile = JSON.parse(responseEvent);
	var timer = new Date();
	for(var i in jsonFile){
		if(document.getElementById(jsonFile[i].folderName + "-" + jsonFile[i].projectName + "-color").innerHTML != jsonFile[i].color){
			if(document.getElementById(jsonFile[i].folderName + "-" + jsonFile[i].projectName + "-color").innerHTML != "")
				document.querySelector(".events").innerHTML += "<br>Project = " + jsonFile[i].folderName + "-" + jsonFile[i].projectName +
																"<b> ====> Old color was " + document.getElementById(jsonFile[i].folderName + "-" + jsonFile[i].projectName + "-color").innerHTML +
																" new color is " + jsonFile[i].color + "</b> ====> Time info : " + timer.getHours() + ":" + timer.getMinutes() + ":" + timer.getSeconds();
			document.getElementById(jsonFile[i].folderName + "-" + jsonFile[i].projectName + "-color").innerHTML = jsonFile[i].color;												
		}
	}
	//printValues(jsonFile);	
	//clientWebSocket.close(); //for testing
}

function printValues(obj) {
    for(var k in obj) {
        if(obj[k] instanceof Object) {
            printValues(obj[k]);
        } else {
            document.querySelector(".events").innerHTML += obj[k] + "<br>";
        }
    }
}