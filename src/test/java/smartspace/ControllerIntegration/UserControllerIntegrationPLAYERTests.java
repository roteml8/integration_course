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

	@Autowired
	public void setGenerator(FakeUserGenerator generator) {
		this.generator = generator;
	}

	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String mySmartspace) {
		this.mySmartspace = mySmartspace;
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
		UserEntity manager = generator.getUser();
		manager.setRole(userRoleToCheck);
		manager.setUserSmartspace(null);

		NewUserForm userForm = new NewUserForm(manager);

		this.restTemplate.postForObject(this.baseUrl, userForm, NewUserForm.class);

		// THEN the database contains a single user
		// AND this user's email , avatar ,role and username fields
		// are exactly the same as the fields in userForm
		// AND his smartspace field is the same as the local project's smartspace
		List<UserEntity> rv = this.userDao.readAll();
		assertThat(rv).hasSize(1);

		assertThat(rv.get(0)).isNotNull()
				.extracting("userEmail", "username", "avatar", "role", "userSmartspace", "points")
				.containsExactly(manager.getUserEmail(), manager.getUsername(), manager.getAvatar(), manager.getRole(),
						this.mySmartspace, Long.MIN_VALUE);

	}
	
	@Test (expected = HttpClientErrorException.class)
	public void testPostNewUserWithBadForm() throws Exception {
		// GIVEN the user database is empty

		// WHEN I POST new user with a new user form that have no email
		UserEntity manager = generator.getUser();
		manager.setRole(null);
		manager.setUserEmail(null);
		manager.setUserSmartspace(null);

		NewUserForm userForm = new NewUserForm(manager);

		try {
		this.restTemplate.postForObject(
				this.baseUrl,
				userForm,
				NewUserForm.class);
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
		UserEntity manager = generator.getUser();
		manager.setRole(userRoleToCheck);
		this.userDao.create(manager);

		// WHEN i login with manager's smartspace and email
		UserBoundary result = this.restTemplate.getForObject(
				this.baseUrl + this.loginUrl,
				UserBoundary.class,
				manager.getUserSmartspace(),
				manager.getUserEmail());

		// THEN the login will retrieve the user's details.
		assertThat(result).isNotNull().isEqualToComparingFieldByField(new UserBoundary(manager));
	}

	@Test
	public void testPostNewUserAndLogin() throws Exception {
		// GIVEN the user database is empty

		// WHEN I POST new user with new user form
		UserEntity manager = generator.getUser();
		manager.setRole(userRoleToCheck);
		manager.setUserSmartspace(null);

		NewUserForm userForm = new NewUserForm(manager);

		this.restTemplate.postForObject(this.baseUrl, userForm, NewUserForm.class);

		// And i login with manager's smartspace and email
		UserBoundary result = this.restTemplate.getForObject(this.baseUrl + this.loginUrl, UserBoundary.class,
				this.mySmartspace, manager.getUserEmail());

		// THEN the database contains a single user
		// AND this user's email , avatar ,role and username fields
		// are exactly the same as the fields in userForm
		// AND his smartspace field is the same as the local project's smartspace
		// AND the login will retrieve the user's details.

		List<UserEntity> rv = this.userDao.readAll();
		assertThat(rv).hasSize(1);

		assertThat(result.convertToEntity()).isNotNull()
				.extracting("userEmail", "username", "avatar", "role", "userSmartspace", "points")
				.containsExactly(manager.getUserEmail(), manager.getUsername(), manager.getAvatar(), manager.getRole(),
						this.mySmartspace, Long.MIN_VALUE);

	}

	@Test
	public void testPutUpdateWithUserInDatabase() throws Exception {
		// GIVEN the user database contains only manager
		UserEntity manager = generator.getUser();
		manager.setRole(userRoleToCheck);
		this.userDao.create(manager);

		// WHEN i update manager's details with updetedUser details using PUT
		UserEntity updatedUser = generator.getUser();
		updatedUser.setUserEmail(manager.getUserEmail());
		updatedUser.setUserSmartspace(manager.getUserSmartspace());

		this.restTemplate.put(this.baseUrl + this.loginUrl, new UserBoundary(updatedUser), manager.getUserSmartspace(),
				manager.getUserEmail());

		// THEN the user in the database will have details exactly like updatedUser
		// except for their points.
		assertThat(this.userDao.readAll().get(0)).isNotNull()
				.extracting("userEmail", "username", "avatar", "role", "userSmartspace", "points")
				.containsExactly(updatedUser.getUserEmail(), updatedUser.getUsername(), updatedUser.getAvatar(),
						updatedUser.getRole(), updatedUser.getUserSmartspace(), manager.getPoints());
	}

	@Test(expected = Exception.class)
	//@Test(expected = HttpClientErrorException.class)
	public void testPutUpdateWithUserNotInDatabase() throws Exception {
		// GIVEN the user database contains only manager
		UserEntity manager = generator.getUser();
		manager.setRole(userRoleToCheck);
		this.userDao.create(manager);

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
			assertThat(this.userDao.readAll().get(0)).isEqualToComparingFieldByField(manager);

			throw exception;
		}

	}
}
