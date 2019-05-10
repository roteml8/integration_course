package smartspace.layout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

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
	
	private final String baseUrl = "/smartspace/elements";
	private final String baseAdminUrl = "/smartspace/admin/elements";
	
	private final String adminKeyUrl = "/{adminSmartspace}/{adminEmail}";
	private final String managerKeyUrl = "/{managerSmartspace}/{managerEmail}";
	private final String userKeyUrl = "/{userSmartspace}/{userEmail}";
	private final String elementKeyUrl = "/{elementSmartspace}/{elementId}";

	
	@Autowired
	public ElementController (ElementService elementService) {
		this.elementService = elementService;
	}
	
	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
	
	@RequestMapping(
			path=baseAdminUrl+adminKeyUrl,
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] importElements (
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
			.importElement(curElementEntity, adminSmartspace, adminEmail)));
		}
			
		return outPutElements.toArray(new ElementBoundary[0]);
		
	}
	
				
	@RequestMapping(
			path=baseAdminUrl+adminKeyUrl,
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getUsingPagination (
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail){
		return 
			this.elementService
			.getUsingPagination(adminSmartspace, adminEmail, size, page)
			.stream()
			.map(ElementBoundary::new)
			.collect(Collectors.toList())
			.toArray(new ElementBoundary[0]);
	}
	
	
	@RequestMapping(
			path=baseUrl+managerKeyUrl,
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary newElement (
			@RequestBody ElementBoundary elementBoundry, 
			@PathVariable("managerSmartspace") String managerSmartspace,
			@PathVariable("managerEmail") String managerEmail) {
		
		
		return new ElementBoundary(this.elementService
				.newElement(elementBoundry.convertToEntity(), managerSmartspace, managerEmail));
	}
	
	@RequestMapping(
			path=baseUrl+managerKeyUrl + elementKeyUrl,
			method=RequestMethod.PUT,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateElement (
			@RequestBody ElementBoundary elementBoundry, 
			@PathVariable("managerSmartspace") String managerSmartspace,
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId){

		this.elementService
			.updateElement(elementBoundry.convertToEntity(), managerSmartspace, managerEmail, elementSmartspace, elementId);
	}
	
	@RequestMapping(
			path=baseUrl+userKeyUrl + elementKeyUrl,
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary getElement (
			@PathVariable("managerSmartspace") String userSmartspace,
			@PathVariable("managerEmail") String userEmail,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId){
		
		return new ElementBoundary(this.elementService
				.getElement(userSmartspace, userEmail, elementSmartspace, elementId));
	}
	
	/*
	@RequestMapping(
			path=baseUrl+userKeyUrl,
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElements (
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail){
		
		return 
			this.elementService
			.getUsingPagination(userSmartspace, userEmail, size, page)
			.stream()
			.map(ElementBoundary::new)
			.collect(Collectors.toList())
			.toArray(new ElementBoundary[0]);
	}
	*/
	
	@RequestMapping(
			path=baseUrl+userKeyUrl,
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsBy (
			@RequestParam(name="search", required=false, defaultValue="") String search,
			@RequestParam(name="x", required=false, defaultValue="0") int x,
			@RequestParam(name="y", required=false, defaultValue="0") int y,
			@RequestParam(name="distance", required=false, defaultValue="1") int distance,
			@RequestParam(value="value", required=false, defaultValue="") String value,
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail){
		
		switch(search)
		{
		case "":{
			return 
			this.elementService
			.getUsingPagination(userSmartspace, userEmail, size, page)
			.stream()
			.map(ElementBoundary::new)
			.collect(Collectors.toList())
			.toArray(new ElementBoundary[0]);
		}
		case "location":{
		return 
			this.elementService
			.getByLocation(userSmartspace, userEmail, x, y, distance, size, page)
			.stream()
			.map(ElementBoundary::new)
			.collect(Collectors.toList())
			.toArray(new ElementBoundary[0]);
		}
		case "name":{
			return 
				this.elementService
				.getByName(userSmartspace, userEmail, value , size, page)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
		}
		case "type":{
			return 
				this.elementService
				.getByType(userSmartspace, userEmail, value , size, page)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
		}
		default:
			throw new RuntimeException("Unsupported search parameter! wtf is " + search + 
					"? consult the guidebook for a list of supported and FDA approved searches.");
		}
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


