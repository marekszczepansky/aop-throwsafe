package com.example.aop1.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
public class ThrowSavableWithExample implements ThrowSavableWith {
    private static final Logger log = LoggerFactory.getLogger(ThrowSavableWithExample.class);

    @Override
    public void handleException(@NotNull Throwable exception, @NotNull Object target, @NotNull Method method, @NotNull Object[] args) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("\nEXECUTION FAIL!\nThrowSavableAtExample bean used here\n")
                .append("The following method execution failed\n")
                .append("\nClass: ")
                .append(target.getClass().getSimpleName())
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
