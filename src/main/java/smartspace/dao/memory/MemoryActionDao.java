package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;

@Repository
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
	public void deleteAll() {
		this.actionEntitys.clear();		
	}

}
