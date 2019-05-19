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
import smartspace.infra.NotAPlayerException;

//@Component
//@Aspect
public class PlayerGateway {
	
	private EnhancedUserDao<String> users;
	
	@Autowired
	public PlayerGateway(EnhancedUserDao<String> users) {
		super();
		this.users=users;
	}
	
	@Around("@annotation(smartspace.aop.PlayerCheck) && args(smartspace,email,..)")
	public Object logName (ProceedingJoinPoint pjp, String smartspace, String email) throws Throwable{
		Optional<UserEntity> user = this.users.readById(smartspace+"#"+email);
		if ((user.isPresent() == false) || (user.isPresent() && user.get().getRole() != UserRole.PLAYER))
			throw new NotAPlayerException("Only Players are allowed to perform this action!");
		return pjp.proceed();		

		}
		
	}



