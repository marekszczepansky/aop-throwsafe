package com.example.aop1;


import com.example.aop1.services.OtherTestService;
import com.example.aop1.services.OtherTestServiceMethod;
import com.example.aop1.services.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class MainController {
    private final TestService testService;
    private final OtherTestService otherTestService;
    private final OtherTestServiceMethod otherTestServiceMethod;

    public MainController(TestService testService, OtherTestService otherTestService, OtherTestServiceMethod otherTestServiceMethod) {
        this.testService = testService;
        this.otherTestService = otherTestService;
        this.otherTestServiceMethod = otherTestServiceMethod;
    }

    @GetMapping("example1/{no}")
    public Collection<String> getExample1(@PathVariable String no) {
        return List.of("Example1 OK",
                nullSafe(testService.serviceMethod1(no)));
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
        return List.of("Example3 OK",
                nullSafe(otherTestServiceMethod.testMethod2(no)));
    }

    private String nullSafe(String test_me) {
        return test_me == null ? "null" : test_me;
    }
}
