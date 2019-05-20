package smartspace.layout;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.data.ElementEntity;
import smartspace.data.Location;

public class ElementBoundary {
	
	private Map<String,String> key;
	private Map<String,Double> latlng;
	private String name;
	private String elementType;
	private boolean expired;

	private Map<String,String> creator;
	private Date created;
	private Map<String, Object> elementProperties;
	
	public ElementBoundary(){
		
	}
	
	public ElementBoundary (ElementEntity entity) {
		
		this.key = new HashMap<>();
		this.key.put("id", entity.getElementid());
		this.key.put("smartspace", entity.getElementSmartSpace());
		this.creator = new HashMap<>();
		this.creator.put("email",entity.getCreatorEmail());
		this.creator.put("smartspace",entity.getCreatorSmartSpace());
		this.latlng = new HashMap<>();
//		this.latlng.put("lat", Double.valueOf(entity.getLocation().getX()));
//		this.latlng.put("lng", Double.valueOf(entity.getLocation().getY()));
		this.latlng.put("lat",entity.getLocation().getX());
		this.latlng.put("lng", entity.getLocation().getY());

		this.name = entity.getName();
		this.elementType = entity.getType();
		this.expired = entity.isExpired();
		this.elementProperties = entity.getMoreAttributes();
		this.created = entity.getCreationTimeDate();
		
	}


	
	public Map<String,String> getKey() {
		return key;
	}

	public void setKey(Map<String,String> key) {
		this.key = key;
	}

	public Map<String,Double> getLatlng() {
		return latlng;
	}

	public void setLatlng(Map<String,Double> location) {
		this.latlng = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public ElementEntity convertToEntity() {
		ElementEntity entity = new ElementEntity();
		if (key != null && key.get("smartspace")!= null && key.get("id")!= null
				&&!key.get("smartspace").trim().isEmpty()
				&&!key.get("id").trim().isEmpty() )
			entity.setKey(key.get("smartspace")+"#"+key.get("id"));
		/*else
		{
			entity.setElementSmartSpace(key.get("smartspace"));
			entity.setElementid(key.get("id"));
		}
		*/
		Location l = new Location();
		l.setX(this.latlng.get("lat"));
		l.setY(this.latlng.get("lng"));
		entity.setLocation(l);
		entity.setName(name);
		entity.setType(elementType);
		entity.setExpired(expired);
		if (creator != null && creator.get("smartspace")!= null && creator.get("email")!= null
				&&!creator.get("smartspace").trim().isEmpty()
				&&!creator.get("email").trim().isEmpty() )
		{
			entity.setCreatorSmartSpace(this.creator.get("smartspace"));
			entity.setCreatorEmail(this.creator.get("email"));

		}
		entity.setCreationTimeDate(created);
		entity.setMoreAttributes(elementProperties);
		
		return entity;

	}

	public Map<String, String> getCreator() {
		return creator;
	}

	public void setCreator(Map<String, String> creator) {
		this.creator = creator;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Map<String, Object> getElementProperties() {
		return elementProperties;
	}

	public void setElementProperties(Map<String, Object> elementProperties) {
		this.elementProperties = elementProperties;
	}
	

}
