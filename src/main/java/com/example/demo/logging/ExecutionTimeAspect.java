package com.example.demo.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {

    @Around(
            "execution(* com.example.demo.controller..*(..)) || " +
                    "execution(* com.example.demo.service..*(..))"
    )
    public Object logExecutionTime(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        log.info(
                "{} executed in {} ms",
                joinPoint.getSignature().toShortString(),
                (end - start)
        );

        return result;
    }
}