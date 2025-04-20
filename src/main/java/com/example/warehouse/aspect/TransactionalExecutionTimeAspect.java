package com.example.warehouse.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StopWatch;

@Aspect
@Slf4j
@Component
public class TransactionalExecutionTimeAspect {

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object transactionalExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final String className = methodSignature.getDeclaringType().getSimpleName();
        final String methodName = methodSignature.getName();

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            return joinPoint.proceed();
        } finally {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                public void afterCommit() {
                    stopWatch.stop();
                    log.info("Execution time for " + className + "." + methodName + " :: " + stopWatch.getTotalTimeMillis() + " ms");
                }
            });
        }
    }
}
