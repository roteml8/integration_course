package smartspace.plugin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.EnhancedElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;

@Component
public class UpdateTaskStatusPlugin implements Plugin{
	
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
			//need to get the status task
	//	Optional<ElementEntity> op = elementDao.readById(actionStatusChangeEntity.getKey());
		
		//the new location
		Location location = new Location();
		double x = Double.parseDouble( (String) actionStatusChangeEntity.getMoreAttributes().get("location"));
		location.setX(x);
	//	location.setY(op.get().getLocation().getY());	
	
		//updateElement is the new task with the current col
		ElementEntity updateElement = new ElementEntity();
		//updateElement = elementDao.readById(taskKey).get();
		updateElement.setLocation(location);
		updateElement.setKey(taskKey);
	 
		this.elementDao.update(updateElement);
		return actionStatusChangeEntity;
	}

	
}