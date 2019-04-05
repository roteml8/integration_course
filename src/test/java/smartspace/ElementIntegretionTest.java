package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
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

	@Test
	public void testCreateUpdateAndRead() throws Exception{
		// GIVEN messages table is empty
		
		// WHEN Create in DB new Element with the name "Column" and the next attributes
		// AND Update the element details
		// AND Read the element from database
		Map<String, Object> details = new HashMap<>();
		Task task = new Task ("clean house","clean your house today",5,"17/3/2019",30);
		details.put("title", task);
		Location location = new Location ();
		location.setX(1.0);
		location.setY(2.0);
	
		ElementEntity newElement = this.dao.create(this.factory.createNewElement("Column", "column",location,new Date(),"tomboukai@gmail.com","columnToDoList",false,details));
		
		Map<String, Object> updatedDetails = new TreeMap<>();
		updatedDetails.put("x", "y");
		updatedDetails.put("y", 8);
		
		ElementEntity update = new ElementEntity();
		update.setKey(newElement.getKey());
		update.setMoreAttributes(updatedDetails);
		
		update.setName("updated test");
		
		this.dao.update(update);
		
		Optional<ElementEntity> rv = this.dao.readById(newElement.getKey());
		
		ObjectMapper jackson = new ObjectMapper();
		Map<String, Object>jacksonDetail = 
			jackson.readValue(	
				jackson.writeValueAsString(updatedDetails),
				Map.class);
		
		// THEN the element exists
		// AND the element name is "updated test"
		// AND the details are updated
		assertThat(rv)
			.isPresent()
			.get()
			.extracting("name", "moreAttributes")
			.containsExactly("updated test", jacksonDetail);
	}

}
