package smartspace.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MeasureTimeAspect {
	Log logger = LogFactory.getLog(MeasureTimeAspect.class);
	
	
	@Before("@annotation(smartspace.aop.MeasureElapsedTime)")
	public void measureBeginTime(JoinPoint jp) {
		String method = jp.getSignature().getName();
		String fullyQualifiedClassName = jp.getTarget().getClass().getName();
		
		long begin = System.currentTimeMillis();
		logger.trace("********* " + fullyQualifiedClassName + "." + method + "() - "+ begin);		
	}
	
	@Around("@annotation(smartspace.aop.MeasureElapsedTime) && args(key,..)")
	public Object measureElapsedTime(ProceedingJoinPoint pjp, Long key) throws Throwable{
		// before
		String method = pjp.getSignature().getName();
		String fullyQualifiedClassName = pjp.getTarget().getClass().getName();
		long begin = System.currentTimeMillis();
		
		Object rv;
		try {
			rv = pjp.proceed();
			return rv;
		} catch (Exception e) {
			throw e;
		}finally {
			// after
			long end = System.currentTimeMillis();
			long elapsed = end - begin;
			logger.debug("********* " + fullyQualifiedClassName + "." + method + "(" + key+ ",...) - "+ elapsed + "ms");
		}		
	}
}
