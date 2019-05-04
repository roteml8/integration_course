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

import smartspace.data.UserEntity;
import smartspace.infra.UserService;


@RestController
public class UserController {
	private UserService userService;
	private String smartspace;
	private final String baseUrl = "/smartspace/admin/users/";
	private final String keyUrl = "{adminSmartspace}/{adminEmail}";

	@Autowired
	public UserController (UserService userService) {
		this.userService = userService;
	}
	
	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
	
	
	@RequestMapping(
			path=baseUrl + keyUrl,
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] newUser (
			@RequestBody UserBoundary[] usersArr,
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail){
		

		List<UserBoundary> outPutUsers = new ArrayList<UserBoundary>();
		List<UserEntity> inputPutEntitys = new ArrayList<UserEntity>();
		UserEntity tempEntity;
		int usersCount = 0;
		
		for(UserBoundary userBound : usersArr)
		{
			tempEntity = userBound.convertToEntity();
			if (this.userService.valiadateSmartspace(tempEntity) == false)
				throw new RuntimeException("Can't import users from local smartspace! check the user at location " + usersCount);
			else 
			{
				usersCount++;
				inputPutEntitys.add(tempEntity);
			}
		}
		
		for(UserEntity curUserEntity : inputPutEntitys)
		{
			outPutUsers.add(new UserBoundary(this.userService
			.newUser(curUserEntity, adminSmartspace + "#" + adminEmail)));
		}
			
		return outPutUsers.toArray(new UserBoundary[0]);
		
	}
	
	@RequestMapping(
			path=baseUrl + keyUrl,
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
			path=baseUrl + keyUrl,
			method=RequestMethod.PUT,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void update (
			@PathVariable("adminSmartspace") String adminSmartspace,
			@PathVariable("adminEmail") String adminEmail,
			@RequestBody UserBoundary user) {
		this.userService.update(user.convertToEntity() ,adminSmartspace + "#" + adminEmail);
	}
}



