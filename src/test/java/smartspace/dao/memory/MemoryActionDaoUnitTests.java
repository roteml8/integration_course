package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })

public class MemoryActionDaoUnitTests {

	@Test
	public void testCreateActionEntity() throws Exception {
		// GIVEN a Dao is available
		MemoryActionDao dao = new MemoryActionDao();

		// WHEN we create a new action
		// AND we invoke create on the dao
		ActionEntity actionEntity = new ActionEntity();
		ActionEntity rvAction = dao.create(actionEntity);

		// THEN the action was added to the dao
		// AND the rvAction has a key and the key > 0
		assertThat(dao.readAll()).usingElementComparatorOnFields("actionId").contains(actionEntity);

		assertThat(Integer.valueOf(rvAction.getKey())).isNotNull().isGreaterThan(0);

		dao.deleteAll();
		

	}

	@Test
	public void testDeleteByActionEntity() throws Exception {
		// GIVEN a Dao is available
		// AND has an ActionEntity in it
		MemoryActionDao dao = new MemoryActionDao();
		ActionEntity actionEntity = new ActionEntity();
	//	ActionEntity rvAction = dao.create(actionEntity);
		dao.create(actionEntity);
		// WHEN we invoke deleteAll
		dao.delete(actionEntity);

		// THEN the dao contains nothing
		List<ActionEntity> list = dao.readAll();
		assertThat(list).isEmpty();

		dao.deleteAll();

	}
	
	@Test
	public void testDeleteByActionEntityKey() throws Exception {
		// GIVEN a Dao is available
		// AND has an ActionEntity in it
		MemoryActionDao dao = new MemoryActionDao();
		ActionEntity actionEntity = new ActionEntity();
	//	ActionEntity rvAction = dao.create(actionEntity);
		dao.create(actionEntity);
		// WHEN we invoke deleteAll
		dao.deleteByKey(actionEntity.getKey());

		// THEN the dao contains nothing
		List<ActionEntity> list = dao.readAll();
		assertThat(list).isEmpty();

		dao.deleteAll();

	}
	
	@Test
	public void testActionEntityDeleteAll() throws Exception {
		
		// GIVEN a Action is available
		MemoryActionDao dao = new MemoryActionDao();
		// AND  user entities are added to the dao
		ActionEntity actionEntity = new ActionEntity();
		ActionEntity actionEntity2 = new ActionEntity();
		dao.create(actionEntity);
		dao.create(actionEntity2);;
				
		// WHEN invoking deleteAll on the dao
		dao.deleteAll();

		// THEN the returned list is the dao's user entities list
		List<ActionEntity> list = dao.readAll();
		assertThat(list).isEmpty();
		
		dao.deleteAll();		
	}
	
	

	@Test
	public void testActionEntityReadAll() throws Exception {
		
		// GIVEN a dao is available 
		MemoryActionDao dao = new MemoryActionDao();
		// AND  action entities are added to the dao
		ActionEntity actionEntity = new ActionEntity();
		ActionEntity actionEntity2 = new ActionEntity();
		dao.create(actionEntity);
		dao.create(actionEntity2);

				
		// WHEN invoking readAll on the dao
		List<ActionEntity> result = dao.readAll();

		// THEN the returned list is the dao's element entities list
		assertThat(result).containsExactly(actionEntity, actionEntity2);
		
		dao.deleteAll();		
	}
	
	@Test
	public void testActionEntityUpdate () throws Exception {
		
		// GIVEN a dao is available 
		MemoryActionDao dao = new MemoryActionDao();
		// AND a action entity is added to the dao
		ActionEntity actionEntity = new ActionEntity();
		actionEntity.setActionType("player");
		ActionEntity rvA =  dao.create(actionEntity);
				
		// WHEN updating one or more of actionEntity's attributes
		// AND invoking update on the dao
		
		actionEntity.setActionType("amit");
		dao.update(rvA);
				
		// THEN the user entity with rvAction's key is updated in the dao 
		assertThat(rvA).isNotNull().isEqualToComparingFieldByField(actionEntity);
		
		dao.deleteAll();
	}

	
	

}