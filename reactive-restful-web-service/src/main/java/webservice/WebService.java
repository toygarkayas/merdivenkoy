package webservice;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebService {

	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
		SpringApplication.run(WebService.class, args);
		ReactiveWebSocketHandler reactiveWebSocketHandler = new ReactiveWebSocketHandler();
		while(true){
			reactiveWebSocketHandler.getJenkinsContent();
			Thread.sleep(1000);//get jenkins content each 1 seconds.
			//System.out.println("Getting jenkins content.");
		}	
	}
}