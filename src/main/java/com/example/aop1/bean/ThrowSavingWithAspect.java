package com.example.aop1.bean;


import com.example.aop1.base.ThrowSafe;
import com.example.aop1.base.ThrowSavingAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static java.util.Objects.requireNonNull;

@Aspect
@Component
class ThrowSavingWithAspect {
    private final ApplicationContext applicationContext;
    private static final Logger log = LoggerFactory.getLogger(ThrowSavingAspect.class);

    ThrowSavingWithAspect(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Around("@annotation(throwSafe) && @target(throwSafeWith) && target(target)")
    public Object throwSave(ProceedingJoinPoint point, ThrowSafe throwSafe, ThrowSafeWith throwSafeWith, Object target) {
        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable ex) {
            try {
                Method method = ((MethodSignature) point.getSignature()).getMethod();
                requireNonNull(method, "Method cannot be null");
                requireNonNull(throwSafeWith.value(), "@ThrowSafeWith value cannot be null");

                applicationContext
                        .getBean(throwSafeWith.value())
                        .handleException(ex, target, method, point.getArgs());
            } catch (Throwable exx) {
                log.error("Exception boom", exx);
            }
        }
        return result;
    }
}
