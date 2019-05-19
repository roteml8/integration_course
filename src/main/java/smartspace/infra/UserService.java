package smartspace.infra;
import java.util.List;
import smartspace.data.UserEntity;

public interface UserService {

	public List <UserEntity> getUsingPagination(String adminSmartspace, String adminEmail, int size, int page);
	public List<UserEntity> importUsers(String adminSmartspace, String adminEmail, UserEntity[]users);
	public void update(String userSmartspace, String userEmail, UserEntity userEntity);
	public UserEntity newUser(UserEntity user);
	public UserEntity login(String string);

}

