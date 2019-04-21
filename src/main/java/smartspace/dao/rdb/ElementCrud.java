package smartspace.dao.rdb;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import smartspace.data.ElementEntity;
import smartspace.data.Location;

public interface ElementCrud extends PagingAndSortingRepository<ElementEntity, String>{
	
	public List<ElementEntity> findAllByNameLike(@Param("pattern") String pattern, Pageable pageable);
	
	public List<ElementEntity> findAllByLocationLike(@Param("location") Location location, Pageable pageable);

	public List<ElementEntity> findAllByTypeLike(@Param("pattern") String pattern, Pageable pageable);

	public List<ElementEntity> findAllByExpiredLike(@Param("expired") boolean expired, Pageable pageable);
	
	public List<ElementEntity> findAllByCreatorEmailLike(@Param("pattern") String pattern, Pageable pageable);
	
	public List<ElementEntity> findAllByCreatorSmartspaceLike(@Param("pattern") String pattern, Pageable pageable);
	
	public List<ElementEntity> findAllByCreationTimeStampLike(@Param("creationTimeStamp") Date stamp, Pageable pageable);
	
	public List<ElementEntity> findAllByMoreAttributesLike(@Param("moreAttributes") Map<String,Object> moreAttributes, Pageable pageable);

	
	



	
	

	
	

	
	

}
