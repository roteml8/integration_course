package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;

public class MemoryElementDaoDelateAllUnitTests {
	@Test
	public void testElementEntityDeleteAll() throws Exception {
		
		// GIVEN a Element is available
		MemoryElementDao dao = new MemoryElementDao();
		// AND  user entities are added to the dao
		ElementEntity elementEntity = new ElementEntity();
		ElementEntity elementEntity2 = new ElementEntity();
		ElementEntity elementEntity3 = new ElementEntity(); 
		ElementEntity rvE = dao.create(elementEntity);
		ElementEntity rvE2 = dao.create(elementEntity2);;

				
		// WHEN invoking readAll on the dao
		List<ElementEntity> result = dao.readAll();

		// THEN the returned list is the dao's user entities list
		assertThat(result).containsExactly(elementEntity, elementEntity2);
		
		dao.deleteAll();		
	}
}
