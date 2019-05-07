package smartspace.infra;
import java.util.List;
import smartspace.data.ActionEntity;

public interface ActionService {
	
	List <ActionEntity> getUsingPagination (int size, int page);
	public ActionEntity newAction(ActionEntity action, String string, String string2);
	ActionEntity newAction(ActionEntity actionEntity);

}
