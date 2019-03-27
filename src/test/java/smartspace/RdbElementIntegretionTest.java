package smartspace;
import static org.assertj.core.api.Assertions.assertThat;    
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


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"spring.profiles.active=default"})
public class RdbElementIntegretionTest {
	private ElementDao <String>elementDao;
	

	@Autowired
	public void setUserDao(ElementDao<String> elementDao) {
		this.elementDao = elementDao;
	}



	@After
	public void teardown() {
		this.elementDao.deleteAll();
	}



	@Test
	public void testCreateUpdateAndRead() throws Exception{
		// GIVEN messages table is empty
		
		// WHEN Create in DB new Message with name "Test"
		// AND Update message details
		// AND Read message from database
		ElementEntity newElement = this.elementDao.create(new ElementEntity("xz"));
		
		Map<String, Object> updatedDetails = new TreeMap<>();
		updatedDetails.put("x", "y");
		updatedDetails.put("y", 8);
		
		ElementEntity update = new ElementEntity();
		update.setKey(newElement.getKey());
		update.setMoreAtrributes(updatedDetails);
		
		update.setName("updated test");
		
		this.elementDao.update(update);
		
		Optional<ElementEntity> rv = this.elementDao.readById(newElement.getKey());
		
		ObjectMapper jackson = new ObjectMapper();
		Map<String, Object>jacksonDetail = 
			jackson.readValue(	
				jackson.writeValueAsString(updatedDetails),
				Map.class);
		
		// THEN the message exists
		// AND the message name is "Test"
		// AND the details are updated
		assertThat(rv)
			.isPresent()
			.get()
			.extracting("name", "details")
			.containsExactly("updated test", jacksonDetail);
	}

}
