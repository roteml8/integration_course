package smartspace.infra;
import java.util.List;
import smartspace.data.UserEntity;

public interface UserService {

	public List <UserEntity> getUsingPagination(int size, int page);
	public List<UserEntity> importUsers(UserEntity[] users, String key);
	
	/**
	 * Checks if user's smartspace is similar to local project smartspace and throws an exception if it is.
	 * 
	 * @param  user   the user we want to check.
	 * @return        false if the user's smartspace is equal to the local project smartspace , otherwise  true.
	 */
	public boolean valiadateSmartspace(UserEntity user);

	public void update(UserEntity userEntity, String key);
	public UserEntity newUser(UserEntity user);
	public UserEntity login(String string);

}

