package smartspace.dao.memory;

import java.util.Optional;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import smartspace.data.UserEntity;

public class MemoryUserDaoReadByIdUnitTests {
	
	@Test
	public void testReadUserById() throws Exception{
		
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
	public void testReadUserByIdNonexistingKey() throws Exception{
		
		// GIVEN a dao is available 
		MemoryUserDao dao = new MemoryUserDao();

				
		// WHEN invoking readById on the dao with a random key that does not exist in the dao
				
		Optional<UserEntity> result = dao.readById("randomkey");
				
		// THEN the method returns a container with null value
		
		assertThat(!result.isPresent());
		dao.deleteAll();		

				
	}

}
