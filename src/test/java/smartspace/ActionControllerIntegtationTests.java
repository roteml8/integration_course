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
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.FakeActionGenerator;
import smartspace.infra.ActionService;
import smartspace.layout.ActionBoundary;
import smartspace.layout.ElementBoundary;
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
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/actions/";
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
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		int size = 3;
		IntStream.range(1, size + 1)
			.mapToObj(i->new ActionEntity("demo" + i, this.generator.getAction().getElementSmartspace(),
					this.generator.getAction().getActionType(),this.generator.getAction().getCreationTimestamp(),
					this.generator.getAction().getPlayerEmail(), this.generator.getAction().getPlayerSmartspace(), 
					this.generator.getAction().getMoreAttributes()))
			.forEach(this.actionDao::create);
		
		ActionBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + "{adminSmartspace}/{adminEmail}?page={page}&psize={size}", 
					ActionBoundary[].class, 
					"2019B.Amitz4.SmartSpace","amit@gmail.com",0, 10);
		
		assertThat(response)
			.hasSize(size);
	}


	@Test(expected=Exception.class)
	public void testPostNewActionSameSmartspace() throws Exception{
		
		// GIVEN the action database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST new action with local smartspace
		// with the email&smartspace of the admin 
		
		ActionEntity a = generator.getAction();
		a.setActionSmartspace("2019B.Amitz4.SmartSpace");
		a.setElementId("1");
		ActionBoundary newAction = new ActionBoundary(a);
		ActionBoundary[] arr = new ActionBoundary[1];
		arr[0] = newAction;
		try {
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr, 
					ActionBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email");
		}
		// THEN the database is empty
		// and Post method throws an exception 
		catch(Exception exception) {
		assertThat(this.actionDao
			.readAll())
			.hasSize(0);
		throw exception;
		}
	}
	
	

	@Test(expected=Exception.class)
	public void testPostTwoActionsOneLocalSecondExternal() throws Exception{
		
		// GIVEN the action database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST  an array containing an action from local smartspace
		// and an action with external smartspace
		// with the email&smartspace of the admin 
		
		ActionEntity a = generator.getAction();
		a.setActionSmartspace("2019B.Amitz4.SmartSpace");
		a.setElementId("1");
		ActionEntity a2 = generator.getAction();
		a2.setActionSmartspace("Space");
		a2.setElementId("2");
		ActionBoundary newAction = new ActionBoundary(a);
		ActionBoundary newAction2 = new ActionBoundary(a2);
		ActionBoundary[] arr = new ActionBoundary[2];
		arr[1] = newAction;
		arr[0] = newAction2;
		try {
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr, 
					ActionBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email");
		}
		catch(Exception exception) {
		// THEN the database is empty
		// and Post method throws an exception 
		assertThat(this.actionDao
			.readAll())
			.hasSize(0);
		throw exception;
		}
	}
	

	@Test(expected=Exception.class)
	public void testPostTwoActionsOneLocalSecondExternalNotTogether() throws Exception{
		
		// GIVEN the action database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST one action from local smartspace
		// and POST one action with external smartspace
		// with the email&smartspace of the admin 
		
		ActionEntity a = generator.getAction();
		a.setActionSmartspace("2019B.Amitz4.SmartSpace");
		a.setActionId("1");
		ActionEntity e2 = generator.getAction();
		e2.setActionSmartspace("Space");
		e2.setActionId("1");
		ActionBoundary newAction = new ActionBoundary(a);
		ActionBoundary newAction2 = new ActionBoundary(e2);
		ActionBoundary[] arr1 = new ActionBoundary[1];
		ActionBoundary[] arr2 = new ActionBoundary[1];
		arr1[0] = newAction;
		arr2[0] = newAction2;
		
		try {
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr1, 
					ActionBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email");
		}
		catch(Exception exception) {
		this.restTemplate
		.postForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
				arr2, 
				ActionBoundary[].class, 
				"2019B.Amitz4.SmartSpace","Email");
	
		// THEN the database contains the external element only 
		// and Post method throws an exception 
		assertThat(this.actionDao
			.readAll())
			.hasSize(1)
			.containsOnly(e2);
		throw exception;
		}
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
					this.baseUrl + "{adminSmartspace}/{adminEmail}?page={page}&psize={size}",
					ActionBoundary[].class, 
					"2019B.Amitz4.SmartSpace","tom@gmail.com",1, 2);
		
		assertThat(result)
			.isEmpty();
		
	}


}
