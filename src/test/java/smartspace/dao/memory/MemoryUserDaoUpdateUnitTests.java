package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;



public class MemoryUserDaoUpdateUnitTests {
	
	@Test
	public void testUserEntityUpdate () throws Exception {
		
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
		assertThat(rvUser).isNotNull().isEqualToComparingFieldByField(userEntity);
		
		dao.deleteAll();
	}
	

}
