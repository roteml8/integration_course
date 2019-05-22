package smartspace.data;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;




@Document(collection = "Elements")
public class ElementEntity implements SmartspaceEntity<String> {
	@Transient
	private String elementSmartspace;
	
	@Transient
	private String elementId;
	
	private Location location;
	private String name;
	private String type;
	private boolean expired;
	private String creatorSmartspace;
	private String creatorEmail;
	private Date creationTimeStamp;
	@Id
	private String key;

	private Map<String, Object> moreAttributes;
	
	public ElementEntity() {
	}

	public ElementEntity(String name, String type, Location loaction, Date creationTimeStamp, String creatorEmail,
			String creatorSmartspace, boolean expired, Map<String, Object> moreAttributes) {

		super();
		this.creatorSmartspace = creatorSmartspace;
		this.location = loaction;
		this.name = name;
		this.type = type;
		this.expired = expired;
		this.creatorEmail = creatorEmail;
		this.creationTimeStamp = creationTimeStamp;
		this.moreAttributes = moreAttributes;
	}

	
	public String getKey() {
		return this.key;
	}


	public void setKey(String key) {
		String[] parts = key.split("#");
		this.elementSmartspace = parts[0];
		this.elementId = parts[1];
		this.key = key;
	}

	public String getElementSmartSpace() {
		return this.elementSmartspace;
	}

	public void setElementSmartSpace(String elementSmartSpace) {
		this.elementSmartspace = elementSmartSpace;
	}

	public String getElementid() {
		return this.elementId;
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
		return creationTimeStamp;
	}

	public void setCreationTimeDate(Date creationTimeDate) {
		this.creationTimeStamp = creationTimeDate;
	}

	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementEntity other = (ElementEntity) obj;
		if (creationTimeStamp == null) {
			if (other.creationTimeStamp != null)
				return false;
		} else if (!creationTimeStamp.equals(other.creationTimeStamp))
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
		if (moreAttributes == null) {
			if (other.moreAttributes != null)
				return false;
		} else if (!moreAttributes.equals(other.moreAttributes))
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
				+ creatorSmartspace + ", creatorEmail=" + creatorEmail + ", creationTimestap=" + creationTimeStamp
				+ ", moreAttributes=" + moreAttributes + "]";
	}

}
