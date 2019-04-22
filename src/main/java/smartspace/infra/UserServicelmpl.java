package smartspace.infra;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;


@Service
public class UserServicelmpl implements UserService {
	private EnhancedUserDao <String> userDao;
	
	@Autowired
	public UserServicelmpl (EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	
	public UserEntity newUser(UserEntity user, int code) {
		// validate code
		if (code % 2 != 0) {
			throw new RuntimeException("you are not allowed to create users");
		}
		
		if (valiadate(user)) {
			return this.userDao.create(user);
		}else {
			throw new RuntimeException("invalid user");
		}
	}
	
	private boolean valiadate(UserEntity user) {
		return user.getUserSmartspace() != null &&
				user.getUserEmail() != null &&
				user.getUsername() != null &&
				user.getAvatar() != null &&
				user.getRole() != null &&
				user.getKey() != null;}

	

	public List<UserEntity> getUsingPagination(int size, int page) {
		return this.userDao.readAll("key",size,page);
	}

	
}
