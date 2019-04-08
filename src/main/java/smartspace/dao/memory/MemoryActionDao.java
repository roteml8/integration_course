 package smartspace.dao.memory;

import java.util.ArrayList;  
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

 
import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
//@Repository
public class MemoryActionDao implements ActionDao{
	
	
	private List<ActionEntity> actionEntitys;
	private AtomicLong nextId;
	
	public MemoryActionDao() {
		this.actionEntitys = Collections.synchronizedList(new ArrayList<>());
		this.nextId = new AtomicLong(1);
	}


	@Override
	public ActionEntity create(ActionEntity actionEntity) {
		actionEntity.setKey(nextId.getAndIncrement()+"");
		this.actionEntitys.add(actionEntity);
		return actionEntity;
	}

	@Override
	public List<ActionEntity> readAll() {
     return this.actionEntitys;		
	}


	@Override
	public Optional<ActionEntity> readById(String actionEntity) {
		ActionEntity target = null;
		for (ActionEntity current : this.actionEntitys) {
			if (current.getKey().equals(actionEntity)) {
				target = current;
			}
		}
		if (target != null) {
			return Optional.of(target);
		} else {
			return Optional.empty();
		}
	}


	@Override
	public void update(ActionEntity actionEntity) {
		synchronized (this.actionEntitys) {
			ActionEntity existing = this.readById(actionEntity.getKey())
					.orElseThrow(() -> new RuntimeException("Action Entity is not in dao, can't update!"));
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
		}

	}
	
	@Override
	public void deleteAll() {
		this.actionEntitys.clear();		
	}
	
	
	@Override
	public void deleteByKey(String actionEntity) {
		for (int i = 0; i < actionEntitys.size(); i++)
			if (actionEntitys.get(i).getKey().equals(actionEntity)) {
				this.actionEntitys.remove(i);
				break;
			}
	}
	
	@Override
	public void delete(ActionEntity actionEntity) {
		for (int i = 0; i < actionEntitys.size(); i++)
			if (actionEntitys.get(i).equals(actionEntity)) {
				this.actionEntitys.remove(i);
				break;

			}
	}



}
