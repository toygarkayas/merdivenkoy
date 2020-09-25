package webservice;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan
@EnableScheduling
public class WebService {

	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
		SpringApplication.run(WebService.class, args);
	}
}