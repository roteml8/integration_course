package smartspace.ControllerIntegration;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.fail;

import java.util.ArrayList;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.*;
import smartspace.infra.UserService;
import smartspace.layout.UserBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties= {"spring.profiles.active=default"})

public class UserControllerIntegrationADMINTests {
	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedUserDao<String> userDao;
	private UserService userService;
	private FakeUserGenerator generator;
	
	@Autowired
	public void setGenerator(FakeUserGenerator generator) {
		this.generator = generator;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
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
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/users/";
	}
	
	@After
	public void tearDown() {
		this.userDao
			.deleteAll();
	}
	
	@Test
	public void testPostNewUser() throws Exception{
		UserEntity admin = new UserEntity();
		admin.setUserEmail("tom@gmail.com");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		UserEntity e = new UserEntity();
		e.setUserSmartspace("smartspace");
		e.setUserEmail("t@gm.com");
		e.setAvatar("avatar");
		e.setPoints(111);
		e.setRole(UserRole.ADMIN);
		e.setUsername("Tom");
		e.setKey("smartspace#t@gm.com");
		UserBoundary newUser = new UserBoundary(e);

		UserBoundary[] arr = new UserBoundary[1];
		arr[0] = newUser;
		
		this.restTemplate
			.postForObject(
					this.baseUrl + "{adminSmartspace}/{adminEmail}", 
					arr, 
					UserBoundary[].class, 
					"2019B.Amitz4.SmartSpace","tom@gmail.com");
		
		assertThat(this.userDao
			.readAll())
			.hasSize(2);
	}
	

	@Test (expected=HttpClientErrorException.class)
	public void testPostNewUserInvalid() throws Exception{
		
		// GIVEN the user database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST an invalid user
		// with email&smartspace of the admin 
		
		UserEntity e = generator.getUser();
		// e has no smartspace and no id 
		e.setUsername(null);
		e.setUserSmartspace("bla");
		UserBoundary newUser = new UserBoundary(e);
		UserBoundary[] arr = new UserBoundary[1];
		arr[0] = newUser;
		try {
			this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr, 
					UserBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email");
			}
		// THEN the database contains only the admin
		// AND post method throws the correct exception 
			catch(HttpClientErrorException exception) {
				assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
				assertThat(this.userDao
						.readAll().get(0))
						.isEqualToComparingFieldByField(admin);
				throw exception;
			}	
	}
	
	@Test
	public void testPostUserWithExistingKey() throws Exception{
		
		// GIVEN the user database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST new user with email&smartspace of the admin 
		
		UserEntity e = generator.getUser();
		e.setUserSmartspace("space");
		e.setKey("email#space");
		UserBoundary newUser1 = new UserBoundary(e);
		UserBoundary[] arr = new UserBoundary[1];
		arr[0] = newUser1;
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr, 
					UserBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email");
		
		// AND I POST another user with the same key 
		UserEntity e2 = generator.getUser();
		e2.setUserSmartspace("space");
		e2.setKey("email#space");
		arr[0] = new UserBoundary(e2);
		this.restTemplate
		.postForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
				arr, 
				UserBoundary[].class, 
				"2019B.Amitz4.SmartSpace","Email");

		/*
		
		assertThat(this.userDao
			.readAll())
			.hasSize(2);
			*/
		
		/*
		UserBoundary[] response = 
				this.restTemplate
					.getForObject(
							this.baseUrl + "{adminSmartspace}/{adminEmail}?page={page}&psize={size}", 
							UserBoundary[].class, 
							"2019B.Amitz4.SmartSpace","tom@gmail.com",0, 10);
		*/
		
