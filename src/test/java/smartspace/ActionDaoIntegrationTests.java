package smartspace;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.util.EntityFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class ActionDaoIntegrationTests {
	private ActionDao dao;
	private EntityFactory factory;

	@Autowired
	public void setDao(ActionDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@Before
	public void setup() {
		dao.deleteAll();
	}

	@After
	public void teardown() {
		dao.deleteAll();
	}

	@Test(expected = Exception.class)
	public void testCreateWithNullAction() throws Exception {
		// GIVEN nothing

		// WHEN I create a null action
		this.dao.create(null);

		// THEN create method throws exception
	}

	@Test
	public void testCreateWithValidAction() throws Exception {
		// GIVEN dao is initialized and clean

		// WHEN I create an action
		// AND we get the action by key
		String elementId = "123";
		String elementSmartspace = "columnToDoList";
		String actionType = "AddMission";
		Date creationTimestamp = new Date();
		String playerEmail = "tomboukai@gmail.com";
		String playerSmartspace = "tomboukai";

		Map<String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("test", new Boolean(true));
		ActionEntity action1 = this.factory.createNewAction(elementId, elementSmartspace, actionType, creationTimestamp,
				playerEmail, playerSmartspace, moreAttributes);

		ActionEntity actionInDB = this.dao.create(action1);

		// THEN the same action is in the dao
		assertThat(actionInDB).isNotNull()
				.extracting("elementId", "elementSmartspace", "actionType", "creationTimestamp", "playerEmail",
						"playerSmartspace", "moreAttributes")
				.containsExactly(elementId, elementSmartspace, actionType, creationTimestamp, playerEmail,
						playerSmartspace, moreAttributes);
		// AND the action key is not null and is smartspace+"#"+id and  id > 0 and 
		assertThat(actionInDB.getKey()).isNotNull().isEqualTo(actionInDB.getActionSmartspace()+"#"+actionInDB.getActionId());
		assertThat(actionInDB.getActionId()).isGreaterThan("0");



	}

	@Test
	public void testCheckTwoIdNotEqual() throws Exception {
		//GIVEN nothing
		
		//when the user add 2 actionEntity
		//the key will be not equal
		
		Map<String,Object> attribute = new HashMap<String, Object>();
		attribute.put("langauge", "EN");
		attribute.put("interesting", false);
		ActionEntity actionEntity1 = this.factory.createNewAction("123", "columnToDoList", "AddMission", new Date(),
				"amitzoarez@gmail.com", "amitzoarez", attribute);
		ActionEntity actionEntity2 = this.factory.createNewAction("123", "columnToDoList", "AddMission", new Date(),
				"amitzoarez@gmail.com", "amitzoarez", attribute);
		ActionEntity actionEntity1InDB = this.dao.create(actionEntity1);
		ActionEntity actionEntity2InDB = this.dao.create(actionEntity2);
		this.dao.deleteAll();
		List<ActionEntity> list = this.dao.readAll();
		
		assertThat(actionEntity1InDB.getKey()).isNotEqualTo(actionEntity2InDB.getKey());

		assertThat(list).isEmpty();

	}
	
	@Test
	public void testReadAll() throws Exception {
		// GIVEN nothing

		// WHEN I create a new action and add it to the dao
		Map<String,Object> attribute = new HashMap<String, Object>();
		attribute.put("langauge", "EN");
		attribute.put("interesting", false);
		ActionEntity actionEntity1 = this.factory.createNewAction("123", "columnToDoList", "AddMission", new Date(),
				"amitzoarez@gmail.com", "amitzoarez", attribute);
		this.dao.create(actionEntity1);
		ActionEntity actionEntity2 = this.factory.createNewAction("123", "columnToDoList", "AddMission", new Date(),
				"amitzoarez@gmail.com", "amitzoarez", attribute);
		this.dao.create(actionEntity2);

		// AND try to readById with a none existing key
		List<ActionEntity> ActionEntityFromDB = this.dao.readAll();
		assertThat(ActionEntityFromDB).isNotEmpty();
		
		ActionEntityFromDB.clear();
		dao.deleteAll();
		//assertThat(ActionEntityFromDB).contains(actionEntity1InDB);

	}
	
	@Test
	public void testCreateDeleteAllReadAll() throws Exception {
		// GIVEN nothing

		// WHEN I create a new action
		// AND Delete all the ActionEntity in the dao
		// AND read all
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("title", "hello");
		attributes.put("signature", 2123154546416841684L);
		attributes.put("langauge", "EN");
		attributes.put("interesting", false);
		ActionEntity action1 = this.factory.createNewAction("123", "columnToDoList", "AddMission", new Date(),
				"tomboukai@gmail.com", "tomboukai", attributes);

		action1 = this.dao.create(action1);

		this.dao.deleteAll();

		List<ActionEntity> list = this.dao.readAll();

		// THEN the created action received an id > 0
		// AND the dao contains nothing
		assertThat(action1.getKey()).isNotNull();
		assertThat(action1.getActionId()).isGreaterThan("0");


		assertThat(list).isEmpty();

	}
	
	@Test
	public void testCreateOneAddToDBAndCheck() throws Exception {
		// GIVEN nothing

		// WHEN I create two actions and add one to the dao
		Map<String,Object> attribute = new HashMap<String, Object>();
		attribute.put("langauge", "EN");
		attribute.put("interesting", false);
		
		ActionEntity actionEntity1 = this.factory.createNewAction("123", "columnToDoList", "AddMission", null,
				"amitzoarez@gmail.com", "amitzoarez", attribute);
		ActionEntity actionEntity1InDB =this.dao.create(actionEntity1);
		ActionEntity actionEntity2InDB = new ActionEntity();
		

		// AND read all from the dao
		List<ActionEntity> list = this.dao.readAll();
				
		//THEN the returned list does not contain the un-added action 
		assertThat(list).doesNotContain(actionEntity2InDB);
		//AND the returned list has 1 action 
		assertThat(list).hasSize(1);
	}
	
	
}