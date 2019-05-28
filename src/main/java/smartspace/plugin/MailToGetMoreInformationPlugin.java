package smartspace.plugin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;

public class MailToGetMoreInformationPlugin implements Plugin{

	private EnhancedElementDao<String> elementDao;
	
	@Autowired
	public void setElementDao(EnhancedElementDao<String> elementDao) {
		this.elementDao=elementDao;
	}
	

	
	@Override
	public ActionEntity process(ActionEntity actionStatusChangeEntity) {
		String taskKey= actionStatusChangeEntity.getElementSmartspace() + "#" + actionStatusChangeEntity.getElementId();
		
		//check if the element is task
		String useremail=(String) actionStatusChangeEntity.getMoreAttributes().get("useremail");
		String useremail2=(String) actionStatusChangeEntity.getMoreAttributes().get("useremail2");
		if(!elementDao.readById(taskKey).get().getType().equals("Task"))
			throw new RuntimeException("this element type is not Task!");
		
		System.err.println(actionStatusChangeEntity.getPlayerEmail());
		if(!useremail.equals(actionStatusChangeEntity.getPlayerEmail())&&
				!useremail2.equals(actionStatusChangeEntity.getPlayerEmail()))
			throw new RuntimeException("this player type is not belong to this element!");

		String massage=(String) actionStatusChangeEntity.getMoreAttributes().get("massage");
		String user=(String) actionStatusChangeEntity.getMoreAttributes().get("userKey");
		
		
		
		//updateElement is the new task with the current col
		ElementEntity updateElement = new ElementEntity();
		//updateElement = elementDao.readById(taskKey).get();
		updateElement.setKey(taskKey);
	 
		this.elementDao.update(updateElement);
		return actionStatusChangeEntity;	}

}
