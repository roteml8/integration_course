package smartspace.infra;

import java.util.List;
import smartspace.data.ElementEntity;

public interface ElementService {
	public List<ElementEntity> importElements(ElementEntity[] elements, String adminSmartspace, String adminEmail);

	public List<ElementEntity> getUsingPagination(String userSmartspace, String userEmail, int size, int page);

	void updateElement(ElementEntity element, String managerSmartspace, String managerEmail, String elementSmartspace, String elementId);

	public ElementEntity newElement(String managerSmartspace, String managerEmail, ElementEntity entity);

	public ElementEntity getElement(String userSmartspace, String userEmail, String elementSmartspace,
			String elementId);

	public List<ElementEntity> getByName(String userSmartspace, String userEmail, String value, int size,
			int page);

	public List<ElementEntity> getByType(String userSmartspace, String userEmail, String value, int size,
			int page);

	public List<ElementEntity> getByLocation(String userSmartspace, String userEmail, double x, double y,
			int distance, int size, int page);
}
