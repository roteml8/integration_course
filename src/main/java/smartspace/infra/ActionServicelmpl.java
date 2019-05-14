package smartspace.infra;

import java.util.ArrayList;
import java.util.Date;  

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.util.FailedValidationException;
import smartspace.data.util.ImportFromLocalException;
import smartspace.data.util.NotAnAdminException;


@Service
public class ActionServicelmpl implements ActionService {
	private EnhancedActionDao actionDao;
	private EnhancedUserDao<String> userDao;
	private EnhancedElementDao<String> elementDao;
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
	
	@Autowired
	public void setElementDao(EnhancedElementDao<String> elementDao) {
	this.elementDao = elementDao;
	}
	

	@Override
	public List<ActionEntity> getUsingPagination(String userSmartspace, String userEmail, int size, int page) {
		return this.actionDao
				.readAll("key", size, page);
	}

	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String smartspace) {
		this.mySmartspace = smartspace;
	}
	
	@Override
	public List<ActionEntity> importActions(ActionEntity[] actions, String adminSmartspace, String adminEmail) {

		if (!userDao.isAdmin(adminSmartspace+"#"+adminEmail)) {
			throw new NotAnAdminException("actions!");
		}
		int count=0;
		for (ActionEntity a: actions)
		{
			if (a.getActionSmartspace().equals(mySmartspace)) 
				throw new ImportFromLocalException(count);
			if (!valiadate(a))
				throw new FailedValidationException("action");
			if (!elementDao.readById(a.getElementSmartspace()+"#"+a.getElementId()).isPresent())
				throw new ElementNotInDBException();
			count++;
		}
		List<ActionEntity> created = new ArrayList<>();
		// validate element status
		for (ActionEntity a: actions)
		{
			this.actionDao.importAction(a);
		}
		
		return created; 

	}

	@Override
	public ActionEntity invoke(ActionEntity action) {
		if(action.getActionType().equals("ECHO"))
		{
			if (valiadate(action)) {
				action.setCreationTimestamp(new Date());
				return this.actionDao.create(action);
			} else {
				throw new RuntimeException("Invalid element");
			}
		}
		else
			throw new RuntimeException("Unsupported action type");
		
	}

}