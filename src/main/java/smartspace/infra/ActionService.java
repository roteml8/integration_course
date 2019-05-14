package smartspace.infra;
import java.util.List;

import smartspace.data.ActionEntity;

public interface ActionService {
	
	public List <ActionEntity> getUsingPagination (String userSmartspace, String userEmail, int size, int page);
	public List<ActionEntity> importActions (ActionEntity[] actions, String adminSmartspace, String adminEmail);
	public ActionEntity invoke(ActionEntity convertToEntity);

}
