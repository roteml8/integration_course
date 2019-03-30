package smartspace.dao.rdb;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.UserDao;
import smartspace.data.UserEntity;





@Repository
public class RdbUserDao implements UserDao<String> {
private UserCrud userCrud;
//TODO remove this
	private AtomicLong nextId;
	
	@Autowired	
	public  RdbUserDao(UserCrud userCrud) {
		super();
		this.userCrud = userCrud;
		
		// TODO remove this
		this.nextId = new AtomicLong(100);
	}
	
	
	
	
	@Override
	@Transactional
	public UserEntity create(UserEntity userEntity) throws Exception {
		// SQL: INSERT INTO MESSAGES (ID, NAME) VALUES (?,?);
		
				// TODO replace this with id stored in db
		userEntity.setKey(nextId.getAndIncrement()+"");
				
				
				if (!this.userCrud.existsById(userEntity.getKey())) {
					UserEntity rv = this.userCrud.save(userEntity);
					return rv;
				}else {
					throw new RuntimeException("message already exists with key: " + userEntity.getKey());
				}
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<UserEntity> readById(String userkey) {


		return this.userCrud.findById(userkey);
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll() {

List<UserEntity> rv = new ArrayList<>();
		
		// SQL: SELECT
		this.userCrud.findAll()
			.forEach(rv::add);
		
		return rv;
		
	}

	@Override
	@Transactional
	public void update(UserEntity userEntity) {
		UserEntity existing = 
				this.readById(userEntity.getKey())
					.orElseThrow(() -> new RuntimeException("not message to update"));
	
		if (userEntity.getAvatar() != null) {
			existing.setAvatar(userEntity.getAvatar());
		}
		if (userEntity.getRole()!=null) {
			existing.setRole(userEntity.getRole());
		}
		if (userEntity.getUserEmail() != null) {
			existing.setUserEmail(userEntity.getUserEmail());
		}
		if (userEntity.getUsername()!= null) {
			existing.setUsername(userEntity.getUsername());
		}
		if (userEntity.getUserSmartspace() != null) {
			existing.setUserSmartspace(userEntity.getUserSmartspace());
		}
		//if (userEntity.getPoints()) {
			existing.setPoints(userEntity.getPoints()); 
			//TODO not good , if we don't want to update points it still overwrite the points attribute
		//}
		
		// SQL: UPDATE
		this.userCrud.save(existing);
	}

	@Override
	public void deleteAll() {
		// SQL: DELETE
		this.userCrud.deleteAll();
	}

}
