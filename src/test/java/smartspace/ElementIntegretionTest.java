package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.Task;
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
	
	@Before
	public void setup() {
		dao.deleteAll();
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
		// GIVEN nothing

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
	// AND elementInDB has a key that is not null and is elementSmartspace+"#"+elementId (elementId > 0)
		assertThat(elementInDB.getKey()).isNotNull().isEqualTo(element.getElementSmartSpace()+"#"+element.getElementid());
		assertThat(elementInDB.getElementid()).isNotNull().isGreaterThan("0");

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
		// AND elementInDB has a key that is not null and is elementSmartspace+"#"+elementId (elementId > 0)
		assertThat(elementInDB.getKey()).isNotNull().isEqualTo(element.getElementSmartSpace()+"#"+element.getElementid());
		assertThat(elementInDB.getElementid()).isNotNull().isGreaterThan("0");
		assertThat(list).isEmpty();

	}

	@Test
	public void testCreateUpdateAndRead() throws Exception{
		// GIVEN nothing
		
		// WHEN Create in DB a new element 
		// AND Update some element details
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
		updatedElement.setCreatorSmartSpace("testSmartspace");
		updatedElement.setExpired(true);
		updatedElement.setLocation(new Location(2.0,1.0));
		
		this.dao.update(updatedElement);
		
		Optional<ElementEntity> rv = this.dao.readById(elementInDB.getKey());
			
		// THEN the element exists
		// AND all the changed details are updated
		assertThat(rv)
			.isPresent()
			.get()
			.extracting("name", "type", "location","creatorEmail","expired","creatorSmartspace","moreAttributes")
			.containsExactly(updatedElement.getName(),updatedElement.getType(),updatedElement.getLocation(),
					updatedElement.getCreatorEmail(),
					updatedElement.isExpired(),updatedElement.getCreatorSmartSpace(),
					updatedElement.getMoreAttributes());
		// AND the elementInDB creationTimeStamp is the same (can't change)
		assertThat(rv.get().getCreationTimeDate().compareTo(elementInDB.getCreationTimeDate()));
	}
	

	@Test
	public void testCreateReadById() throws Exception {
		// GIVEN nothing

		// WHEN I create a new element and add it to dao
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

		// AND read the element from the dao
		Optional<ElementEntity> elementFromDB = this.dao.readById(elementInDB.getKey());

		// THEN the elementFromDB has the same fields as the new element
		assertThat(elementFromDB.get()).isNotNull().isEqualToComparingOnlyGivenFields(element, "name","type","location","creatorSmartspace","creatorEmail","expired","moreAttributes","elementId","elementSmartspace");
		assertThat(elementFromDB.get().getCreationTimeDate().compareTo(element.getCreationTimeDate()));

		
	}
	
	@Test
	public void testCreateReadAll() throws Exception {
		// GIVEN nothing

		// WHEN I create a new element and add it to dao
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

		// AND read all from the dao
		List<ElementEntity> list = this.dao.readAll();
		// THEN the returned list contains only one element
		assertThat(list).hasSize(1);


		
	}

	@Test
	public void testCreateReadByIdWithBadID() throws Exception {
		// GIVEN nothing

		// WHEN I create a new element and add it to the dao
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

		// AND try to readById with a none existing key
		Optional<ElementEntity> elementFromDB = this.dao.readById("badID");

		// THEN readById returns null
		assertThat(elementFromDB.isPresent()).isFalse();
	}
	
	@Test
	public void testCreateDeleteByKeyRead() throws Exception {
		// GIVEN nothing

		// WHEN I create a new element and add it to the dao
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
		
		// AND delete from the dao by the created element ID
		this.dao.deleteByKey(elementInDB.getKey());
		
		// AND read from the dao by the created element ID
		Optional<ElementEntity> rv = this.dao.readById(elementInDB.getKey());
		
		//THEN the returned value is Null (dao contains no element with the specified key)
		assertThat(rv.isPresent()).isFalse();
	}
	
	@Test
	public void testCreateDeleteRead() throws Exception {
		// GIVEN nothing

		// WHEN I create a new element and add it to the dao
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
		
		// AND delete the created element from the dao
		this.dao.delete(elementInDB);
		
		// AND read from the dao by the created element ID
			Optional<ElementEntity> rv = this.dao.readById(elementInDB.getKey());
				
		//THEN the returned value is Null (dao contains no element with the specified key)
			assertThat(rv.isPresent()).isFalse();

	}
	
	@Test(expected = Exception.class)
	public void testCreateDeleteUpdate() throws Exception {
		// GIVEN nothing

		// WHEN I create a new element and add it to the dao
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
		
		// AND delete the created element from the dao
		this.dao.delete(elementInDB);
		
		// AND update some of element's details
		ElementEntity updatedElement = new ElementEntity();
		updatedElement.setKey(elementInDB.getKey());
		updatedElement.setCreatorSmartSpace("updatedSmartspace");
		updatedElement.setName("updatedName");
		
		//THEN the update method throws an exception (element is not in the dao)
		
		this.dao.update(updatedElement);
	}
	
	@Test
	public void testCreateDeleteReadAll() throws Exception {
		// GIVEN nothing

		// WHEN I create a new element and add it to the dao
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
		
		// AND delete the created element from the dao
		this.dao.delete(elementInDB);
		
		// AND read all from the dao
		List<ElementEntity> list = this.dao.readAll();
				
		//THEN the returned list is empty
		assertThat(list).isEmpty();
	}
	
	@Test
	public void testCreateTwoDeleteOneReadAll() throws Exception {
		// GIVEN nothing

		// WHEN I create two elements and add it to the dao
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
		ElementEntity elementInDB1 = this.dao.create(element);
		ElementEntity elementInDB2 = this.dao.create(element);
		
		
		// AND delete the first created element from the dao
		this.dao.delete(elementInDB1);
		
		// AND read all from the dao
		List<ElementEntity> list = this.dao.readAll();
				
		//THEN the returned list is of size 1 
		assertThat(list).hasSize(1);
		//AND the dao does not contain the deleted element
		assertThat(dao.readById(elementInDB1.getKey()).isPresent()).isFalse();
		//AND the dao contains the undeleted element
		assertThat(dao.readById(elementInDB2.getKey()).isPresent());
		//AND the two elements have different keys
		assertThat(elementInDB1.getKey()).isNotEqualTo(elementInDB2.getKey());
	}

}
