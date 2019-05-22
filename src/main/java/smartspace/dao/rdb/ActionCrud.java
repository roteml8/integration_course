package smartspace.dao.rdb;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import smartspace.data.ActionEntity;


public interface ActionCrud extends PagingAndSortingRepository<ActionEntity, String>{

	public List<ActionEntity> findAllByActionTypeLike(Pageable pageable);

	public List<ActionEntity> findAllByElementSmartspaceLike(
			@Param("pattern") String pattern, 
			Pageable pageable);

	public List<ActionEntity> findAllByElementIdLike(
			@Param("pattern") String pattern, 
			Pageable pageable);

	
	public List<ActionEntity> findAllByPlayerSmartspaceLike(
			@Param("pattern") String pattern, 
			Pageable pageable);

	
	public List<ActionEntity> findAllByPlayerEmailLike(
			@Param("pattern") String pattern, 
			Pageable pageable);


	public List<ActionEntity> findAllByCreationTimestampLike(
			@Param("creationTimestamp") Date stamp, Pageable pageable);
	
	public List<ActionEntity> findAllByMoreAttributesLike
	(@Param("moreAttributes") Map<String,Object> moreAttributes, Pageable pageable);



}
