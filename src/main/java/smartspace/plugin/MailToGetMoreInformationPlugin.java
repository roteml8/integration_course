package smartspace.plugin;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementType;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.infra.SendEmailTLS;
import smartspace.infra.UnsupportedActionTypeException;

@Component
public class MailToGetMoreInformationPlugin implements Plugin{

	private EnhancedElementDao<String> elementDao;
	private EnhancedUserDao<String> userDao;
	private SendEmailTLS mailSender;

	@Autowired
	public void setMailSender(SendEmailTLS mailSender) {
		this.mailSender = mailSender;
	}

	@Autowired
	public void setElementDao(EnhancedElementDao<String> elementDao) {
		this.elementDao=elementDao;
	}
	
	@Autowired
	public void setUserDao(EnhancedUserDao<String> userDao) {
		this.userDao=userDao;
	}
	

	
	@Override
	public ActionEntity process(ActionEntity actionStatusChangeEntity) {
		
		String taskKey= actionStatusChangeEntity.getElementSmartspace() + "#" + actionStatusChangeEntity.getElementId();
		
		//Check if the element is task
		ElementEntity element = elementDao.readById(taskKey).get();
		if(!element.getType().equalsIgnoreCase(ElementType.TASK.toString()))
			throw new WrongElementTypeException("Task");
		
		Map<String,Object> elementDetails = element.getMoreAttributes();
		String useremail="", useremail2="";
		// Players of the task
		if (elementDetails.containsKey("useremail"))
			 useremail =  (String) elementDetails.get("useremail");
		if (elementDetails.containsKey("useremail2"))
			 useremail2 =  (String) elementDetails.get("useremail2");
			
		//Check if the task belongs to the player
		System.err.println(actionStatusChangeEntity.getPlayerEmail());			
		if(!useremail.equals(actionStatusChangeEntity.getPlayerEmail())&&
				!useremail2.equals(actionStatusChangeEntity.getPlayerEmail()))
			throw new PlayerNotRegisteredToTaskException();
		
		
		String userKey= actionStatusChangeEntity.getPlayerSmartspace() + "#" + actionStatusChangeEntity.getPlayerEmail(); // Player
		UserEntity from =  userDao.readById(userKey).get();

		String creatorEmail  = elementDao.readById(taskKey).get().getCreatorEmail(); // Manager
		String messageText=(String) actionStatusChangeEntity.getMoreAttributes().get("message");
		if(messageText.isEmpty()) {
			mailSender.sendMail(
					creatorEmail ,
					"This message sent from: " + from.getUsername(),
					"Maybe he's shy");
		}else {
			mailSender.sendMail(
					creatorEmail ,
					"This message sent from: " + from.getUsername(),
					messageText);
		}		
		
//		//updateElement is the new task with the current col
//		ElementEntity updateElement = new ElementEntity();
//		//updateElement = elementDao.readById(taskKey).get();
//		updateElement.setKey(taskKey);
//	 
//		this.elementDao.update(updateElement);
		return actionStatusChangeEntity;	}

}
