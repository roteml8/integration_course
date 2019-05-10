package smartspace.infra;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;

@Service
public class ElementServiceImpl implements ElementService {
	private EnhancedUserDao<String> userDao;
	private EnhancedElementDao<String> dao;
	private String mySmartspace;

	@Autowired
	public ElementServiceImpl(EnhancedElementDao<String> dao) {
		super();
		this.dao = dao;
	}
	
	@Autowired
	public void setUserDao(EnhancedUserDao<String> userDao) {
	this.userDao = userDao;
	}
	
	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.mySmartspace = smartspace;
	}

	@Override
	public ElementEntity importElement(ElementEntity entity, String adminSmartspace, String adminEmail) {
		
		if (!userDao.isAdmin(adminSmartspace+"#"+adminEmail)) {
			throw new RuntimeException("Only admins are allowed to import elements!");
		}

		if (valiadate(entity)) {
			entity.setCreationTimeDate(new Date());
			return this.dao.importElement(entity);
		} else {
			throw new RuntimeException("Invalid element!");
		}
	}
	

	private boolean valiadate(ElementEntity entity) {
		return entity.getCreatorEmail() != null && 
				!entity.getCreatorEmail().trim().isEmpty() &&
				entity.getCreatorSmartSpace() != null && 
				!entity.getCreatorSmartSpace().trim().isEmpty() &&
				entity.getLocation() != null && 
				entity.getMoreAttributes() != null && 
				entity.getName() != null &&
				!entity.getName().trim().isEmpty() &&
				entity.getType() != null && 
				!entity.getType().trim().isEmpty()&&
				entity.getElementSmartSpace()!=null &&
				!entity.getElementSmartSpace().trim().isEmpty() &&
				entity.getElementid()!= null &&
				!entity.getElementid().trim().isEmpty()
				;
	}

	
	  @Override 
	  public List<ElementEntity> getUsingPagination(String userSmartspace, String userEmail, int size, int page) {
		  	return this.dao.readAll("key", size, page);
	  }

	@Override
	public void updateElement(ElementEntity element, String managerSmartspace, String managerEmail,
			String elementSmartspace, String elementId) {
		element.setElementSmartSpace(elementSmartspace);
		element.setElementid(elementId);
		this.dao.update(element);
	}

	@Override
	public ElementEntity newElement(ElementEntity entity, String managerSmartspace, String managerEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementEntity getElement(String userSmartspace, String userEmail, String elementSmartspace,
			String elementId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getByName(String userSmartspace, String userEmail, String value, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getByType(String userSmartspace, String userEmail, String value, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementEntity> getByLocation(String userSmartspace, String userEmail, int x, int y, int distance,
			int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}
	 
}
