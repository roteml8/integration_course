package smartspace.aop;

import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.infra.NotAUserException;

@Component
@Aspect
public class UserGateway {
	
	private EnhancedUserDao<String> users;
	
	@Autowired
	public UserGateway(EnhancedUserDao<String> users) {
		super();
		this.users=users;
	}
	
	@Around("@annotation(smartspace.aop.UserCheck) && args(userSmartspace,userEmail,..)")
	public Object logName (ProceedingJoinPoint pjp, String userSmartspace, String userEmail) throws Throwable{
		Optional<UserEntity> user = this.users.readById(userSmartspace+"#"+userEmail);
		if ((user.isPresent() == false) || (user.isPresent() && user.get().getRole() == UserRole.ADMIN))
			throw new NotAUserException("Only Users are allowed to perform this action!");
		return pjp.proceed();		

		}

}
