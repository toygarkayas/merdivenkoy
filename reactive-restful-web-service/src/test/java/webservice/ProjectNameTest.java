package webservice;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class ProjectNameTest {
	protected JSONObject jsonObject = new JSONObject();
	protected JSONArray jsonArray = new JSONArray();
	protected ReactiveWebSocketHandler reactiveWebSocketHandler = new ReactiveWebSocketHandler();
	
	@Test
	public void projectsNamesTest() throws IOException {
		String fileName = "src/test/resources/getProjectsNamesTest1.json";		
        assertTrue(reactiveWebSocketHandler.getJSONObject(fileName, "").getString("projectName").toString().equals("project-name-test1"));
        fileName = "src/test/resources/getProjectsNamesTest2.json";
        assertTrue(reactiveWebSocketHandler.getJSONObject(fileName, "").getString("projectName").toString().equals("project-name-test2"));
	}
}