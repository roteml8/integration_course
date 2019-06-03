package smartspace.plugin;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
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
			
		if(!useremail.equals(actionStatusChangeEntity.getPlayerEmail())&&
				!useremail2.equals(actionStatusChangeEntity.getPlayerEmail()))
			throw new PlayerNotRegisteredToTaskException();
		
		//need to get the status task
		//Optional<ElementEntity> op = elementDao.readById(actionStatusChangeEntity.getKey());
		
		//the new location
		Location location = new Location();
		double x = Double.parseDouble( (String) actionStatusChangeEntity.getMoreAttributes().get("location"));
		location.setX(x);
		//location.setY(op.get().getLocation().getY());	
	
		
		//set points 
		String userKey= actionStatusChangeEntity.getPlayerSmartspace() + "#" + actionStatusChangeEntity.getPlayerEmail();
		//(String) actionStatusChangeEntity.getMoreAttributes().get("userKey");
		UserEntity updateUser =  userDao.readById(userKey).get();
		
		int deadline = Integer.parseInt((String) elementDetails.get("deadline"));
		Date creationDate = elementDao.readById(taskKey).get().getCreationTimeDate();
		long deadlineDay = creationDate.getTime()+deadline*24*60*60*1000;
		Date today = Calendar.getInstance().getTime();
		long difference =  today.getTime() - deadlineDay;
	    float daysBetween = (difference / (1000*60*60*24));
	    
		if(daysBetween>=0) {
			if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				updateUser.setPoints(updateUser.getPoints()+increment*2);
			}else {
				updateUser.setPoints(updateUser.getPoints()+increment);
			}
		}else {
			updateUser.setPoints(updateUser.getPoints()-decrement);
		}
		
		userDao.updatePoints(updateUser);
		
		//updateElement is the new task with the current col
		ElementEntity updateElement = new ElementEntity();
		//updateElement = elementDao.readById(taskKey).get();
		updateElement.setLocation(location);
		updateElement.setKey(taskKey);
		this.elementDao.update(updateElement);
		return actionStatusChangeEntity;
	}

	
}