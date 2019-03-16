package smartspace;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import smartspace.dao.UserDao;
import smartspace.data.*;
import smartspace.data.util.EntityFactory;

@Component
public class UserEntityDemo implements CommandLineRunner {
	private EntityFactory factory;
	private UserDao<String> dao;

	public UserEntityDemo() {
	}

	@Autowired
	public UserEntityDemo(EntityFactory factory, UserDao<String> dao) {
		this.factory = factory;
		this.dao = dao;
	}

	@Override
	public void run(String... args) throws Exception {

		UserEntity user1 = this.factory.createNewUser("bla@google.co.il", "trello", "bla", "wolf", UserRole.MANAGER, 0);

		System.err.println("key before storing: " + user1.getKey());

		user1 = this.dao.create(user1);
		System.err.println("key after storing: " + user1.getKey());

		UserEntity update = new UserEntity();
		update.setRole(UserRole.PLAYER);
		update.setKey(user1.getKey());
		this.dao.update(update);

		Optional<UserEntity> messageOp = this.dao.readById(user1.getKey());
		if (messageOp.isPresent()) {
			user1 = messageOp.get();
		} else {
			throw new RuntimeException("message was lost after update");
		}
		System.err.println("message after update: " + user1.getUsername());

		this.dao.deleteAll();
		if (this.dao.readAll().isEmpty()) {
			System.err.println("messages deleted successfully");
		} else {
			throw new RuntimeException("some messages were not deleted");
		}

	}

}
