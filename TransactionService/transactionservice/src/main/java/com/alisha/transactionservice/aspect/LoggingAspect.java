package com.alisha.transactionservice.aspect;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.alisha.transactionservice.service.*.*(..))")
    public Object logExecutionTime(
            ProceedingJoinPoint joinPoint)
            throws Throwable {

        long start = System.currentTimeMillis();

        String methodName = joinPoint.getSignature()
                .toShortString();

        log.info(
                "Entering method {}",
                methodName);

        try {

            Object result = joinPoint.proceed();

            long executionTime = System.currentTimeMillis()
                    - start;

            log.info(
                    "Exiting method {}",
                    methodName);

            log.info(
                    "Method {} executed in {} ms",
                    methodName,
                    executionTime);

            return result;

        } catch (Exception ex) {

            log.error(
                    "Exception in method {}",
                    methodName,
                    ex);

            throw ex;
        }
    }
}