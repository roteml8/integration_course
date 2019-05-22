package smartspace.data;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "Users")
public class UserEntity implements SmartspaceEntity<String> {
	@Transient
	private String userSmartspace;
	@Transient
	private String userEmail;
	
	private String username;
	private String avatar;
	private UserRole role;
	private long points = Long.MIN_VALUE;
	private Map<String, Object> details;
	private String key;

	public UserEntity() {
	}

	public UserEntity(String userEmail, String userSmartspace, String username, String avatar, UserRole role,
			long points) {

		super();
		this.userEmail = userEmail;
		this.userSmartspace = userSmartspace;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
		this.points = points;
	}

	public UserEntity(String userEmail) {
		super();
		this.userEmail = userEmail;
	}

	public String getUserSmartspace() {
		return userSmartspace;
	}

	public void setUserSmartspace(String userSmartspace) {
		this.userSmartspace = userSmartspace;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	@Id
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		String[] parts = key.split("#");
		this.userSmartspace = parts[0];
		this.userEmail = parts[1];
		this.key = key;
	}
	
	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "UserEntity [userSmartspace=" + userSmartspace + ", userEmail=" + userEmail + ", username=" + username
				+ ", avatar=" + avatar + ", role=" + role + ", points=" + points + ", details=" + details + "]";
	}

}
