package smartspace.dao.rdb;
import org.springframework.data.repository.CrudRepository;

import smartspace.data.ActionEntity;

public interface ActionCrud extends CrudRepository<ActionEntity, String>{

}
