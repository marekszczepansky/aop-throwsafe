# APO ThrowSafe

This code shows an example of Aspect Oriented exception handling. 
The purpose of this handling is to react on an exception with a handling code (logging) and don't break a execution flow.
After handling exception the affected method will return null (or void)

# Test application

This is test application where you can find all implementation working. Application exposes 4 GET REST endpoints:
- http://localhost:8081/example1/1234
- http://localhost:8081/example2/1234
- http://localhost:8081/example3/1234
- http://localhost:8081/example4/1234

Last ura part `1234` is an url variable, which is parsed as a long number. To check exception handling just replace `1234` with not valid 
long number eg. `12w34`.

# Implementation examples

## Interface implementation

 This implementation requires your bean to implement interface `ThrowSavable` and a method to be annotated with `@ThrowSafe`
 
 ```java
@Service
public class BaseTestService implements ThrowSavable {
    @ThrowSafe
    public String serviceMethod1(String test_me) {
        return "service OK " + Long.valueOf(test_me);
    }

    @Override
    public void handleException(@NotNull Throwable exception, @NotNull Method method, @NotNull Object[] args) {
        log.warn("Exception handled", exception);
    }
}
```  

To test it you need to go to link: http://localhost:8081/example1/1234

Aspect definition located in component [ThrowSavingAspect.java](src/main/java/com/example/aop1/base/ThrowSavingAspect.java)  

```java
@Aspect
@Component
public class ThrowSavingAspect {
    @Around("@annotation(throwSafe) && target(throwSavable)")
    public Object throwSave(ProceedingJoinPoint point, ThrowSafe throwSafe, ThrowSavable throwSavable) {
        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable ex) {
// handler implementation
        }
        return result;
    }
}
```

## Handling exceptions by bean associated with a bean type 

This and following examples uses a bean of developer's choice to handle an exception. 
This makes handler implementation loosely coupled to its usage.

Your bean don't need to implement interface exception handling in favor of annotate class with `@ThrowSafeWith`.
An exception handling bean have to implement interface `ThrowSaveableWith`.
A method have to be annotated with  `@ThrowSafe`.

```java
@Component
public class ThrowSavableWithExample implements ThrowSavableWith {
    @Override
    public void handleException(@NotNull Throwable exception, @NotNull Object target, @NotNull Method method, @NotNull Object[] args) {
        log.warn("Exception handled", exception);
    }
}
```

```java
@Service
@ThrowSafeWith(ThrowSavableWithExample.class)
public class OtherTestService {
    @ThrowSafe
    public String testMethod1(String testArg) {
        return "Other test service OK " + Long.valueOf(testArg);
    }
}
```  

To test it you need to go to link: http://localhost:8081/example2/1234

Aspect definition located in component [ThrowSavingWithAspect](src/main/java/com/example/aop1/bean/ThrowSavingWithAspect.java)  

```java
@Aspect
@Component
class ThrowSavingWithAspect {
    @Around("@annotation(throwSafe) && @target(throwSafeWith) && target(target)")
    public Object throwSave(ProceedingJoinPoint point, ThrowSafe throwSafe, ThrowSafeWith throwSafeWith, Object target) {
        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable ex) {
// exception handling
        }
        return result;
    }
}
```

## Handling exceptions by bean associated with a method

You have to annotate each method with `@ThrowSafeWith` and define a bean handling an exception

```java
@Service
public class OtherTestServiceMethod {
    @ThrowSafeWith(ThrowSavableWithExample.class)
    public String testMethod1(String testArg) {
        return "Method test service OK " + Long.parseLong(testArg);
    }
}
``` 

To test it you need to go to link: http://localhost:8081/example3/1234

Aspect definition located in component [ThrowSavingWithMethodAspect](src/main/java/com/example/aop1/method/ThrowSavingWithMethodAspect.java)  

## Handling exceptions by bean associated with a method by meta-annotation

Annotating each method with an annotation requiring a bean type parameter may be error prone. You can enclose this definition
into mata annotation defined for the error handling bean. 

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@ThrowSafeWith(ThrowSavableWithExample.class)
public @interface ThrowSafeWithExample {
}
``` 

```java
@Service
public class OtherTestServiceMethod {
    @ThrowSafeWithExample
    public String testMethod2(String testArg) {
        return "Method meta annotation test service OK " + Long.parseLong(testArg);
    }
}
```

To test it you need to go to link: http://localhost:8081/example4/1234

Aspect definition located in component [ThrowSavingWithMetaAspect](src/main/java/com/example/aop1/meta/ThrowSavingWithMetaAspect.java)  
