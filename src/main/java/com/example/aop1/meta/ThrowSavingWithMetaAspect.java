package com.example.aop1.meta;


import com.example.aop1.base.ThrowSavingAspect;
import com.example.aop1.bean.ThrowSafeWith;
import com.example.aop1.bean.ThrowSavableWith;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static java.util.Objects.requireNonNull;

@Aspect
@Component
class ThrowSavingWithMetaAspect {
    private final ApplicationContext applicationContext;
    private static final Logger log = LoggerFactory.getLogger(ThrowSavingAspect.class);

    ThrowSavingWithMetaAspect(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Pointcut("execution(@(@com.example.aop1.bean.ThrowSafeWith *) * *(..))")
    public void throwSafeMeta(){}

    @Around("throwSafeMeta() && target(target)")
    public Object throwSave(ProceedingJoinPoint point, Object target) {
        Object result = null;
        Method method = null;
        try {
            method = ((MethodSignature) point.getSignature()).getMethod();
            result = point.proceed();
        } catch (Throwable ex) {
            try {
                log.info("--==@ThrowSaveWith meta annotation at method used==--");
                requireNonNull(method, "Method cannot be null");
                ThrowSafeWith throwSafeWith = AnnotationUtils.findAnnotation(method, ThrowSafeWith.class);
                requireNonNull(throwSafeWith);
                requireNonNull(throwSafeWith.value(), "@ThrowSafeWith value cannot be null");
                final ThrowSavableWith throwSavableBean = applicationContext.getBean(throwSafeWith.value());
                throwSavableBean.handleException(ex, target, method, point.getArgs());
            } catch (Throwable exx) {
                log.error("Exception boom", exx);
            }
        }
        return result;
    }
}
