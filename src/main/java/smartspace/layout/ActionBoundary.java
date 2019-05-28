package smartspace.layout;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.data.ActionEntity;

public class ActionBoundary {
	
	private Map<String,String> actionKey;
	private Map<String,String> element;
	private Map<String,String> player;
	private String type;
	private Date created;
	private Map<String, Object> properties;
	
	public Map<String, String> getKey() {
		return actionKey;
	}

	public void setKey(Map<String, String> key) {
		this.actionKey = key;
	}

	public Map<String, String> getElement() {
		return element;
	}

	public void setElement(Map<String, String> element) {
		this.element = element;
	}

	public Map<String, String> getPlayer() {
		return player;
	}

	public void setPlayer(Map<String, String> player) {
		this.player = player;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreationTimeStamp() {
		return created;
	}

	public void setCreationTimeStamp(Date creationTimeStamp) {
		this.created = creationTimeStamp;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> moreAttributes) {
		this.properties = moreAttributes;
	}


	
	public ActionBoundary(){
		
	}
	
	public ActionBoundary (ActionEntity entity) {
		
		this.actionKey = new HashMap<>();
		this.actionKey.put("id", entity.getActionId());
		this.actionKey.put("smartspace", entity.getActionSmartspace());	
		this.type = entity.getActionType();
		this.created = entity.getCreationTimestamp();
		this.element = new HashMap<>();	
		this.element.put("smartspace", entity.getElementSmartspace());
		this.element.put("id", entity.getElementId());
		this.player = new HashMap<>();
		this.player.put("smartspace",entity.getPlayerSmartspace());
		this.player.put("email", entity.getPlayerEmail());

		this.properties = entity.getMoreAttributes();
		
	}

	
	public ActionEntity convertToEntity() {
		ActionEntity entity = new ActionEntity();
		System.err.println(properties);
		if (actionKey != null && actionKey.get("smartspace")!= null && actionKey.get("id")!= null
				&&!actionKey.get("smartspace").trim().isEmpty()
				&&!actionKey.get("id").trim().isEmpty() )
			entity.setKey(actionKey.get("smartspace")+"#"+actionKey.get("id"));
		//entity.setKey(key.get("smartspace")+"#"+key.get("email"));
		if (element != null && element.get("smartspace")!= null && element.get("id")!= null) {
			entity.setElementId(element.get("id"));
			entity.setElementSmartspace(element.get("smartspace"));
		}
		if (player != null && player.get("smartspace")!= null && player.get("email")!= null) {
			entity.setPlayerEmail(player.get("email"));
			entity.setPlayerSmartspace(player.get("smartspace"));
		}
           
		entity.setActionType(type);
		entity.setCreationTimestamp(created);
		
		//if(moreAttributes != null)
			entity.setMoreAttributes(properties);
		//else
			//entity.setMoreAttributes(new HashMap<>());
		
		return entity;

	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
