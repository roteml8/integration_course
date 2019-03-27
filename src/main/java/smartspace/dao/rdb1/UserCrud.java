package smartspace.dao.rdb1;
import org.springframework.data.repository.CrudRepository;

import smartspace.data.UserEntity;

public interface UserCrud extends CrudRepository<UserEntity,String>{

}
