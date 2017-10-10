package ru.leasoft.challenge.aggregator.persistence.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.leasoft.challenge.aggregator.persistence.PersistenceLevelException;

@Component
@Aspect
@Order(50)
public class PersistenceMethodAspect {

    @Pointcut("@annotation(ru.leasoft.challenge.aggregator.persistence.aspects.PersistenceMethod)")
    public void annotated() {}

    @Pointcut("execution(public * *(..))")
    public void publicMethodCall() {}

    @Around(value = "publicMethodCall() && annotated()")
    public Object persistenceMethodExecution(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();

        } catch (Exception e) {
            Logger log = LoggerFactory.getLogger(pjp.getTarget().getClass());
            log.error(e.toString());
            throw new PersistenceLevelException(e);
        }
    }

}
