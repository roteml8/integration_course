package smartspace.infra;

import java.util.List;

import smartspace.data.ElementEntity;

public interface ElementService {
	public ElementEntity newElement(ElementEntity entity, String adminSmartspace, String adminEmail);

	public List<ElementEntity> getUsingPagination(int size, int page);
}
