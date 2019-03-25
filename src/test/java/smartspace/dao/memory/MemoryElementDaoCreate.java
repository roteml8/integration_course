package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import smartspace.dao.ElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;

public class MemoryElementDaoCreate {
	
	@Test
	public void testCreateElementEntity() throws Exception {
		// GIVEN a Element is available
		MemoryElementDao dao = new MemoryElementDao();
		//EntityFactoryImpl factory =new EntityFactoryImpl();
		
		
		// WHEN creating a new elementEntity
		// AND invoking create method on the dao 
		ElementEntity ee = new ElementEntity();
		ElementEntity rvEe = dao.create(ee);
		
		// THEN the user was added to the dao
		// AND the rvEe has a key which is a string != null that contains userSmartspace+"#"+userEmail
			
		assertThat(dao.readAll()).usingElementComparatorOnFields("elementId").contains(ee);
		assertThat(rvEe.getElementid()).isNotNull().isEqualTo(ee.getElementid());

		dao.deleteAll();
	}
		
	@Test
	public void testDeleteElementEntity() throws Exception {
		// GIVEN a Dao is available
		// AND has an ElementEntity in it
		MemoryElementDao dao = new MemoryElementDao();
		ElementEntity ee = new ElementEntity();
		ElementEntity rvEe = dao.create(ee);
		// WHEN we invoke deleteAll
		dao.deleteAll();

		// THEN the dao contains nothing
		List<ElementEntity> list = dao.readAll();
		assertThat(list).isEmpty();

		dao.deleteAll();

	}
}
