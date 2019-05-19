package smartspace.ControllerIntegration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.FakeElementGenerator;
import smartspace.infra.ElementService;
import smartspace.layout.ElementBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class ElementControllerIntegrationADMINTests {
	
	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedElementDao<String> elementDao;
	private EnhancedUserDao<String> userDao;
	private ElementService elementService;
	private FakeElementGenerator generator;

	@Autowired
	public void setGenerator(FakeElementGenerator generator) {
		this.generator = generator;
	}

	
	@Autowired
	public void setMessageService(ElementService elementService) {
		this.elementService = elementService;
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
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/elements/";
	}
	
	@Before
	public void setup() {
		this.elementDao
			.deleteAll();
		this.userDao.deleteAll();
	}
	
	@After
	public void tearDown() {
		this.elementDao
			.deleteAll();
		this.userDao.deleteAll();
	}
	

	
	@Test
	public void testPostNewElement() throws Exception{
		
		// GIVEN the element database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST new element with email&smartspace of the admin 
		
		ElementEntity e = generator.getElement();
		e.setElementSmartSpace("space");
		e.setElementid("1");
		ElementBoundary newElement = new ElementBoundary(e);
		ElementBoundary[] arr = new ElementBoundary[1];
		arr[0] = newElement;
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr, 
					ElementBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email");
		
		// THEN the database contains a single element
		assertThat(this.elementDao
			.readAll())
			.hasSize(1);
	}
	
	@Test (expected=Exception.class)
	public void testPostNewElementInvalid() throws Exception{
		
		// GIVEN the element database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST an invalid element
		// with email&smartspace of the admin 
		
		ElementEntity e = generator.getElement();
		// e has no smartspace and no id 
		ElementBoundary newElement = new ElementBoundary(e);
		ElementBoundary[] arr = new ElementBoundary[1];
		arr[0] = newElement;
		try {
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr, 
					ElementBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email");
		}
		// THEN the database is empty
		// AND post method throws an exception 
		catch(Exception exception) {
		assertThat(this.elementDao
			.readAll())
			.isEmpty();
		throw exception;
		}
	}
	
	@Test
	public void testPostElementWithExistingKey() throws Exception{
		
		// GIVEN the element database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST new element with email&smartspace of the admin 
		
		ElementEntity e = generator.getElement();
		e.setElementSmartSpace("space");
		e.setElementid("1");
		ElementBoundary newElement = new ElementBoundary(e);
		ElementBoundary[] arr = new ElementBoundary[1];
		arr[0] = newElement;
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr, 
					ElementBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email");
		
		// AND I POST another element with the same key 
		ElementEntity e2 = generator.getElement();
		e2.setElementSmartSpace("space");
		e2.setElementid("1");
		arr[0] = new ElementBoundary(e2);
		this.restTemplate
		.postForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
				arr, 
				ElementBoundary[].class, 
				"2019B.Amitz4.SmartSpace","Email");

		
		// THEN the database contains a single element 
		assertThat(this.elementDao
			.readAll())
			.hasSize(1);
	}
	
	@Test(expected=Exception.class)
	public void testPostNewElementSameSmartspace() throws Exception{
		
		// GIVEN the element database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST new element with local smartspace
		// with the email&smartspace of the admin 
		
		ElementEntity e = generator.getElement();
		e.setElementSmartSpace("2019B.Amitz4.SmartSpace");
		e.setElementid("1");
		ElementBoundary newElement = new ElementBoundary(e);
		ElementBoundary[] arr = new ElementBoundary[1];
		arr[0] = newElement;
		try {
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr, 
					ElementBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email");
		}
		// THEN the database is empty
		// and Post method throws an exception 
		catch(Exception exception) {
		assertThat(this.elementDao
			.readAll())
			.hasSize(0);
		throw exception;
		}
	}
	
	
	
	@Test(expected=Exception.class)
	public void testPostTwoElementsOneLocalSecondExternal() throws Exception{
		
		// GIVEN the element database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST  an array containing an element from local smartspace
		// and an element with external smartspace
		// with the email&smartspace of the admin 
		
		ElementEntity e = generator.getElement();
		e.setElementSmartSpace("2019B.Amitz4.SmartSpace");
		e.setElementid("1");
		ElementEntity e2 = generator.getElement();
		e2.setElementSmartSpace("Space");
		e2.setElementid("2");
		ElementBoundary newElement = new ElementBoundary(e);
		ElementBoundary newElement2 = new ElementBoundary(e2);
		ElementBoundary[] arr = new ElementBoundary[2];
		arr[1] = newElement;
		arr[0] = newElement2;
		try {
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr, 
					ElementBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email");
		
		// THEN the database is empty
		// and Post method throws an exception 
		}
		catch(Exception exception) {
		assertThat(this.elementDao
			.readAll())
			.hasSize(0);
		throw exception;
		}
	}
	
	@Test(expected=Exception.class)
	public void testPostTwoElementsOneLocalSecondExternalNotTogether() throws Exception{
		
		// GIVEN the element database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST one element from local smartspace
		// and POST one element with external smartspace
		// with the email&smartspace of the admin 
		
		ElementEntity e = generator.getElement();
		e.setElementSmartSpace("2019B.Amitz4.SmartSpace");
		e.setElementid("1");
		ElementEntity e2 = generator.getElement();
		e2.setElementSmartSpace("Space");
		e2.setElementid("1");
		ElementBoundary newElement = new ElementBoundary(e);
		ElementBoundary newElement2 = new ElementBoundary(e2);
		ElementBoundary[] arr1 = new ElementBoundary[1];
		ElementBoundary[] arr2 = new ElementBoundary[1];
		arr1[0] = newElement;
		arr2[0] = newElement2;
		try {
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr1, 
					ElementBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email");
		}
		catch(Exception exception) {
		this.restTemplate
		.postForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
				arr2, 
				ElementBoundary[].class, 
				"2019B.Amitz4.SmartSpace","Email");
	
		// THEN the database contains the external element only 
		// and Post method throws an exception 
		assertThat(this.elementDao
			.readAll())
			.hasSize(1);
		
		assertThat(this.elementDao
		.readAll().get(1))
		.isEqualToComparingFieldByField(e2);
			
		throw exception;
		}
	}
	
	@Test(expected=HttpClientErrorException.class)
	public void testPostNewElementNoAdmin() throws Exception{
		
		// GIVEN the element database is empty and user database contains a player
		UserEntity player = new UserEntity();
		player.setUserEmail("EmailNotAdmin");
		player.setUserSmartspace("SmartspaceNotAdmin");
		player.setRole(UserRole.PLAYER);
		this.userDao.create(player);

		// WHEN I POST new element with smartspace and email that belong to the player 
		ElementEntity e = generator.getElement();
		e.setElementid("1");
		e.setElementSmartSpace("space");
		ElementBoundary newElement = new ElementBoundary(e);
		ElementBoundary[] arr = new ElementBoundary[1];
		arr[0] = newElement;
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr, 
					ElementBoundary[].class, 
					player.getUserSmartspace(),player.getUserEmail());
		
		// THEN the test ends with exception
	}
	
	@Test
	public void testGetAllElementsUsingPagination() throws Exception{
		
		// GIVEN the database contains 3 elements and user dao contains an admin 
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		int size = 3;
		IntStream.range(1, size + 1)
			.mapToObj(i->generator.getElement())
			.forEach(this.elementDao::create);
		
		// WHEN I GET elements of size 10 and page 0
		ElementBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + "{adminSmartspace}/{adminEmail}?page={page}&psize={size}", 
					ElementBoundary[].class,
					"2019B.Amitz4.SmartSpace", "Email",
					0, 10);
		
		// THEN I receive 3 elements 
		assertThat(response)
			.hasSize(size);
	}
	
	@Test
	public void testGetAllElementsUsingPaginationAndValidateContent() throws Exception{
		
		// GIVEN the database contains 3 elements and user dao contains an admin 
		int size = 3;
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		List<ElementBoundary> all = new ArrayList<>();
		ElementEntity[] arr = new ElementEntity[size];

		for (int i = 0; i<size; i++)
		{
			ElementEntity e = generator.getElement();
			e.setElementSmartSpace("Space"+i);
			e.setElementid(String.valueOf(i));
			arr[i] = e;
			all.add(new ElementBoundary(e));
		}
		this.elementService.importElements("2019B.Amitz4.SmartSpace", "Email", arr);
		
		// WHEN I GET elements of size 10 and page 0
		ElementBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + "{adminSmartspace}/{adminEmail}?page={page}&psize={size}", 
					ElementBoundary[].class, 
					"2019B.Amitz4.SmartSpace", "Email",
					0, 10);
		
		// THEN I receive the exact 3 elements written to the database
		assertThat(response)
			.usingElementComparatorOnFields("key")
			.containsExactlyElementsOf(all);
	}
	
	@Test
	public void testGetAllElementsUsingPaginationAndValidateContentWithAllAttributeValidation() throws Exception{
		// GIVEN the database contains 4 elements and user dao contains an admin 
		int size = 4;
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		ElementEntity[] arr = new ElementEntity[size];
		List<ElementBoundary> all = new ArrayList<>();
		for (int i = 0; i<size; i++)
		{
			ElementEntity e = generator.getElement();
			e.setElementSmartSpace("Space"+i);
			e.setElementid(String.valueOf(i));
			arr[i] = e;
			all.add(new ElementBoundary(e));
		}
		this.elementService.importElements("2019B.Amitz4.SmartSpace", "Email", arr);
		

		
		
		// WHEN I GET elements of size 10 and page 0
		ElementBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl +"{adminSmartspace}/{adminEmail}?page={page}&psize={size}", 
					ElementBoundary[].class, 
					"2019B.Amitz4.SmartSpace", "Email",
					0, 10);
	
		// THEN I receive the exact elements written to the database
		assertThat(response)
			.usingElementComparatorOnFields("key", "name", "latlng", "elementType", "expired","creator","created","elementProperties")
			.containsExactlyElementsOf(all);
	}
	
	@Test
	public void testGetAllElementsUsingPaginationOfSecondPage() throws Exception{
		// GIVEN then database contains 11 elements
		List<ElementBoundary> all = new ArrayList<>();
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		int size = 11;
		ElementEntity[] arr = new ElementEntity[size];
		for (int i=0; i<size; i++)
		{
			ElementEntity e = generator.getElement();
			e.setElementSmartSpace("Space"+i);
			e.setElementid(String.valueOf(i));
			arr[i] = e;
			all.add(new ElementBoundary(e));
		}
		this.elementService.importElements("2019B.Amitz4.SmartSpace", "Email", arr);

		
//		MessageBoundary last = new MessageBoundary( 
//		  all
//			.stream()
//			.sorted((e1,e2)->e2.getKey().compareTo(e1.getKey()))
//			.findFirst()
//			.orElseThrow(()->new RuntimeException("no messages after sorting")));
		
		ElementBoundary last =
			all
			.stream()
			.skip(9)
			.limit(1)
			.findFirst()
			.orElseThrow(()->new RuntimeException("no elements after skipping"));
		

		
		// WHEN I GET elements of size 10 and page 2
		ElementBoundary[] result = this.restTemplate
			.getForObject(
					this.baseUrl + "{adminSmartspace}/{adminEmail}?page={page}&psize={size}", 
					ElementBoundary[].class, 
					"2019B.Amitz4.SmartSpace", "Email",
					1, 2);
		
		// THEN the result contains a single element (last message)
		assertThat(result)
			.usingElementComparator((b1,b2)->b1.toString().compareTo(b2.toString()))
			.containsExactly(last);
	}
	
	@Test
	public void testGetAllElementsUsingPaginationOfSecondNonExistingPage() throws Exception{
		// GIVEN the database contains 10 elements
		IntStream
			.range(0, 10)
			.forEach(i->this.elementDao.create(generator.getElement()));
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		
		// WHEN I GET elements of size 10 and page 2
		ElementBoundary[] result = 
		  this.restTemplate
			.getForObject(
					this.baseUrl + "{adminSmartspace}/{adminEmail}?page={page}&psize={size}", 
					ElementBoundary[].class, 
					"2019B.Amitz4.SmartSpace", "Email",
					1, 10);
		
		// THEN the result is empty
		assertThat(result)
			.isEmpty();
		
	}
