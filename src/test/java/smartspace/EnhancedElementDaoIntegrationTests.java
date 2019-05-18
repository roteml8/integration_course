package smartspace;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.util.FakeElementGenerator;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class EnhancedElementDaoIntegrationTests {
	
	private EnhancedElementDao<String> dao;
	private FakeElementGenerator generator;
	
	@Autowired
	public void setDao(EnhancedElementDao<String> dao) {
		this.dao = dao;
	}
	
	@Autowired
	public void setGenerator(FakeElementGenerator generator) {
		this.generator = generator;
	}

	@Before
	public void setup() {
		dao.deleteAll();
	}

	@After
	public void teardown() {
		dao.deleteAll();
	}

	@Test
	public void testReadAllWithPagination() throws Exception {
		// GIVEN the database contains 50 elements
		IntStream.range(0, 50) // Stream Integer
				.mapToObj(i -> generator.getElement()) // ElementEntity Stream
				.forEach(this.dao::create);

		// WHEN I read 10 elements after skipping the first 10 
		List<ElementEntity> result = this.dao.readAll(10, 1);

		// THEN I receive 10 results
		assertThat(result).hasSize(10);
	}
	
	@Test
	public void testReadAllWithPaginationOfSmallerDB() throws Exception {
		// GIVEN the database contains 12 elements
		IntStream.range(0, 12) // Stream Integer
				.mapToObj(i -> generator.getElement()) // ElementEntity Stream
				.forEach(this.dao::create);

		// WHEN I read 10 elements after skipping the first 10 
		List<ElementEntity> result = this.dao.readAll(10, 1);

		// THEN I receive 2 results
		assertThat(result).hasSize(2);
	}
	
	@Test
	public void testReadAllSortedWithPaginationOfSmallerDB() throws Exception {
		// GIVEN the database contains 12 elements
		List<ElementEntity> ids = IntStream.range(0, 12) // Stream Integer
				.mapToObj(i -> generator.getElement()) // ElementEntity Stream
				.map(this.dao::create) // ElementEntity Stream
				.collect(Collectors.toList());

		ids.sort((l1, l2) -> l1.getKey().compareTo(l2.getKey()));

		// WHEN I read 10 elements after skipping the first 10 ordered by key
		List<ElementEntity> result = this.dao.readAll("key", 10, 1);

		// THEN I receive specific 2 results in specific order
		List<ElementEntity> expectedIds = ids.stream().skip(10).collect(Collectors.toList());

		assertThat(result).usingElementComparatorOnFields("key").containsExactlyElementsOf(expectedIds);
	}
	
	@Test
	public void testGetAllElementsByPatternAndPagination() throws Exception {
		// GIVEN the database contains 12 elements with name containing 'abc'
		// AND the database contains 20 elements that do not have a name containing
		// 'abc'
		IntStream.range(0, 12) // Stream Integer
				.mapToObj(i -> {
					ElementEntity element = generator.getElement();
					element.setName("abc");
					return element;
				}) // ElementEntity Stream
				.forEach(this.dao::create);

		IntStream.range(12, 33) // Stream Integer
				.mapToObj(i -> {
					ElementEntity element = generator.getElement();
					element.setName("xyz");
					return element;
				}) // UserEntity Stream
				.forEach(this.dao::create);

		// WHEN I read 10 elements with name containing 'abc' after skipping first 10
		// elements
		List<ElementEntity> result = this.dao.readElementWithName("abc", 10, 1);

		// THEN I receive 2 results
		assertThat(result).hasSize(2);

	}
	
	

}
