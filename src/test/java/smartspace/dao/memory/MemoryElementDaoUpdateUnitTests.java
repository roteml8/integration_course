package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class MemoryElementDaoUpdateUnitTests {

	
	@Test
	public void testElementEntityUpdate () throws Exception {
		
		// GIVEN a dao is available 
		MemoryElementDao dao = new MemoryElementDao();
		// AND a element entity is added to the dao
		ElementEntity elementEntity = new ElementEntity();
		ElementEntity rvE = dao.create(elementEntity);
				
		// WHEN updating one or more of elementEntity's attributes
		// AND invoking update on the dao with rvUser 
		
		elementEntity.setName("table");
		elementEntity.setExpired(true);
		elementEntity.setKey("aaa");
		dao.update(rvE);
				
		// THEN the user entity with rvUser's key is updated in the dao 
		assertThat(rvE).isNotNull().isEqualToComparingFieldByField(elementEntity);
		
		dao.deleteAll();
	}
	
	
}
