package smartspace.infra;
import java.util.List;
import smartspace.data.UserEntity;

public interface UserService {

	public List <UserEntity> getUsingPagination(int size, int page);
	public List<UserEntity> importUsers(UserEntity[] users, String key);
	public void update(UserEntity userEntity, String key);
	public UserEntity newUser(UserEntity user);
	public UserEntity login(String string);

}

