package webservice;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class GetProjectName {
	private String url;	
	private String [] folders = {"dev","prod","stable","stage"};	

	@Bean
	public List<String> getProjectsNames() {
		List<String> projects = new ArrayList<>();
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
		if(projects.isEmpty())
			return null;
		return projects;
	}
	
	@Bean
	public List<String> getProjects(){
		List<String> projects = null;
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
