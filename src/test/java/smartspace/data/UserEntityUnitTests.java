package smartspace.data;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })

public class UserEntityUnitTests {
	
	@Test
	public void testSetAndGetUserSmartspace() throws Exception {

		// GIVEN a UserEntity is available
		// AND a string
		UserEntity ue = new UserEntity();
		String expected = "smartspace";

		// WHEN we invoke setUserSmartspace with the expected String
		ue.setUserSmartspace(expected);

		// THEN the userSmartspace is the expected String
		assertThat(ue.getUserSmartspace()).usingDefaultComparator().isEqualTo(expected);

		ue.setUserSmartspace(null);

	}
	
	@Test
	public void testSetAndGetUserEmail() throws Exception {

		// GIVEN a UserEntity is available
		// AND a string representing email address
		UserEntity ue = new UserEntity();
		String expected = "theemail@test.com";

		// WHEN we invoke setUserEmail with the expected String
		ue.setUserEmail(expected);

		// THEN the userEmail is the expected String
		assertThat(ue.getUserEmail()).usingDefaultComparator().isEqualTo(expected);

		ue.setUserEmail(null);

	}
	
	@Test
	public void testSetAndGetUsername() throws Exception {

		// GIVEN a UserEntity is available
		// AND a string representing username
		UserEntity ue = new UserEntity();
		String expected = "thename";

		// WHEN we invoke setUsername with the expected String
		ue.setUsername(expected);

		// THEN the username is the expected String
		assertThat(ue.getUsername()).usingDefaultComparator().isEqualTo(expected);

		ue.setUsername(null);

	}
	
	@Test
	public void testSetAndGetUserAvatar() throws Exception {

		// GIVEN a UserEntity is available
		// AND a string representing an avatar
		UserEntity ue = new UserEntity();
		String expected = "theavatar";

		// WHEN we invoke setUserAvatar with the expected String
		ue.setAvatar(expected);

		// THEN the userAvatar is the expected String
		assertThat(ue.getAvatar()).usingDefaultComparator().isEqualTo(expected);

		ue.setAvatar(null);

	}
	
	@Test
	public void testSetAndGetUserRole() throws Exception {

		// GIVEN a UserEntity is available
		// AND a UserRole object 
		UserEntity ue = new UserEntity();
		UserRole expected = UserRole.MANAGER;

		// WHEN we invoke setUserRole with the expected UserRole
		ue.setRole(expected);

		// THEN the userRole is the expected UserRole
		assertThat(ue.getRole()).usingDefaultComparator().isEqualTo(expected);

		ue.setRole(null);

	}
	
	@Test
	public void testSetAndGetUserPoins() throws Exception {

		// GIVEN a UserEntity is available
		// AND a long representing points
		UserEntity ue = new UserEntity();
		long expected = 88;

		// WHEN we invoke setPoints with the expected long
		ue.setPoints(expected);

		// THEN the points is the expected UserRole
		assertThat(ue.getPoints()).usingDefaultComparator().isEqualTo(expected);

		ue.setPoints(0);

	}




}
