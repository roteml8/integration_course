package smartspace.dao.rdb;
import java.util.ArrayList; 
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;



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
		action.setKey(smartspace + "#" + nextId.getAndIncrement());
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

}
