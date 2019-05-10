package smartspace.layout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.data.ActionEntity;
import smartspace.infra.ActionService;


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
			.getUsingPagination(size, page)
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
	public ActionBoundary[] newAction (
			@RequestBody ActionBoundary[] actionsArr,
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail){
		

		List<ActionBoundary> outPutActions = new ArrayList<ActionBoundary>();
		List<ActionEntity> inputPutEntitys = new ArrayList<ActionEntity>();
		ActionEntity tempEntity;
		
		for(ActionBoundary actionBound : actionsArr)
		{
				tempEntity = actionBound.convertToEntity();
				inputPutEntitys.add(tempEntity);
		}
		
		for(ActionEntity curActionEntity : inputPutEntitys)
		{
			outPutActions.add(new ActionBoundary(this.actionService
			.newAction(curActionEntity)));
		}
			
		return outPutActions.toArray(new ActionBoundary[0]);
		
	}
	
	@RequestMapping(
			path=baseUrl,
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String invokeAction (
			@RequestBody ActionBoundary actionBoundry){
		ObjectMapper jackson = new ObjectMapper();

		try {
		return(jackson.writeValueAsString
				(this.actionService
				.invoke(actionBoundry.convertToEntity())));
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}


