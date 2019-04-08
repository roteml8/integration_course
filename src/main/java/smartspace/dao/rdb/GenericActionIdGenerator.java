package smartspace.dao.rdb;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GenericActionIdGenerator {
	private Long id;
	
	public GenericActionIdGenerator() {
	}

	@Id
	@GeneratedValue//(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
