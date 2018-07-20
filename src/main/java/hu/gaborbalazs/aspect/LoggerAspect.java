package hu.gaborbalazs.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggerAspect {

	private static final String BEFORE = ">> ";
	private static final String AFTER = "<< ";
	private static final String BRACKET = "()";

	@Pointcut("within(hu.gaborbalazs.crawler..*)")
	private void crawlers() {
	}

	@Pointcut("within(hu.gaborbalazs.batch..*)")
	private void schedulers() {
	}

	@Around("crawlers() || schedulers()")
	public Object aroundRestOrDataMethods(ProceedingJoinPoint joinPoint) throws Throwable {
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
		logger.trace(BEFORE + joinPoint.getSignature().getName() + BRACKET);
		Object retval = joinPoint.proceed();
		logger.trace(AFTER + joinPoint.getSignature().getName() + BRACKET);
		return retval;
	}
}
