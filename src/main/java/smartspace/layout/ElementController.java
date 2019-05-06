package smartspace.layout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.infra.ElementService;


@RestController
public class ElementController {
	private ElementService elementService;
	private String smartspace;

	private final String baseUrl = "/smartspace/admin/elements/";
	private final String keyUrl = "{adminSmartspace}/{adminEmail}";
	
	@Autowired
	public ElementController (ElementService elementService) {
		this.elementService = elementService;
	}
	
	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
	
	@RequestMapping(
			path=baseUrl+keyUrl,
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] newElement (
			@RequestBody ElementBoundary[] elementArr, 
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail) {
		
		List<ElementBoundary> outPutElements = new ArrayList<ElementBoundary>();
		List<ElementEntity> inputEntitys = new ArrayList<>();
		ElementEntity tempEntity;
		int elementsCount = 0;
		
		for(ElementBoundary elementBound : elementArr)
		{
			tempEntity = elementBound.convertToEntity();
			if (tempEntity.getElementSmartSpace().equals(smartspace))
				throw new RuntimeException("Can't import elements from local smartspace! check the element at location " + elementsCount);
			else 
			{
				elementsCount++;
				inputEntitys.add(tempEntity);
			}
		}
		
		for(ElementEntity curElementEntity : inputEntitys)
		{
			outPutElements.add(new ElementBoundary(this.elementService
			.newElement(curElementEntity, adminSmartspace, adminEmail)));
		}
			
		return outPutElements.toArray(new ElementBoundary[0]);
		
	}
	
				
	@RequestMapping(
			path=baseUrl+keyUrl,
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getUsingPagination (
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail){
		return 
			this.elementService
			.getUsingPagination(size, page)
			.stream()
			.map(ElementBoundary::new)
			.collect(Collectors.toList())
			.toArray(new ElementBoundary[0]);
	}
	
	/*
	@RequestMapping(
			path="/elementdemo/{pattern}/{sortBy}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElements(
			@PathVariable("pattern") String pattern,
			@PathVariable("sortBy") String sortBy,
			@RequestParam(name="size", required=false, defaultValue="10") int size, 
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return
		this.elementService
			.getElementsByPattern(pattern, sortBy, size, page)
			.stream()
			.map(ElementBoundary::new)
			.collect(Collectors.toList())
			.toArray(new ElementBoundary[0]);
	}
	*/
	
}


