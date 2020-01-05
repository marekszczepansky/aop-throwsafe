package com.example.aop1.base;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

public interface ThrowSavable {
    void handleException(@NotNull Throwable exception, @NotNull Method method, @NotNull Object[] args);
}
