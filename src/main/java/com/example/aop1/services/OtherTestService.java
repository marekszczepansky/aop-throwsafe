package com.example.aop1.services;

import com.example.aop1.base.ThrowSafe;
import com.example.aop1.bean.ThrowSafeWith;
import com.example.aop1.bean.ThrowSavableWithExample;
import org.springframework.stereotype.Service;

@Service
@ThrowSafeWith(ThrowSavableWithExample.class)
public class OtherTestService {

    @ThrowSafe
    public String testMethod1(String testArg) {
        return "Other test service OK " + Long.valueOf(testArg);
    }
}
