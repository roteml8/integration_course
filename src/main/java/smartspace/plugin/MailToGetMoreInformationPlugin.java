package smartspace.plugin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

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
		
		//check if the element is task
		String useremail=(String) actionStatusChangeEntity.getMoreAttributes().get("useremail");
		String useremail2=(String) actionStatusChangeEntity.getMoreAttributes().get("useremail2");
		if(!elementDao.readById(taskKey).get().getType().equals(ElementType.TASK))
			throw new RuntimeException("this element type is not Task!");
		
		//check if the player belong to this element
		System.err.println(actionStatusChangeEntity.getPlayerEmail());
		if(!useremail.equals(actionStatusChangeEntity.getPlayerEmail())&&
				!useremail2.equals(actionStatusChangeEntity.getPlayerEmail()))
			throw new RuntimeException("this player type is not belong to this element!");
		
		
		String userKey= actionStatusChangeEntity.getPlayerSmartspace() + "#" + actionStatusChangeEntity.getPlayerEmail();
		UserEntity from =  userDao.readById(userKey).get();

		String creatorEmail  = elementDao.readById(taskKey).get().getCreatorEmail();
		String messageText=(String) actionStatusChangeEntity.getMoreAttributes().get("massage");
		if(messageText.isEmpty()) {
			mailSender.sendMail(
					creatorEmail ,
					"This message send from: " + from.getUsername(),
					"Maybe he's shy");
		}else {
			mailSender.sendMail(
					creatorEmail ,
					"This message send from: " + from.getUsername(),
					messageText);
		}		
		
		//updateElement is the new task with the current col
		ElementEntity updateElement = new ElementEntity();
		//updateElement = elementDao.readById(taskKey).get();
		updateElement.setKey(taskKey);
	 
		this.elementDao.update(updateElement);
		return actionStatusChangeEntity;	}

}
