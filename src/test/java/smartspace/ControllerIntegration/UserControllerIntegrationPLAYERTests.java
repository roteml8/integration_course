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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.FakeUserGenerator;
import smartspace.layout.NewUserForm;
import smartspace.layout.UserBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class UserControllerIntegrationPLAYERTests {

	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedUserDao<String> userDao;
	private FakeUserGenerator generator;
	private String mySmartspace;
	private String loginUrl;
	private UserRole userRoleToCheck;
	private String adminKeyUrl;
	private String baseAdminUrl;
	private long defualtStartingPoints;

	@Autowired
	public void setGenerator(FakeUserGenerator generator) {
		this.generator = generator;
	}

	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String mySmartspace) {
		this.mySmartspace = mySmartspace;
	}
	
	@Value("${defualt.starting.points:100}")
	public void setdefualtStartingPoints(String points) {
		this.defualtStartingPoints = Long.parseLong(points);
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
		this.baseUrl = "http://localhost:" + port + "/smartspace/users";
		this.loginUrl = "/login/{userSmartspace}/{userEmail}";
		this.baseAdminUrl = "http://localhost:" + port + "/smartspace/admin/users/";
		this.adminKeyUrl = "/{adminSmartspace}/{adminEmail}";
		this.userRoleToCheck = UserRole.PLAYER;
	}

	@Before
	public void setUp() {
		this.userDao.deleteAll();
	}

	@After
	public void tearDown() {
		this.userDao.deleteAll();
	}

	@Test
	public void testPostNewUser() throws Exception {
		// GIVEN the user database is empty

		// WHEN I POST new user with new user form
		UserEntity player = generator.getUser();
		player.setRole(userRoleToCheck);
		player.setUserSmartspace(null);

		NewUserForm userForm = new NewUserForm(player);

		UserBoundary recivedBoundary = this.restTemplate.
				postForObject(
						this.baseUrl,
						userForm,
						UserBoundary.class);

		// THEN the database contains a single user
		// AND this user's email , avatar ,role and username fields
		// are exactly the same as the fields in userForm
		// AND his smartspace field is the same as the local project's smartspace
		// AND the received boundary from the post is the same as the entity in the DB
		List<UserEntity> rv = this.userDao.readAll();
		assertThat(rv).hasSize(1);

		assertThat(rv.get(0)).isNotNull()
				.extracting("userEmail", "username", "avatar", "role", "userSmartspace", "points")
				.containsExactly(player.getUserEmail(), player.getUsername(), player.getAvatar(), player.getRole(),
						this.mySmartspace, defualtStartingPoints);
		
		assertThat(rv.get(0)).isNotNull()
		.isEqualToComparingFieldByField(recivedBoundary.convertToEntity());

	}
	
	@Test (expected = HttpClientErrorException.class)
	public void testPostNewUserWithBadForm() throws Exception {
		// GIVEN the user database is empty

		// WHEN I POST new user with a new user form that have no email
		UserEntity player = generator.getUser();
		player.setRole(null);
		player.setUserEmail(null);
		player.setUserSmartspace(null);

		NewUserForm userForm = new NewUserForm(player);

		try {
		this.restTemplate.postForObject(
				this.baseUrl,
				userForm,
				UserBoundary.class);
		} catch (HttpClientErrorException exception) {
			
		// THEN an exception will be thrown.
		// AND the database will stay empty.
		assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
		List<UserEntity> rv = this.userDao.readAll();
		assertThat(rv).hasSize(0);
		throw exception;
		}
		

	}


	@Test
	public void testGetLoginWithValidUser() throws Exception {
		// GIVEN the user database contains manager
		UserEntity player = generator.getUser();
		player.setRole(userRoleToCheck);
		this.userDao.create(player);

		// WHEN i login with manager's smartspace and email
		UserBoundary result = this.restTemplate.getForObject(
				this.baseUrl + this.loginUrl,
				UserBoundary.class,
				player.getUserSmartspace(),
				player.getUserEmail());

		// THEN the login will retrieve the user's details.
		assertThat(result).isNotNull().isEqualToComparingFieldByField(new UserBoundary(player));
	}

	@Test
	public void testPostNewUserAndLogin() throws Exception {
		// GIVEN the user database is empty

		// WHEN I POST new user with new user form
		UserEntity player = generator.getUser();
		player.setRole(userRoleToCheck);
		player.setUserSmartspace(null);

		NewUserForm userForm = new NewUserForm(player);

		UserBoundary postResult = this.restTemplate.postForObject(this.baseUrl, userForm, UserBoundary.class);

		// And i login with manager's smartspace and email
		UserBoundary loginResult =  this.restTemplate.getForObject(this.baseUrl + this.loginUrl, UserBoundary.class,
				this.mySmartspace, player.getUserEmail());

		// THEN the database contains a single user
		// AND this user's email , avatar ,role and username fields
		// are exactly the same as the fields in userForm
		// AND his smartspace field is the same as the local project's smartspace
		// AND the login will retrieve the user's details.

		List<UserEntity> rv = this.userDao.readAll();
		assertThat(rv).hasSize(1);

		assertThat(loginResult.convertToEntity()).isNotNull()
				.extracting("userEmail", "username", "avatar", "role", "userSmartspace", "points")
				.containsExactly(player.getUserEmail(), player.getUsername(), player.getAvatar(), player.getRole(),
						this.mySmartspace, defualtStartingPoints);

		assertThat(postResult)
		.isEqualToComparingFieldByField(loginResult);
	}

	@Test
	public void testPutUpdateWithUserInDatabase() throws Exception {
		// GIVEN the user database contains only player
		UserEntity player = generator.getUser();
		player.setRole(userRoleToCheck);
		this.userDao.create(player);

		// WHEN i update player's details with updetedUser details using PUT
		UserEntity updatedUser = generator.getUser();
		updatedUser.setUserEmail(player.getUserEmail());
		updatedUser.setUserSmartspace(player.getUserSmartspace());

		this.restTemplate.put(this.baseUrl + this.loginUrl, new UserBoundary(updatedUser), player.getUserSmartspace(),
				player.getUserEmail());

		// THEN the user in the database will have details exactly like updatedUser
		// except for their points.
		assertThat(this.userDao.readAll().get(0)).isNotNull()
				.extracting("userEmail", "username", "avatar", "role", "userSmartspace", "points")
				.containsExactly(updatedUser.getUserEmail(), updatedUser.getUsername(), updatedUser.getAvatar(),
						updatedUser.getRole(), updatedUser.getUserSmartspace(), player.getPoints());
	}

	@Test(expected = Exception.class)
	//@Test(expected = HttpClientErrorException.class)
	public void testPutUpdateWithUserNotInDatabase() throws Exception {
		// GIVEN the user database contains only player
		UserEntity player = generator.getUser();
		player.setRole(userRoleToCheck);
		this.userDao.create(player);

		// WHEN i try to update a user not in the database
		UserEntity updatedUser = generator.getUser();
		try {
			this.restTemplate.put(this.baseUrl + this.loginUrl, new UserBoundary(updatedUser),
					updatedUser.getUserSmartspace(), updatedUser.getUserEmail());
		} catch (Exception exception) {
		//catch (HttpClientErrorException exception) {
			// THEN an exception will be thrown.
			// AND the user will stay unchanged.
			//assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
			assertThat(this.userDao.readAll().get(0)).isEqualToComparingFieldByField(player);

			throw exception;
		}

	}
	
	@Test(expected=HttpClientErrorException.class)
	public void testPostImportNewUsersAsPlayer() throws Exception{
		
		// GIVEN the user database is empty and user database contains a player
		UserEntity player = new UserEntity();
		player.setUserEmail("EmailNotAdmin@bla.com");
		player.setUserSmartspace("SmartspaceNotAdmin");
		player.setRole(this.userRoleToCheck);
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
					baseAdminUrl + adminKeyUrl, 
					arr, 
					UserBoundary[].class, 
					"SmartspaceNotAdmin@bla.com","EmailNotAdmin");
		}
		catch(HttpClientErrorException exception) {
		// THEN the test ends with exception
		// AND the user in the database in unchanged
		assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(this.userDao.readAll().get(0)).isNotNull().isEqualToComparingFieldByField(player);
		throw exception;
		}
	}
}
