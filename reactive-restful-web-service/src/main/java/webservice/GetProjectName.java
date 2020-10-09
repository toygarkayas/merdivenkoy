package webservice;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import java.io.IOException;

public class GetProjectName {
	private String url;	
	private String [] folders = {"dev","prod","stable","stage"};	

	@Bean
	public ArrayList<String> getProjectsNames() throws ClientProtocolException, IOException {
		ArrayList<String> projects = new ArrayList<String>();
		for(String folderName : folders) {
	    	url="http://localhost:8080/job/" + folderName + "/api/json?pretty=true";
	        JSONObject obj = new JSONObject(getResponse());
	        JSONObject jsonTmp;
	        JSONArray jsonArray = obj.getJSONArray("jobs");
	        for(int i=0; i < obj.getJSONArray("jobs").length(); i++) {
	        	jsonTmp = jsonArray.getJSONObject(i);
	        	if(!projects.contains(jsonTmp.getString("name"))) { //can be changed if projects in all folders is not same
	        		projects.add(jsonTmp.getString("name"));
	        	}
	        }
		}
		if(projects.size() == 0)
			return null;
		return projects;
	}
	
	@Bean
	public ArrayList<String> getProjects(){
		ArrayList<String> projects = new ArrayList<String>();
		try {
			projects = this.getProjectsNames();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return projects;
	}
	
	public String getResponse() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		return response.getBody();
	}
}
