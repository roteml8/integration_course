package smartspace.layout;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import smartspace.data.ElementEntity;
import smartspace.data.Location;

public class ElementBoundary {
	
	private Map<String,Object> key;
	private Location location;
	private String name;
	private String type;
	private boolean expired;
	private String creatorSmartspace;
	private String creatorEmail;
	private Date creationTimeStamp;
	private Map<String, Object> moreAttributes;
	
	public ElementBoundary(){
		
	}
	
	public ElementBoundary (ElementEntity entity) {
		
		this.key = new HashMap<>();
		this.key.put("id", entity.getElementid());
		this.key.put("smartspace", entity.getElementSmartSpace());
		this.location = entity.getLocation();
		this.name = entity.getName();
		this.type = entity.getType();
		this.expired = entity.isExpired();
		this.creatorSmartspace = entity.getCreatorSmartSpace();
		this.creatorEmail = entity.getCreatorEmail();
		this.moreAttributes = entity.getMoreAttributes();
		
	}


	
	public Map<String,Object> getKey() {
		return key;
	}

	public void setKey(Map<String,Object> key) {
		this.key = key;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public String getCreatorSmartspace() {
		return creatorSmartspace;
	}

	public void setCreatorSmartspace(String creatorSmartspace) {
		this.creatorSmartspace = creatorSmartspace;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
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

	public ElementEntity convertToEntity() {
		ElementEntity entity = new ElementEntity();
		
		entity.setKey(key.get("smartspace")+"#"+key.get("id"));
		entity.setLocation(location);
		entity.setName(name);
		entity.setType(type);
		entity.setExpired(expired);
		entity.setCreatorSmartSpace(creatorSmartspace);
		entity.setCreatorEmail(creatorEmail);
		entity.setCreationTimeDate(creationTimeStamp);
		entity.setMoreAttributes(moreAttributes);
		
		return entity;

	}
	

}
