package smartspace.dao;
import java.util.Date;
import java.util.List;
import java.util.Map;

import smartspace.data.ActionEntity;
public interface EnhancedActionDao extends ActionDao{
	
	
	public List<ActionEntity> readAll(int size, int page);
	
	public List<ActionEntity> readAll(String sortBy, int size, int page);

	public List<ActionEntity> readElementWithCreationTimestamp(Date stamp, int size, int page);
	
	List<ActionEntity> readActionWithPlayerEmailContaining(String text, int size, int page);
	
	List<ActionEntity> readActionWithPlayerSmartspaceContaining(String text, int size, int page);
	
	List<ActionEntity> readActionWithElementSmartspaceContaining(String text, int size, int page);
	
	List<ActionEntity> readActionWithElementIdContaining(String text, int size, int page);
	
	List<ActionEntity> readActionWithActionTypeContaining(String text, int size, int page);
	
	public List<ActionEntity> readElementWithMoreAttributes(Map<String, Object> moreAttributes, int size, int page);

}
