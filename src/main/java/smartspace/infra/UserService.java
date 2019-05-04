package smartspace.infra;
import java.util.List;
import smartspace.data.UserEntity;

public interface UserService {

	public UserEntity newUser(UserEntity user, String key);
	List <UserEntity> getUsingPagination(int size, int page);
	public void update(UserEntity userEntity, String key);
	
	/**
	 * Checks if user's smartspace is similar to local project smartspace and throws an exception if it is.
	 * 
	 * @param  user   the user we want to check.
	 * @return        false if the user's smartspace is equal to the local project smartspace , otherwise  true.
	 */
	public boolean valiadateSmartspace(UserEntity user);

}
