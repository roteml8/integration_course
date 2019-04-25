package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;

@Repository
public class RdbElementDao implements EnhancedElementDao<String> {

	private String smartspace;
	private ElementCrud elementCrud;
	// TODO remove this
	private GenericIdGeneratorCrud genericElementIdGeneratorCrud; 

	@Autowired
	public RdbElementDao(ElementCrud elementCrud,
			GenericIdGeneratorCrud genericElementIdGeneratorCrud) {
		super();
		this.elementCrud = elementCrud;
		this.genericElementIdGeneratorCrud = genericElementIdGeneratorCrud;
	}

	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	@Override
	@Transactional
	public ElementEntity create(ElementEntity elementEntity) {
		// SQL: INSERT INTO MESSAGES (ID, NAME) VALUES (?,?);

		// TODO replace this with id stored in db
		if(elementEntity.getCreatorSmartSpace().equals(smartspace))
		{
		GenericIdGenerator nextId = 
				this.genericElementIdGeneratorCrud.save(new GenericIdGenerator());
		 
		elementEntity.setKey(smartspace + "#" + nextId.getId());
		this.genericElementIdGeneratorCrud.delete(nextId);
		}
		
		if (!this.elementCrud.existsById(elementEntity.getKey())) {
			ElementEntity rv = this.elementCrud.save(elementEntity);
			return rv;
		}
		else {
			throw new RuntimeException("elementEntity already exists with key: " + elementEntity.getKey());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ElementEntity> readById(String elementKey) {
		// SQL: SELECT
		return this.elementCrud.findById(elementKey);

	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAll() {

		List<ElementEntity> rv = new ArrayList<>();

		// SQL: SELECT
		this.elementCrud.findAll().forEach(rv::add);

		return rv;

	}

	@Override
	@Transactional
	public void update(ElementEntity elementEntity) {
		ElementEntity existing = this.readById(elementEntity.getKey())
				.orElseThrow(() -> new RuntimeException("no element entity to update"));

		if (elementEntity.getCreatorEmail() != null) {
			existing.setCreatorEmail(elementEntity.getCreatorEmail());
		}
		if (elementEntity.getCreatorSmartSpace() != null) {
			existing.setCreatorSmartSpace(elementEntity.getCreatorSmartSpace());
		}
		if (elementEntity.getElementid() != null) {
			existing.setElementid(elementEntity.getElementid());
		}
		if (elementEntity.getElementSmartSpace() != null) {
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
		if (elementEntity.getMoreAttributes() != null) {
			existing.setMoreAttributes(elementEntity.getMoreAttributes());

		}
		existing.setExpired(elementEntity.isExpired());


			
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

	@Override
	@Transactional
	public List<ElementEntity> readAll(int size, int page) {
		return this.elementCrud.findAll(PageRequest.of(page, size)).getContent();

	}

	@Override
	@Transactional
	public List<ElementEntity> readAll(String sortBy, int size, int page) {
		return this.elementCrud.findAll(PageRequest.of(page, size, Direction.ASC, sortBy)).getContent();

	}

	@Override
	@Transactional
	public List<ElementEntity> readElementWithNameContaining(String text, int size, int page) {
		return this.elementCrud.findAllByNameLike("%" + text + "%", PageRequest.of(page, size));

	}

	@Override
	@Transactional
	public List<ElementEntity> readElementWithLocation(Location location, int size, int page) {
		return this.elementCrud.findAllByLocationLike(location, PageRequest.of(page, size));
	}

	@Override
	@Transactional
	public List<ElementEntity> readElementWithTypeContaining(String text, int size, int page) {
		return this.elementCrud.findAllByTypeLike("%" + text + "%", PageRequest.of(page, size));

	}

	@Override
	@Transactional
	public List<ElementEntity> readElementWithExpired(boolean expired, int size, int page) {
		return this.elementCrud.findAllByExpiredLike(expired, PageRequest.of(page, size));

	}

	@Override
	@Transactional
	public List<ElementEntity> readElementWithCreatorEmailContaining(String text, int size, int page) {
		return this.elementCrud.findAllByCreatorEmailLike("%" + text + "%", PageRequest.of(page, size));

	}

	@Override
	@Transactional
	public List<ElementEntity> readElementWithCreatorSmartspaceContaining(String text, int size, int page) {
		return this.elementCrud.findAllByCreatorSmartspaceLike("%" + text + "%", PageRequest.of(page, size));

	}

	@Override
	@Transactional
	public List<ElementEntity> readElementWithCreationTimeStamp(Date stamp, int size, int page) {
		return this.elementCrud.findAllByCreationTimeStampLike(stamp, PageRequest.of(page, size));

	}

	@Override
	@Transactional
	public List<ElementEntity> readElementWithMoreAttributes(Map<String, Object> moreAttributes, int size, int page) {
		return this.elementCrud.findAllByMoreAttributesLike(moreAttributes, PageRequest.of(page, size));

	}

}
