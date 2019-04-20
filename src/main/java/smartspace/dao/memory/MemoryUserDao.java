package smartspace.dao.memory;

import java.util.ArrayList;  

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

import smartspace.dao.*;
import smartspace.data.*;

//@Repository
public class MemoryUserDao implements UserDao<String> {
	private String smartspace;
	private List<UserEntity> userEntitys;
	
	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
  
	
	public MemoryUserDao() {
		this.userEntitys = Collections.synchronizedList(new ArrayList<>());
	}	
	
	@Override
	public UserEntity create(UserEntity userEntity)  {
		userEntity.setKey(smartspace+"#"+userEntity.getUserEmail());
		this.userEntitys.add(userEntity);
		return userEntity;
	}

	@Override
	public Optional<UserEntity> readById(String userkey) {
		UserEntity target = null;
		for (UserEntity current : this.userEntitys) {
			if (current.getKey().equals(userkey)) {
				target = current;
			}
		}
		if (target != null) {
			return Optional.of(target);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public List<UserEntity> readAll() {
		return this.userEntitys;
	}

	@Override
	public void update(UserEntity userEntity) {
		synchronized (this.userEntitys) {
			UserEntity existing = this.readById(userEntity.getKey())
					.orElseThrow(() -> new RuntimeException("User Entity is not in dao, can't update!"));
			if (userEntity.getAvatar() != null) {
				existing.setAvatar(userEntity.getAvatar());
			}

			if (userEntity.getRole() != null) {
				existing.setRole(userEntity.getRole());
			}
			if (userEntity.getUserEmail() != null) {
				existing.setUserEmail(userEntity.getUserEmail());
			}

			if (userEntity.getUsername() != null) {
				existing.setUsername(userEntity.getUsername());
			}

			if (userEntity.getUserSmartspace() != null) {
				existing.setUserSmartspace(userEntity.getUserSmartspace());
			}
			
			if (userEntity.getPoints() != Long.MIN_VALUE) {
				existing.setPoints(userEntity.getPoints());
			}
		}
	}

	@Override
	public void deleteAll() {
		this.userEntitys.clear();

	}

}
