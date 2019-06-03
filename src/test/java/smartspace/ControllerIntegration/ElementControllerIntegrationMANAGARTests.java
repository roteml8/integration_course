package smartspace.ControllerIntegration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.FakeElementGenerator;
import smartspace.data.util.FakeUserGenerator;
import smartspace.infra.NotAManagerException;
import smartspace.layout.ElementBoundary;
import smartspace.layout.UserBoundary;

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
	
	private final String adminKeyUrl = "/{adminSmartspace}/{adminEmail}";
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

		ElementBoundary recivedBoundary = this.restTemplate.postForObject(
				this.baseUrl + this.managerKeyUrl,
				elementBoundary,
				ElementBoundary.class,
				this.myManager.getUserSmartspace(),
				myManager.getUserEmail());

		// THEN the element database contains a single element
		// AND this element's  fields are exactly the same as the fields in element except for elementSmartspace and creationTimeDate
		// AND his smartspace field is the same as the local project's smartspace and he has a valid Id.
		// AND the received boundary from the post is the same as the entity in the DB

		List<ElementEntity> rv = this.elementDao.readAll();
		assertThat(rv).hasSize(1);
		
		assertThat(rv.get(0)).isNotNull()
				.extracting("elementSmartspace", "location", "name", "type", "expired", "creatorSmartspace", "creatorEmail")
				.containsExactly(this.mySmartspace, element.getLocation(), element.getName(), element.getType(), false,
						element.getCreatorSmartSpace(), element.getCreatorEmail());
		assertThat(rv.get(0).getCreationTimeDate()).isNotEqualTo(element.getCreationTimeDate());
		assertThat(rv.get(0).getElementid()).isNotNull().isGreaterThan("0");
		assertThat(rv.get(0)).isEqualToIgnoringGivenFields(recivedBoundary.convertToEntity(), "creationTimeStamp");
	}
	
	@Test (expected=HttpClientErrorException.class)
	public void testPostWithBadNewElement() throws Exception {
		// GIVEN the element database is empty
		// AND the user database has a manager

		// WHEN I POST new element with an element boundary with null key and null creator fields
		ElementEntity element = generator.getElement();

		ElementBoundary elementBoundary = new ElementBoundary(element);
		elementBoundary.setCreator(null);
		elementBoundary.setKey(null);

		try {
		this.restTemplate.postForObject(
				this.baseUrl + this.managerKeyUrl,
				elementBoundary,
				ElementBoundary.class,
				this.myManager.getUserSmartspace(),
				myManager.getUserEmail());
		}
		// THEN the database stays empty
		// AND post method throws the correct exception 
		catch(HttpClientErrorException exception) 
		{
			List<ElementEntity> rv = this.elementDao.readAll();
			assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
			assertThat(rv).isNotNull().hasSize(0);
			
			throw exception;
		}
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
	
	@Test(expected = Exception.class)
	//@Test(expected = HttpClientErrorException.class)
	public void testPutUpdateWithElementNotInDatabase() throws Exception {
		// GIVEN the user database has that manager
		// AND the element database contains element that was created by that manager
		ElementEntity element = generator.getElement();
		element.setCreatorEmail(this.myManager.getUserEmail());
		element.setCreatorSmartSpace(this.myManager.getUserSmartspace());
		ElementEntity elementInDB = this.elementDao.create(element);

		// WHEN i try to update an element not stored in the database
		ElementEntity updatedElement = generator.getElement();
		updatedElement.setCreatorEmail(this.myManager.getUserEmail());
		updatedElement.setCreatorSmartSpace(this.myManager.getUserSmartspace());
		
		ElementBoundary elementBoundary = new ElementBoundary(updatedElement);
		elementBoundary.setKey(null);
		
		try {
		this.restTemplate.put(
				this.baseUrl + this.managerKeyUrl + this.elementKeyUrl,
				elementBoundary,
				myManager.getUserSmartspace(),
				myManager.getUserEmail(),
				"notMySmartSpace",
				2);

		}
		catch(HttpClientErrorException exception) {
			// THEN the test ends with exception
			// AND the element in the database in unchanged
		
			assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

			List<ElementEntity> rv = this.elementDao.readAll();
		
			assertThat(rv.get(0)).isNotNull()
				.extracting("elementSmartspace", "location", "name", "type", "expired", "creatorSmartspace", "creatorEmail" , "moreAttributes")
				.containsExactly(this.mySmartspace, element.getLocation(), element.getName(), element.getType(), element.isExpired(),
						element.getCreatorSmartSpace(), element.getCreatorEmail(), element.getMoreAttributes());
		
			assertThat(rv.get(0).getElementid()).isNotNull().isGreaterThan("0");
			assertThat(rv).hasSize(1);
		
		throw exception;

		}
	}
	
	@Test(expected = HttpClientErrorException.class)
	public void testPostNewElementNotManager() throws Exception {
		// GIVEN the element database is empty
		// AND the user database has a player

		// WHEN I POST new element with an element boundary with null key and player's key
		ElementEntity element = generator.getElement();
		UserEntity user = new UserEntity();
		user.setRole(UserRole.PLAYER);
		user.setUserEmail("email");
		user.setUserSmartspace("smartspace");
		this.userDao.create(user);
		ElementBoundary elementBoundary = new ElementBoundary(element);
		elementBoundary.setKey(null);
		
		try 
		{
		this.restTemplate.postForObject(
				this.baseUrl + this.managerKeyUrl,
				elementBoundary,
				ElementBoundary.class,
				user.getUserSmartspace(),
				user.getUserEmail());
		}
		catch(HttpClientErrorException exception) {
			assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
			throw exception;
		}
		// THEN the test ends with exception 

	}

	
	
	@Test
	public void testGetSpecificElement() throws Exception {
		// GIVEN the user database has that manager
		// AND the element database contains element that was created by that manager
		ElementEntity element = generator.getElement();
		element.setCreatorEmail(this.myManager.getUserEmail());
		element.setCreatorSmartSpace(this.myManager.getUserSmartspace());
		ElementEntity elementInDB = this.elementDao.create(element);
		
		// WHEN i retrieve a that element using GET
		ElementBoundary recievedBoundary = 
				this.restTemplate
				.getForObject(
						this.baseUrl + this.userKeyUrl + this.elementKeyUrl ,
						ElementBoundary.class,
						myManager.getUserSmartspace(),
						myManager.getUserEmail(),
						elementInDB.getElementSmartSpace(),
						elementInDB.getElementid());
		
		// THEN the received boundary is equals to the element in the DB
		
		assertThat(elementInDB).isNotNull().isEqualToComparingFieldByField(recievedBoundary.convertToEntity());
		//assertThat(this.elementDao.readAll().get(0)).isNotNull().isEqualToComparingFieldByField(recievedBoundary.convertToEntity());

	}
	
	//@Test(expected = HttpClientErrorException.class)
	@Test(expected = Exception.class)
	public void testGetSpecificElementNotInDB() throws Exception {
		// GIVEN the user database has that manager
		// AND the element database contains element that was created by that manager
		ElementEntity element = generator.getElement();
		element.setCreatorEmail(this.myManager.getUserEmail());
		element.setCreatorSmartSpace(this.myManager.getUserSmartspace());
		ElementEntity elementInDB = this.elementDao.create(element);
		
		// WHEN i try to retrieve an element not in the DB using GET
		try {
		ElementBoundary recievedBoundary = 
				this.restTemplate
				.getForObject(
						this.baseUrl + this.userKeyUrl + this.elementKeyUrl ,
						ElementBoundary.class,
						myManager.getUserSmartspace(),
						myManager.getUserEmail(),
						elementInDB.getElementSmartSpace() + "bla",
						elementInDB.getElementid());
		}
		catch(Exception exception) {
			// THEN an exception will be thrown.
			// AND the element will stay unchanged.
			//assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
			assertThat(this.elementDao
					.readAll().get(0)).isNotNull().isEqualToIgnoringGivenFields(elementInDB, "creationTimeStamp");
			
			throw exception;
		}
	}
	
	
	@Test
	public void testGetAllElementsUsingPaginationAndPost() throws Exception {
		
		// GIVEN the database contains 3 elements 
		// AND user dao contains a manager 
				int size = 3;
				
				List<ElementBoundary> all = new ArrayList<>();
				ElementEntity[] arr = new ElementEntity[size];

				for (int i = 0; i<size; i++)
				{
					ElementEntity e = generator.getElement();
					ElementBoundary recivedBoundary = this.restTemplate.postForObject(
							this.baseUrl + this.managerKeyUrl,
							new ElementBoundary(e),
							ElementBoundary.class,
							this.myManager.getUserSmartspace(),
							myManager.getUserEmail());
				}
				
				// WHEN I GET elements of size 10 and page 0
				ElementBoundary[] response = 
				this.restTemplate
					.getForObject(
							this.baseUrl + this.userKeyUrl + this.pageAndKeyUrl, 
							ElementBoundary[].class, 
							this.myManager.getUserSmartspace(),
							this.myManager.getUserEmail(),
							0, 10);
				
				// THEN I receive the exact 3 elements written to the database
				for (int i = 0; i<size; i++)
				{
					arr[i] = response[i].convertToEntity();
				}
				
				assertThat(arr)
					.usingElementComparatorOnFields("key")
					.containsExactlyElementsOf(this.elementDao.readAll());
	}
	
	@Test
	public void testGetElementsUsingPagination() throws Exception {
		
		// GIVEN the database contains 3 elements 
		// AND user dao contains a manager 
				int size = 3;
				
				List<ElementBoundary> all = new ArrayList<>();
				ElementEntity[] arr = new ElementEntity[size];

				for (int i = 0; i<size; i++)
				{
					ElementEntity e = generator.getElement();
					this.elementDao.create(e);
				}
				
				// WHEN I GET elements of size 10 and page 0
				ElementBoundary[] response = 
				this.restTemplate
					.getForObject(
							this.baseUrl + this.userKeyUrl + this.pageAndKeyUrl, 
							ElementBoundary[].class, 
							this.myManager.getUserSmartspace(),
							this.myManager.getUserEmail(),
							0, 10);
				
				// THEN I receive the exact 3 elements written to the database
				for (int i = 0; i<size; i++)
				{
					arr[i] = response[i].convertToEntity();
				}
				
				assertThat(arr)
					.usingElementComparatorOnFields("key")
					.containsExactlyElementsOf(this.elementDao.readAll());
	}
	
	
	@Test
	public void testPostAndGetElementsUsingPagination() throws Exception {
		
		// GIVEN the database contains 3 elements 
		// AND user dao contains a manager 
				int size = 3;
				
				List<ElementBoundary> all = new ArrayList<>();
				ElementEntity[] arr = new ElementEntity[size];

				for (int i = 0; i<size; i++)
				{
					ElementEntity e = generator.getElement();
					ElementBoundary recivedBoundary = this.restTemplate.postForObject(
							this.baseUrl + this.managerKeyUrl,
							new ElementBoundary(e),
							ElementBoundary.class,
							this.myManager.getUserSmartspace(),
							myManager.getUserEmail());
				}
				
				// WHEN I GET elements of size 10 and page 0
				ElementBoundary[] response = 
				this.restTemplate
					.getForObject(
							this.baseUrl + this.userKeyUrl + this.pageAndKeyUrl, 
							ElementBoundary[].class, 
							this.myManager.getUserSmartspace(),
							this.myManager.getUserEmail(),
							0, 10);
				
				// THEN I receive the exact 3 elements written to the database
				for (int i = 0; i<size; i++)
				{
					arr[i] = response[i].convertToEntity();
				}
				
				List<ElementEntity> elementsInDB = this.elementDao.readAll();
				
				assertThat(elementsInDB.size()).isEqualTo(size);
				
				assertThat(arr)
					.usingElementComparatorOnFields("key")
					.containsAll(elementsInDB);
	}
	
	
	@Test
	public void testGetElementsByName() throws Exception {
		
		// GIVEN the database contains 11 elements, some with the name bla1 and some with the name bla2
		// AND user dao contains a manager 
				int size = 11;
				String nameToGet = "bla1";

				for (int i = 0; i<size; i++)
				{
					ElementEntity e = generator.getElement();
					e.setName("bla" + String.valueOf(new Random().nextInt(2)));
					this.elementDao.create(e);
				}
				
				// WHEN I GET elements by name with size 11 and page 0
				ElementBoundary[] response = 
				this.restTemplate
					.getForObject(
							this.baseUrl + this.userKeyUrl + this.getElementsByNameUrl, 
							ElementBoundary[].class, 
							this.myManager.getUserSmartspace(),
							this.myManager.getUserEmail(),
							nameToGet,
							0,
							11);
				
				// THEN I receive all the elements written to the database with the name bla1
				List<ElementEntity> resultsAsEntitys = new ArrayList<>();
				
				for (int i = 0; i<response.length; i++)
				{
					resultsAsEntitys.add(response[i].convertToEntity());
				}
				
				assertThat(resultsAsEntitys)
					.usingElementComparatorOnFields("key")
					.containsExactlyElementsOf(this.elementDao.readElementWithName(nameToGet, 11, 0));
				
			//	assertThat(resultsAsEntitys)
			//	.usingFieldByFieldElementComparator()
			//	.containsExactlyElementsOf(this.elementDao.readElementWithName(nameToGet, 11, 0));

	}
	
	@Test
	public void testGetElementsByType() throws Exception {
		
		// GIVEN the database contains 11 elements, some with the name bla1 and some with the name bla2
		// AND user dao contains a manager 
				int size = 11;
				String typeToGet = "bla1";

				for (int i = 0; i<size; i++)
				{
					ElementEntity e = generator.getElement();
					e.setType("bla" + String.valueOf(new Random().nextInt(3)));
					this.elementDao.create(e);
				}
				
				// WHEN I GET elements by type with size 11 and page 0
				ElementBoundary[] response = 
				this.restTemplate
					.getForObject(
							this.baseUrl + this.userKeyUrl + this.getElementsByTypeUrl, 
							ElementBoundary[].class, 
							this.myManager.getUserSmartspace(),
							this.myManager.getUserEmail(),
							typeToGet,
							0,
							11);
				
				// THEN I receive all the elements written to the database with the type bla1
				List<ElementEntity> resultsAsEntitys = new ArrayList<>();
				
				for (int i = 0; i<response.length; i++)
				{
					resultsAsEntitys.add(response[i].convertToEntity());
				}
				
				assertThat(resultsAsEntitys)
					.usingElementComparatorOnFields("key")
					.containsExactlyElementsOf(this.elementDao.readElementWithType(typeToGet, 11, 0));

	}
	
	@Test
	public void testGetElementsByLocationWithDistanceZero() throws Exception {
		
		// GIVEN the database contains 3 elements 
		// AND user dao contains a manager 
				int size = 3;
				List<ElementEntity> entitysInDB = new ArrayList<>();

				for (int i = 0; i<size; i++)
				{
					ElementEntity e = generator.getElement();
					e.setLocation(new Location(i , i));
					entitysInDB.add(this.elementDao.create(e));
				}
								
				// WHEN I GET elements by location with x=1, y=1 and distance=0 and size 11 and page 0
				double x = 1;
				double y = 1;
				int distance = 0;
				ElementBoundary[] response = 
				this.restTemplate
					.getForObject(
							this.baseUrl + this.userKeyUrl + this.getElementsByLocationUrl, 
							ElementBoundary[].class, 
							this.myManager.getUserSmartspace(),
							this.myManager.getUserEmail(),
							x,
							y,
							distance,
							0, 
							10);
				
				// THEN I receive all the elements written to the database that are located at 1,1
				List<ElementEntity> resultsAsEntitys = new ArrayList<>();
				
				for (int i = 0; i<response.length; i++)
				{
					resultsAsEntitys.add(response[i].convertToEntity());
				}
					
				assertThat(resultsAsEntitys)
					.usingElementComparatorOnFields("key")
					//.containsExactlyElementsOf(this.elementDao.readElementWithLocation(location, size, page));
					.containsExactlyElementsOf(this.elementDao.readElementWithLocation(new Location(x, y), size, 0));
	}
	
	@Test
	public void testGetElementsByLocationWithDistanceOne() throws Exception {
		
		// GIVEN the database contains 3 elements 
		// AND user dao contains a manager 
				int size = 3;
				List<ElementEntity> entitysInDB = new ArrayList<>();

				for (int i = 0; i<size; i++)
				{
					ElementEntity e = generator.getElement();
					e.setLocation(new Location(i , i));
					entitysInDB.add(this.elementDao.create(e));
				}
								
				// WHEN I GET elements by location with x=1, y=1 and distance=1 and size 11 and page 0
				double x = 1;
				double y = 1;
				int distance = 1;
				ElementBoundary[] response = 
				this.restTemplate
					.getForObject(
							this.baseUrl + this.userKeyUrl + this.getElementsByLocationUrl, 
							ElementBoundary[].class, 
							this.myManager.getUserSmartspace(),
							this.myManager.getUserEmail(),
							x,
							y,
							distance,
							0, 
							10);
				
				// THEN I receive all the elements written to the database that are located no more then a distance of one from the location
				List<ElementEntity> resultsAsEntitys = new ArrayList<>();
				
				for (int i = 0; i<response.length; i++)
				{
					resultsAsEntitys.add(response[i].convertToEntity());
				}
					
				assertThat(resultsAsEntitys)
					.usingElementComparatorOnFields("key")
					//.containsExactlyElementsOf(this.elementDao.readElementWithLocation(location, size, page));
					.containsExactlyElementsOf(entitysInDB);
	}

	

}
