package smartspace.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import smartspace.data.ElementEntity;
import smartspace.data.Location;

public interface EnhancedElementDao<Key> extends ElementDao<Key> {
	
	public ElementEntity importElement(ElementEntity elementEntity);
	
	public List<ElementEntity> readAll(int size, int page);
	
	public List<ElementEntity> readAll(String sortBy, int size, int page);
	
	public List<ElementEntity> readElementWithNameContaining(String text, int size, int page);
	
	public List<ElementEntity> readElementWithLocation(Location location, int size, int page);

	public List<ElementEntity> readElementWithTypeContaining(String text, int size, int page);
	
	public List<ElementEntity> readElementWithExpired(boolean expired, int size, int page);
	
	public List<ElementEntity> readElementWithCreatorEmailContaining(String text, int size, int page);
	
	public List<ElementEntity> readElementWithCreatorSmartspaceContaining(String text, int size, int page);
	
	public List<ElementEntity> readElementWithCreationTimeStamp(Date stamp, int size, int page);
	
	public List<ElementEntity> readElementWithMoreAttributes(Map<String, Object> moreAttributes, int size, int page);
	

	



	
	


	
	
	


	
	
	

}
