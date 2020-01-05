package com.example.aop1.services;

import com.example.aop1.bean.ThrowSafeWith;
import com.example.aop1.bean.ThrowSavableWithExample;
import org.springframework.stereotype.Service;

@Service
public class OtherTestServiceMethod {

    @ThrowSafeWith(ThrowSavableWithExample.class)
    public String testMethod1(String testArg) {
        return "Method test service OK " + Long.parseLong(testArg);
    }
}
