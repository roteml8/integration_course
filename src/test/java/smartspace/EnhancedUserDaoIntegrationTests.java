package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.util.EntityFactory;
import smartspace.data.util.FakeUserGenerator;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class EnhancedUserDaoIntegrationTests {

	private EnhancedUserDao<String> dao;
	private EntityFactory factory;
	private String smartspace;
	private FakeUserGenerator generator;

	@Autowired
	public void setDao(EnhancedUserDao<String> dao) {
		this.dao = dao;
	}

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@Autowired
	public void setGenerator(FakeUserGenerator generator) {
		this.generator = generator;
	}

	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
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
		// GIVEN the database contains 50 Users
		IntStream.range(0, 50) // Stream Integer
				.mapToObj(i -> generator.getUser()) // UserEntity Stream
				.forEach(this.dao::create);

		// WHEN I read 10 Users after skipping first 10 users
		List<UserEntity> result = this.dao.readAll(10, 1);

		// THEN I receive 10 results
		assertThat(result).hasSize(10);
	}

	@Test
	public void testReadAllWithPaginationOfSmallerDB() throws Exception {
		// GIVEN the database contains 12 users
		IntStream.range(0, 12) // Stream Integer
				.mapToObj(i -> generator.getUser()) // UserEntity Stream
				.forEach(this.dao::create);

		// WHEN I read 10 messages after skipping first 10 messages
		List<UserEntity> result = this.dao.readAll(10, 1);

		// THEN I receive 2 results
		assertThat(result).hasSize(2);
	}

	@Test
	public void testReadAllSortedWithPaginationOfSmallerDB() throws Exception {
		// GIVEN the database contains 12 users
		List<UserEntity> ids = IntStream.range(0, 12) // Stream Integer
				.mapToObj(i -> generator.getUser()) // UserEntity Stream
				.map(this.dao::create) // UserEntity Stream
//		.map(MessageEntity::getKey)// Long Stream
				.collect(Collectors.toList());

		ids.sort((l1, l2) -> l1.getKey().compareTo(l2.getKey()));

		// WHEN I read 10 messages after skipping first 10 messages ordered by key
		List<UserEntity> result = this.dao.readAll("key", 10, 1);

		// THEN I receive specific 2 results in specific order
		List<UserEntity> expectedIds = ids.stream().skip(10).collect(Collectors.toList());

		assertThat(result).usingElementComparatorOnFields("key").containsExactlyElementsOf(expectedIds);
	}

	@Test
	public void testGetAllMessagesByPatternAndPagination() throws Exception {
		// GIVEN the database contains 12 users with name containing 'abc'
		// AND the database contains 20 users that do not have with name containing
		// 'abc'
		IntStream.range(0, 12) // Stream Integer
				.mapToObj(i -> {
					UserEntity user = generator.getUser();
					user.setUsername("abc" + i);
					return user;
				}) // UserEntity Stream
				.forEach(this.dao::create);

		IntStream.range(12, 33) // Stream Integer
				.mapToObj(i -> {
					UserEntity user = generator.getUser();
					user.setUsername("xyz" + i);
					return user;
				}) // UserEntity Stream
				.forEach(this.dao::create);

		// WHEN I read 10 users with name containing 'abc' after skipping first 10
		// users
		List<UserEntity> result = this.dao.readUserWithNameContaining("abc", 10, 1);

		// THEN I receive 2 results
		assertThat(result).hasSize(2);

	}
}
