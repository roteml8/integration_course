package smartspace;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import smartspace.dao.UserDao;
import smartspace.dao.memory.MemoryElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;
import smartspace.data.*;



@Component
public class ElementEntityDemo implements CommandLineRunner{
		private EntityFactory factory;
		private MemoryElementDao dao;
		
		public ElementEntityDemo()
		{
			
		}
		
		@Autowired
		public ElementEntityDemo(EntityFactory factory, MemoryElementDao dao) {
			this.factory = factory;
			this.dao = dao;
		}
		
		
		@Override
		public void run(String... args) throws Exception {
			Map<String, Object> details = new HashMap<>();
			Task task = new Task ("clean house","clean your house today",5,"17/3/2019",30);
			details.put("title", task);
			Location location = new Location ();
			location.setX(1.0);
			location.setY(2.0);
			ElementEntity element1 = this.factory.createNewElement("Column", "column",location,new Date(),"tomboukai@gmail.com","columnToDoList",false,details);
			ElementEntity element2 = this.factory.createNewElement("Column", "column",location,new Date(),"tomboukai@gmail.com","columnToDoList",false,details);
			ElementEntity element3 = this.factory.createNewElement("Archive", "Archive",location,new Date(),"tomboukai@gmail.com","columnToDoList",false,details);
					
			
			System.err.println("\n"+"Data before storing: " + element1.getCreatorEmail() + ", " + element1.getName() + ", " +element1.getKey()); //ADD toString to see all data
			
			element1 = this.dao.create(element1);
			
			/*Test - delete elements from memory
			
			element2 = this.dao.create(element2);
			element3 = this.dao.create(element3);
			for (int i=0;i<this.dao.readAll().size();i++)
			{
				System.out.println(this.dao.readAll().get(i).toString());
			}
			
			this.dao.deleteByKey("2");
			this.dao.delete(element2);
			*/
			
			
			System.err.println("Data after storing: " + element1.getCreatorEmail() + ", "+ element1.getName()+", " + element1.getKey() + "details = "+element1.getMoreAtrributes());
			
			ElementEntity update = new ElementEntity();
			update.setName("Archive");
			update.setKey(element1.getKey());
			
			this.dao.update(update);
			Optional<ElementEntity> messageOp = this.dao.readById(element1.getKey()); 
			if (messageOp.isPresent()) {
				element1 = messageOp.get();
			}else {
				throw new RuntimeException("Data was lost after update");
			}
			System.err.println("Data after update: " + element1.getCreatorEmail() +", " +element1.getName() +", "+ element1.getKey() );
			
		
			/*Test - delete elements from memory
			for (int i=0;i<this.dao.readAll().size();i++)
			{
				System.out.println(this.dao.readAll().get(i).toString());
			}
			*/
			
			
			
			this.dao.deleteAll();
			if (this.dao.readAll().isEmpty()) {
				System.err.println("Data deleted successfully"+"\n");
			}else {
				throw new RuntimeException("some data were not deleted");
			}
			
		}

	}

	

