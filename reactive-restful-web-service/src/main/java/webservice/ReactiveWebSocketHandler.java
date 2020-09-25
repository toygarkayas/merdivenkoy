package webservice;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import java.nio.charset.StandardCharsets;

import java.time.Duration;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component("ReactiveWebSocketHandler")
public class ReactiveWebSocketHandler implements WebSocketHandler {

    private String url,username="toygar",password="123456";	
	private String [] folders = {"dev","prod","stable","stage"};
	private GetProjectName names = new GetProjectName();
	private ArrayList<String> projects = names.getProjects();
	private ObjectMapper objectMapper = new ObjectMapper();
	
    public void getJenkinsContent() throws ClientProtocolException, IOException {
    	for(String folderName : folders) {
			for(String projectName : projects) {
    			url="http://localhost:8080/job/" + folderName + "/job/" + projectName + "/api/json?pretty=true";
                URI uri = URI.create(url);
                HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
                    new UsernamePasswordCredentials(username, password));
                // Create AuthCache instance
                AuthCache authCache = new BasicAuthCache();
                // Generate BASIC scheme object and add it to the local auth cache
                BasicScheme basicAuth = new BasicScheme();
                authCache.put(host, basicAuth);
                CloseableHttpClient httpClient =
                    HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
                HttpGet httpGet = new HttpGet(uri);
                // Add AuthCache to the execution context
                HttpClientContext localContext = HttpClientContext.create();
                localContext.setAuthCache(authCache);
                CloseableHttpResponse response = httpClient.execute(host, httpGet, localContext);
    			FileWriter file = new FileWriter("src/main/resources/" + folderName + "-" + projectName + ".json");
    			file.write(EntityUtils.toString(response.getEntity()));
    			file.close();
			}
		}
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
    
    @Bean
    public void init() throws ClientProtocolException, IOException {
    	getJenkinsContent();
    }
}