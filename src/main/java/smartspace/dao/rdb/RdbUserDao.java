package smartspace.dao.rdb;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Repository
public class RdbUserDao implements EnhancedUserDao<String> {

	private String smartspace;
	private UserCrud userCrud;
	private long defualtStartingPoints;

	@Autowired
	public RdbUserDao(UserCrud userCrud) {
		super();
		this.userCrud = userCrud;
		

	}

	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
	
	@Value("${defualt.starting.points:100}")
	public void setdefualtStartingPoints(String points) {
		this.defualtStartingPoints = Long.parseLong(points);
	}

	public String getSmartspace() {
		return this.smartspace;
	}

	@Override
	@Transactional
	public UserEntity create(UserEntity userEntity) {

		// SQL: INSERT INTO MESSAGES (ID, NAME) VALUES (?,?);
		userEntity.setKey(smartspace + "#" + userEntity.getUserEmail());

		if (!this.userCrud.existsById(userEntity.getKey())) {
			UserEntity rv = this.userCrud.save(userEntity);
			return rv;
		} else {
			throw new RuntimeException("userEntity already exists with key: " + userEntity.getKey());
		}

	}

	@Override
	@Transactional
	public UserEntity importUser(UserEntity userEntity) {
		UserEntity rv = this.userCrud.save(userEntity);
		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserEntity> readById(String userkey) {
		Optional<UserEntity> u = this.userCrud.findById(userkey);
		if(u.isPresent()) {
			u.get().setKey(userkey);
		}
		return u;
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readAll() {

		List<UserEntity> rv = new ArrayList<>();

		// SQL: SELECT
		this.userCrud.findAll().forEach(rv::add);
		for(int i = 0; i < rv.size(); i++) {
			rv.get(i).setKey(rv.get(i).getKey());
		}

		return rv;

	}

	@Override
	@Transactional
	public void update(UserEntity userEntity) {
		UserEntity existing = this.readById(userEntity.getKey())
				.orElseThrow(() -> new RuntimeException("no userEntity to update"));
		
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
		
		// SQL: UPDATE
		this.userCrud.save(existing);
	}
	
	@Override
	@Transactional
	public void updatePoints(UserEntity userEntity) {
		UserEntity existing = this.readById(userEntity.getKey())
				.orElseThrow(() -> new RuntimeException("no userEntity to update"));
		
		if (userEntity.getPoints() != defualtStartingPoints) {
			existing.setPoints(userEntity.getPoints());
		}
		
		// SQL: UPDATE
		this.userCrud.save(existing);
	}

	@Override
	public void deleteAll() {
		// SQL: DELETE
		this.userCrud.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readAll(int size, int page) {
		List<UserEntity> list = this.userCrud.findAll(PageRequest.of(page, size)).getContent();
		for(int i = 0; i < list.size(); i++) {
			list.get(i).setKey(list.get(i).getKey());
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readAll(String sortBy, int size, int page) {
		List<UserEntity> list = this.userCrud.findAll(PageRequest.of(page, size, Direction.ASC, sortBy)).getContent();
		for(int i = 0; i < list.size(); i++) {
			list.get(i).setKey(list.get(i).getKey());
		}
		return list;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readAll(String sortBy, Direction direction , int size, int page) {
		List<UserEntity> list = this.userCrud.findAll(PageRequest.of(page, size, direction, sortBy)).getContent();
		
		for(int i = 0; i < list.size(); i++) {
			list.get(i).setKey(list.get(i).getKey());
		}
	return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readUserWithNameContaining(String text, int size, int page) {
		List<UserEntity> allUsers = this.userCrud.findAll(PageRequest.of(page, size)).getContent();
		
		List<UserEntity> filteredUsers = new ArrayList<>();
		
		for(UserEntity user : allUsers) {
			if(user.getUsername() != null) {
				if(user.getUsername().contains(text)) {
					user.setKey(user.getKey());
					filteredUsers.add(user);
				}	
			}
		}
		return filteredUsers;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readUserWithEmailContaining(String text, int size, int page) {
		List<UserEntity> allUsers = this.userCrud.findAll(PageRequest.of(page, size)).getContent();
		
		List<UserEntity> filteredUsers = new ArrayList<>();
		
		for(UserEntity user : allUsers) {
			if(user.getUserEmail() != null) {
				if(user.getUserEmail() .contains(text)) {
					user.setKey(user.getKey());
					filteredUsers.add(user);
				}	
			}
		}
		return filteredUsers;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readUserWithAvaterContaining(String text, int size, int page) {
		List<UserEntity> allUsers = this.userCrud.findAll(PageRequest.of(page, size)).getContent();
		
		List<UserEntity> filteredUsers = new ArrayList<>();
		
		for(UserEntity user : allUsers) {
			if(user.getAvatar() != null) {
				if(user.getAvatar().contains(text)) {
					user.setKey(user.getKey());
					filteredUsers.add(user);
				}	
			}
		}
		return filteredUsers;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readUserWithRole(UserRole role, int size, int page) {
		List<UserEntity> allUsers = this.userCrud.findAll(PageRequest.of(page, size)).getContent();
		
		List<UserEntity> filteredUsers = new ArrayList<>();
		
		for(UserEntity user : allUsers) {
			if(user.getRole() != null) {
				if(user.getRole() == role) {
					user.setKey(user.getKey());
					filteredUsers.add(user);
				}	
			}
		}
		return filteredUsers;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readUserWithPoints(long points, int size, int page) {
		List<UserEntity> allUsers = this.userCrud.findAll(PageRequest.of(page, size)).getContent();
		
		List<UserEntity> filteredUsers = new ArrayList<>();
		
		for(UserEntity user : allUsers) {
			if(user.getPoints() == points) {
				user.setKey(user.getKey());
				filteredUsers.add(user);	
			}
		}
		return filteredUsers;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readUserWithPointsMore(long points, int size, int page) {
		List<UserEntity> allUsers = this.userCrud.findAll(PageRequest.of(page, size)).getContent();
		
		List<UserEntity> filteredUsers = new ArrayList<>();
		
		for(UserEntity user : allUsers) {
			if(user.getPoints() > points) {
				user.setKey(user.getKey());
				filteredUsers.add(user);	
			}
		}
		return filteredUsers;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readUserWithPointsLess(long points, int size, int page) {
		List<UserEntity> allUsers = this.userCrud.findAll(PageRequest.of(page, size)).getContent();
		
		List<UserEntity> filteredUsers = new ArrayList<>();
		
		for(UserEntity user : allUsers) {
			if(user.getPoints() < points) {
				user.setKey(user.getKey());
				filteredUsers.add(user);	
			}
		}
		return filteredUsers;
	}

	@Override
	public boolean isAdmin(String key) {
		UserEntity existing = this.readById(key).orElse(null);

		// if the user is in the UserDao and it's UserRole is admin return true.
		if (existing != null && existing.getRole() == UserRole.ADMIN)
			return true;
		else
			return false;
	}
	
	@Override
	public boolean isManager(String key) {
		UserEntity existing = this.readById(key).orElse(null);

		// if the user is in the UserDao and it's UserRole is admin return true.
		if (existing != null && existing.getRole() == UserRole.MANAGER)
			return true;
		else
			return false;
	}
	
	@Override
	public boolean isPlayer(String key) {
		UserEntity existing = this.readById(key).orElse(null);

		// if the user is in the UserDao and it's UserRole is admin return true.
		if (existing != null && existing.getRole() == UserRole.PLAYER)
			return true;
		else
			return false;
	}

}
