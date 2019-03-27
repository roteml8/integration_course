package smartspace.dao.rdb1;
import org.springframework.data.repository.CrudRepository;

import smartspace.data.ActionEntity;

public interface ActionCrud extends CrudRepository<ActionEntity, String>{

}
