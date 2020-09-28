package webservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProjectColorTest {
	
	@Autowired
	private ReactiveWebSocketHandler reactiveWebSocketHandler;
	
	@Test
	public void projectsColorsTest() throws JSONException, IOException {
		String fileName = "src/test/resources/getProjectsNamesTest1.json";
		assertThat(reactiveWebSocketHandler.getJSONObject(fileName, "").getString("color").toString()).isEqualTo("project-color-test1");
        fileName = "src/test/resources/getProjectsNamesTest2.json";
        assertThat(reactiveWebSocketHandler.getJSONObject(fileName, "").getString("color").toString()).isEqualTo("project-color-test2");
	}
}
