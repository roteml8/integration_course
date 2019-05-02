package smartspace.dao;

import java.util.List;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public interface EnhancedUserDao<Key> extends UserDao<Key> {
	public List<UserEntity> readAll(int size, int page);

	public List<UserEntity> readAll(String sortBy, int size, int page);
	
	public List<UserEntity> readUserWithNameContaining(String text, int size, int page);
	
	public List<UserEntity> readUserWithEmailContaining(String text, int size, int page);

	public List<UserEntity> readUserWithAvaterContaining(String text, int size, int page);

	public List<UserEntity> readUserWithRole(UserRole role, int size, int page);

	public List<UserEntity> readUserWithPoints(long points, int size, int page);
	
	public List<UserEntity> readUserWithPointsMore(long points, int size, int page);
	
	public List<UserEntity> readUserWithPointsLess(long points, int size, int page);
	
	
	/**
	 * Checks if the key belong's to an existing user with ADMIN credentials.
	 * 
	 * @param  key    the key of the user we want to check.
	 * @return        true if the key belongs to a user with UserRole set to ADMIN , otherwise  false.
	 */
	public boolean isAdmin(Key key);
	
	public UserEntity importUser (UserEntity userEntity);
	
}
