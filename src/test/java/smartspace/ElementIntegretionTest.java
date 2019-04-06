package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.junit.After;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.dao.ElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.Task;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class ElementIntegretionTest {
	
	private ElementDao<String> dao;
	private EntityFactory factory;

	@Autowired
	public void setDao(ElementDao<String> dao) {
		this.dao = dao;
	}

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@Autowired
	public void setUserDao(ElementDao<String> elementDao) {
		this.dao = elementDao;
	}

	@After
	public void teardown() {
		this.dao.deleteAll();
	}
	
	@Test(expected = Exception.class)
	public void testCreateWithNullElement() throws Exception {
		// GIVEN nothing

		// WHEN I invoke create on the dao with a null element
		this.dao.create(null);

		// THEN create method throws exception
	}
	
	@Test
	public void testCreateWithValidElement() throws Exception {
		// GIVEN dao is initialized and clean

		// WHEN creating an element 
		// AND adding the element to the dao
		String name = "Column1";
		String type = "Column";
		Location location = new Location(1.0,1.0);
		Date creationTimeStamp = new Date();
		String creatorEmail = "missroteml@gmail.com";
		String creatorSmartspace = "2019B.Amitz4.SmartSpace";
		boolean expired = false;
		Map<String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("test", new Boolean(true));
		
		ElementEntity element = this.factory.createNewElement(name, type, location, creationTimeStamp, creatorEmail, creatorSmartspace, expired, moreAttributes);
		ElementEntity elementInDB = this.dao.create(element);

		// THEN the same element is in the dao
		assertThat(elementInDB).isNotNull().extracting("name", "type", "location","creationTimeStamp","creatorEmail","expired","creatorSmartspace","moreAttributes")
			.containsExactly(element.getName(),element.getType(),element.getLocation(),
					element.getCreationTimeDate(),element.getCreatorEmail(),
					element.isExpired(),element.getCreatorSmartSpace(),
					element.getMoreAttributes());
//TODO		// AND elementInDB has a key that is...

	}
	
	@Test
	public void testCreateDeleteAllReadAll() throws Exception {
		// GIVEN nothing

		// WHEN I create a new element and add it to dao
		// AND Delete all elements
		// AND read all elements
		
		String name = "Column1";
		String type = "Column";
		Location location = new Location(1.0,1.0);
		Date creationTimeStamp = new Date();
		String creatorEmail = "missroteml@gmail.com";
		String creatorSmartspace = "2019B.Amitz4.SmartSpace";
		boolean expired = false;
		Map<String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("test", new Boolean(true));
		ElementEntity element = this.factory.createNewElement(name, type, location, creationTimeStamp, creatorEmail, creatorSmartspace, expired, moreAttributes);
		ElementEntity elementInDB = this.dao.create(element);

		this.dao.deleteAll();

		List<ElementEntity> list = this.dao.readAll();

		// THEN the created user received a key != null which is .... 
		// AND the dao contains nothing
//TODO		assertThat(userInDB.getKey()).isNotNull().isEqualTo(user.getUserSmartspace() + "#" + user.getUserEmail());
		assertThat(list).isEmpty();

	}

	@Test
	public void testCreateUpdateAndRead() throws Exception{
		// GIVEN dao is initialized and clean
		
		// WHEN Create in DB new a new element 
		// AND Update the element details
		// AND Read the element from database
		
		Map<String, Object> details = new HashMap<>();
		Task task = new Task ("clean house","clean your house today",5,"17/3/2019",30);
		details.put("title", task);
		Location location = new Location ();
		location.setX(1.0);
		location.setY(2.0);
	
		ElementEntity elementInDB = this.dao.create(this.factory.createNewElement("Column", "column",location,new Date(),"tomboukai@gmail.com","columnToDoList",false,details));
		
		ElementEntity updatedElement = new ElementEntity();
		updatedElement.setKey(elementInDB.getKey());
		
		Map<String, Object> updatedDetails = new TreeMap<>();
		updatedDetails.put("x", "y");
		updatedDetails.put("y", 8);
		
		updatedElement.setMoreAttributes(updatedDetails);	
		updatedElement.setName("updated test");
		updatedElement.setType("testType");
		updatedElement.setCreatorEmail("missroteml@gmail.com");
		updatedElement.setCreationTimeDate(new Date());
		updatedElement.setCreatorSmartSpace("testSmartspace");
		updatedElement.setExpired(true);
		updatedElement.setLocation(new Location(2.0,1.0));
		
		this.dao.update(updatedElement);
		
		Optional<ElementEntity> rv = this.dao.readById(elementInDB.getKey());
		

		
		// THEN the element exists
		// AND the element name is "updated test"
		// AND all the details are updated
		assertThat(rv)
			.isPresent()
			.get()
			.extracting("name", "type", "location","creatorEmail","expired","creatorSmartspace","moreAttributes")
			.containsExactly(updatedElement.getName(),updatedElement.getType(),updatedElement.getLocation(),
					updatedElement.getCreatorEmail(),
					updatedElement.isExpired(),updatedElement.getCreatorSmartSpace(),
					updatedElement.getMoreAttributes());
	}

}
