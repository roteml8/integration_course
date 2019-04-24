package smartspace.infra;

import java.util.List;

import smartspace.data.ElementEntity;

public interface ElementService {
	public ElementEntity newElement(ElementEntity entity, int code);

	//TODO return this code when enhanced element entity is done.
	public List<ElementEntity> getUsingPagination(int size, int page);
}
