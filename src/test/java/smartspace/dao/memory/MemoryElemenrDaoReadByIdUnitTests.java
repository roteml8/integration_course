package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;

import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;

public class MemoryElemenrDaoReadByIdUnitTests {
	@Test
	public void testReadElementById() throws Exception{
		
		// GIVEN a dao is available 
		MemoryElementDao dao = new MemoryElementDao();
		// AND a element entity is added to the dao
		ElementEntity elementEntity = new ElementEntity();
		ElementEntity rvE = dao.create(elementEntity);
				
		// WHEN invoking readById on the dao with rvE key
				
		Optional<ElementEntity> result = dao.readById(rvE.getElementid());
				
		// THEN the method returns a element entity with elementEntity's key 
		
		assertThat(result.isPresent());
		assertThat(result.get().getElementid()).isEqualTo(elementEntity.getElementid());
				
		dao.deleteAll();		

	}
}
