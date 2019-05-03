package smartspace;

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
public class ElementControllerIntegrationTests {
	
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
		this.baseUrl = "http://localhost:" + port + "/elementdemo";
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
		e.setElementSmartSpace("smartspace");
		e.setElementid("123");
		ElementBoundary newElement = new ElementBoundary(e);
		System.out.println(e.toString());
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					newElement, 
					ElementBoundary.class, 
					"2019B.Amitz4.SmartSpace","Email");
		
		// THEN the database contains a single element
		assertThat(this.elementDao
			.readAll())
			.hasSize(1);
	}
	
	@Test(expected=Exception.class)
	public void testPostNewElementNoAdmin() throws Exception{
		
		// GIVEN the element database is empty and user database contains an admin
		UserEntity admin = new UserEntity();
		admin.setUserEmail("EmailNotAdmin");
		admin.setUserSmartspace("SmartspaceNotAdmin");
		admin.setRole(UserRole.PLAYER);
		// WHEN I POST new element with smartspace and email that belong to a user who is not an admin 
		ElementBoundary newElement = new ElementBoundary(generator.getElement());
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					newElement, 
					ElementBoundary.class, 
					"SmartspaceNotAdmin","EmailNotAdmin");
		
		// THEN the test ends with exception
	}
	
	@Test
	public void testGetAllElementsUsingPagination() throws Exception{
		// GIVEN the database contains 3 elements
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
					this.baseUrl + "?size={size}&page={page}", 
					ElementBoundary[].class, 
					10, 0);
		
		// THEN I receive 3 elements 
		assertThat(response)
			.hasSize(size);
	}
	
	@Test
	public void testGetAllElementsUsingPaginationAndValidateContent() throws Exception{
		// GIVEN the database contains 3 messages
		int size = 3;
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		java.util.List<ElementBoundary> all = 
		IntStream.range(1, size + 1)
			.mapToObj(i->generator.getElement())
			.map(this.elementDao::create)
			.map(ElementBoundary::new)
			.collect(Collectors.toList());
		
		// WHEN I GET messages of size 10 and page 0
		ElementBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={page}", 
					ElementBoundary[].class, 
					10, 0);
		
		// THEN I receive the exact 3 messages written to the database
		assertThat(response)
			.usingElementComparatorOnFields("key")
			.containsExactlyElementsOf(all);
	}
	
	@Test
	public void testGetAllMessagesUsingPaginationAndValidateContentWithAllAttributeValidation() throws Exception{
		// GIVEN the database contains 4 messages
		int size = 4;
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		List<ElementBoundary> all = new ArrayList<>();
		for (int i = 1; i<=size; i++)
		{
			ElementEntity e = generator.getElement();
			e.setElementid(String.valueOf(i));
			e.setElementSmartSpace("Space");
			ElementEntity rv = this.elementService.newElement(e, "2019B.Amitz4.SmartSpace", "Email");
			all.add(new ElementBoundary(rv));
		}

		
		
		// WHEN I GET messages of size 10 and page 0
		ElementBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={page}", 
					ElementBoundary[].class, 
					10, 0);
	
		// THEN I receive the exact messages written to the database
		assertThat(response)
			.usingElementComparatorOnFields("key", "name", "location", "type", "expired","creator","creationTimeStamp","moreAttributes")
			.containsExactlyElementsOf(all);
	}
	
	@Test
	public void testGetAllMessagesUsingPaginationOfSecondPage() throws Exception{
		// GIVEN then database contains 11 messages
		List<ElementEntity> all = 
		IntStream.range(0,11)
			.mapToObj(i->generator.getElement())
			.map(this.elementDao::create)
			.collect(Collectors.toList());
		
//		MessageBoundary last = new MessageBoundary( 
//		  all
//			.stream()
//			.sorted((e1,e2)->e2.getKey().compareTo(e1.getKey()))
//			.findFirst()
//			.orElseThrow(()->new RuntimeException("no messages after sorting")));
		
		ElementBoundary last =
			all
			.stream()
			.skip(10)
			.limit(1)
			.map(ElementBoundary::new)
			.findFirst()
			.orElseThrow(()->new RuntimeException("no elements after skipping"));
		
		// WHEN I GET messages of size 10 and page 1
		ElementBoundary[] result = this.restTemplate
			.getForObject(
					this.baseUrl + "?page={page}&size={size}", 
					ElementBoundary[].class, 
					2, 1);
		
		// THEN the result contains a single message (last message)
		assertThat(result)
			.usingElementComparator((b1,b2)->b1.toString().compareTo(b2.toString()))
			.containsExactly(last);
	}
	
	@Test
	public void testGetAllMessagesUsingPaginationOfSecondNonExistingPage() throws Exception{
		// GIVEN the database contains 10 messages
		IntStream
			.range(0, 10)
			.forEach(i->this.elementDao.create(generator.getElement()));
		
		// WHEN I GET messages of size 10 and page 1
		ElementBoundary[] result = 
		  this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={pp}", 
					ElementBoundary[].class, 
					1, 2);
		
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
