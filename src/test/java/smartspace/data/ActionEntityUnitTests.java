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
public class ActionEntityUnitTests {

	@Test
	public void testSetAndGetActionSmartspaceActionEntity() throws Exception {

		// GIVEN an ActionEntity is available
		// AND a string
		ActionEntity ae = new ActionEntity();
		String expected = "testASS";

		// WHEN we invoke setActionSmartspace with the expected String
		ae.setActionSmartspace(expected);

		// THEN the actionSmartspace will equals the expected String
		assertThat(ae.getActionSmartspace()).usingDefaultComparator().isEqualTo(expected);

		ae.setActionSmartspace(null);

	}

	@Test
	public void testSetAndGetActionIdActionEntity() throws Exception {

		// GIVEN an ActionEntity is available
		// AND a string
		ActionEntity ae = new ActionEntity();
		String expected = "testAID";

		// WHEN we invoke setActionId with the expected String
		ae.setActionId(expected);

		// THEN the actionSmartspace will equals the expected String
		assertThat(ae.getActionId()).usingDefaultComparator().isEqualTo(expected);

		ae.setActionId(null);

	}

	@Test
	public void testSetAndGetElementSmartspaceActionEntity() throws Exception {

		// GIVEN an ActionEntity is available
		// AND a string
		ActionEntity ae = new ActionEntity();
		String expected = "testESS";

		// WHEN we invoke setElementSmartspace with the expected String
		ae.setElementSmartspace(expected);

		// THEN the actionSmartspace will equals the expected String
		assertThat(ae.getElementSmartspace()).usingDefaultComparator().isEqualTo(expected);

		ae.setElementSmartspace(null);

	}

	@Test
	public void testSetAndGetElementIdActionEntity() throws Exception {

		// GIVEN an ActionEntity is available
		// AND a string
		ActionEntity ae = new ActionEntity();
		String expected = "testEID";

		// WHEN we invoke setElementId with the expected String
		ae.setElementId(expected);

		// THEN the elementId will equals the expected String
		assertThat(ae.getElementId()).usingDefaultComparator().isEqualTo(expected);

		ae.setElementId(null);

	}

	@Test
	public void testSetAndGetPlayerSmartspaceActionEntity() throws Exception {

		// GIVEN an ActionEntity is available
		// AND a string
		ActionEntity ae = new ActionEntity();
		String expected = "testPSS";

		// WHEN we invoke setPlayerSmartspace with the expected String
		ae.setPlayerSmartspace(expected);

		// THEN the playerSmartspace will equals the expected String
		assertThat(ae.getPlayerSmartspace()).usingDefaultComparator().isEqualTo(expected);

		ae.setPlayerSmartspace(null);

	}

	@Test
	public void testSetAndGetPlayerEmailActionEntity() throws Exception {

		// GIVEN an ActionEntity is available
		// AND a string
		ActionEntity ae = new ActionEntity();
		String expected = "testPEM";

		// WHEN we invoke setActionId with the expected String
		ae.setPlayerEmail(expected);

		// THEN the playerEmail will equals the expected String
		assertThat(ae.getPlayerEmail()).usingDefaultComparator().isEqualTo(expected);

		ae.setPlayerEmail(null);

	}

	@Test
	public void testSetAndGetActionTypeActionEntity() throws Exception {

		// GIVEN an ActionEntity is available
		// AND a string
		ActionEntity ae = new ActionEntity();
		String expected = "testAT";

		// WHEN we invoke setActionId with the expected String
		ae.setActionType(expected);

		// THEN the actionType will equals the expected String
		assertThat(ae.getActionType()).usingDefaultComparator().isEqualTo(expected);

		ae.setActionType(null);

	}

	@Test
	public void testSetAndGetCreationTimestampActionEntity() throws Exception {

		// GIVEN an ActionEntity is available
		// AND a Date
		ActionEntity ae = new ActionEntity();
		Date expectedDate = new Date();

		// WHEN we invoke setActionId with the expected Time stamp
		ae.setCreationTimestamp(expectedDate);

		// THEN the creationTimestamp will equals the expected Date
		assertThat(ae.getCreationTimestamp()).usingDefaultComparator().isEqualTo(expectedDate);

		ae.setCreationTimestamp(null);

	}

	@Test
	public void testSetAndGetMoreAttributesActionEntity() throws Exception {

		// GIVEN an ActionEntity is available
		// AND a Map with values
		ActionEntity ae = new ActionEntity();
		Map<String, Object> expectedMap = new HashMap<>();
		expectedMap.put("test", new Boolean(true));

		// WHEN we invoke setActionId with the expected String
		ae.setMoreAttributes(expectedMap);

		// THEN the moreAttributes will equals the expected Map
		assertThat(ae.getMoreAttributes()).usingDefaultComparator().isEqualTo(expectedMap);

		ae.setMoreAttributes(null);
	}
	
	@Test
	public void testSetAndGetKey() throws Exception {
		// GIVEN an actionEntity is available
		// AND a string
		
		ActionEntity ae = new ActionEntity();
		String expected = "KeyTest";

		// WHEN we invoke setKey with the expected String
		ae.setKey(expected);

		// THEN the CreatorEmail will equals the expected String
		assertThat(ae.getKey()).usingDefaultComparator().isEqualTo(expected);

		ae.setKey(null);
	}

}
