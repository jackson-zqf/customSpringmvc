package com.zqf.demo.service.impl;

import com.zqf.customframwork.annotations.Service;
import com.zqf.demo.service.CustomService;

@Service
public class CustomServiceImpl implements CustomService {

    @Override
    public String getName(String name) {
        System.out.println("service 打印name：" + name);
        return name;
    }
}
