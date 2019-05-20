package smartspace.dao.rdb;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;

import org.springframework.data.domain.Sort.Direction;



@Repository
public class RdbActionDao implements EnhancedActionDao {

	private ActionCrud actionCrud;
	private String smartspace;
	private GenericIdGeneratorCrud genericActionIdGeneratorCrud; 
	
	@Autowired
	public  RdbActionDao(ActionCrud actionCrud,
			GenericIdGeneratorCrud genericActionIdGeneratorCrud) {
		super();
	this.actionCrud=actionCrud;
	this.genericActionIdGeneratorCrud = genericActionIdGeneratorCrud;
	}
	
	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}


	@Override
	@Transactional
	public ActionEntity create(ActionEntity action) {
		// SQL: INSERT INTO MESSAGES (ID, NAME) VALUES (?,?);

		GenericIdGenerator nextId = 
				this.genericActionIdGeneratorCrud.save(new GenericIdGenerator());

		action.setKey(smartspace + "#" + nextId.getId());
		this.genericActionIdGeneratorCrud.delete(nextId);

		if (!this.actionCrud.existsById(action.getKey())) {
			ActionEntity rv = this.actionCrud.save(action);
			return rv;
		}
		else {
			throw new RuntimeException("elementEntity already exists with key: " + action.getKey());
		}

	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<ActionEntity> readById(String actionKey) {
		// SQL: SELECT
		return this.actionCrud.findById(actionKey);

	}
	

	@Override
	@Transactional
	public void update(ActionEntity actionEntity) {
		ActionEntity existing = this.readById(actionEntity.getKey())
				.orElseThrow(() -> new RuntimeException("no element entity to update"));

		if (actionEntity.getElementSmartspace() != null) {
			existing.setElementSmartspace(actionEntity.getElementSmartspace());
		}
		if (actionEntity.getActionSmartspace() != null) {
			existing.setActionSmartspace(actionEntity.getActionSmartspace());
		}
		if (actionEntity.getActionId() != null) {
			existing.setActionId(actionEntity.getActionId());
		}
		if (actionEntity.getElementId() != null) {
			existing.setElementId(actionEntity.getElementId());
		}
		if (actionEntity.getPlayerSmartspace() != null) {
			existing.setPlayerSmartspace(actionEntity.getPlayerSmartspace());
		}

		if (actionEntity.getPlayerEmail() != null) {
			existing.setPlayerEmail(actionEntity.getPlayerEmail());
		}
		if (actionEntity.getActionType() != null) {
			existing.setActionType(actionEntity.getActionType());
		}
		if (actionEntity.getCreationTimestamp() != null) {
			existing.setCreationTimestamp(actionEntity.getCreationTimestamp());
		}
		if (actionEntity.getMoreAttributes() != null) {
			existing.setMoreAttributes(actionEntity.getMoreAttributes());

		}


			
		// SQL: UPDATE
		this.actionCrud.save(existing);
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
	@Transactional(readOnly=true)	
	public List<ActionEntity> readAll(int size, int page) {
		return this.actionCrud
				.findAll(PageRequest.of(page, size))
				.getContent();
	}

	
	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readAll(String sortBy, int size, int page) {
		return this.actionCrud
				.findAll(PageRequest.of(
						page, size, 
						Direction.ASC, sortBy))
				.getContent();
	}

	



	
	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readActionWithPlayerEmailContaining(String text, int size, int page) {
		return this.actionCrud
				.findAllByPlayerSmartspaceLike(
						"%" + text + "%",
						PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readActionWithPlayerSmartspaceContaining(String text, int size, int page) {
		return this.actionCrud
				.findAllByPlayerSmartspaceLike(
						"%" + text + "%",
						PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readActionWithElementSmartspaceContaining(String text, int size, int page) {
		return this.actionCrud
				.findAllByElementSmartspaceLike(
						"%" + text + "%",
						PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readActionWithElementIdContaining(String text, int size, int page) {
		return this.actionCrud
				.findAllByElementIdLike(
						"%" + text + "%",
						PageRequest.of(page, size));
	}



	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readActionWithActionTypeContaining(String text, int size, int page) {
		return this.actionCrud
				.findAllByActionTypeLike(
						"%" + text + "%",
						PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readElementWithCreationTimestamp(Date stamp, int size, int page) {
		return this.actionCrud.findAllByCreationTimestampLike(stamp, PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readElementWithMoreAttributes(Map<String, Object> moreAttributes, int size, int page) {
		return this.actionCrud.findAllByMoreAttributesLike(moreAttributes, PageRequest.of(page, size));
	}

	@Override
	@Transactional
	public ActionEntity importAction(ActionEntity actionEntity) {
		ActionEntity rv = this.actionCrud.save(actionEntity);
		return rv;
	}

	private void deleteByKey(String key) {
		this.actionCrud.deleteById(key);		
	}
	
	

}
