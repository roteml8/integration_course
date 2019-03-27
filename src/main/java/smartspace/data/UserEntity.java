package smartspace.data;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ACTIONS")
public class UserEntity implements SmartspaceEntity<String> {

	private String userSmartspace;
	private String userEmail;
	private String username;
	private String avatar;
	private UserRole role;
	private long points;

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
	
	public UserEntity (String userEmail) {
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
	@Enumerated(EnumType.STRING)
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

	@Override
	@Id
	@Column(name="ID")
	public String getKey() {
		return this.userSmartspace+"#"+this.userEmail;
	}

	@Override
	public void setKey(String key) {
		String[] parts = key.split("#");
		this.userSmartspace = parts[0];
		this.userEmail = parts[1];
	}

}
