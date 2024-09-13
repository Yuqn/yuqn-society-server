package com.yuqn.controller.test;

import cn.hutool.core.date.DateUtil;
import com.yuqn.entity.test.YuqnTest;
import com.yuqn.service.test.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@Tag(name = "test接口")
public class TestController {

    private TestService testService;

    @Autowired
    public void setTestService(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/getmessage")
    @Operation(summary = "获取message")
    public String getmes(){
        String mes = testService.getMessage();
        return mes;
    }

    @GetMapping("/getmesforxml")
    @Operation(summary = "获取xx")
    public List<YuqnTest> getmesforxml(){
        return testService.getAllMessage();
    }

    @GetMapping("/getDate")
    @Operation(summary = "获取xx")
    public List<YuqnTest> getDate(){
        System.out.println(DateUtil.today());
        return testService.getAllMessage();
    }
}
