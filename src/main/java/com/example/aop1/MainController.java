package com.example.aop1;


import com.example.aop1.services.OtherTestService;
import com.example.aop1.services.OtherTestServiceMethod;
import com.example.aop1.services.BaseTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNullElse;

@RestController
public class MainController {
    private final BaseTestService baseTestService;
    private final OtherTestService otherTestService;
    private final OtherTestServiceMethod otherTestServiceMethod;

    public MainController(BaseTestService baseTestService,
                          OtherTestService otherTestService,
                          OtherTestServiceMethod otherTestServiceMethod) {
        this.baseTestService = baseTestService;
        this.otherTestService = otherTestService;
        this.otherTestServiceMethod = otherTestServiceMethod;
    }

    @GetMapping("example1/{no}")
    public Collection<String> getExample1(@PathVariable String no) {
        return List.of("Example1 OK",
                nullSafe(baseTestService.serviceMethod1(no)));
    }
    @GetMapping("example2/{no}")
    public Collection<String> getExample2(@PathVariable String no) {
        return List.of("Example2 OK",
                nullSafe(otherTestService.testMethod1(no)));
    }
    @GetMapping("example3/{no}")
    public Collection<String> getExample3(@PathVariable String no) {
        return List.of("Example3 OK",
                nullSafe(otherTestServiceMethod.testMethod1(no)));
    }

    @GetMapping("example4/{no}")
    public Collection<String> getExample4(@PathVariable String no) {
        return List.of("Example4 OK",
                nullSafe(otherTestServiceMethod.testMethod2(no)));
    }

    private String nullSafe(String test_me) {
        return requireNonNullElse(test_me, "null");
    }
}
