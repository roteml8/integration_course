package smartspace.plugin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import smartspace.dao.EnhancedElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;

public class PreviousColPlugin implements Plugin{

	private EnhancedElementDao<String> elementDao;
	
	@Autowired
	public void setElementDao(EnhancedElementDao<String> elementDao) {
		this.elementDao=elementDao;
	}
	
	@Override
	public ActionEntity process(ActionEntity actionStatusChangeEntity) {
		String taskKey= actionStatusChangeEntity.getElementSmartspace() + "#" + actionStatusChangeEntity.getElementId();
		
		//check if the element is task
		if(!elementDao.readById(taskKey).get().getType().equals("Task"))
			throw new RuntimeException("this element type is not Task!");
		
		//need to get the status task
		Optional<ElementEntity> op = elementDao.readById(actionStatusChangeEntity.getKey());
		double newLoction = op.get().getLocation().getX();
		
		//the new location 
		Location location = new Location();
		location.setX(newLoction-1);
		location.setY(op.get().getLocation().getY());	
		
		//updateElement is the new task with the current col
		ElementEntity updateElement = new ElementEntity();
		updateElement = elementDao.readById(taskKey).get();
		updateElement.setLocation(location);
		updateElement.setKey(taskKey);
		this.elementDao.update(updateElement);
		return actionStatusChangeEntity;
		}

}
