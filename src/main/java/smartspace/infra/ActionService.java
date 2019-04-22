package smartspace.infra;
import java.util.List;
import smartspace.data.ActionEntity;

public interface ActionService {
	
	public ActionEntity newAction (ActionEntity action, int code);
	List <ActionEntity> getUsingPagination (int size, int page);

}
