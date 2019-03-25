package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.ActionEntity;

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
	public void testDeleteActionEntity() throws Exception {
		// GIVEN a Dao is available
		// AND has an ActionEntity in it
		MemoryActionDao dao = new MemoryActionDao();
		ActionEntity actionEntity = new ActionEntity();
		ActionEntity rvAction = dao.create(actionEntity);

		// WHEN we invoke deleteAll
		dao.deleteAll();

		// THEN the dao contains nothing
		List<ActionEntity> list = dao.readAll();
		assertThat(list).isEmpty();

		dao.deleteAll();

	}

}