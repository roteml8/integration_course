package smartspace.infra;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import smartspace.aop.ManagerCheck;
import smartspace.aop.MeasureElapsedTime;
import smartspace.aop.MyLogger;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;

@Service
public class ElementServiceImpl implements ElementService {
	private EnhancedUserDao<String> userDao;
	private EnhancedElementDao<String> dao;
	private String mySmartspace;
//	private ApplicationContext ctx;


	@Autowired
	public ElementServiceImpl(EnhancedElementDao<String> dao, ApplicationContext ctx) {
		super();
		this.dao = dao;
//		this.ctx = ctx;
	}
	
	@Autowired
	public void setUserDao(EnhancedUserDao<String> userDao) {
	this.userDao = userDao;
	}
	
	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String smartspace) {
		this.mySmartspace = smartspace;
	}

	@Override
	public List<ElementEntity> importElements(ElementEntity[] elements, String adminSmartspace, String adminEmail) {
		
		if (!userDao.isAdmin(adminSmartspace+"#"+adminEmail)) {
			throw new NotAnAdminException(" elements!");
		}
		int count=0;
		for (ElementEntity e: elements)
		{
			if (e.getElementSmartSpace().equals(mySmartspace)) 
				throw new ImportFromLocalException(" check your array at location " + count);
			if (!valiadate(e))
					throw new FailedValidationException(" element");
			count++;

		}
		List<ElementEntity> created = new ArrayList<>();
		for (ElementEntity e: elements)
		{
			this.dao.importElement(e);
		}
		
		return created; 
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
	
	private boolean validateNew(ElementEntity entity) {
		return entity.getCreatorEmail() != null && 
				!entity.getCreatorEmail().trim().isEmpty() &&
				entity.getCreatorSmartSpace() != null && 
				!entity.getCreatorSmartSpace().trim().isEmpty() &&
				entity.getName() != null &&
				!entity.getName().trim().isEmpty() &&
				entity.getType() != null && 
				!entity.getType().trim().isEmpty()
				; 
	}
	
	private List<ElementEntity> filterExpiredElements (List<ElementEntity> all){
		List<ElementEntity> filtered = new ArrayList<ElementEntity>();
		for (ElementEntity e: all)
			if (e.isExpired() == false)
				filtered.add(e);
		return filtered; 
		
	}
	
	  @Override 
	  public List<ElementEntity> getUsingPagination(String userSmartspace, String userEmail, int size, int page) {
		  	return this.dao.readAll("key", size, page);
	  }

	@Override
	@MyLogger
	@ManagerCheck
	@MeasureElapsedTime
	public void updateElement(ElementEntity element, String managerSmartspace, String managerEmail,
			String elementSmartspace, String elementId) {
		element.setElementSmartSpace(elementSmartspace);
		element.setElementid(elementId);
		this.dao.update(element);
	}

	@Override
	@MyLogger
	@ManagerCheck
	@MeasureElapsedTime
	public ElementEntity newElement(String managerSmartspace, String managerEmail, ElementEntity entity) {
		if (validateNew(entity))
		{
			entity.setCreationTimeDate(new Date());
			entity.setExpired(false);
			return this.dao.create(entity);
		}
		else
			throw new FailedValidationException();

	}

	@Override
	public ElementEntity getElement(String userSmartspace, String userEmail, String elementSmartspace,
			String elementId) {
		return this.dao.readById(elementSmartspace+"#"+elementId).orElseThrow(() -> 
		new RuntimeException("No such element in the DB!"));

	}

	@Override
	public List<ElementEntity> getByName(String userSmartspace, String userEmail, String value, int size, int page){ 
		return this.dao.readElementWithName(value, size, page);
	}

	@Override
	public List<ElementEntity> getByType(String userSmartspace, String userEmail, String value, int size, int page) {
		return this.dao.readElementWithType(value, size, page);
	}

	@Override
	public List<ElementEntity> getByLocation(String userSmartspace, String userEmail, double x, double y, int distance,
			int size, int page) {
		List<ElementEntity> result = new ArrayList<>();
		for (double lat = x-distance; lat <= x+distance; lat++)
		{
			for (double lng = y-distance; lng<=y+distance; lng++)
				result.addAll(this.dao.readElementWithLocation(new Location(lat,lng), size, page));
		}
		return result;

	}

	 
}
