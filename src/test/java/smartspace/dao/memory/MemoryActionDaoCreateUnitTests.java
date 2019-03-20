package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import smartspace.data.ActionEntity;

public class MemoryActionDaoCreateUnitTests {

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

}