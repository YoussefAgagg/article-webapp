package com.example.articlewebapp.aop.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Ensures that method calls can be logged with entry-exit logs in console or log file.
 *
 * @author Youssef Agagg
 */

@Aspect
@Component
public class LoggingAspect {

    /**
     * Retrieves the {@link Logger} associated to the given {@link JoinPoint}.
     *
     * @param joinPoint join point we want the logger for.
     * @return {@link Logger} associated to the given {@link JoinPoint}.
     */
    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice.
     * @param e exception.
     */
    @AfterThrowing(pointcut = "@annotation(com.example.articlewebapp.aop.logging.Loggable)", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger(joinPoint)
                .error(
                    "Exception in {}() with cause = '{}' and exception = '{}'",
                    joinPoint.getSignature().getName(),
                    e.getCause() != null ? e.getCause() : "NULL",
                    e.getMessage(),
                    e
                );


    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice.
     * @return result.
     * @throws Throwable if an error occurs
     */
    @Around("execution(* *(..)) && @annotation(com.example.articlewebapp.aop.logging.Loggable)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);

        log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));


        Object result = joinPoint.proceed();

        log.debug("Exit: {}() with result = {}", joinPoint.getSignature().getName(), result);

        return result;

    }
}
