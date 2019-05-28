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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.data.ActionEntity;
import smartspace.infra.ActionService;
import smartspace.infra.EntityNotInDBException;
import smartspace.infra.FailedValidationException;
import smartspace.infra.ImportFromLocalException;
import smartspace.infra.NotAPlayerException;
import smartspace.infra.NotAnAdminException;
import smartspace.infra.UnsupportedActionTypeException;


@RestController
public class ActionController {
	private ActionService actionService;
	
	private final String baseUrl = "/smartspace/actions";
	private final String baseAdminUrl = "/smartspace/admin/actions";
	private final String keyUrl = "/{adminSmartspace}/{adminEmail}";
	
	@Autowired
	public ActionController (ActionService actionService) {
		this.actionService = actionService;
	}
	

/*				
	@RequestMapping(
			//path="/smartspace/admin/actions/{adminSmartspace}/{adminEmail}?page={page}&size={size}",
			path="/actiondemo",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] getUsingPagination (
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		return 
			this.actionService
			.getUsingPagination(size, page)
			.stream()
			.map(ActionBoundary::new)
			.collect(Collectors.toList())
			.toArray(new ActionBoundary[0]);
	}*/

	@RequestMapping(
			path=baseAdminUrl + keyUrl,
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] getUsingPagination (
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail) {
		return 
			this.actionService
			.getUsingPagination(adminSmartspace, adminEmail, size, page)
			.stream()
			.map(ActionBoundary::new)
			.collect(Collectors.toList())
			.toArray(new ActionBoundary[0]);
	}
	
	
	
	
	@RequestMapping(
			path=baseAdminUrl + keyUrl,
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] importActions (
			@RequestBody ActionBoundary[] actionsArr,
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail){
		
		ActionEntity[] toImport = new ActionEntity[actionsArr.length];
		ActionEntity tempEntity;
		
		for(int i=0; i<actionsArr.length; i++)
		{
			tempEntity = actionsArr[i].convertToEntity();
			toImport[i] = tempEntity;

		}
			
		return this.actionService.importActions(adminSmartspace, adminEmail, toImport)
				.stream()
				.map(ActionBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ActionBoundary[0]);
		
	}
	
	@RequestMapping(
			path=baseUrl,
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary invokeAction (
			@RequestBody ActionBoundary actionBoundry){
		
		return new ActionBoundary(this.actionService
				.invoke(actionBoundry.convertToEntity()));
	}
	 
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
	public ErrorMessage handleException (UnsupportedActionTypeException e){
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
	public ErrorMessage handleException (NotAPlayerException e){
		String message = e.getMessage();
		if (message == null) {
			message = "The name you have provided is invalid";
		}
		
		return new ErrorMessage(message);
	}
}


