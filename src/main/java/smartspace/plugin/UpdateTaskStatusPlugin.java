package smartspace.plugin;

import java.util.Date;
import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementType;
import smartspace.data.Location;
import smartspace.data.UserEntity;

@Component
public class UpdateTaskStatusPlugin implements Plugin{
	
	private EnhancedElementDao<String> elementDao;
	
	@Autowired
	public void setElementDao(EnhancedElementDao<String> elementDao) {
		this.elementDao=elementDao;
	}

	private EnhancedUserDao<String> userDao;
	private int decrement;
	private int increment;
	
	@Autowired
	public void setUserDao(EnhancedUserDao<String> userDao) {
		this.userDao=userDao;
	}
	@Value("${points.increment:increment}")
	public void setIncrement(int increment) {
		this.increment = increment;
	}
	
	@Value("${points.decrement:decrement}")
	public void setDecrement(int decrement) {
		this.decrement = decrement;
	}
	
	@Override
	public ActionEntity process(ActionEntity actionStatusChangeEntity) {
		String taskKey= actionStatusChangeEntity.getElementSmartspace() + "#" + actionStatusChangeEntity.getElementId();
		//check if the element is task
		String useremail=(String) actionStatusChangeEntity.getMoreAttributes().get("useremail");
		String useremail2=(String) actionStatusChangeEntity.getMoreAttributes().get("useremail2");
		if(!elementDao.readById(taskKey).get().getType().equals(ElementType.TASK))
			throw new RuntimeException("this element type is not Task!");
		System.err.println(actionStatusChangeEntity.getPlayerEmail());
		if(!useremail.equals(actionStatusChangeEntity.getPlayerEmail())&&
				!useremail2.equals(actionStatusChangeEntity.getPlayerEmail()))
			throw new RuntimeException("this player type is not belong to this element!");
			//need to get the status task
	//	Optional<ElementEntity> op = elementDao.readById(actionStatusChangeEntity.getKey());
		
		//the new location
		Location location = new Location();
		double x = Double.parseDouble( (String) actionStatusChangeEntity.getMoreAttributes().get("location"));
		location.setX(x);
	//	location.setY(op.get().getLocation().getY());	
	
		
		//set points 
		String key= actionStatusChangeEntity.getPlayerEmail() + "#" + actionStatusChangeEntity.getPlayerSmartspace();
		//(String) actionStatusChangeEntity.getMoreAttributes().get("userKey");
		UserEntity updateUser =  userDao.readById(key).get();
		int deadline = (int) actionStatusChangeEntity.getMoreAttributes().get("deadline");
		Date dateBefore = elementDao.readById(taskKey).get().getCreationTimeDate();
		long deadDay = dateBefore.getTime()+deadline*24*60*60*1000;
		
		long difference = deadDay - dateBefore.getTime();
	    float daysBetween = (difference / (1000*60*60*24));
		if(daysBetween>deadline) {
			updateUser.setPoints(updateUser.getPoints()+increment);
		}else {
			updateUser.setPoints(updateUser.getPoints()-decrement);
		}
		userDao.update(updateUser);
		//updateElement is the new task with the current col
		ElementEntity updateElement = new ElementEntity();
		//updateElement = elementDao.readById(taskKey).get();
		updateElement.setLocation(location);
		updateElement.setKey(taskKey);
	 
		this.elementDao.update(updateElement);
		return actionStatusChangeEntity;
	}

	
}