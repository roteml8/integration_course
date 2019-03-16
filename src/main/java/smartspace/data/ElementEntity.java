package smartspace.data;

import java.util.Date;
import java.util.Map;

public class ElementEntity implements SmartspaceEntity<String>{

	private String elementSmartspace;
	private String elementId;
	private Location location;
	private String name;
	private String type;
	private boolean expired;
	private String creatorSmartspace;
	private String creatorEmail;
	private Date creationTimestap;

	private Map<String, Object> moreAtrributes;


	
	
	
	
	public ElementEntity(String name, String type, Location loaction, Date creationTimestap,
			String creatorEmail, String elementSmartspace, boolean expired, Map<String, Object> moreAtrributes) {
		super();
		this.elementSmartspace = elementSmartspace;
		
		this.location = loaction;
		this.name = name;
		this.type = type;
		this.expired = expired;
		
		this.creatorEmail = creatorEmail;
		this.creationTimestap = creationTimestap;
		this.moreAtrributes = moreAtrributes;
		
		//check what to do with elementId
	}

	public ElementEntity() {
		
	}

	@Override
	public String getKey() {
		return this.elementId;
	}

	@Override
	public void setKey(String key) {
		this.elementId = key;
	}

	public String getElementSmartSpace() {
		return elementSmartspace;
	}

	public void setElementSmartSpace(String elementSmartSpace) {
		this.elementSmartspace = elementSmartSpace;
	}

	public String getElementid() {
		return elementId;
	}

	public void setElementid(String elementid) {
		this.elementId = elementid;
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

	public String getCreatorSmartSpace() {
		return creatorSmartspace;
	}

	public void setCreatorSmartSpace(String creatorSmartSpace) {
		this.creatorSmartspace = creatorSmartSpace;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	public Date getCreationTimeDate() {
		return creationTimestap;
	}

	public void setCreationTimeDate(Date creationTimeDate) {
		this.creationTimestap = creationTimeDate;
	}

	public Map<String, Object> getMoreAtrributes() {
		return moreAtrributes;
	}

	public void setMoreAtrributes(Map<String, Object> moreAtrributes) {
		this.moreAtrributes = moreAtrributes;
	}


}
