package smartspace.dao.rdb1;
import org.springframework.data.repository.CrudRepository;

import smartspace.data.ElementEntity;

public interface ElementCrud extends CrudRepository<ElementEntity,String>{

}
