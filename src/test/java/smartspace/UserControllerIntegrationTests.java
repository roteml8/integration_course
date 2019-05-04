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
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.FakeElementGenerator;
import smartspace.data.util.FakeUserGenerator;
import smartspace.infra.UserService;
import smartspace.layout.ElementBoundary;
import smartspace.layout.UserBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties= {"spring.profiles.active=default"})

public class UserControllerIntegrationTests {
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
					this.baseUrl +"{adminSmartspace}/{adminEmail}", 
					newUser, 
					UserBoundary.class, 
					"2019B.Amitz4.SmartSpace","tom@gmail.com");
	
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
	public void testGetAllMessagesUsingPaginationAndValidateContentWithAllAttributeValidation() throws Exception{
		// GIVEN the database contains 4 messages
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
			UserEntity rv = this.userService.newUser(e, "2019B.Amitz4.SmartSpace#Email");
			all.add(new UserBoundary(rv));
		}
		
		all.add(0, new UserBoundary(admin));
		
		
		// WHEN I GET messages of size 10 and page 0
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
		
		// WHEN I GET messages of size 1 and page 2
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
