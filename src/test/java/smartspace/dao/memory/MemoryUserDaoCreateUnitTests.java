package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import smartspace.data.UserEntity;

public class MemoryUserDaoCreateUnitTests {
	
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
		// AND the rvUser has a key which is a string != null that contains userSmartspace+"#"+userEmail
		
		assertThat(dao.readAll()).usingElementComparatorOnFields("userEmail").contains(userEntity);
		assertThat(rvUser.getKey()).isNotNull().isEqualTo(MemoryUserDao.smartspace+"#"+email);

		dao.deleteAll();
		
	}
	
	@Test(expected = Exception.class)
	public void testCreateUserEntityNoEmail() throws Exception{
		
		// GIVEN a dao is available 
		MemoryUserDao dao = new MemoryUserDao();
		
		// WHEN creating a new user entity with no email
		// AND invoking create method on the dao 
		
		UserEntity userEntity = new UserEntity();
		
		// THEN create method throws exception 
		
		UserEntity rvUser = dao.create(userEntity);
		dao.deleteAll();
	}
	
}
	

