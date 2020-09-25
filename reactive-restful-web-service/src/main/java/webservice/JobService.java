package webservice;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobService implements Job{
	@Autowired
	private ReactiveWebSocketHandler reactiveWebSocketHandler;
	
	public void execute(JobExecutionContext context) throws JobExecutionException{
		try {
			reactiveWebSocketHandler.getJenkinsContent();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
