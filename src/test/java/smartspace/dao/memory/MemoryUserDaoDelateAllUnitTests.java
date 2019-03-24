package smartspace.dao.memory;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import smartspace.data.UserEntity;

public class MemoryUserDaoDelateAllUnitTests {
	

	@Test
	public void testUserEntityDeleteAll() throws Exception {
		
		// GIVEN a dao is available 
		MemoryUserDao dao = new MemoryUserDao();
		// AND  user entities are added to the dao
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

}
