package smartspace.data.util;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import smartspace.data.*;

@Component
public class EntityFactoryImpl implements EntityFactory {

	@Override
	public UserEntity createNewUser(String userEmail, String userSmartspace, String username, String avatar,
			UserRole role, long points) {

		return new UserEntity(userEmail, userSmartspace, username, avatar, role, points);
	}

	@Override
	public ElementEntity createNewElement(String name, String type, Location location, Date creationTimeStamp,
			String creatorEmail, String creatorSmartspace, boolean expired, Map<String, Object> moreAttributes) {

		return new ElementEntity(name, type, location, creationTimeStamp, creatorEmail, creatorSmartspace, expired,
				moreAttributes);
	}

	@Override
	public ActionEntity createNewAction(String elementId, String elementSmartspcae, String actionType,
			Date creationTimeStamp, String playerEmail, String playerSmartspace, Map<String, Object> moreAttributes) {

		return new ActionEntity(elementId, elementSmartspcae, actionType, creationTimeStamp, playerEmail,
				playerSmartspace, moreAttributes);
	}

}
