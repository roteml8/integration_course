package smartspace.ControllerIntegration;

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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementType;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.FakeActionGenerator;
import smartspace.data.util.FakeElementGenerator;
import smartspace.data.util.FakeUserGenerator;
import smartspace.infra.ActionService;
import smartspace.layout.ActionBoundary;
import smartspace.layout.ElementBoundary;
import smartspace.layout.UserBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class ActionControllerIntegrtationPLAYERTests {
	
	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedActionDao actionDao;
	private EnhancedUserDao<String> userDao;
	private EnhancedElementDao<String> elementDao;
	private ActionService actionService;
	private FakeElementGenerator elementGenerator;
	private FakeUserGenerator userGenerator;
	
	
	@Autowired
	public void setActionDao(EnhancedActionDao actionDao) {
		this.actionDao = actionDao;
	}
	
	@Autowired
	public void setUserDao(EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	@Autowired
	public void setElementDao(EnhancedElementDao<String> elementDao) {
		this.elementDao = elementDao;
	}
	
	@Autowired
	public void setGenerator(FakeElementGenerator elementGenerator) {
		this.elementGenerator = elementGenerator;
	}
	
	@Autowired
	public void setGenerator(FakeUserGenerator userGenerator) {
		this.userGenerator = userGenerator;
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}
	
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/actions/";
	}
	
	@Before
	public void setup() {
		this.actionDao.deleteAll();
		this.userDao.deleteAll();
		this.elementDao.deleteAll();
	}
	
	@After
	public void tearDown() {
		this.actionDao.deleteAll();
		this.userDao.deleteAll();
		this.elementDao.deleteAll();
	}
	
	@Test
	public void testPostInvokeAction() throws Exception {
		// GIVEN the element database has an element
		// AND the user database has a user
		UserEntity user = this.userGenerator.getUser();
		user.setRole(UserRole.PLAYER);
		ElementEntity element = this.elementGenerator.getElement();
		
		UserEntity userInDB = this.userDao.create(user);
		
		element.setCreatorEmail(user.getUserEmail());
		element.setCreatorSmartSpace(user.getUserSmartspace());
		
		ElementEntity elementInDB = this.elementDao.create(element);
		
		ActionEntity action = new ActionEntity();
		action.setActionType("Echo");
		action.setElementId(elementInDB.getElementid());
		action.setElementSmartspace(elementInDB.getElementSmartSpace());
		action.setPlayerEmail(user.getUserEmail());
		action.setPlayerSmartspace(user.getUserSmartspace());
		action.setMoreAttributes(new HashMap<String,Object>());
		ActionBoundary bound = new ActionBoundary(action);
		
		// WHEN invoke a new action of type ECHO with POST
		ActionBoundary recivedBoundary = 
				this.restTemplate
				.postForObject(
						this.baseUrl,
						bound,
						ActionBoundary.class);
		
		//THEN the jason i receive is the same as the input boundary except for the key, creationTImeStamp and moreAttributes fields
		//AND moreAtrributes got the echo signature
		assertThat(recivedBoundary).isNotNull().isEqualToIgnoringGivenFields(bound, "actionKey","created","properties");
		Map<String,Object> attributes = new HashMap<String,Object>();
		attributes.put("echo", "echo");
		assertThat(recivedBoundary.getProperties()).isEqualTo(attributes);
		assertThat(recivedBoundary.getCreationTimeStamp()).isNotNull();
		assertThat(recivedBoundary.getKey()).isNotNull();
		assertThat(Long.parseLong((String) recivedBoundary.getKey().get("id"))).isGreaterThan(0);
		

		
	}
	
	/* if i want to run this test i need to run the tests without 
	 * using deleteAll before and after every test (this deletes the score board element). 
	 * this test's assert is not good, but using a breakpoint at the end you can see that UpdateScoreBoardPlugin does its job.
	
	@Test
	public void testPostInvokeUpdateScoreBoard() throws Exception {
		// GIVEN the element database has an element
		// AND the user database has a user
		UserEntity user = this.userGenerator.getUser();
		user.setRole(UserRole.PLAYER);
		ElementEntity element = this.elementGenerator.getElement();
		
		UserEntity userInDB = this.userDao.create(user);
		
		element.setCreatorEmail(user.getUserEmail());
		element.setCreatorSmartSpace(user.getUserSmartspace());
		
		ElementEntity scoreBoardInDBbefore = this.elementDao.readElementWithType(ElementType.SCORE_BOARD.toString() , 1, 0).get(0);
		
		ActionEntity action = new ActionEntity();
		action.setActionType("UpdateScoreBoard");
		action.setElementId(scoreBoardInDBbefore.getElementid());
		action.setElementSmartspace(scoreBoardInDBbefore.getElementSmartSpace());
		action.setPlayerEmail(user.getUserEmail());
		action.setPlayerSmartspace(user.getUserSmartspace());
		action.setMoreAttributes(new HashMap<String,Object>());
		ActionBoundary bound = new ActionBoundary(action);
		
		// WHEN invoke a new action of type SCORE_BOARD with POST
		ActionBoundary recivedBoundary = 
				this.restTemplate
				.postForObject(
						this.baseUrl,
						bound,
						ActionBoundary.class);
		
		//THEN the jason i receive is the same as the input boundary except for the key, creationTImeStamp and moreAttributes fields
		//AND moreAtrributes got the echo signature
		
		ElementEntity scoreBoardInDBafter = this.elementDao.readElementWithType(ElementType.SCORE_BOARD.toString() , 1, 0).get(0);
		
		List<UserEntity> bestFiveUsersInDB = this.userDao.readAll("points" , Direction.DESC, 5 ,0 );
		
		assertThat(scoreBoardInDBafter.getMoreAttributes().get("users")).isEqualToComparingFieldByField(bestFiveUsersInDB);

	}
	
	*/

	
	
	
	
	


}
