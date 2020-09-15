package webservice;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebService {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		SpringApplication.run(WebService.class, args);
		ReactiveWebSocketHandler a = new ReactiveWebSocketHandler();
		//a.getProjectsNames();
		a.getJenkinsContent();
	}
}