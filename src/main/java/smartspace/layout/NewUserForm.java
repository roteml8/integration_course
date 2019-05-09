package smartspace.layout;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class NewUserForm {

	private String email;
	private UserRole role;
	private String username;
	private String avatar;

	
	public NewUserForm(){
		
	}
	
	public NewUserForm (UserEntity entity) {
		
		this.email = entity.getUserEmail();
		this.role = entity.getRole();
		this.username = entity.getUsername();
		this.avatar = entity.getAvatar();
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public UserEntity convertToEntity() {
		UserEntity entity = new UserEntity();
		
		entity.setUserEmail(email);
		entity.setUsername(username);
		entity.setAvatar(avatar);
		entity.setRole(role);
		
		return entity;

	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
