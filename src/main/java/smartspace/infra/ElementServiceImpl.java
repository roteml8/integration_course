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
	public ElementEntity newElement(ElementEntity entity, String adminSmartspace, String adminEmail) {
		
		if (entity.getElementSmartSpace().equals(mySmartspace)) {
			throw new RuntimeException("Can't import elements from local smartspace!");

		}
		if (!userDao.isAdmin(adminSmartspace+"#"+adminEmail)) {
			throw new RuntimeException("Only admins are allowed to import elements!");
		}

		if (valiadate(entity)) {
			entity.setCreationTimeDate(new Date());
			return this.dao.importElement(entity);
		} else {
			throw new RuntimeException("Invalid element");
		}
	}
	
	public void updateElement(ElementEntity element) {
		this.dao.update(element);
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
				!entity.getType().trim().isEmpty() ;
	}

	
	  @Override 
	  public List<ElementEntity> getUsingPagination(int size, int page) {
		  	return this.dao.readAll("key", size, page);
	  }
	 
}
