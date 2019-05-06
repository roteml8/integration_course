package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.FakeActionGenerator;
import smartspace.infra.ActionService;
import smartspace.layout.ActionBoundary;
import smartspace.layout.UserBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class ActionControllerIntegtationTests {
	
	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedActionDao actionDao;
	private EnhancedUserDao<String> userDao;
	private ActionService actionService;
	private FakeActionGenerator generator;

	@Autowired
	public void setGenerator(FakeActionGenerator generator) {
		this.generator = generator;
	}

	
	@Autowired
	public void setMessageService(ActionService actionService) {
		this.actionService = actionService;
	}
	
	@Autowired
	public void setElementDao(EnhancedActionDao actionDao) {
		this.actionDao = actionDao;
	}
	
	@Autowired
	public void setUserDao(EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}
	
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/actiondemo";
	}
	
	@Before
	public void setup() {
		this.actionDao
			.deleteAll();
		this.userDao.deleteAll();
	}
	
	@After
	public void tearDown() {
		this.actionDao
			.deleteAll();
		this.userDao.deleteAll();
	}
	

	
	@Test
	public void testPostNewAction() throws Exception{
		
		// GIVEN the action database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST new action with email&smartspace of the admin 
		
		ActionEntity action = generator.getAction();
		action.setActionSmartspace("smartspace");
		action.setActionId("123");
		ActionBoundary newAction = new ActionBoundary(action);
		System.out.println(action.toString());
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					newAction, 
					ActionBoundary.class, 
					"2019B.Amitz4.SmartSpace","Email");
		
		// THEN the database contains a single action
		assertThat(this.actionDao
			.readAll())
			.hasSize(1);
	}
	
	@Test(expected=Exception.class)
	public void testPostNewActionNoAdmin() throws Exception{
		
		// GIVEN the action database is empty and user database contains an admin
		UserEntity admin = new UserEntity();
		admin.setUserEmail("EmailNotAdmin");
		admin.setUserSmartspace("SmartspaceNotAdmin");
		admin.setRole(UserRole.PLAYER);
		// WHEN I POST new action with smartspace and email that belong to a user who is not an admin 
		ActionBoundary newAction = new ActionBoundary(generator.getAction());
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					newAction, 
					ActionBoundary.class, 
					"SmartspaceNotAdmin","EmailNotAdmin");
		
		// THEN the test ends with exception
	}
	
	@Test
	public void testGetAllActionsUsingPagination() throws Exception{
		// GIVEN the database contains 3 actions
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		int size = 3;
		IntStream.range(1, size + 1)
			.mapToObj(i->generator.getAction())
			.forEach(this.actionDao::create);
		
		// WHEN I GET actions of size 10 and page 0
		ActionBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={page}", 
					ActionBoundary[].class, 
					10, 0);
		
		// THEN I receive 3 actions 
		assertThat(response)
			.hasSize(size);
	}

	@Test
	public void testGetAllActionsUsingPaginationAndValidateContentWithAllAttributeValidation() throws Exception{
		// GIVEN the database contains 4 actions
		int size = 4;
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		List<ActionBoundary> all = new ArrayList<>();
		for (int i = 1; i<=size; i++)
		{
			ActionEntity action = generator.getAction();
			action.setActionId(String.valueOf(i));
			action.setActionSmartspace("Space");
			ActionEntity rv = this.actionService.newAction(action, "2019B.Amitz4.SmartSpace", "Email");
			all.add(new ActionBoundary(rv));
		}

		
		
		// WHEN I GET action of size 10 and page 0
		ActionBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={page}", 
					ActionBoundary[].class, 
					10, 0);
	
		// THEN I receive the exact action written to the database
		assertThat(response)
		.usingElementComparatorOnFields("key")
		.containsExactlyElementsOf(all);
	}



	@Test
	public void testGetAllActionsUsingPaginationOfSecondNonExistingPage() throws Exception{
		Map<String, Object> moreAttributes = null;
		IntStream
			.range(0, 10)
			.forEach(i->this.actionDao.create(new ActionEntity("" + i,this.generator.getAction().getElementSmartspace()
					, this.generator.getAction().getActionType()
					,  this.generator.getAction().getCreationTimestamp()
					,  this.generator.getAction().getPlayerEmail()
					,  this.generator.getAction().getPlayerSmartspace()
					,  moreAttributes)));
	
		ActionBoundary[] result = 
		  this.restTemplate
			.getForObject(
					this.baseUrl + "?page={page}&psize={size}", 
					ActionBoundary[].class, 
					1, 2);
		
		assertThat(result)
			.isEmpty();
		
	}


}
