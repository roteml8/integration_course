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

import smartspace.data.UserEntity;
import smartspace.infra.UserService;


@RestController
public class UserController {
	private UserService userService;
	
	private final String baseUrl = "/smartspace/users";
	private final String loginUrl = "/login/{userSmartspace}/{userEmail}";

	private final String baseAdminUrl = "/smartspace/admin/users";
	private final String adminKeyUrl = "/{adminSmartspace}/{adminEmail}";

	@Autowired
	public UserController (UserService userService) {
		this.userService = userService;
	}
	
	
	@RequestMapping(
			path=baseAdminUrl + adminKeyUrl,
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] importUsers (
			@RequestBody UserBoundary[] usersArr,
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail){
		
		UserEntity[] toImport = new UserEntity[usersArr.length];
		UserEntity tempEntity;		
		for(int i=0; i< usersArr.length; i++)
		{
			tempEntity = usersArr[i].convertToEntity();
			toImport[i] = tempEntity;	
		}
			
		  return 
				this.userService
				.importUsers(toImport, adminSmartspace + "#" + adminEmail)
				.stream()
				.map(UserBoundary::new)
				.collect(Collectors.toList())
				.toArray(new UserBoundary[0]);
		 
	}
	
	@RequestMapping(
			path=baseAdminUrl + adminKeyUrl,
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getUsingPagination (
			@RequestParam(name="size", required=false, defaultValue="10") int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page,
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail) {
		return 
			this.userService
			.getUsingPagination(size, page)
			.stream()
			.map(UserBoundary::new)
			.collect(Collectors.toList())
			.toArray(new UserBoundary[0]);
	}
	
	@RequestMapping(
			path=baseUrl + loginUrl,
			method=RequestMethod.PUT,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void update (
			@PathVariable("userSmartspace") String adminSmartspace,
			@PathVariable("userEmail") String adminEmail,
			@RequestBody UserBoundary user) {
		this.userService.update(user.convertToEntity() ,adminSmartspace + "#" + adminEmail);
	}
	
	@RequestMapping(
			path=baseUrl,
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary newUser (
			@RequestBody NewUserForm newUserForm){
			
		return new UserBoundary(this.userService.newUser(newUserForm.convertToEntity()));
		
	}
	
	@RequestMapping(
			path=baseUrl + loginUrl,
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary login (
			@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail){
			
		return new UserBoundary(this.userService.login(userSmartspace + "#" + userEmail));
		
	}
}



