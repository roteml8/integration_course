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
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Repository
public class RdbUserDao implements EnhancedUserDao<String> {

	private String smartspace;
	private UserCrud userCrud;

	@Autowired
	public RdbUserDao(UserCrud userCrud) {
		super();
		this.userCrud = userCrud;

	}

	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
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

		return this.userCrud.findById(userkey);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readAll() {

		List<UserEntity> rv = new ArrayList<>();

		// SQL: SELECT
		this.userCrud.findAll().forEach(rv::add);

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
	public void deleteAll() {
		// SQL: DELETE
		this.userCrud.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readAll(int size, int page) {
		return this.userCrud.findAll(PageRequest.of(page, size)).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readAll(String sortBy, int size, int page) {
		return this.userCrud.findAll(PageRequest.of(page, size, Direction.ASC, sortBy)).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readUserWithNameContaining(String text, int size, int page) {
		return this.userCrud.findAllByUsernameLike("%" + text + "%", PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readUserWithEmailContaining(String text, int size, int page) {
		return this.userCrud.findAllByUserEmailLike("%" + text + "%", PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readUserWithAvaterContaining(String text, int size, int page) {
		return this.userCrud.findAllByAvatarLike("%" + text + "%", PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readUserWithRole(UserRole role, int size, int page) {
		return this.userCrud.findAllByRoleLike(role, PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readUserWithPoints(long points, int size, int page) {
		return this.userCrud.findAllByPointsLike(points, PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readUserWithPointsMore(long points, int size, int page) {
		return this.userCrud.findAllByPointsGreaterThanEqual(points, PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readUserWithPointsLess(long points, int size, int page) {
		return this.userCrud.findAllByPointsLessThanEqual(points, PageRequest.of(page, size));
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
