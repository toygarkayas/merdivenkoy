package webservice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GetProjectNamesTest {

	@MockBean
	GetProjectName getProjectName;	
	
	@Autowired
	ReactiveWebSocketHandler reactiveWebSocketHandler;
	
	@Test
	public void getProjectNamesTest() {
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.add("project-name-test1");
		tmp.add("project-name-test2");
		when(getProjectName.getProjects()).thenReturn(tmp);
		assertEquals("project-name-test1",reactiveWebSocketHandler.getProjectNames().get(0));
		assertEquals("project-name-test2",reactiveWebSocketHandler.getProjectNames().get(1));
	}
}
