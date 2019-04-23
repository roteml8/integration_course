package smartspace.infra;

import java.util.Date;  
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedActionDao;
import smartspace.data.ActionEntity;


@Service
public class ActionServicelmpl implements ActionService {
	private EnhancedActionDao actionDao;

	private boolean valiadate(ActionEntity entity) {
		return !entity.getActionType().trim().isEmpty() &&
				entity.getActionType() != null &&
				!entity.getElementId().trim().isEmpty() &&
				entity.getElementId()!= null &&
				entity.getElementSmartspace()!= null &&
				!entity.getElementSmartspace().trim().isEmpty() &&
				entity.getPlayerEmail() != null&&
				!entity.getPlayerEmail().trim().isEmpty()&&
				entity.getMoreAttributes() != null&&
					entity.getPlayerSmartspace()!=null&&
					!entity.getPlayerSmartspace().trim().isEmpty()	;
	}
	
	@Autowired
	public ActionServicelmpl(EnhancedActionDao actionDao) {
		super();
		this.actionDao=actionDao;
	}
	
	@Override
	public ActionEntity newAction(ActionEntity actionEntity, int code) {
		if (code % 2 != 0) {
			throw new RuntimeException("you are not allowed to create actions");
		}
		
		if (valiadate(actionEntity)) {
			actionEntity.setCreationTimestamp(new Date());
			return this.actionDao.create(actionEntity);
		}else {
			throw new RuntimeException("invalid action");
		}
	}

	@Override
	public List<ActionEntity> getUsingPagination(int size, int page) {
		return this.actionDao
				.readAll(size, page);
	}

}