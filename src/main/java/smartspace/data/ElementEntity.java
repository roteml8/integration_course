package smartspace.data;

import java.util.Date;
import java.util.Map;

public class ElementEntity implements SmartspaceEntity<String> {



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

	public ElementEntity(String name, String type, Location loaction, Date creationTimestap, String creatorEmail,
			String elementSmartspace, boolean expired, Map<String, Object> moreAtrributes) {

		super();
		this.elementSmartspace = creatorSmartspace;
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

	

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementEntity other = (ElementEntity) obj;
		if (creationTimestap == null) {
			if (other.creationTimestap != null)
				return false;
		} else if (!creationTimestap.equals(other.creationTimestap))
			return false;
		if (creatorEmail == null) {
			if (other.creatorEmail != null)
				return false;
		} else if (!creatorEmail.equals(other.creatorEmail))
			return false;
		if (creatorSmartspace == null) {
			if (other.creatorSmartspace != null)
				return false;
		} else if (!creatorSmartspace.equals(other.creatorSmartspace))
			return false;
		if (elementId == null) {
			if (other.elementId != null)
				return false;
		} else if (!elementId.equals(other.elementId))
			return false;
		if (elementSmartspace == null) {
			if (other.elementSmartspace != null)
				return false;
		} else if (!elementSmartspace.equals(other.elementSmartspace))
			return false;
		if (expired != other.expired)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (moreAtrributes == null) {
			if (other.moreAtrributes != null)
				return false;
		} else if (!moreAtrributes.equals(other.moreAtrributes))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ElementEntity [elementSmartspace=" + elementSmartspace + ", elementId=" + elementId + ", location="
				+ location + ", name=" + name + ", type=" + type + ", expired=" + expired + ", creatorSmartspace="
				+ creatorSmartspace + ", creatorEmail=" + creatorEmail + ", creationTimestap=" + creationTimestap
				+ ", moreAtrributes=" + moreAtrributes + "]";
	}

}
