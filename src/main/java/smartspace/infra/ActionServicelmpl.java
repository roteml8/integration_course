package smartspace.infra;

import java.util.Date;  
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;


@Service
public class ActionServicelmpl implements ActionService {
	private EnhancedActionDao actionDao;
	private EnhancedUserDao<String> userDao;
	private String mySmartspace;
	
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
	
	
	@Autowired
	public void setUserDao(EnhancedUserDao<String> userDao) {
	this.userDao = userDao;
	}
	
	@Override
	public ActionEntity newAction(ActionEntity actionEntity, int code) {
		if (code % 2 != 0) {
			throw new RuntimeException("you are not allowed to create actions");
		}
		String [] splitElementSmartspace =actionEntity.getElementSmartspace().split("#");
		if(userDao.readById(splitElementSmartspace[1]).isPresent())
			throw new RuntimeException("your element not in DB");
		
		if(userDao.isAdmin(actionEntity.getElementId()))
				throw new RuntimeException("your not an admin");	
		
		if (valiadate(actionEntity)) {
			actionEntity.setCreationTimestamp(new Date());
			return this.actionDao.importAction(actionEntity);
		}else {
			throw new RuntimeException("invalid action  ");
		}
	}

	@Override
	public List<ActionEntity> getUsingPagination(int size, int page) {
		return this.actionDao
				.readAll(size, page);
	}

	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.mySmartspace = smartspace;
	}
	
	@Override
	public ActionEntity newAction(ActionEntity action, String adminSmartspace, String adminEmail) {
		if (action.getActionSmartspace().equals(mySmartspace)) {
			throw new RuntimeException("Can't import elements from local smartspace!");

		}
		if (!userDao.isAdmin(adminSmartspace+"#"+adminEmail)) {
			throw new RuntimeException("Only admins are allowed to import action!");
		}

		if (valiadate(action)) {
			action.setCreationTimestamp(new Date());
			return this.actionDao.importAction(action);
		} else {
			throw new RuntimeException("Invalid element");
		}
	}

}