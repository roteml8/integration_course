package smartspace.infra;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.util.FailedValidationException;
import smartspace.data.util.ImportFromLocalException;
import smartspace.data.util.NotAnAdminException;


@Service
public class UserServicelmpl implements UserService {
	private EnhancedUserDao <String> userDao;
	private String mySmartspace;
	
	@Autowired
	public UserServicelmpl (EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String smartspace) {
		this.mySmartspace = smartspace;
	}

	@Override
	public UserEntity newUser(UserEntity user) {
		// validate user status
		if (valiadateNewUser(user)) {
			return this.userDao.create(user);
		}else {
			throw new FailedValidationException("user");
		}
	}
	
	@Override
	public List<UserEntity> importUsers(UserEntity[] users, String adminKey) {
		// validate Admin status
		
		if (userDao.isAdmin(adminKey) == false) {
			throw new NotAnAdminException("users!");
		}
		int count=0;
		for (UserEntity user: users)
		{
			if (user.getUserSmartspace().equals(mySmartspace)) 
				throw new ImportFromLocalException(count);
			if (!valiadate(user))
				throw new FailedValidationException("user");
			count++;
		}
		List<UserEntity> created = new ArrayList<>();
		// validate user status
		for (UserEntity user: users)
		{
			this.userDao.importUser(user);
		}
		
		return created; 
	
	}
	
	
	private boolean valiadateNewUser(UserEntity user) 
	{
		return  user.getUserEmail() != null &&
				user.getUsername() != null &&
				user.getAvatar() != null &&
				user.getRole() != null;
				
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
	
	/*
	private boolean valiadate(UserEntity user) 
	{
	//	 if (user.getUserSmartspace() == null)
	//	 {
	//		System.err.println("invalid smartspace");
	//		return false;
	//	 }
		 
		 if (user.getUserEmail() == null)
		 {
			System.err.println("invalid Email");
			return false;
		 }
		 
	//	 if (user.getUsername() == null)
	//	 {
	//		System.err.println("invalid username");
	//		return false;
	//	 }
		 
	//	 if (user.getAvatar() == null)
	//	 {
	//		System.err.println("invalid avatar");
	//		return false;
	//	 }
		 

		 if (user.getRole() == null)
		 {
			System.err.println("invalid role");
			return false;
		 }
		 

		 if (user.getKey() == null)
		 {
			System.err.println("invalid key");
			return false;
		 }
		 
		 return true;
	}
	 
	 */
	
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

	@Override
	public UserEntity login(String key) {
		return this.userDao.readById(key).orElseThrow(() -> new RuntimeException("Login failed, there is no such user in the DB."));
	}

}


