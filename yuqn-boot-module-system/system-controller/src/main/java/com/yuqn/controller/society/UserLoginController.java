package com.yuqn.controller.society;

import com.yuqn.entity.society.SocietyGrade;
import com.yuqn.service.society.SocietyUserLoginService;
import com.yuqn.vo.CollegeMajorClass;
import com.yuqn.vo.Result;
import com.yuqn.vo.SocietyUserVo;
import com.yuqn.vo.UserLoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/society")
@Tag(name = "用户登录接口")
public class UserLoginController {

    @Autowired
    private SocietyUserLoginService societyUserLoginService;

    /**
     * @author: yuqn
     * @Date: 2024/11/20 8:55
     * @description:
     * 获取班级数据
     * @param: null
     * @return: null
     */
    @GetMapping("/getDataTree")
    @Operation(summary = "获取联级选择器数据")
    public Result getDataTree(){
        List<CollegeMajorClass> dataTree = societyUserLoginService.getDataTree();
        return Result.OK(dataTree);
    }
    /**
     * @author: yuqn
     * @Date: 2024/11/20 8:55
     * @description:
     * 获取年级数据
     * @param: null
     * @return: null
     */
    @GetMapping("/getDataGrade")
    @Operation(summary = "获取年级数据")
    public Result getDataGrade(){
        List<CollegeMajorClass> dataTree = societyUserLoginService.getDataGrade();
        return Result.OK(dataTree);
    }

    // 注册用户
    @PostMapping("/register")
    @Operation(summary = "新用户注册")
    public Result register(@RequestBody SocietyUserVo societyUserVo){
        System.out.println("societyUserVo = " + societyUserVo);
        int i = societyUserLoginService.registerUser(societyUserVo);
        if (i == 1){
            return Result.ok("注册成功");
        }else{
            return Result.error("注册失败");
        }
    }

    // 用户登录
    @PostMapping("/userLogin")
    @Operation(summary = "用户登录")
    public Result userLogin(@RequestBody UserLoginVo userLoginVo){
        Result result = societyUserLoginService.userLogin(userLoginVo);
        return result;
    }
}
