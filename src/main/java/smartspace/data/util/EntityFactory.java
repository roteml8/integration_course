package smartspace.data.util;

import java.util.Date;
import java.util.Map;

import smartspace.data.*;

public interface  EntityFactory {
public UserEntity createNewUser (String userEmail,String userSmartspace,
		String username,String avatar, UserRole role,long points);

public ElementEntity createNewElement(String name,String type,Location loaction,  Date creationTimestap
		,String creatorEmail,String elementSmartSpace, boolean expired,Map<String, Object> moreAtrributes);


public ActionEntity createNewAction(String actionid,String elementSmartspcae,String actionType, Date creationTimestamp
		,String  playerEmail,String playerSmartspace,Map<String,Object> moreAttributes);
}
