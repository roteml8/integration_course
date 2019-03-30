package smartspace;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import smartspace.dao.UserDao;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;

@Component
@Profile("production")
public class UserEntityDemo implements CommandLineRunner{
	private EntityFactory factory;
	private UserDao<String> dao;
	
	public UserEntityDemo()
	{
		
	}
	
	@Autowired
	public UserEntityDemo(EntityFactory factory, UserDao<String> dao) {
		this.factory = factory;
		this.dao = dao;
	}
	
	
	@Override
	public void run(String... args) throws Exception {
		UserEntity user1 = this.factory.createNewUser("tom@gmail.com", "Trello","tomboukai","wolf",UserRole.MANAGER,0);
		System.err.println("\n"+"Data before storing: " + user1.getUsername() + ", " + user1.getKey()); //ADD toString to see all data
		
		user1 = this.dao.create(user1);
		System.err.println("Data after storing: " + user1.getUsername() + ", "+ user1.getKey() );
		
		UserEntity update = new UserEntity();
		update.setUsername("Oded");
		update.setKey(user1.getKey());
		
		this.dao.update(update);
		Optional<UserEntity> messageOp = this.dao.readById(user1.getKey()); 
		if (messageOp.isPresent()) {
			user1 = messageOp.get();
		}else {
			throw new RuntimeException("Data was lost after update");
		}
		System.err.println("Data after update: " + user1.getUsername() +", " + user1.getKey() );
		
		this.dao.deleteAll();
		if (this.dao.readAll().isEmpty()) {
			System.err.println("Data deleted successfully"+"\n");
		}else {
			throw new RuntimeException("Some data were not deleted");
		}
		
	}

}
