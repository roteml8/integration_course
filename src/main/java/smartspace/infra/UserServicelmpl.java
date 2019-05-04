package smartspace.infra;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;


@Service
public class UserServicelmpl implements UserService {
	private EnhancedUserDao <String> userDao;
	private String mySmartspace;
	
	@Autowired
	public UserServicelmpl (EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.mySmartspace = smartspace;
	}
	
	@Override
	public UserEntity newUser(UserEntity user, String adminKey) {
		// validate Admin status
		if (userDao.isAdmin(adminKey) == true) {
			throw new RuntimeException("Only admins are allowed to import users!");
		}
		
		// validate user status
		if (valiadate(user)) {
			return this.userDao.importUser(user);
		}else {
			throw new RuntimeException("invalid user");
		}
	}
	
	
	
	private boolean valiadate(UserEntity user) 
	{
		return user.getUserSmartspace() != null &&
				user.getUserEmail() != null &&
				user.getUsername() != null &&
				user.getAvatar() != null &&
				user.getRole() != null &&
				user.getKey() != null;
	}
	
	@Override
	public boolean valiadateSmartspace(UserEntity user) 
	{
		return user.getUserSmartspace().equals(mySmartspace) == false;
	}

	
	@Override
	public List<UserEntity> getUsingPagination(int size, int page) {
		return this.userDao.readAll("key",size,page);
	}

	@Override
	public void update(UserEntity userEntity, String key) {
		userEntity.setKey(key);
		this.userDao.update(userEntity);
	}

	
}
