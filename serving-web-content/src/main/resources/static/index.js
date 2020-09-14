var clientWebSocket = new WebSocket("ws://localhost:8090/event-emitter");
var initialized = false;
clientWebSocket.onopen = function() {
    console.log("clientWebSocket.onopen", clientWebSocket);
    console.log("clientWebSocket.readyState", "websocketstatus");
    //clientWebSocket.send("event-me-from-browser");
}
clientWebSocket.onclose = function(error) {
    console.log("clientWebSocket.onclose", clientWebSocket, error);
	document.querySelector(".events").innerHTML += "Closing connection <br>";
}
clientWebSocket.onerror = function(error) {
    console.log("clientWebSocket.onerror", clientWebSocket, error);
	document.write("An error occured <br>");
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
		document.querySelector("." + jsonFile[i].folderName).innerHTML += "<div id= " + elementName + ">" + elementName +"</div>";
	}
}

function events(responseEvent) {
	var jsonFile = JSON.parse(responseEvent);
	for(var i in jsonFile){
		document.getElementById(jsonFile[i].folderName + "-" + jsonFile[i].projectName).innerHTML = jsonFile[i].folderName + "-" + jsonFile[i].projectName + "--->  color = " + jsonFile[i].color;
	}
	//printValues(jsonFile);	
	//clientWebSocket.close(); for testing
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