package smartspace.infra;
import java.util.List;
import smartspace.data.UserEntity;

public interface UserService {

	public UserEntity newUser(UserEntity user, String string);
	List <UserEntity> getUsingPagination(int size, int page);
	public void update(UserEntity userEntity);

}
