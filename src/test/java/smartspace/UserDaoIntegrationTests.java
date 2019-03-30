package smartspace;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;
import java.util.Optional;

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
import smartspace.dao.rdb.RdbUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;

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

	@Test(expected = Exception.class)
	public void testCreateWithNullAction() throws Exception {
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
		String userSmartspace = smartspace;
		String userEmail = "missroteml@gmail.com";
		String userName = "rotemlevi";
		String userAvatar = "cat";
		UserRole userRole = UserRole.PLAYER;
		long userPoints = 100;
		UserEntity user = this.factory.createNewUser(userEmail, userSmartspace, userName, userAvatar,
				userRole, userPoints);

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
		String userEmail = "missroteml@gmail.com";
		String userName = "rotemlevi";
		String userAvatar = "cat";
		UserRole userRole = UserRole.PLAYER;
		long userPoints = 100;
		UserEntity user = this.factory.createNewUser(userEmail, smartspace, userName, userAvatar,
				userRole, userPoints);

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
	public void testCreateUpdateRead() throws Exception {
		// GIVEN nothing

		// WHEN I create a new user and add it to dao
		String userEmail = "missroteml@gmail.com";
		String userName = "rotemlevi";
		String userAvatar = "cat";
		UserRole userRole = UserRole.PLAYER;
		long userPoints = 100;
		UserEntity user = this.factory.createNewUser(userEmail, smartspace, userName, userAvatar,
				userRole, userPoints);
		UserEntity userInDB = this.dao.create(user);

		// AND change the user details and update in the dao
		user.setKey(userInDB.getKey());
		user.setUsername("rot");
		user.setAvatar("kitten");
		user.setPoints(34);
		this.dao.update(user);

		// AND read the user from the dao
		Optional<UserEntity> userFromDB = this.dao.readById(user.getKey());

		// THEN the returned object is the updated user
		assertThat(userFromDB.get()).isNotNull().isEqualToComparingFieldByField(user);
	}
}
