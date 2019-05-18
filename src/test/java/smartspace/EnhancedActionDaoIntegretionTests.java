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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.EnhancedActionDao;
 
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.util.EntityFactory;
import smartspace.data.util.FakeActionGenerator;
 
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class EnhancedActionDaoIntegretionTests {
private EnhancedActionDao actionDao;
private FakeActionGenerator generator;
private EntityFactory factory;
private String smartspace;



@Autowired
public void setDao(EnhancedActionDao actionDao) {
	this.actionDao = actionDao;
}

@Autowired
public void setFactory(EntityFactory factory) {
	this.factory = factory;
}

@Autowired
public void setGenerator(FakeActionGenerator generator) {
	this.generator = generator;
}

@Value("${name.of.Smartspace:smartspace}")
public void setSmartspace(String smartspace) {
	this.smartspace = smartspace;
}



@Before
public void setup() {
	actionDao.deleteAll();
}

@After
public void teardown() {
	actionDao.deleteAll();
}


@Test
public void testReadAllWithPagination() throws Exception {
	// GIVEN the database contains 50 elements
	IntStream.range(0, 50) // Stream Integer
			.mapToObj(i -> generator.getAction()) // ElementEntity Stream
			.forEach(this.actionDao::create);

	// WHEN I read 10 elements after skipping the first 10 
	List<ActionEntity> result = this.actionDao.readAll(10, 1);

	// THEN I receive 10 results
	assertThat(result).hasSize(10);
}


@Test
public void testReadAllWithPaginationOfSmallerDB() throws Exception {
	// GIVEN the database contains 12 elements
	IntStream.range(0, 12) // Stream Integer
			.mapToObj(i -> generator.getAction()) // ElementEntity Stream
			.forEach(this.actionDao::create);

	// WHEN I read 10 elements after skipping the first 10 
	List<ActionEntity> result = this.actionDao.readAll(10, 1);

	// THEN I receive 2 results
	assertThat(result).hasSize(2);
}


@Test
public void testReadAllSortedWithPaginationOfSmallerDB() throws Exception {
	// GIVEN the database contains 12 elements
	List<ActionEntity> ids = IntStream.range(0, 12) // Stream Integer
			.mapToObj(i -> generator.getAction()) // ElementEntity Stream
			.map(this.actionDao::create) // ElementEntity Stream
			.collect(Collectors.toList());

	ids.sort((l1, l2) -> l1.getKey().compareTo(l2.getKey()));

	// WHEN I read 10 elements after skipping the first 10 ordered by key
	List<ActionEntity> result = this.actionDao.readAll("key", 10, 1);

	// THEN I receive specific 2 results in specific order
	List<ActionEntity> expectedIds = ids.stream().skip(10).collect(Collectors.toList());

	assertThat(result).usingElementComparatorOnFields("key").containsExactlyElementsOf(expectedIds);
}

@Test
public void testGetAllActionsByPatternAndPagination() throws Exception {
	// GIVEN the database contains 12 elements with name containing 'abc'
	// AND the database contains 20 elements that do not have a name containing
	// 'abc'
	IntStream.range(0, 12) // Stream Integer
			.mapToObj(i -> {
				ActionEntity action = generator.getAction();
				action.setActionType("abc" + i);
				return action;
			}) // ElementEntity Stream
			.forEach(this.actionDao::create);

	IntStream.range(12, 33) // Stream Integer
			.mapToObj(i -> {
				ActionEntity action = generator.getAction();
				action.setActionType("xyz" + i);
				return action;
			}) // UserEntity Stream
			.forEach(this.actionDao::create);

	// WHEN I read 10 elements with name containing 'abc' after skipping first 10
	// elements
	List<ActionEntity> result = this.actionDao.readActionWithActionTypeContaining("abc", 10, 1);

	// THEN I receive 2 results
	assertThat(result).hasSize(2);

}

}
