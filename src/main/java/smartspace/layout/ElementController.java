package smartspace.layout;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.ElementEntity;
import smartspace.infra.ElementService;
import smartspace.infra.EntityNotInDBException;
import smartspace.infra.FailedValidationException;
import smartspace.infra.ImportFromLocalException;
import smartspace.infra.NotAManagerException;
import smartspace.infra.NotAPlayerException;
import smartspace.infra.NotAUserException;
import smartspace.infra.NotAnAdminException;


@RestController
public class ElementController {
	private ElementService elementService;
	
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
	
	
	@RequestMapping(
			path=baseAdminUrl+adminKeyUrl,
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] importElements (
			@RequestBody ElementBoundary[] elementArr, 
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail) {
		
		ElementEntity[] toImport = new ElementEntity[elementArr.length];
		ElementEntity tempEntity;
		
		for(int i=0; i<elementArr.length; i++)
		{
			tempEntity = elementArr[i].convertToEntity();
			toImport[i] = tempEntity;

		}
			
		return this.elementService.importElements(adminSmartspace, adminEmail, toImport)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
		
	}
	
				
	@RequestMapping(
			path=baseUrl+userKeyUrl,
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getUsingPagination (
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail){
		return 
			this.elementService
			.getAllElementsUsingPagination(userSmartspace, userEmail, size, page)
			.stream()
			.map(ElementBoundary::new)
			.collect(Collectors.toList())
			.toArray(new ElementBoundary[0]);
	}
	
	@RequestMapping(
			path=baseAdminUrl+adminKeyUrl,
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] exportElements (
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail){
		return 
			this.elementService
			.exportElements(adminSmartspace, adminEmail, size, page)
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
				.newElement(managerSmartspace, managerEmail, elementBoundry.convertToEntity()));
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
			.updateElement(managerSmartspace, managerEmail, elementBoundry.convertToEntity(), elementSmartspace, elementId);
	}
	
	@RequestMapping(
			path=baseUrl+userKeyUrl + elementKeyUrl,
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary getElement (
			@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail,
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
			@RequestParam(name="x", required=false, defaultValue="0") double x,
			@RequestParam(name="y", required=false, defaultValue="0") double y,
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
			.exportElements(userSmartspace, userEmail, size, page)
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
			throw new UnsupportedSearchException(search);
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
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
	public ErrorMessage handleException (UnsupportedSearchException e){
		String message = e.getMessage();
		if (message == null) {
			message = "The name you have provided is invalid";
		}
		
		return new ErrorMessage(message);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorMessage handleException (NotAnAdminException e){
		String message = e.getMessage();
		if (message == null) {
			message = "The name you have provided is invalid";
		}
		
		return new ErrorMessage(message);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ErrorMessage handleException (FailedValidationException e){
		String message = e.getMessage();
		if (message == null) {
			message = "The name you have provided is invalid";
		}
		
		return new ErrorMessage(message);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ErrorMessage handleException (EntityNotInDBException e){
		String message = e.getMessage();
		if (message == null) {
			message = "The name you have provided is invalid";
		}
		
		return new ErrorMessage(message);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorMessage handleException (ImportFromLocalException e){
		String message = e.getMessage();
		if (message == null) {
			message = "The name you have provided is invalid";
		}
		
		return new ErrorMessage(message);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorMessage handleException (NotAManagerException e){
		String message = e.getMessage();
		if (message == null) {
			message = "The name you have provided is invalid";
		}
		
		return new ErrorMessage(message);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorMessage handleException (NotAPlayerException e){
		String message = e.getMessage();
		if (message == null) {
			message = "The name you have provided is invalid";
		}
		
		return new ErrorMessage(message);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorMessage handleException (NotAUserException e){
		String message = e.getMessage();
		if (message == null) {
			message = "The name you have provided is invalid";
		}
		
		return new ErrorMessage(message);
	}
	
}


