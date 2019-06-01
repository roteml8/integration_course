package smartspace.plugin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.ElementType;
import smartspace.data.UserEntity;


public class UpdateScoreBoardPlugin implements Plugin {

	private EnhancedElementDao<String> elementDao;
	private EnhancedUserDao<String> userDao;

	@Autowired
	public void setElementDao(EnhancedElementDao<String> elementDao) {
		this.elementDao = elementDao;
	}

	@Autowired
	public void setUserDao(EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}

	@Override
	public ActionEntity process(ActionEntity actionEntity) {

		String taskKey = actionEntity.getElementSmartspace() + "#" + actionEntity.getElementId();

		// Check if the element is a score board
		ElementEntity element = elementDao.readById(taskKey).get();
		if (!element.getType().equalsIgnoreCase(ElementType.SCORE_BOARD.toString()))
			throw new WrongElementTypeException("Not a score board element");

		ElementEntity scoreBoard = elementDao.readElementWithType(ElementType.SCORE_BOARD.toString() , 1, 0).get(0);
		
		List<UserEntity> bestUsers = userDao.readAll("points" ,10 ,0 ); // get the 10 users with the highest score.
		
		scoreBoard.getMoreAttributes().replace("users", bestUsers);
		
		return actionEntity;
	}

}
