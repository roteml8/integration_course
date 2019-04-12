package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.UserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;
import smartspace.infra.FakeUserGenerator;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class UserDaoIntegrationTests {

	private UserDao<String> dao;
	private EntityFactory factory;
	private String smartspace;

	@Autowired
	public void setDao(UserDao<String> dao) {
		this.dao = dao;
	}

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	@Before
	public void setup() {
		dao.deleteAll();
	}

	@After
	public void teardown() {
		dao.deleteAll();
	}
	
	public UserEntity fakeUserGenerator() {

		String generatedString = RandomString.make();

		String userSmartspace = smartspace;
		String userEmail = generatedString + "missroteml@gmail.com";
		String userName = generatedString + "rotemlevi";
		String userAvatar = generatedString + "cat";
		UserRole userRole = UserRole.values()[new Random().nextInt(UserRole.values().length)];
		long userPoints = new Random().nextLong();
		
		UserEntity fakeUser = this.factory.createNewUser(userEmail, userSmartspace, userName, userAvatar, userRole,
				userPoints);
		return fakeUser;
	}

	@Test(expected = Exception.class)
	public void testCreateWithNull() throws Exception {
		// GIVEN nothing

		// WHEN I create null user
		this.dao.create(null);

		// THEN create method throws exception
	}

	@Test
	public void testCreateWithValidUser() throws Exception {

		// GIVEN dao is initialized and empty

		// WHEN creating a user
		// AND adding the user to the dao
		/*
		String userSmartspace = smartspace;
		String userEmail = "missroteml@gmail.com";
		String userName = "rotemlevi";
		String userAvatar = "cat";
		UserRole userRole = UserRole.PLAYER;
		long userPoints = 100;
		UserEntity user = this.factory.createNewUser(userEmail, userSmartspace, userName, userAvatar, userRole,
				userPoints);
		*/
		
		UserEntity user = fakeUserGenerator();

		UserEntity userInDB = this.dao.create(user);

		// THEN the same user is in the dao
		assertThat(userInDB).isNotNull().isEqualToComparingFieldByField(user);

	}

	@Test
	public void testCreateDeleteAllReadAll() throws Exception {
		// GIVEN nothing

		// WHEN I create a new user and add it to dao
		// AND Delete all users
		// AND read all users
		/*
		String userEmail = "missroteml@gmail.com";
		String userName = "rotemlevi";
		String userAvatar = "cat";
		UserRole userRole = UserRole.PLAYER;
		long userPoints = 100;
		UserEntity user = this.factory.createNewUser(userEmail, smartspace, userName, userAvatar, userRole, userPoints);
*/
		
		UserEntity user = fakeUserGenerator();
		UserEntity userInDB = this.dao.create(user);

		this.dao.deleteAll();

		List<UserEntity> list = this.dao.readAll();

		// THEN the created user received a string key != null which is
		// userSmartspace+"#"+userEmail
		// AND the dao contains nothing
		assertThat(userInDB.getKey()).isNotNull().isEqualTo(user.getUserSmartspace() + "#" + user.getUserEmail());
		assertThat(list).isEmpty();

	}

	@Test
	public void testCreateUpdateReadPointsUpdated() throws Exception {
		// GIVEN nothing

		// WHEN I create a new user and add it to dao
		/*
		String userEmail = "missroteml@gmail.com";
		String username = "rotemlevi";
		String avatar = "cat";
		UserRole role = UserRole.PLAYER;
		long points = 100;
		UserEntity user = this.factory.createNewUser(userEmail, smartspace, username, avatar, role, points);
		*/
		
		UserEntity user = fakeUserGenerator();
		UserEntity userInDB = this.dao.create(user);

		// AND change the user details (username , avater, pooints) and update in the
		// dao
		UserEntity update = new UserEntity();
		update.setKey(userInDB.getKey());
		update.setUsername("rot");
		update.setAvatar("kitten");
		update.setPoints(34);
		this.dao.update(update);

		// AND read the user from the dao
		Optional<UserEntity> userFromDB = this.dao.readById(update.getKey());

		// THEN the returned object is the updated user merged with user at the fields
		// update didn't
		// change (role)
		assertThat(userFromDB.get()).isNotNull().extracting("userEmail", "username", "avatar", "role", "points")
				.containsExactly(update.getUserEmail(), update.getUsername(), update.getAvatar(), user.getRole(),
						update.getPoints());
	}

	@Test
	public void testCreateUpdateReadPointsNotUpdated() throws Exception {
		// GIVEN nothing

		// WHEN I create a new user and add it to dao
		/*
		String userEmail = "missroteml@gmail.com";
		String username = "rotemlevi";
		String avatar = "cat";
		UserRole role = UserRole.PLAYER;
		long points = 100;
		UserEntity user = this.factory.createNewUser(userEmail, smartspace, username, avatar, role, points);
		*/
		
		UserEntity user = fakeUserGenerator();

		UserEntity userInDB = this.dao.create(user);

		// AND change the user details (username , avater) and update in the dao
		UserEntity update = new UserEntity();
		update.setKey(userInDB.getKey());
		update.setUsername("rot");
		update.setAvatar("kitten");
		this.dao.update(update);

		// AND read the user from the dao
		Optional<UserEntity> userFromDB = this.dao.readById(update.getKey());

		// THEN the returned object is the updated user merged with user at the fields
		// update didn't
		// change (role, points and userSmartspace)
		assertThat(userFromDB.get()).isNotNull()
				.extracting("userEmail", "username", "avatar", "role", "points", "userSmartspace")
				.containsExactly(update.getUserEmail(), update.getUsername(), update.getAvatar(), user.getRole(),
						user.getPoints(), user.getUserSmartspace());
	}

	@Test
	public void testCreateReadById() throws Exception {
		// GIVEN nothing

		// WHEN I create a new user and add it to dao
		/*
		String userEmail = "missroteml@gmail.com";
		String username = "rotemlevi";
		String avatar = "cat";
		UserRole role = UserRole.PLAYER;
		long points = 100;
		UserEntity user = this.factory.createNewUser(userEmail, smartspace, username, avatar, role, points);
		*/
		
		UserEntity user = fakeUserGenerator();
		UserEntity userInDB = this.dao.create(user);

		// AND read the user from the dao
		Optional<UserEntity> userFromDB = this.dao.readById(userInDB.getKey());

		// THEN the userFromDB have the same fields as the new user
		assertThat(userFromDB.get()).isNotNull().isEqualToComparingFieldByField(user);
	}

	@Test
	public void testCreateReadByIdWithBadID() throws Exception {
		// GIVEN nothing

		// WHEN I create a new user and add it to dao
		
		/*
		String userEmail = "missroteml@gmail.com";
		String username = "rotemlevi";
		String avatar = "cat";
		UserRole role = UserRole.PLAYER;
		long points = 100;
		UserEntity user = this.factory.createNewUser(userEmail, smartspace, username, avatar, role, points);
		*/
		
		//UserEntity user = new FakeUserGenerator().getUser();
		UserEntity user = fakeUserGenerator();
		
		UserEntity userInDB = this.dao.create(user);

		// AND try to readById with a bad id
		Optional<UserEntity> userFromDB = this.dao.readById(userInDB.getKey() + "bad");

		// THEN readById returns null
		assertThat(userFromDB.isPresent()).isFalse();
	}

	@Test(expected = Exception.class)
	public void testCreateTheSameUserTwice() throws Exception {
		// GIVEN nothing

		// WHEN I create a new user and add it twice to dao
		/*
		String userEmail = "missroteml@gmail.com";
		String username = "rotemlevi";
		String avatar = "cat";
		UserRole role = UserRole.PLAYER;
		long points = 100;
		UserEntity user = this.factory.createNewUser(userEmail, smartspace, username, avatar, role, points);
		*/
		
		UserEntity user = fakeUserGenerator();
		
		UserEntity userInDB = this.dao.create(user);
		UserEntity userInDB2 = this.dao.create(user);

		// THEN create method throws excetpion

	}

	@Test(expected = Exception.class)
	public void testCreateTwoUserWithTheSameKey() throws Exception {
		// GIVEN nothing

		// WHEN I create two new users with the same email and smartspace and add them
		// to the dao
		/*
		String userEmail = "missroteml@gmail.com";
		String username = "rotemlevi";
		String avatar = "cat";
		UserRole role = UserRole.PLAYER;
		long points = 100;
		UserEntity user = this.factory.createNewUser(userEmail, smartspace, username, avatar, role, points);
		*/
		
		UserEntity user = fakeUserGenerator();
		UserEntity userInDB = this.dao.create(user);

		/*
		String userEmail2 = "missroteml@gmail.com";
		String username2 = "rvi";
		String avatar2 = "at";
		UserRole role2 = UserRole.MANAGER;
		long points2 = 130;
		UserEntity user2 = this.factory.createNewUser(userEmail2, smartspace, username2, avatar2, role2, points2);
		*/
		
		UserEntity user2 = fakeUserGenerator();
		user2.setUserEmail(user.getUserEmail());
		UserEntity userInDB2 = this.dao.create(user);

		// THEN create method throws excetpion

	}
}
