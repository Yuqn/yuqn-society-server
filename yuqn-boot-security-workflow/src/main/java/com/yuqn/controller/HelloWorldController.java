package com.yuqn.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @RequestMapping("/hello")
    // @PreAuthorize("hasAuthority('system:test:list11')")
    @PreAuthorize("@ex.hasAuthority('system:test:list')") // 使用自定义验证方式
    public String getHello(){
        return "hello world";
    }
}
