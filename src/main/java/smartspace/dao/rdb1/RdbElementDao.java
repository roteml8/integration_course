package smartspace.dao.rdb1;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;



@Repository
public class RdbElementDao implements ElementDao<String> {

	private ElementCrud elementCrud;
	//TODO remove this
		private AtomicLong nextId;
		
   @Autowired	
	public	  RdbElementDao(ElementCrud elementCrud){
		super();
		this.elementCrud = elementCrud;
		
		// TODO remove this
		this.nextId = new AtomicLong(100);
	} 
		
		
	@Override
	@Transactional
	public ElementEntity create(ElementEntity elementEntity) {
		// SQL: INSERT INTO MESSAGES (ID, NAME) VALUES (?,?);
		
				// TODO replace this with id stored in db
				elementEntity.setKey(nextId.getAndIncrement()+"");
				
				
				if (!this.elementCrud.existsById(elementEntity.getKey())) {
					ElementEntity rv = this.elementCrud.save(elementEntity);
					return rv;
				}else {
					throw new RuntimeException("message already exists with key: " + elementEntity.getKey());
				}
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<ElementEntity> readById(String elementKey) {
		// SQL: SELECT
				return this.elementCrud.findById(elementKey);
			
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll() {

      List<ElementEntity> rv = new ArrayList<>();
		
		// SQL: SELECT
		this.elementCrud.findAll()
			.forEach(rv::add);
		
		return rv;
		
	}

	@Override
	@Transactional
	public void update(ElementEntity elementEntity) {
		ElementEntity existing = 
				this.readById(elementEntity.getKey())
					.orElseThrow(() -> new RuntimeException("not message to update"));
	
		if (elementEntity.getCreatorEmail() != null) {
			existing.setCreatorEmail(elementEntity.getCreatorEmail());
		}
		if (elementEntity.getCreatorSmartSpace()!=null) {
			existing.setCreatorSmartSpace(elementEntity.getCreatorSmartSpace());
		}
		if (elementEntity.getElementid() != null) {
			existing.setElementid(elementEntity.getElementid());
		}
		if (elementEntity.getElementSmartSpace()!= null) {
			existing.setElementSmartSpace(elementEntity.getElementSmartSpace());
		}
		if (elementEntity.getLocation() != null) {
			existing.setLocation(elementEntity.getLocation());
		}
		
		if (elementEntity.getName() != null) {
			existing.setName(elementEntity.getName());
		}
		if (elementEntity.getType() != null) {
			existing.setType(elementEntity.getType());
		}
		// SQL: UPDATE
		this.elementCrud.save(existing);
	}

	@Override
	@Transactional
	public void deleteByKey(String elementKey) {
	this.elementCrud.deleteById(elementKey);
		
	}

	@Override
	@Transactional
	public void delete(ElementEntity elementEntity) {
		this.elementCrud.delete(elementEntity);		
	}

	@Override
	@Transactional
	public void deleteAll() {
		// SQL: DELETE
		this.elementCrud.deleteAll();
	}

}
