package smartspace.dao.memory;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import smartspace.data.UserEntity;

public class MemoryUserDaoReadAllUnitTests {
	
	@Test
	public void testUserEntityReadAll() throws Exception {
		
		// GIVEN a dao is available 
		MemoryUserDao dao = new MemoryUserDao();
		// AND  user entities are added to the dao
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

}
