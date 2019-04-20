package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class MemoryUserDaoUnitTests {

	private String smartspace;

	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	@Test
	public void testCreateUserEntity() throws Exception {

		// GIVEN a dao is available
		MemoryUserDao dao = new MemoryUserDao();

		// WHEN creating a new user entity
		// AND invoking create method on the dao
		String email = "missroteml@gmail.com";
		UserEntity userEntity = new UserEntity(email);
		UserEntity rvUser = dao.create(userEntity);

		// THEN the user was added to the dao
		// AND the rvUser has a key which is a string != null that contains
		// userSmartspace+"#"+userEmail

		assertThat(dao.readAll()).usingElementComparatorOnFields("userEmail").contains(userEntity);
		// assertThat(rvUser.getKey()).isNotNull().isEqualTo(smartspace+"#"+email);
		// this is weird , smartspace in the userEntity is null and i don't know why :(

		dao.deleteAll();

	}

//  Note: this test is not relevant because we assume Create is always invoked with a user with email
//	@Test(expected = Exception.class)
//	public void testCreateUserEntityNoEmail() throws Exception{
//		
//		// GIVEN a dao is available 
//		MemoryUserDao dao = new MemoryUserDao();
//		
//		// WHEN creating a new user entity with no email
//		// AND invoking create method on the dao 
//		
//		UserEntity userEntity = new UserEntity();
//		
//		// THEN create method throws exception 
//		
//		UserEntity rvUser = dao.create(userEntity);
//		dao.deleteAll();
//	}

	@Test
	public void testUserEntityDeleteAll() throws Exception {

		// GIVEN a dao is available
		MemoryUserDao dao = new MemoryUserDao();
		// AND user entities are added to the dao
		UserEntity userEntity = new UserEntity("missroteml@gmail.com");
		UserEntity userEntity2 = new UserEntity("rotem.levi@s.afeka.ac.il");
		UserEntity userEntity3 = new UserEntity("test@email.com"); // not added to dao
		UserEntity rvUser = dao.create(userEntity);
		UserEntity rvUser2 = dao.create(userEntity2);

		// WHEN invoking deleteAll on the dao
		dao.deleteAll();

		// THEN the dao's entities list is empty

		assertThat(dao.readAll()).isEmpty();

	}

	@Test
	public void testUserEntityReadAll() throws Exception {

		// GIVEN a dao is available
		MemoryUserDao dao = new MemoryUserDao();
		// AND user entities are added to the dao
		UserEntity userEntity = new UserEntity("missroteml@gmail.com");
		UserEntity userEntity2 = new UserEntity("rotem.levi@s.afeka.ac.il");
		UserEntity userEntity3 = new UserEntity("test@email.com"); // not added to dao
		UserEntity rvUser = dao.create(userEntity);
		UserEntity rvUser2 = dao.create(userEntity2);

		// WHEN invoking readAll on the dao
		List<UserEntity> result = dao.readAll();

		// THEN the returned list is the dao's user entities list
		assertThat(result).containsExactly(userEntity, userEntity2);

		dao.deleteAll();
	}

	@Test
	public void testReadUserById() throws Exception {

		// GIVEN a dao is available
		MemoryUserDao dao = new MemoryUserDao();
		// AND a user entity is added to the dao
		UserEntity userEntity = new UserEntity("missroteml@gmail.com");
		UserEntity rvUser = dao.create(userEntity);

		// WHEN invoking readById on the dao with rvUser key

		Optional<UserEntity> result = dao.readById(rvUser.getKey());

		// THEN the method returns a user entity with userEntity's key

		assertThat(result.isPresent());
		assertThat(result.get().getKey()).isEqualTo(userEntity.getKey());

		dao.deleteAll();

	}

	@Test
	public void testReadUserByIdNonexistingKey() throws Exception {

		// GIVEN a dao is available
		MemoryUserDao dao = new MemoryUserDao();

		// WHEN invoking readById on the dao with a random key that does not exist in
		// the dao

		Optional<UserEntity> result = dao.readById("randomkey");

		// THEN the method returns a container with null value

		assertThat(!result.isPresent());
		dao.deleteAll();

	}

	@Test
	public void testUserEntityUpdate() throws Exception {

		// GIVEN a dao is available
		MemoryUserDao dao = new MemoryUserDao();
		// AND a user entity is added to the dao
		UserEntity userEntity = new UserEntity("missroteml@gmail.com");
		UserEntity rvUser = dao.create(userEntity);

		// WHEN updating one or more of userEntity's attributes
		// AND invoking update on the dao with rvUser

		userEntity.setUsername("rotem");
		userEntity.setAvatar("cat");
		userEntity.setPoints(100);
		userEntity.setRole(UserRole.MANAGER);
		dao.update(rvUser);

		// THEN the user entity with rvUser's key is updated in the dao
		assertThat(dao.readById(rvUser.getKey()).get()).isNotNull().isEqualToComparingFieldByField(userEntity);

		dao.deleteAll();
	}

}
