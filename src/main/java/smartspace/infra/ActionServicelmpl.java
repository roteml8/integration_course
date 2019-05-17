package smartspace.infra;

import java.util.ArrayList;

import java.util.Date;  

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.plugin.Plugin;


@Service
public class ActionServicelmpl implements ActionService {
	private EnhancedActionDao actionDao;
	private EnhancedUserDao<String> userDao;
	private EnhancedElementDao<String> elementDao;
	private ApplicationContext ctx;

	private String mySmartspace;
	
	@Autowired
	public ActionServicelmpl(EnhancedActionDao actionDao,
			ApplicationContext ctx) {
		this.actionDao=actionDao;
		this.ctx = ctx;
	}
	
	private boolean valiadateImport(ActionEntity entity) {
		return  entity.getKey()!= null &&
				!entity.getKey().trim().isEmpty() &&
				entity.getActionSmartspace() != null &&
				!entity.getActionSmartspace().trim().isEmpty() &&
				entity.getActionId()!= null &&
				!entity.getActionId().trim().isEmpty() &&
				entity.getActionType() != null &&
				!entity.getActionType().trim().isEmpty() &&
				entity.getElementId()!= null &&
				!entity.getElementId().trim().isEmpty() &&
				entity.getElementSmartspace()!= null &&
				!entity.getElementSmartspace().trim().isEmpty() &&
				entity.getPlayerEmail() != null&&
				!entity.getPlayerEmail().trim().isEmpty()&&
				entity.getMoreAttributes() != null&&
					entity.getPlayerSmartspace()!=null&&
					!entity.getPlayerSmartspace().trim().isEmpty();
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
			throw new NotAnAdminException(" actions!");
		}
		int count=0;
		for (ActionEntity a: actions)
		{
			if (a.getActionSmartspace().equals(mySmartspace)) 
				throw new ImportFromLocalException(" check your array at location " + count);
			if (!valiadateImport(a))
				throw new FailedValidationException(" action");
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
 
		if(valiadateInvoke(action) == false)
		{
			throw new FailedValidationException(action.getActionType());
		}
		
		try {
			String type = action.getActionType();
			String className =
					"smartspace.plugin." 
					+ type.toUpperCase().charAt(0) 
					+ type.substring(1, type.length())
					+ "Plugin";
			Class<?> theClass = Class.forName(className);
			Plugin plugin = (Plugin) this.ctx.getBean(theClass);
			
			action.setCreationTimestamp(new Date());
			action = plugin.process(action);
			
		//	this.actionDao.create(action);
		//	return action;
			return this.actionDao.create(action);
		} catch (Exception e) {
			//throw new UnsupportedActionTypeException(e);
			throw new UnsupportedActionTypeException(action.getActionType());
		}
	}
	
	private boolean valiadateInvoke(ActionEntity entity) {
		return entity.getActionType() != null &&
				!entity.getActionType().trim().isEmpty() &&
				entity.getElementId()!= null &&
				!entity.getElementId().trim().isEmpty() &&
				entity.getElementSmartspace()!= null &&
				!entity.getElementSmartspace().trim().isEmpty() &&
				entity.getPlayerEmail() != null&&
				!entity.getPlayerEmail().trim().isEmpty()&&
				entity.getPlayerSmartspace()!=null&&
				!entity.getPlayerSmartspace().trim().isEmpty() &&
				entity.getMoreAttributes()!=null;
	}

}