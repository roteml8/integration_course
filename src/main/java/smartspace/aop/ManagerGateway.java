package smartspace.aop;

import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.infra.NotAManagerException;

@Component
@Aspect
public class ManagerGateway {
	
	private EnhancedUserDao<String> users;
	
	@Autowired
	public ManagerGateway(EnhancedUserDao<String> users) {
		super();
		this.users=users;
	}
	
	@Around("@annotation(smartspace.aop.ManagerCheck) && args(smartspace,email,..)")
	public Object logName (ProceedingJoinPoint pjp, String smartspace, String email) throws Throwable{
		Optional<UserEntity> user = this.users.readById(smartspace+"#"+email);
		if ((user.isPresent() == false) || (user.isPresent() && user.get().getRole() != UserRole.MANAGER))
			throw new NotAManagerException("Only Managers are allowed to perform this action!");
		return pjp.proceed();		

		}
		
	}


