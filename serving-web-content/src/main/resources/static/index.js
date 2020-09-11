var clientWebSocket = new WebSocket("ws://localhost:8090/event-emitter");
clientWebSocket.onopen = function() {
    console.log("clientWebSocket.onopen", clientWebSocket);
    console.log("clientWebSocket.readyState", "websocketstatus");
    //clientWebSocket.send("event-me-from-browser");
}
clientWebSocket.onclose = function(error) {
    console.log("clientWebSocket.onclose", clientWebSocket, error);
    events("Closing connection");
}
clientWebSocket.onerror = function(error) {
    console.log("clientWebSocket.onerror", clientWebSocket, error);
    events("An error occured");
}
clientWebSocket.onmessage = function(error) {
    console.log("clientWebSocket.onmessage", clientWebSocket, error);
    events(error.data);
}
function events(responseEvent) {
	if(typeof responseEvent !== "string"){
		document.querySelector(".events").innerHTML += "Response is not a string.";
	}
	try{
		responseEvent = JSON.parse(responseEvent);
		document.querySelector(".events").innerHTML += responseEvent + "<br>";	
		clientWebSocket.close();// for testing
	} catch (error){
		return false;
	}
}