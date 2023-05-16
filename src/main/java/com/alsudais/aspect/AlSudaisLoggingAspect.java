package com.alsudais.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;

@Aspect
@Configuration
public class AlSudaisLoggingAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	@Pointcut(value = "execution(* com.alsudais..*(..))")
	private void methodInsideReturnPointCut(){}

	@Around(value = "methodInsideReturnPointCut()")
	public Object loggingAdviceAroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
		String className = methodSignature.getDeclaringType().getSimpleName();
		String methodName = methodSignature.getName();
		Object[] args = proceedingJoinPoint.getArgs();

		LOGGER.debug("Inside {} method of class {} with parameters :: {}", methodName, className, Arrays.deepToString(args));
		Object result;
		try {
			result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
		} catch (Throwable e) {
			LOGGER.error("Exception occurred from Logging Aspect ", e);
			throw e;
		}
		LOGGER.debug("Returning from {} method of class {} with return value :: {}", methodName, className, result != null ? result.toString() : null);
		return result;
	}
}

