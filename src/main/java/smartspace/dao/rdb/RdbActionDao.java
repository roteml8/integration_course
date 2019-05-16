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

import org.springframework.data.domain.Sort.Direction;



@Repository
public class RdbActionDao implements EnhancedActionDao {

	private String smartspace;
	
	private ActionCrud actionCrud;
//	private GenericIdGeneratorCrud genericActionIdGeneratorCrud; 

	
	@Autowired
	public  RdbActionDao(ActionCrud actionCrud) {
		super();
	this.actionCrud=actionCrud;
//	this.smartspace = "2019B.Amitz4.SmartSpace";
	if(this.actionCrud.count() > 0) {
		List<ActionEntity> allActions= this.actionCrud.
				findAll(PageRequest.of(0, 5, Direction.DESC, "creationDate")).getContent();
		
		List<ActionEntity> filteredActionsBySmartspace = new ArrayList<>();
		for(ActionEntity action : allActions) {
			action.setKey(action.getKey());
			System.err.println("1" + this.smartspace);
			if(action.getActionSmartspace().equals(smartspace)) {
//				System.err.println("2" + this.smartspace);
				filteredActionsBySmartspace.add(action);
			}
		}
		
		GeneratedId.setActionId(filteredActionsBySmartspace.size());
//		System.err.println(filteredActionsBySmartspace.size());
	}
//	this.genericActionIdGeneratorCrud = genericActionIdGeneratorCrud;
	}
	
	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}


	@Override
	@Transactional
	public ActionEntity create(ActionEntity action) {
		// SQL: INSERT INTO MESSAGES (ID, NAME) VALUES (?,?);
//		GenericIdGenerator nextId = 
//				this.genericActionIdGeneratorCrud.save(new GenericIdGenerator());
		
		long number = GeneratedId.getNextActionValue();
//		action.setActionId("" + number);
//		action.setActionSmartspace(smartspace);
		
		action.setKey(smartspace + "#" + number);
//		this.genericActionIdGeneratorCrud.delete(nextId);
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
		List<ActionEntity> allActions = this.actionCrud.findAll(PageRequest.of(page, size)).getContent();
		
		List<ActionEntity> filteredActions = new ArrayList<>();
		
		for(ActionEntity action : allActions) {
			if(action.getPlayerEmail() != null) {
				if(action.getPlayerEmail().contains(text)) {
					filteredActions.add(action);
				}	
			}
		}
		return filteredActions;	
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readActionWithPlayerSmartspaceContaining(String text, int size, int page) {
		List<ActionEntity> allActions = this.actionCrud.findAll(PageRequest.of(page, size)).getContent();
		
		List<ActionEntity> filteredActions = new ArrayList<>();
		
		for(ActionEntity action : allActions) {
			if(action.getPlayerSmartspace() != null) {
				if(action.getPlayerSmartspace().contains(text)) {
					filteredActions.add(action);
				}	
			}
		}
		return filteredActions;	
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readActionWithElementSmartspaceContaining(String text, int size, int page) {
		List<ActionEntity> allActions = this.actionCrud.findAll(PageRequest.of(page, size)).getContent();
		
		List<ActionEntity> filteredActions = new ArrayList<>();
		
		for(ActionEntity action : allActions) {
			if(action.getElementSmartspace() != null) {
				if(action.getElementSmartspace().contains(text)) {
					filteredActions.add(action);
				}	
			}
		}
		return filteredActions;	
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readActionWithElementIdContaining(String text, int size, int page) {
		List<ActionEntity> allActions = this.actionCrud.findAll(PageRequest.of(page, size)).getContent();
		
		List<ActionEntity> filteredActions = new ArrayList<>();
		
		for(ActionEntity action : allActions) {
			if(action.getElementId() != null) {
				if(action.getElementId().contains(text)) {
					filteredActions.add(action);
				}	
			}
		}
		return filteredActions;	
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readActionWithActionTypeContaining(String text, int size, int page) {
		List<ActionEntity> allActions = this.actionCrud.findAll(PageRequest.of(page, size)).getContent();
		
		List<ActionEntity> filteredActions = new ArrayList<>();
		
		for(ActionEntity action : allActions) {
			if(action.getActionType() != null) {
				if(action.getActionType().contains(text)) {
					filteredActions.add(action);
				}	
			}
		}
		return filteredActions;	
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readElementWithCreationTimestamp(Date stamp, int size, int page) {
		List<ActionEntity> allActions = this.actionCrud.findAll(PageRequest.of(page, size)).getContent();
		
		List<ActionEntity> filteredActions = new ArrayList<>();
		
		for(ActionEntity action : allActions) {
			if(action.getCreationTimestamp() != null) {
				if(action.getActionType().equals(stamp)) {
					filteredActions.add(action);
				}	
			}
		}
		return filteredActions;	
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readElementWithMoreAttributes(Map<String, Object> moreAttributes, int size, int page) {
		List<ActionEntity> allActions = this.actionCrud.findAll(PageRequest.of(page, size)).getContent();
		
		List<ActionEntity> filteredActions = new ArrayList<>();
		
		for(ActionEntity action : allActions) {
			if(action.getMoreAttributes() != null) {
				if(action.getMoreAttributes().equals(moreAttributes)) {
					filteredActions.add(action);
				}	
			}
		}
		return filteredActions;	
	}

	@Override
	@Transactional
	public ActionEntity importAction(ActionEntity actionEntity) {
		if (this.actionCrud.existsById(actionEntity.getKey()))
			this.deleteByKey(actionEntity.getKey());
		ActionEntity rv = this.actionCrud.save(actionEntity);
		return rv;
	}

	private void deleteByKey(String key) {
		this.actionCrud.deleteById(key);		
	}
	
	

}
