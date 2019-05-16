package smartspace.infra;
import java.util.ArrayList;
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
	private long defualtStartingPoints;
	
	@Autowired
	public UserServicelmpl (EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String smartspace) {
		this.mySmartspace = smartspace;
	}
	
	@Value("${defualt.starting.points:100}")
	public void setdefualtStartingPoints(String points) {
		this.defualtStartingPoints = Long.parseLong(points);
	}

	@Override
	public UserEntity newUser(UserEntity user) {
		// validate user status
		if (valiadateNewUser(user)) {
			user.setPoints(defualtStartingPoints);
			return this.userDao.create(user);
		}else {
			throw new FailedValidationException(" bad user form");
		}
	}
	
	@Override
	public List<UserEntity> importUsers(UserEntity[] users, String adminKey) {
		// validate Admin status
		
		if (userDao.isAdmin(adminKey) == false) {
			throw new NotAnAdminException(" user");
		}
		int count=0;
		for (UserEntity user: users)
		{
			if (user.getUserSmartspace().equals(mySmartspace)) 
				throw new ImportFromLocalException(" check your array at location " + count);
			if (!valiadate(user))
				throw new FailedValidationException(" user");
			count++;
		}
		List<UserEntity> created = new ArrayList<>();
		// validate user status
		for (UserEntity user: users)
		{
			user.setPoints(defualtStartingPoints);
			this.userDao.importUser(user);
		}
		
		return created; 
	
	}
	
	
	private boolean valiadateNewUser(UserEntity user) 
	{
		return  user.getUserEmail() != null &&
				!user.getUserEmail().trim().isEmpty() &&
				user.getUsername() != null &&
				!user.getUsername().trim().isEmpty() &&
				user.getAvatar() != null &&
				!user.getAvatar().trim().isEmpty() &&
				user.getRole() != null;
				
	}
	
	private boolean valiadate(UserEntity user) 
	{
		return user.getKey() != null &&
				!user.getKey().trim().isEmpty() &&
				user.getUserSmartspace() != null &&
				!user.getUserSmartspace().trim().isEmpty() &&
				user.getUserEmail() != null &&
				!user.getUserEmail().trim().isEmpty() &&
				user.getUsername() != null &&
				!user.getUsername().trim().isEmpty() &&
				user.getAvatar() != null &&
				!user.getAvatar().trim().isEmpty() &&
				user.getRole() != null ;
	}

	@Override
	public List<UserEntity> getUsingPagination(int size, int page) {
		return this.userDao.readAll("key",size,page);
	}

	@Override
	public void update(UserEntity userEntity, String key) {
		userEntity.setKey(key);
		//try {
			this.userDao.update(userEntity);
		//}
		//catch(RuntimeException e)
		//{
		//	throw new EntityNotInDBException(e.getMessage());
		//}
	}

	@Override
	public UserEntity login(String key) {
		return this.userDao.readById(key).orElseThrow(() -> new EntityNotInDBException("user login failed, there is no such user in the DB"));
	}

}


