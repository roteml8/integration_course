package smartspace;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import smartspace.dao.memory.MemoryActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.util.EntityFactory;

@Component
@Profile("production")
public class ActionEntityDemo implements CommandLineRunner {
	private EntityFactory factory;
	private MemoryActionDao dao;

	public ActionEntityDemo() {

	}

	@Autowired
	public ActionEntityDemo(EntityFactory factory, MemoryActionDao dao) {
		this.factory = factory;
		this.dao = dao;
	}

	@Override
	public void run(String... args) throws Exception {
		Map<String, Object> details = new HashMap<>();
		details.put("title", "word");

		ActionEntity action1 = this.factory.createNewAction("123", "columnToDoList", "AddMission", new Date(),
				"tomboukai@gmail.com", "tomboukai", details);

		System.err.println("\n" + "Data before storing: " + action1.getActionType() + ", " + action1.getPlayerEmail()
				+ ", " + action1.getElementId() + ", " + action1.getActionId()); // ADD toString to see all data

		action1 = this.dao.create(action1);
		System.err.println("Data after storing: " + action1.getActionType() + ", " + action1.getPlayerEmail() + ", "
				+ action1.getElementId() + ", " + action1.getActionId()); // ADD toString to see all data

		this.dao.deleteAll();
		if (this.dao.readAll().isEmpty()) {
			System.err.println("Data deleted successfully" + "\n");
		} else {
			throw new RuntimeException("some data were not deleted");
		}

	}

}
