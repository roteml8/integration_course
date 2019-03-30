package smartspace.dao.rdb;
import org.springframework.data.repository.CrudRepository;

import smartspace.data.ElementEntity;

public interface ElementCrud extends CrudRepository<ElementEntity,String>{

}
