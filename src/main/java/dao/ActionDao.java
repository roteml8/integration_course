import java.util.List;

public interface ActionDao {
public ActionEntity create(ActionEntity actionEntity);
public List<ActionEntity> readAll(List<ActionEntity>listActionEntity);
public void deleteAll();
}
