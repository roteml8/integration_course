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
	public ElementEntity createNewElement(String name, String type, Location loaction, Date creationTimeStamp,
			String creatorEmail, String creatorSmartspace, boolean expired, Map<String, Object> moreAtrributes) {

		return new ElementEntity(name, type, loaction, creationTimeStamp, creatorEmail, creatorSmartspace, expired,
				moreAtrributes);
	}

	@Override
	public ActionEntity createNewAction(String elementId, String elementSmartspcae, String actionType,
			Date creationTimestamp, String playerEmail, String playerSmartspace, Map<String, Object> moreAttributes) {

		return new ActionEntity(elementId, elementSmartspcae, actionType, creationTimestamp, playerEmail,
				playerSmartspace, moreAttributes);
	}

}
