package smartspace.data.util;

import java.util.Date;
import java.util.Map;

import smartspace.data.*;

public interface  EntityFactory {
public UserEntity createNewUser (String userEmail,String userSmartspace,
		String username,String avatar, UserRole role,long points);

public ElementEntity createNewElement(String name,String type,Location location,  Date creationTimeStamp
		,String creatorEmail,String creatorSmartspace, boolean expired,Map<String, Object> moreAttributes);


public ActionEntity createNewAction(String actionId,String elementSmartspcae,String actionType, Date creationTimeStamp
		,String playerEmail,String playerSmartspace,Map<String,Object> moreAttributes);
}
