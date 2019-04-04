package smartspace.dao.memory;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.ElementEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class MemoryElementDaoUnitTests {
	
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
	
	@Test
	public void testUserEntityReadAll() throws Exception {
		
		// GIVEN a dao is available 
		MemoryElementDao dao = new MemoryElementDao();
		// AND  element entities are added to the dao
		ElementEntity elementEntity = new ElementEntity();
		ElementEntity elementEntity2 = new ElementEntity();
		ElementEntity elementEntity3 = new ElementEntity();
		ElementEntity rvE = dao.create(elementEntity);
		ElementEntity rvE2 = dao.create(elementEntity2);

				
		// WHEN invoking readAll on the dao
		List<ElementEntity> result = dao.readAll();

		// THEN the returned list is the dao's element entities list
		assertThat(result).containsExactly(elementEntity, elementEntity2);
		
		dao.deleteAll();		
	}
	
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
