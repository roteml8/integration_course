package smartspace.data.util;

import java.util.Date;
import java.util.Map;

import smartspace.data.*;

public class EntityFactoryImpl implements EntityFactory {

	@Override
	public UserEntity createNewUser(String userEmail, String userSmartspace, String username, String avatar,
			UserRole role, long points) {

		return new UserEntity(userEmail, userSmartspace, username, avatar, role, points);
	}

	@Override
	public ElementEntity createNewElement(String name, String type, Location loaction, Date creationTimestap,
			String creatorEmail, String elementSmartSpace, boolean expired, Map<String, Object> moreAtrributes) {

		return new ElementEntity(name, type, loaction, creationTimestap, creatorEmail, elementSmartSpace, expired,
				moreAtrributes);
	}

	@Override
	public ActionEntity createNewAction(String actionid, String elementSmartspcae, String actionType,
			Date creationTimestamp, String playerEmail, String playerSmartspace, Map<String, Object> moreAttributes) {

		return new ActionEntity(actionid, elementSmartspcae, actionType, creationTimestamp, playerEmail,
				playerSmartspace, moreAttributes);
	}

}
