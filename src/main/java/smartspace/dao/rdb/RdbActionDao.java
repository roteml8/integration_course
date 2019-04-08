package smartspace.dao.rdb;
import java.util.ArrayList; 
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;



@Repository
public class RdbActionDao implements ActionDao {

	private ActionCrud actionCrud;
	private String smartspace;


	// TODO remove this
	private AtomicLong nextId;
	
	@Autowired
	public  RdbActionDao(ActionCrud actionCrud) {
		super();
	this.actionCrud=actionCrud;
	
	// TODO remove this
	this.nextId = new AtomicLong(100);
	}
	
	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	
	@Override
	@Transactional
	public ActionEntity create(ActionEntity action) {
		// SQL: INSERT INTO MESSAGES (ID, NAME) VALUES (?,?);
		
		// TODO replace this with id stored in db
		action.setKey(nextId.getAndIncrement()+"");
		action.setActionSmartspace(smartspace);	
		if (!this.actionCrud.existsById(action.getKey())) {
			ActionEntity rv = this.actionCrud.save(action);
			return rv;
		}
		else {
			throw new RuntimeException("action already exists with key: " + action.getKey());
		}
	
	}
	
	

	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readAll() {

        List<ActionEntity> rv = new ArrayList<>();
		
		// SQL: SELECT
		this.actionCrud.findAll()
			.forEach(rv::add);
		
		return rv;
	
	}

	@Override
	@Transactional
	public void deleteAll() {
		// SQL: DELETE
		this.actionCrud.deleteAll();
	}

	@Override
	public void deleteByKey(String actionEntity) {
		this.actionCrud.deleteById(actionEntity);		
	}

	@Override
	public void delete(ActionEntity actionEntity) {
		this.actionCrud.delete(actionEntity);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ActionEntity> readById(String actionEntity) {
		// SQL: SELECT
		return this.actionCrud.findById(actionEntity);
	}

	@Override
	public void update(ActionEntity actionEntity) {
		ActionEntity existing = this.readById(actionEntity.getKey())
				.orElseThrow(() -> new RuntimeException("no element entity to update"));

		if (actionEntity.getActionId() != null) {
			existing.setActionId(actionEntity.getActionId());
		}
		if (actionEntity.getActionSmartspace() != null) {
			existing.setActionSmartspace(actionEntity.getActionSmartspace());
		}
		if (actionEntity.getActionType() != null) {
			existing.setActionType(actionEntity.getActionType());
		}
		if (actionEntity.getCreationTimestamp()!= null) {
			existing.setCreationTimestamp(actionEntity.getCreationTimestamp());
		}
		if (actionEntity.getElementId() != null) {
			existing.setElementId(actionEntity.getElementId() );
		}
		if (actionEntity.getElementSmartspace() != null) {
			existing.setElementSmartspace(actionEntity.getElementSmartspace());

		}
		if (actionEntity.getKey() != null) {
			existing.setKey(actionEntity.getKey());

		}
		if (actionEntity.getPlayerEmail() != null) {
			existing.setPlayerEmail(actionEntity.getPlayerEmail() );

		}
		if (actionEntity.getPlayerSmartspace() != null) {
			existing.setPlayerSmartspace(actionEntity.getPlayerSmartspace() );

		}
			
		// SQL: UPDATE
		this.actionCrud.save(existing);		
	}

}