		// THEN the user in the database is the second user posted
		assertThat(this.userDao
				.readAll().get(1)).isEqualToComparingFieldByField(e2);
		
	}
	
	@Test(expected=HttpClientErrorException.class)
	public void testPostNewUserSameSmartspace() throws Exception{
		
		// GIVEN the user database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST new user with local smartspace
		// with the email&smartspace of the admin 
		
		UserEntity e = generator.getUser();
		e.setUserSmartspace("2019B.Amitz4.SmartSpace");
		e.setUserEmail(admin.getUserEmail());
		UserBoundary newUser = new UserBoundary(e);
		UserBoundary[] arr = new UserBoundary[1];
		arr[0] = newUser;
		try {
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr, 
					UserBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email");
		}
		// THEN the database is contains only the admin
		// AND post method throws an exception 
		catch(HttpClientErrorException exception) {
		assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		assertThat(this.userDao
			.readAll())
			.hasSize(1);
			throw exception;
		}			
			
	}
	
	@Test(expected=HttpClientErrorException.class)
	public void testPostTwoUsersOneLocalSecondExternal() throws Exception{
		
		// GIVEN the user database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST  an array containing an user from local smartspace
		// and an user with external smartspace
		// with the email&smartspace of the admin 
		
		UserEntity e = generator.getUser();
		
		e.setUserSmartspace("2019B.Amitz4.SmartSpace");
		e.setUserEmail("Email");
		
		
		UserEntity e2 = generator.getUser();
		e2.setUserSmartspace("Space");
		e2.setUserEmail("Email");
		
		UserBoundary newUser1 = new UserBoundary(e);
		UserBoundary newUser2 = new UserBoundary(e2);
		UserBoundary[] arr = new UserBoundary[2];
		arr[1] = newUser1;
		arr[0] = newUser2;
		try {
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr, 
					UserBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email");
		}
		// THEN the database is empty
		// and Post method throws an exception 
		catch(HttpClientErrorException exception) {
		assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		assertThat(this.userDao
			.readAll())
			.hasSize(1);
		throw exception;
		}
	}
	
	@Test(expected=HttpClientErrorException.class)
	public void testPostTwoUsersOneLocalSecondExternalNotTogether() throws Exception{
		
		// GIVEN the user database is empty and user database contains an admin
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		// WHEN I POST one user from local smartspace
		// and POST one user with external smartspace
		// with the email&smartspace of the admin 
		
		UserEntity e = generator.getUser();
		e.setUserSmartspace("2019B.Amitz4.SmartSpace");
		e.setUserEmail("Email");
		UserEntity e2 = generator.getUser();
		e2.setUserSmartspace("Space");
		e2.setUserEmail("Email");
		UserBoundary newUser1 = new UserBoundary(e);
		UserBoundary newUser2 = new UserBoundary(e2);
		UserBoundary[] arr1 = new UserBoundary[1];
		UserBoundary[] arr2 = new UserBoundary[1];
		arr1[0] = newUser1;
		arr2[0] = newUser2;
		
		try {
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr1, 
					UserBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email");
		}
		catch(HttpClientErrorException exception) {
		assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		this.restTemplate
		.postForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
				arr2, 
				UserBoundary[].class, 
				"2019B.Amitz4.SmartSpace","Email");
	
		// THEN the database contains the external and admin user only 
		// and Post method throws an exception 
		assertThat(this.userDao
			.readAll())
			.hasSize(2);
			
		assertThat(this.userDao
				.readAll().get(0)).isEqualToComparingFieldByField(admin);
		
		assertThat(this.userDao
				.readAll().get(1)).isEqualToComparingFieldByField(e2);
		
		throw exception;
		}
	}
	
	@Test(expected=HttpClientErrorException.class)
	public void testPostNewUserNoAdmin() throws Exception{
		
		// GIVEN the user database is empty and user database contains a player
		UserEntity player = new UserEntity();
		player.setUserEmail("EmailNotAdmin");
		player.setUserSmartspace("SmartspaceNotAdmin");
		player.setRole(UserRole.PLAYER);
		this.userDao.create(player);

		// WHEN I POST new user with smartspace and email that belong to the player 
		UserEntity e = generator.getUser();
		e.setUserEmail("mail");
		e.setUserSmartspace("space");
		UserBoundary newUser = new UserBoundary(e);
		UserBoundary[] arr = new UserBoundary[1];
		arr[0] = newUser;
		try {
		this.restTemplate
			.postForObject(
					this.baseUrl + "/{adminSmartspace}/{adminEmail}", 
					arr, 
					UserBoundary[].class, 
					"SmartspaceNotAdmin","EmailNotAdmin");
		}
		catch(HttpClientErrorException exception) {
		// THEN the test ends with exception
		assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		throw exception;
		}
	}
	
	
	@Test
	public void testGetAllUsersUsingPagination() throws Exception{
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		int size = 3;
		IntStream.range(1, size + 1)
			.mapToObj(i->new UserEntity("demo" + i))
			.forEach(this.userDao::create);
		
		UserBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + "{adminSmartspace}/{adminEmail}?page={page}&psize={size}", 
					UserBoundary[].class, 
					"2019B.Amitz4.SmartSpace","tom@gmail.com",0, 10);
		
		assertThat(response)
			.hasSize(size+1);
	}

	@Test
	public void testGetAllUsersUsingPaginationAndValidateContent() throws Exception{
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
	
		int size = 3;
		java.util.List<UserBoundary> all = 
		IntStream.range(1, size + 1)
			.mapToObj(i->new UserEntity("demo" + i))
			.map(this.userDao::create)
			.map(UserBoundary::new)
			.collect(Collectors.toList());
		
		all.add(0, new UserBoundary(admin));
		
		UserBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + "{adminSmartspace}/{adminEmail}?page={page}&psize={size}", 
					UserBoundary[].class, 
					"2019B.Amitz4.SmartSpace","tom@gmail.com",0, 10);
		
		assertThat(response)
			.usingElementComparatorOnFields("key")
			.containsExactlyElementsOf(all);
	}
	
	
	@Test
	public void testGetAllUsersUsingPaginationAndValidateContentWithAllAttributeValidation() throws Exception{
		
		int size = 4;
		
		UserEntity admin = new UserEntity();
		admin.setUserEmail("Email");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
		
		List<UserBoundary> all = new ArrayList<>();
		for (int i = 1; i<=size; i++)
		{
			UserEntity e = generator.getUser();
			e.setUserSmartspace("Space"+i);
			e.setUserEmail("t@g.co.il"+i);
			UserEntity rv = this.userDao.create(e);
			all.add(new UserBoundary(rv));
		}
		
		all.add(0, new UserBoundary(admin));
		
		
		// WHEN I GET user of size 10 and page 0
		UserBoundary[] response = 
		this.restTemplate
			.getForObject(
					this.baseUrl + "{adminSmartspace}/{adminEmail}?page={page}&psize={size}", 
					UserBoundary[].class, 
					"2019B.Amitz4.SmartSpace","Email",0, 10);
	
		// THEN I receive the exact users to the database
		assertThat(response)
			.usingElementComparatorOnFields("key")
			.containsExactlyElementsOf(all);
	}
	
	
	@Test
	public void testGetAllUsersUsingPaginationOfSecondPage() throws Exception{
		UserEntity admin = new UserEntity();
		admin.setUserEmail("tom@gmail.com");
		admin.setUserSmartspace("2019B.Amitz4.SmartSpace");
		admin.setRole(UserRole.ADMIN);
		this.userDao.create(admin);
	
		
		List<UserEntity> all = 
		IntStream.range(0,10)
			.mapToObj(i->new UserEntity("user" + i))
			.map(this.userDao::create)
			.collect(Collectors.toList());
		
		
		UserBoundary last =
			all
			.stream()
			.skip(9)
			.limit(1)
			.map(UserBoundary::new)
			.findFirst()
			.orElseThrow(()->new RuntimeException("no users after skipping"));
		
		// WHEN I GET user of size 1 and page 2
		UserBoundary[] result = this.restTemplate
			.getForObject(
					this.baseUrl + "{adminSmartspace}/{adminEmail}?page={page}&psize={size}", 
					UserBoundary[].class, 
					"2019B.Amitz4.SmartSpace","tom@gmail.com",1, 2);
		
		// THEN the result contains a single user
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
					this.baseUrl + "{adminSmartspace}/{adminEmail}?page={page}&psize={size}", 
					String[].class, 
					"2019B.Amitz4.SmartSpace","tom@gmail.com",1, 2);
		
		assertThat(result)
			.isEmpty();
		
	}
	
		
	
}
