package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import smartspace.dao.*;
import smartspace.data.*;

public class MemoryElementDao implements ElementDao<String> {

	
	
	private List<ElementEntity> elementEntitys;
	private AtomicLong nextId;
	
	public MemoryElementDao() {
		this.elementEntitys = Collections.synchronizedList(new ArrayList<>());
		this.nextId = new AtomicLong(1);
	}
	@Override
	public ElementEntity create(ElementEntity elementEntity) {
		elementEntity.setKey(nextId.getAndIncrement()+"");
		this.elementEntitys.add(elementEntity);
		return elementEntity;
	}

	@Override
	public Optional<ElementEntity> readById(String elementkey) {
		ElementEntity target = null;
		for (ElementEntity current : this.elementEntitys) {
			if (current.getKey().equals(elementkey)) {
				target = current;
			}
		}
		if (target != null) {
			return Optional.of(target);
		}else {
			return Optional.empty();
		}
	}

	@Override
	public List<ElementEntity> readAll() {
		return this.elementEntitys;

		
	}

	@Override
	public void update(ElementEntity elementEntity) {
		synchronized (this.elementEntitys) {
			ElementEntity existing = this.readById(elementEntity.getKey())
					.orElseThrow(() -> new RuntimeException("not message to update"));
			if (elementEntity.getCreationTimeDate() != null) {
				existing.setCreationTimeDate(elementEntity.getCreationTimeDate());
			}
			if (elementEntity.getCreatorEmail() != null) {
				existing.setCreatorEmail(elementEntity.getCreatorEmail());
			}
			if (elementEntity.getCreatorSmartSpace()!= null) {
				existing.setCreatorSmartSpace(elementEntity.getCreatorSmartSpace());
			}
			if (elementEntity.getElementid() != null) {
				existing.setElementid(elementEntity.getElementid());
			}
			if (elementEntity.getName() != null) {
				existing.setName(elementEntity.getName());
			}
			if(elementEntity.getElementSmartSpace()!=null)
			{
				existing.setElementSmartSpace(elementEntity.getElementSmartSpace());
				
			}
			if(elementEntity.getLocation()!=null) {
				existing.setLocation(elementEntity.getLocation());
				
			}
			if(elementEntity.getType()!=null) {
				existing.setType(elementEntity.getType());
				
			}
			if(elementEntity.getMoreAtrributes()!=null) {
				existing.setMoreAtrributes(elementEntity.getMoreAtrributes());
				
			}
		}
		
	}
	@Override
	public void deleteByKey(String elementKey) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void delete(ElementEntity elementEntity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}


}
