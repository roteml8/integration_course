package smartspace;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.infra.UserService;
import smartspace.layout.UserBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties= {"spring.profiles.active=default"})

public class UserControllerIntegrationTests {
	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedUserDao<Long> userDao;
	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setUserDao(EnhancedUserDao<Long> userDao) {
		this.userDao = userDao;
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}
	
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/messagedemo";
	}
	
	@After
	public void tearDown() {
		this.userDao
			.deleteAll();
	}
	
	@Test
	public void testPostNewUser() throws Exception{
		
		Map<String, Object> details = new HashMap<>();
		details.put("y", 10.0);
		details.put("x", "10");
		UserBoundary newUser = new UserBoundary();
		newUser.setUsername("Demo1");
		newUser.setAvatar("avatar");;
		newUser.setRole(UserRole.ADMIN);
		newUser.setPoints(111);
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{code}", 
					newUser, 
					UserBoundary.class, 
					1332);
		
		assertThat(this.userDao
			.readAll())
			.hasSize(1);
	}
	
	@Test(expected=Exception.class)
	public void testPostNewUserWithBadCode() throws Exception{
		
		int code = 133;
		Map<String, Object> details = new HashMap<>();
		details.put("y", 10.0);
		details.put("x", "10");
		UserBoundary newUser = new UserBoundary();
		newUser.setUsername("Demo1");
		newUser.setAvatar("avatar");;
		newUser.setRole(UserRole.ADMIN);
		newUser.setPoints(111);
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{code}", 
					newUser, 
					UserBoundary.class, 
					code);
	
	}
	
	@Test
	public void testGetAllUsersUsingPagination() throws Exception{
		int size = 3;
		IntStream.range(1, size + 1)
			.mapToObj(i->new UserEntity("demo" + i))
			.forEach(this.userDao::create);
		
		UserBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={page}", 
					UserBoundary[].class, 
					10, 0);
		
		assertThat(response)
			.hasSize(size);
	}

	@Test
	public void testGetAllUsersUsingPaginationAndValidateContent() throws Exception{
		int size = 3;
		java.util.List<UserBoundary> all = 
		IntStream.range(1, size + 1)
			.mapToObj(i->new UserEntity("demo" + i))
			.map(this.userDao::create)
			.map(UserBoundary::new)
			.collect(Collectors.toList());
		
		UserBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={page}", 
					UserBoundary[].class, 
					10, 0);
		
		assertThat(response)
			.usingElementComparatorOnFields("key")
			.containsExactlyElementsOf(all);
	}
	
	
	@Test
	public void testGetAllMessagesUsingPaginationAndValidateContentWithAllAttributeValidation() throws Exception{
		// GIVEN the database contains 4 messages
		int size = 4;
		Map<String, Object> details = new HashMap<>();
		details.put("y", 10.0);
		details.put("x", "10");
		String code = "1";
		
		java.util.List<UserBoundary> all = 
		IntStream.range(1, size + 1)
			.mapToObj(i->new UserEntity("demo" + i))
			.peek(msg->msg.setUsername(msg.getUsername()))
			.peek(msg->msg.setRole((Math.random() > 0.5)?UserRole.ADMIN:UserRole.MANAGER))
			.peek(msg->msg.setDetails(details))
			.map(msg->this.userService.newUser(msg, code))
			.map(UserBoundary::new)
			.collect(Collectors.toList());
		
		
		UserBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={page}", 
					UserBoundary[].class, 
					10, 0);
		
		assertThat(response)
			.usingElementComparatorOnFields("userName", "userEmail", "key")
			.containsExactlyElementsOf(all);
	}
	
	
	@Test
	public void testGetAllUsersUsingPaginationOfSecondPage() throws Exception{
		List<UserEntity> all = 
		IntStream.range(0,11)
			.mapToObj(i->new UserEntity("user" + i))
			.map(this.userDao::create)
			.collect(Collectors.toList());
		
		
		UserBoundary last =
			all
			.stream()
			.skip(10)
			.limit(1)
			.map(UserBoundary::new)
			.findFirst()
			.orElseThrow(()->new RuntimeException("no users after skipping"));
		
		// WHEN I GET messages of size 10 and page 1
		UserBoundary[] result = this.restTemplate
			.getForObject(
					this.baseUrl + "?page={page}&size={size}", 
					UserBoundary[].class, 
					1, 10);
		
		// THEN the result contains a single message (last message)
		assertThat(result)
			.usingElementComparator((b1,b2)->b1.toString().compareTo(b2.toString()))
			.containsExactly(last);
	}
	
	@Test
	public void testGetAllUsersUsingPaginationOfSecondNonExistingPage() throws Exception{
		IntStream
			.range(0, 10)
			.forEach(i->this.userDao.create(new UserEntity("user" + i)));
		
		String[] result = 
		  this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={pp}", 
					String[].class, 
					10, 1);
		
		assertThat(result)
			.isEmpty();
		
	}
	
	@Test
	public void testUpdateMessage() throws Exception{
		// GIVEN the database contains a message
		UserEntity user = new UserEntity("test");
		user.setDetails(new HashMap<>());
		user = this.userDao
			.create(user);
		
		Map<String, Object> newDetails = new HashMap<>();
		newDetails.put("x", 10.0);
		newDetails.put("y", "new details");
		newDetails.put("expired", true);
		
		UserBoundary boundry = new UserBoundary();
		boundry.setDetails(newDetails);
		this.restTemplate
			.put(this.baseUrl + "/{key}", 
					boundry, 
					user.getKey());
		
		assertThat(this.userDao.readById(user.getKey()))
			.isNotNull()
			.isPresent()
			.get()
			.extracting("key", "details")
			.containsExactly(user.getKey(), newDetails);
		
	}
	
	@Test
	public void testDeleteByKey() throws Exception{
		String key = 
		this.userDao
			.create(new UserEntity("test"))
			.getKey();
		
		// WHEN I delete using the message key
		this.restTemplate
			.delete(this.baseUrl + "/{key}", key);
		
		// THEN the database is empty
		assertThat(this.userDao
				.readAll())
			.isEmpty();
	}
	
	@Test
	public void testDeleteByKeyWhileDatabseIsNotEmptyAtTheEnd() throws Exception{
		List<UserEntity> all101Messages = 
		IntStream.range(1, 102)
			.mapToObj(i->new UserEntity("message #" + i))
			.map(this.userDao::create)
			.collect(Collectors.toList());
		
		UserEntity third = all101Messages.get(2);
		
		String thridKey = 
				third
				.getKey();
		
		this.restTemplate
			.delete(this.baseUrl + "/{key}", thridKey);
	
		assertThat(this.userDao
				.readAll())
			.hasSize(100)
			.usingElementComparatorOnFields("key")
			.doesNotContain(third);
	}
	
	
	
	
	
	
	
}
