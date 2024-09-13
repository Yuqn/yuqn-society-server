package com.yuqn.controller.test;

import com.yuqn.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys")
@Tag(name = "test3接口")
public class Test3Controller {

    @GetMapping("/login")
    @Operation(summary = "登录")
    public Result getmes(){
        return new Result().success("token");
    }
    @GetMapping("/getOne")
    @Operation(summary = "获取一个用户信息")
    public Map<String,Object> getOne(){
        Map<String,Object> obj = new HashMap<>();
        obj.put("loginId",123456789);
        obj.put("token","aaashshsh");
        obj.put("name","yuqn");
        return obj;
    }
}
