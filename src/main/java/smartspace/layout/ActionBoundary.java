package smartspace.layout;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import smartspace.data.ActionEntity;

public class ActionBoundary {
	
	private Map<String,Object> key;
	private String elementSmartspace;
	private String elementId;
	private String playerSmartspace;
	private String playerEmail;
	private String actionType;
	private Date creationTimeStamp;
	private Map<String, Object> moreAttributes;
	
	public ActionBoundary(){
		
	}
	
	public ActionBoundary (ActionEntity entity) {
		
		this.key = new HashMap<>();
		this.key.put("id", entity.getActionId());
		this.key.put("smartspace", entity.getActionSmartspace());
		this.actionType = entity.getActionType();
		this.creationTimeStamp = entity.getCreationTimestamp();
		this.key = new HashMap<>();
		this.key.put("id", entity.getElementId());
		this.key.put("smartspace", entity.getElementSmartspace());
		this.key = new HashMap<>();
		this.key.put("smartspace", entity.getPlayerSmartspace());
		this.key.put("email", entity.getPlayerEmail());
		
	}

	public  Map<String,Object> getKey() {
		return key;
	}

	public void setKey( Map<String,Object> key) {
		this.key = key;
	}

	public String getElementSmartspace() {
		return elementSmartspace;
	}

	public void setElementSmartspace(String elementSmartspace) {
		this.elementSmartspace = elementSmartspace;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getPlayerSmartspace() {
		return playerSmartspace;
	}

	public void setPlayerSmartspace(String playerSmartspace) {
		this.playerSmartspace = playerSmartspace;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Date getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(Date creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}

	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}
	
	public ActionEntity convertToEntity() {
		ActionEntity entity = new ActionEntity();
		
		entity.setKey(key.get("smartspace")+"#"+key.get("email"));
		entity.setElementSmartspace(elementSmartspace);
		entity.setElementId(elementId);
		entity.setPlayerSmartspace(playerSmartspace);
		entity.setPlayerEmail(playerEmail);
		entity.setActionType(actionType);
		entity.setCreationTimestamp(creationTimeStamp);
		entity.setMoreAttributes(moreAttributes);
		
		return entity;

	}
	

}
