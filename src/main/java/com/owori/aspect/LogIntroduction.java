package com.owori.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogIntroduction {
    private static final String FORMAT = "METHOD : {}, ARGS : {}";

    @Pointcut("execution(* com.owori..*Controller*.*(..))")
    public void allController() {}

    @Pointcut("execution(* com.owori..*Service*.*(..))")
    public void allService() {}

    @Pointcut("execution(* com.owori..*Repository*.*(..))")
    public void allRepository() {}

    @Before("allController()")
    public void controllerLog(JoinPoint joinPoint) {
        logging(joinPoint, log::info);
    }

    @Before("allService() || allRepository()")
    public void serviceAndRepositoryLog(JoinPoint joinPoint) {
        logging(joinPoint, log::debug);
    }

    private void logging(JoinPoint joinPoint, Logger logger) {
        logger.log(FORMAT, joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }
}
