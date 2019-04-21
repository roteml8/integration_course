package smartspace.layout;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class UserBoundary {
	
	private String key;
	private String username;
	private String avatar;
	private UserRole role;
	private long points;
	
	public UserBoundary(){
		
	}
	
	public UserBoundary (UserEntity entity) {
		
		this.key = entity.getKey();
		this.username = entity.getUsername();
		this.avatar = entity.getAvatar();
		this.role = entity.getRole();
		this.points = entity.getPoints();		
	}

	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public UserEntity convertToEntity() {
		UserEntity entity = new UserEntity();
		
		entity.setKey(key);
		entity.setUsername(username);
		entity.setAvatar(avatar);
		entity.setRole(role);
		entity.setPoints(points);
		
		return entity;

	}
	

}
