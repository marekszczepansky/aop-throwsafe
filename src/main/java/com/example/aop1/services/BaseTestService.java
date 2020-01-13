package com.example.aop1.services;

import com.example.aop1.base.ThrowSafe;
import com.example.aop1.base.ThrowSavable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Service
public class BaseTestService implements ThrowSavable {

    private static final Logger log = LoggerFactory.getLogger(BaseTestService.class);

    @ThrowSafe
    public String serviceMethod1(String test_me) {
        return "service OK " + Long.valueOf(test_me);
    }

    @Override
    public void handleException(@NotNull Throwable exception, @NotNull Method method, @NotNull Object[] args) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("\nEXECUTION FAIL!\nThrowSavable interface implementation used here\n")
                .append("The following method execution failed\n")
                .append("\nClass: ")
                .append(this.getClass().getSimpleName())
                .append("\nMethod: ")
                .append(method.getName())
                .append("\nArguments: ");

        final Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            stringBuilder.append("\n    ")
                    .append(parameters[i].getType().getSimpleName())
                    .append(" ")
                    .append(parameters[i].getName())
                    .append(" = ")
                    .append(args[i]);
        }
        stringBuilder.append("\n\nException details: ");

        log.warn(stringBuilder.toString(), exception);
    }
}
