package smartspace.ControllerIntegration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.FakeElementGenerator;
import smartspace.data.util.FakeUserGenerator;
import smartspace.layout.ElementBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class ElementControllerIntegrationPLAYERTests {
	

	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedElementDao<String> elementDao;
	private EnhancedUserDao<String> userDao;

	private FakeElementGenerator generator;
	private FakeUserGenerator userGenerator;

	private String mySmartspace;
	
	private final String managerKeyUrl = "/{managerSmartspace}/{managerEmail}";
	private final String userKeyUrl = "/{userSmartspace}/{userEmail}";
	private final String elementKeyUrl = "/{elementSmartspace}/{elementId}";
	private final String pageAndKeyUrl = "?page={page}&size={size}";
	
	private final String getElementsByLocationUrl = "?search=location&x={x}&y={y}&distance={distance}&page={page}&size={size}";
	private final String getElementsByNameUrl = "?search=name&value={name}&page={page}&size={size}";
	private final String getElementsByTypeUrl = "?search=type&value={type}&page={page}&size={size}";

	@Autowired
	public void setGenerator(FakeElementGenerator generator) {
		this.generator = generator;
	}
	
	@Autowired
	public void setUserGenerator(FakeUserGenerator userGenerator) {
		this.userGenerator = userGenerator;
	}

	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String mySmartspace) {
		this.mySmartspace = mySmartspace;
	}

	@Autowired
	public void setElementDao(EnhancedElementDao<String> elementDao) {
		this.elementDao = elementDao;
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
		this.baseUrl = "http://localhost:" + port + "/smartspace/elements";
		
		/*
		myManager = this.userGenerator.getUser();
		myManager.setRole(this.userRoleToCheck);
		this.userDao.create(myManager);
		*/
	}
	
	/*
	@PreDestroy
	public static void shutdown() {
		this.userDao.deleteAll();
	}      
	 */
	
	@Before
	public void setUp() {
		this.elementDao.deleteAll();
		
	}

	@After
	public void tearDown() {
		this.elementDao.deleteAll();
		this.userDao.deleteAll();
	}
	
	@Test (expected=HttpClientErrorException.class)
	public void testPostNewElement() throws Exception {
		// GIVEN the element database is empty
		// AND the user database has a player
		
		UserEntity user = userGenerator.getUser();
		user.setRole(UserRole.PLAYER);
		this.userDao.create(user);

		// WHEN I POST new element with an element boundary with null key with the player's key
		ElementEntity element = generator.getElement();

		ElementBoundary elementBoundary = new ElementBoundary(element);
		elementBoundary.setKey(null);

		ElementBoundary recivedBoundary = this.restTemplate.postForObject(
				this.baseUrl + this.managerKeyUrl,
				elementBoundary,
				ElementBoundary.class,
				user.getUserSmartspace(),
				user.getUserEmail());
		
		// THEN the test ends with exception 


	}
	
	@Test 
	public void testGetElements() throws Exception {
		// GIVEN the element database has two elements - one expired and second not expired with the same name
		// AND the user database has a player
		
		UserEntity user = userGenerator.getUser();
		user.setRole(UserRole.PLAYER);
		this.userDao.create(user);
		ElementEntity element1 = generator.getElement();
		element1.setExpired(true);
		element1.setName("test");
		ElementEntity element2 = generator.getElement();
		element2.setExpired(false);
		element2.setName("test");
		this.elementDao.create(element1);
		this.elementDao.create(element2);
		ElementBoundary b1 = new ElementBoundary(element1);
		ElementBoundary b2 = new ElementBoundary(element2);

		// WHEN I GET elements by the specific name with user's key

		ElementBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + this.userKeyUrl + this.getElementsByNameUrl, 
					ElementBoundary[].class, 
					user.getUserSmartspace(),
					user.getUserEmail(),
					"test",
					0,
					11);
		// THEN the result contains only the unexpired element
		assertThat(response).hasSize(1).usingElementComparatorOnFields("key").contains(b2).doesNotContain(b1);
	}


}
