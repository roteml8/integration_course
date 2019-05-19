package smartspace.layout;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.data.ActionEntity;

public class ActionBoundary {
	
	private Map<String,String> key;
	private Map<String,String> element;
	private Map<String,String> player;
	private String actionType;
	private Date creationTimeStamp;
	private Map<String, Object> moreAttributes;
	
	public Map<String, String> getKey() {
		return key;
	}

	public void setKey(Map<String, String> key) {
		this.key = key;
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


	
	public ActionBoundary(){
		
	}
	
	public ActionBoundary (ActionEntity entity) {
		
		this.key = new HashMap<>();
		this.key.put("id", entity.getActionId());
		this.key.put("smartspace", entity.getActionSmartspace());
		this.element = new HashMap<>();
		this.element.put("smartspace", entity.getElementSmartspace());
		this.element.put("id", entity.getElementId());
		this.player = new HashMap<>();
		this.player.put("smartspace",entity.getPlayerSmartspace());
		this.player.put("email", entity.getPlayerEmail());
		this.actionType = entity.getActionType();
		this.creationTimeStamp = entity.getCreationTimestamp();
		this.moreAttributes = entity.getMoreAttributes();
		
	}

	
	public ActionEntity convertToEntity() {
		ActionEntity entity = new ActionEntity();
		if (key != null && key.get("smartspace")!= null && key.get("id")!= null
				&&!key.get("smartspace").trim().isEmpty()
				&&!key.get("id").trim().isEmpty() )
			entity.setKey(key.get("smartspace")+"#"+key.get("id"));
		//entity.setKey(key.get("smartspace")+"#"+key.get("email"));
		if (element != null && element.get("smartspace")!= null && element.get("id")!= null) {
			entity.setElementId(element.get("id"));
			entity.setElementSmartspace(element.get("smartspace"));
		}
		if (player != null && player.get("smartspace")!= null && player.get("email")!= null) {
			entity.setPlayerEmail(player.get("email"));
			entity.setPlayerSmartspace(player.get("smartspace"));
		}

		entity.setActionType(actionType);
		entity.setCreationTimestamp(creationTimeStamp);
		
		//if(moreAttributes != null)
			entity.setMoreAttributes(moreAttributes);
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
