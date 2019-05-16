package smartspace.data;
/*
 
 
import java.util.Date;
import java.util.Map;

import javax.persistence.Convert;
import javax.persistence.Lob;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import smartspace.dao.rdb.MapToJsonConverter;
*/
import org.springframework.data.annotation.Transient;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Talk with eyal about action key
// Ask about @value in rdbActionDao: why it doesnt work?
//	for now i do this.smartSpace = "2019B.Amitz4.SmartSpace"
// assertThat(actionInDB.getActionId()).isGreaterThan("0") doesnt work
// Ask why @JsonIgnore doesnt work
// After i read from db, the key fields are null, i must do set to split the key field
// Line 503 in ElementControllerIntegrationADMINTests, the skip shuld get 9 or 10?
// Ask eyal what is the object that return from save function in mongoDb

@Document(collection = "Action")
public class ActionEntity implements SmartspaceEntity<String> {
	private String actionSmartspace;
	private String actionId;
	private String elementSmartspace;
	private String elementId;
	private String playerSmartspace;
	private String playerEmail;
	private String actionType;
	private Date creationTimestamp;
	private Map<String, Object> moreAttributes;
	private String key;


	public ActionEntity() {
	}

	public ActionEntity(String elementId, String elementSmartspace, String actionType, Date creationTimestamp,
			String playerEmail, String playerSmartspace, Map<String, Object> moreAttributes) {
		this.elementId = elementId;
		this.elementSmartspace = elementSmartspace;
		this.playerSmartspace = playerSmartspace;
		this.playerEmail = playerEmail;
		this.actionType = actionType;
		this.creationTimestamp = creationTimestamp;
		this.moreAttributes = moreAttributes;
	}

//	@Transient
	public String getActionSmartspace() {
		return actionSmartspace;
	}

	public void setActionSmartspace(String actionSmartspace) {
		this.actionSmartspace = actionSmartspace;
	}

//	@Transient
	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionid) {
		this.actionId = actionid;
	}

	public String getElementSmartspace() {
		return elementSmartspace;
	}

	public void setElementSmartspace(String elementSmartspcae) {
		this.elementSmartspace = elementSmartspcae;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementid) {
		this.elementId = elementid;
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
//	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}
//	@Lob
//	@Convert(converter=MapToJsonConverter.class)
	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}


	@Id
	public String getKey() {
		return this.actionSmartspace + "#" + this.actionId;
	}

	
	public void setKey(String key) {
		String[] parts = key.split("#");
		this.actionId = parts[1];
		this.actionSmartspace = parts[0];
		this.key = key;
	}

	@Override
	public String toString() {
		return "ActionEntity [actionSmartspace=" + actionSmartspace + ", actionId=" + actionId + ", elementSmartspcae="
				+ elementSmartspace + ", elementId=" + elementId + ", playerSmartspace=" + playerSmartspace
				+ ", playerEmail=" + playerEmail + ", actionType=" + actionType + ", creationTimestamp="
				+ creationTimestamp + ", moreAttributes=" + moreAttributes + "]";
	}

}
