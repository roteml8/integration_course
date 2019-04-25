package smartspace.infra;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;

@Service
public class ElementServiceImpl implements ElementService {
	private EnhancedElementDao<String> dao;
	private String mySmartspace;
	private int CreationCode = 42;

	@Autowired
	public ElementServiceImpl(EnhancedElementDao<String> dao) {
		super();
		this.dao = dao;
	}
	
	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.mySmartspace = smartspace;
	}

	@Override
	public ElementEntity newElement(ElementEntity entity, int code) {
		// validate code
		if (code != CreationCode) {
			throw new RuntimeException("you are not allowed to create messages if you don't know the ultimate answer");
		}

		if (valiadate(entity)) {
			entity.setCreationTimeDate(new Date());
			return this.dao.create(entity);
		} else {
			throw new RuntimeException("invalid element");
		}
	}

	private boolean valiadate(ElementEntity entity) {
		return entity.getCreatorEmail() != null && 
				entity.getCreatorSmartSpace() != null && 
				entity.getCreatorSmartSpace().equals(mySmartspace) == false &&
				entity.getLocation() != null && 
				entity.getMoreAttributes() != null && 
				entity.getName() != null &&
				entity.getName().trim().isEmpty() == false &&
				entity.getType() != null && 
				entity.getType().trim().isEmpty() == false;
	}

	
	  @Override 
	  public List<ElementEntity> getUsingPagination(int size, int page) {
		  	return this.dao.readAll("key", size, page);
	  }
	 
}
