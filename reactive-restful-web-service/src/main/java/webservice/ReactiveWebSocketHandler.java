package webservice;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;

import java.time.Duration;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component("ReactiveWebSocketHandler")
public class ReactiveWebSocketHandler implements WebSocketHandler,InitializingBean {

	private final static Logger logger = LoggerFactory.getLogger(LoggingController.class);
    private String url;	
	private String [] folders = {"dev","prod","stable","stage"};
	private GetProjectName names;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public void afterPropertiesSet() throws ClientProtocolException, NullPointerException, IOException {
		logger.info("InitializingBean is working...");
		getJenkinsContent();
		getProjectNames();
	}
	
	public void destroy() {
		logger.info("All beans are destroyed.");
	}
	
	public ReactiveWebSocketHandler(@Lazy GetProjectName name) {
		super();
		this.names = name;
	}
	
	@Bean
	public GetProjectName getGetProjectName() {
		return new GetProjectName();
	}
	
	public ArrayList<String> getProjectNames(){
		return names.getProjects();
	}
	
    public void getJenkinsContent() throws ClientProtocolException, IOException,NullPointerException {
    	ArrayList<String> projects = getProjectNames();
    	if(projects == null) {
    		logger.error("Error occured while getting project names.");
    		throw new NullPointerException("Error occured while getting project names.");
    	}
    	for(String folderName : folders) {
			for(String projectName : projects) {
    			url = getUrl(folderName,projectName);
    			RestTemplate restTemplate = new RestTemplate();
    			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    			FileWriter file = new FileWriter("src/main/resources/" + folderName + "-" + projectName + ".json");
    			file.write(response.getBody());
    			file.close();
			}
		}
    	logger.info("Got jenkins content.");
    }
    
    public JSONObject getJSONObject(String fileName,String folderName) throws IOException {
    	String jsonTmp = new String();
    	JSONContent jsonContent = new JSONContent();
    	File file = new File(fileName);
		jsonTmp = FileUtils.readFileToString(file,StandardCharsets.UTF_8);
		jsonContent = objectMapper.readValue(jsonTmp, JSONContent.class);
		JSONObject obj = new JSONObject();
		obj.put("folderName",folderName)
		.put("projectName", jsonContent.getName())
		.put("color", jsonContent.getColor());
		return obj;
    }
    
    private Flux<String> eventFlux = Flux.generate(sink -> {
    	try {
    		ArrayList<String> projects = getProjectNames();
    		JSONArray jsonArray = new JSONArray();
    		String fileName;
    		for(String folderName : folders) {
    			for(String projectName : projects) {
    				fileName = "src/main/resources/"+ folderName + "-" + projectName.toString() + ".json";    				
    				jsonArray.put(getJSONObject(fileName,folderName));
    			}
    		}
    		sink.next(jsonArray.toString());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    });
       
    private Flux<String> intervalFlux = Flux.interval(Duration.ofMillis(100L))
      .zipWith(eventFlux, (time, event) -> event);

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
    	return webSocketSession.send(intervalFlux
          .map(webSocketSession::textMessage))
          .and(webSocketSession.receive()
            .map(WebSocketMessage::getPayloadAsText).log());
    }
    
    public String getUrl(String folderName,String projectName) {
    	return "http://localhost:8080/job/" + folderName + "/job/" + projectName + "/api/json?pretty=true";
    }
}