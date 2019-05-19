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
import smartspace.infra.NotAManagerException;
import smartspace.infra.NotAnAdminException;

@Component
@Aspect
public class AdminGateway {
	
private EnhancedUserDao<String> users;
	
	@Autowired
	public AdminGateway(EnhancedUserDao<String> users) {
		super();
		this.users=users;
	}
	
	@Around("@annotation(smartspace.aop.AdminCheck) && args(adminSmartspace,adminEmail,..)")
	public Object logName (ProceedingJoinPoint pjp, String adminSmartspace, String adminEmail) throws Throwable{
		Optional<UserEntity> user = this.users.readById(adminSmartspace+"#"+adminEmail);
		if (user.isPresent() == false || !user.get().getRole().equals(UserRole.ADMIN))
			throw new NotAnAdminException("Only Admins are allowed to perform this action!");
		return pjp.proceed();		

		}

}
