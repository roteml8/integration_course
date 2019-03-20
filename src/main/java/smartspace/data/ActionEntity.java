package smartspace.data;

import java.util.Date;
import java.util.Map;

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

	public ActionEntity() {

	}

	public ActionEntity(String elementId, String elementSmartspace, String actionType, Date creationTimestamp,
			String playerEmail, String playerSmartspace, Map<String, Object> moreAttributes) {

		super();
		this.elementId = elementId;
		this.elementSmartspace = elementSmartspace;
		this.playerSmartspace = playerSmartspace;
		this.playerEmail = playerEmail;
		this.actionType = actionType;
		this.creationTimestamp = creationTimestamp;
		this.moreAttributes = moreAttributes;
	}

	public String getActionSmartspace() {
		return actionSmartspace;
	}

	public void setActionSmartspace(String actionSmartspace) {
		this.actionSmartspace = actionSmartspace;
	}

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

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}

	@Override
	public String getKey() {
		return this.actionId;
	}

	@Override
	public void setKey(String key) {
		this.actionId = key;
	}

	@Override
	public String toString() {
		return "ActionEntity [actionSmartspace=" + actionSmartspace + ", actionId=" + actionId + ", elementSmartspcae="
				+ elementSmartspace + ", elementId=" + elementId + ", playerSmartspace=" + playerSmartspace
				+ ", playerEmail=" + playerEmail + ", actionType=" + actionType + ", creationTimestamp="
				+ creationTimestamp + ", moreAttributes=" + moreAttributes + "]";
	}

}
