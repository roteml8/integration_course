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
public class ElementControllerIntegrationMANAGARTests {

	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedElementDao<String> elementDao;
	private EnhancedUserDao<String> userDao;

	private FakeElementGenerator generator;
	private FakeUserGenerator userGenerator;

	private String mySmartspace;
	private UserRole userRoleToCheck;
	private UserEntity myManager;
	
	private final String managerKeyUrl = "/{managerSmartspace}/{managerEmail}";
	private final String userKeyUrl = "/{userSmartspace}/{userEmail}";
	private final String elementKeyUrl = "/{elementSmartspace}/{elementId}";


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
	public void setUserDao(EnhancedElementDao<String> elementDao) {
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
		this.userRoleToCheck = UserRole.MANAGER;
		
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
		
		myManager = this.userGenerator.getUser();
		myManager.setRole(this.userRoleToCheck);
		this.userDao.create(myManager);
	}

	@After
	public void tearDown() {
		this.elementDao.deleteAll();
		this.userDao.deleteAll();
	}

	@Test
	public void testPostNewElement() throws Exception {
		// GIVEN the element database is empty
		// AND the user database has a manager

		// WHEN I POST new element with an element boundary with null key
		ElementEntity element = generator.getElement();

		ElementBoundary elementBoundary = new ElementBoundary(element);
		elementBoundary.setKey(null);

		this.restTemplate.postForObject(
				this.baseUrl + this.managerKeyUrl,
				elementBoundary,
				ElementBoundary.class,
				this.myManager.getUserSmartspace(),
				myManager.getUserEmail());

		// THEN the element database contains a single element
		// AND this element's  fields are exactly the same as the fields in element except for elementSmartspace and creationTimeDate
		// AND his smartspace field is the same as the local project's smartspace and he has a valid Id.
		List<ElementEntity> rv = this.elementDao.readAll();
		assertThat(rv).hasSize(1);
		
		assertThat(rv.get(0)).isNotNull()
				.extracting("elementSmartspace", "location", "name", "type", "expired", "creatorSmartspace", "creatorEmail")
				.containsExactly(this.mySmartspace, element.getLocation(), element.getName(), element.getType(), element.isExpired(),
						element.getCreatorSmartSpace(), element.getCreatorEmail());
		assertThat(rv.get(0).getCreationTimeDate()).isNotEqualTo(element.getCreationTimeDate());
		assertThat(rv.get(0).getElementid()).isNotNull().isGreaterThan("0");
	}
	
	@Test
	public void testPutUpdateWithElementInDatabase() throws Exception {
		// GIVEN the user database has that manager
		// AND the element database contains element that was created by that manager
		ElementEntity element = generator.getElement();
		element.setCreatorEmail(this.myManager.getUserEmail());
		element.setCreatorSmartSpace(this.myManager.getUserSmartspace());
		ElementEntity elementInDB = this.elementDao.create(element);

		// WHEN i update element with updatedElement
		ElementEntity updatedElement = generator.getElement();
		updatedElement.setCreatorEmail(this.myManager.getUserEmail());
		updatedElement.setCreatorSmartSpace(this.myManager.getUserSmartspace());
		
		ElementBoundary elementBoundary = new ElementBoundary(updatedElement);
		elementBoundary.setKey(null);
		
		String bla = elementInDB.getElementid();

		this.restTemplate.put(
				this.baseUrl + this.managerKeyUrl + this.elementKeyUrl,
				elementBoundary,
				myManager.getUserSmartspace(),
				myManager.getUserEmail(),
				elementInDB.getElementSmartSpace(),
				elementInDB.getElementid());

		// THEN the element in the database will have details exactly like updatedElement except for elemntId
		// AND his smartspace field is the same as the local project's smartspace and he has a valid Id
		// AND his creationTimeStamp is the same (can't change)
		// AND the element database contains a single element
		
		List<ElementEntity> rv = this.elementDao.readAll();
		
		assertThat(rv.get(0)).isNotNull()
				.extracting("elementSmartspace", "location", "name", "type", "expired", "creatorSmartspace", "creatorEmail" , "moreAttributes")
				.containsExactly(this.mySmartspace, updatedElement.getLocation(), updatedElement.getName(), updatedElement.getType(), updatedElement.isExpired(),
						element.getCreatorSmartSpace(), element.getCreatorEmail(), updatedElement.getMoreAttributes());
		
		assertThat(rv.get(0).getElementid()).isNotNull().isGreaterThan("0");
		
		assertThat(rv).hasSize(1);
	}

}
