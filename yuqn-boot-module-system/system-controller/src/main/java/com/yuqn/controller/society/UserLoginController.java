package com.yuqn.controller.society;

import com.yuqn.vo.Result;
import com.yuqn.vo.SocietyUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/society")
@Tag(name = "用户登录接口")
public class UserLoginController {

    /**
     * @author: yuqn
     * @Date: 2024/11/20 8:55
     * @description:
     * 获取连级选择器数据
     * @param: null
     * @return: null
     */
    @PostMapping("/getDataTree")
    @Operation(summary = "获取连级选择器数据")
    public Result getDataTree(){
        return null;
    }

    // 注册用户
    @PostMapping("/register")
    @Operation(summary = "新用户注册")
    public Result register(@RequestBody SocietyUserVo societyUserVo){
        System.out.println("societyUserVo = " + societyUserVo);

        return Result.OK();
    }
}
