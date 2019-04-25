package smartspace.dao.rdb;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public interface UserCrud extends
//CrudRepository<UserEntity,String>{
		PagingAndSortingRepository<UserEntity, String> {

	public List<UserEntity> findAllByUserEmailLike(@Param("pattern") String pattern, Pageable pageable);

	public List<UserEntity> findAllByUsernameLike(@Param("pattern") String pattern, Pageable pageable);

	public List<UserEntity> findAllByAvatarLike(@Param("pattern") String pattern, Pageable pageable);

	public List<UserEntity> findAllByRoleLike(@Param("role") UserRole role, Pageable pageable);

	public List<UserEntity> findAllByPointsLike(@Param("points") long points, Pageable pageable);

	public List<UserEntity> findAllByPointsGreaterThanEqual(@Param("points") long points, Pageable pageable);
	
	public List<UserEntity> findAllByPointsLessThanEqual(@Param("points") long points, Pageable pageable);


}
