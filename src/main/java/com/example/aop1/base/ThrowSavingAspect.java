package com.example.aop1.base;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.notNull;

@Aspect
@Component
public class ThrowSavingAspect {

    private static final Logger log = LoggerFactory.getLogger(ThrowSavingAspect.class);

    @Around("@annotation(throwSafe) && target(throwSavable)")
    public Object throwSave(ProceedingJoinPoint point, ThrowSafe throwSafe, ThrowSavable throwSavable) {
        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable ex) {
            try {
                Method method = ((MethodSignature) point.getSignature()).getMethod();
                requireNonNull(method, "Method cannot be null");

                throwSavable.handleException(ex, method, point.getArgs());
            } catch (Throwable exx) {
                log.error("Exception boom", exx);
            }
        }
        return result;
    }
}

