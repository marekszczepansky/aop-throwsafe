package com.example.aop1.bean;


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

    @Around("@annotation(com.example.aop1.base.ThrowSafe) && @target(throwSaveAt) && target(target)")
    public Object throwSave(ProceedingJoinPoint point, ThrowSafeWith throwSaveAt, Object target) {
        Object result = null;
        Method method = null;
        try {
            method = ((MethodSignature) point.getSignature()).getMethod();
            result = point.proceed();
        } catch (Throwable ex) {
            try {
                requireNonNull(method, "Method cannot be null");
                requireNonNull(throwSaveAt.value(), "@ThrowSafeWith value cannot be null");
                final ThrowSavableWith throwSavableBean = applicationContext.getBean(throwSaveAt.value());
                throwSavableBean.handleException(ex, target, method, point.getArgs());
            } catch (Throwable exx) {
                log.error("Exception boom", exx);
            }
        }
        return result;
    }
}