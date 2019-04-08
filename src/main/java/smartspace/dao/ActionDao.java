package smartspace.dao;

import java.util.List;
import java.util.Optional;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;

public interface ActionDao {
public ActionEntity create(ActionEntity actionEntity);
public List<ActionEntity> readAll();
public void deleteAll();
void deleteByKey(String actionEntity);
void delete(ActionEntity actionEntity);
Optional<ActionEntity> readById(String actionEntity);
void update(ActionEntity actionEntity);
}
