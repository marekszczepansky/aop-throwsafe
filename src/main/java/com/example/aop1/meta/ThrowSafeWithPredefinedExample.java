package com.example.aop1.meta;

import com.example.aop1.bean.ThrowSafeWith;
import com.example.aop1.bean.ThrowSavableWithExample;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@ThrowSafeWith(ThrowSavableWithExample.class)
public @interface ThrowSafeWithPredefinedExample {
}
