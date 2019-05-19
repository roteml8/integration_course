package smartspace.infra;
import java.util.List;

import smartspace.data.ActionEntity;

public interface ActionService {
	
	public List <ActionEntity> getUsingPagination (String adminSmartspace, String adminEmail, int size, int page);
	public List<ActionEntity> importActions (String adminSmartspace, String adminEmail, ActionEntity[] actions);
	public ActionEntity invoke(ActionEntity convertToEntity);

}
