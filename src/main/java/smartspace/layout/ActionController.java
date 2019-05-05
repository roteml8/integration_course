package smartspace.layout;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartspace.infra.ActionService;


@RestController
public class ActionController {
	private ActionService actionService;
	
	@Autowired
	public ActionController (ActionService actionService) {
		this.actionService = actionService;
	}
	
	@RequestMapping(
			path="/actiondemo/{adminSmartspace}/{adminEmail}",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary newAction (
			@RequestBody ActionBoundary action, 
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail) {
		
		return new ActionBoundary(
				this.actionService
					.newAction(action.convertToEntity(), adminSmartspace, adminEmail));
	}
	
	
	
				
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
	}
}


