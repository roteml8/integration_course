import java.util.Date;
import java.util.Map;

public class ElementEntity implements SmartSpaceEntity<String>{

	private String elementSmartSpace;
	private String elementid;
	private Location location;
	private String name;
	private String type;
	private boolean expired;
	private String creatorSmartSpace;
	private String creatorEmail;
	private Date creationTimestap;

	private Map<String, Object> moreAtrributes;


	
	
	
	
	public ElementEntity(String name, String type, Location loaction, Date creationTimestap,
			String creatorEmail, String elementSmartSpace, boolean expired, Map<String, Object> moreAtrributes) {
		
		this.elementSmartSpace = elementSmartSpace;
		
		this.location = loaction;
		this.name = name;
		this.type = type;
		this.expired = expired;
		
		this.creatorEmail = creatorEmail;
		this.creationTimestap = creationTimestap;
		this.moreAtrributes = moreAtrributes;
	}

	public ElementEntity() {
		
	}

	@Override
	public String getKey() {
		return this.elementid;
	}

	@Override
	public void setKey(String key) {
		this.elementid = key;
	}

	public String getElementSmartSpace() {
		return elementSmartSpace;
	}

	public void setElementSmartSpace(String elementSmartSpace) {
		this.elementSmartSpace = elementSmartSpace;
	}

	public String getElementid() {
		return elementid;
	}

	public void setElementid(String elementid) {
		this.elementid = elementid;
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
		return creatorSmartSpace;
	}

	public void setCreatorSmartSpace(String creatorSmartSpace) {
		this.creatorSmartSpace = creatorSmartSpace;
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