//	
//	@Test
//	public void testUpdateMessage() throws Exception{
//		// GIVEN the database contains a message
//		ElementEntity element = generator.getElement();
//		element = this.elementDao
//			.create(element);
//		
//		// WHEN I update the message details
//		Map<String, Object> newDetails = new HashMap<>();
//		newDetails.put("x", 10.0);
//		newDetails.put("y", "new details");
//		newDetails.put("expired", true);
//		
//		ElementBoundary boundary = new ElementBoundary();
//		boundary.setMoreAttributes(newDetails);
//		this.restTemplate
//			.put(this.baseUrl + "/{key}", 
//					boundary, 
//					element.getKey());
//		
//		// THEN the database contains updated details
//		assertThat(this.elementDao.readById(element.getKey()))
//			.isNotNull()
//			.isPresent()
//			.get()
//			.extracting("key", "moreAttributes")
//			.containsExactly(element.getKey(), newDetails);
//		
//	}
//	
//	@Test
//	public void testDeleteByKey() throws Exception{
//		// GIVEN the database contains a single message
//		String key = 
//		this.elementDao
//			.create(generator.getElement())
//			.getKey();
//		
//		// WHEN I delete using the message key
//		this.restTemplate
//			.delete(this.baseUrl + "/{key}", key);
//		
//		// THEN the database is empty
//		assertThat(this.elementDao
//				.readAll())
//			.isEmpty();
//	}
//	
//	@Test
//	public void testDeleteByKeyWhileDatabseIsNotEmptyAtTheEnd() throws Exception{
//		// GIVEN the database contains 101 messages
//		List<ElementEntity> all101Messages = 
//		IntStream.range(1, 102)
//			.mapToObj(i->generator.getElement())
//			.map(this.elementDao::create)
//			.collect(Collectors.toList());
//		
//		ElementEntity third = all101Messages.get(2);
//		
//		String thridKey = 
//				third
//				.getKey();
//		
//		// WHEN I delete the 3rd message using the message key
//		this.restTemplate
//			.delete(this.baseUrl + "/{key}", thridKey);
//		
//		// THEN the database contains 100 messages 
//		// AND the database does not contain the deleted message
//		assertThat(this.elementDao
//				.readAll())
//			.hasSize(100)
//			.usingElementComparatorOnFields("key")
//			.doesNotContain(third);
//	}
	

	
}
