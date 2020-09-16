package webservice;

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

import java.net.URI;

import java.util.ArrayList;

import java.io.IOException;

public class GetProjectName {
	private String url,username="toygar",password="123456";	
	private String [] folders = {"dev","prod","stable","stage"};	
	
	public ArrayList<String> getProjectsNames() throws ClientProtocolException, IOException {
		ArrayList<String> projects = new ArrayList<String>();
		for(String folderName : folders) {
	    	url="http://localhost:8080/job/" + folderName + "/api/json?pretty=true";
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
	        JSONObject obj = new JSONObject(EntityUtils.toString(response.getEntity()));
	        JSONObject jsonTmp;
	        JSONArray jsonArray = obj.getJSONArray("jobs");
	        for(int i=0; i < obj.getJSONArray("jobs").length(); i++) {
	        	jsonTmp = jsonArray.getJSONObject(i);
	        	if(!projects.contains(jsonTmp.getString("name"))) { //can be changed if projects in all folders is not same
	        		projects.add(jsonTmp.getString("name"));
	        	}
	        }
		}
		return projects;
	}
	
	public ArrayList<String> getProjects(){
		ArrayList<String> projects = new ArrayList<String>();
		try {
			projects = this.getProjectsNames();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return projects;
	}
}
