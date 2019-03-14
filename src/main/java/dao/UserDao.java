
import java.util.List;
import java.util.Optional;

public interface UserDao<UserKey> {
public UserEntity create (UserEntity userEntity);
public Optional<UserEntity> readById(UserKey userkey);
public List<UserEntity> readAll(List<UserEntity> listUserEntity);
public void update (UserEntity userEntity);
public void deleteAll();
}
