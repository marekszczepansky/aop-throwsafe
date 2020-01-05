package com.example.aop1.bean;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

public interface ThrowSavableWith {
    void handleException(@NotNull Throwable exception, @NotNull Object target, @NotNull Method method, @NotNull Object[] args);
}
