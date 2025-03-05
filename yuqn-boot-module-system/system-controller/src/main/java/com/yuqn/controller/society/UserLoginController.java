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
import java.util.Map;

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

    // 学生登录
    @PostMapping("/userLogin")
    @Operation(summary = "学生登录")
    public Result userLogin(@RequestBody UserLoginVo userLoginVo){
        Result result = societyUserLoginService.userLogin(userLoginVo);
        return result;
    }

    // 教师登录
    @PostMapping("/teacherLogin")
    @Operation(summary = "教师登录")
    public Result teacherLogin(@RequestBody UserLoginVo userLoginVo){
        Result result = societyUserLoginService.teacherLogin(userLoginVo);
        return result;
    }

    // 判断是否已经注册过
    @GetMapping("/isRegister")
    @Operation(summary = "判断是否有该用户注册过")
    public Result isRegister(@RequestParam String phonenumber){
        System.out.println("phonenumber = " + phonenumber);
        Result result = societyUserLoginService.isRegister(phonenumber);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/2/28 2:01
     * @description:
     * 更改密码
     * @param: null
     * @return: null
     */
    @PostMapping("/changePassword")
    @Operation(summary = "更改密码")
    public Result changePassword(@RequestBody Map<String, String> requestBody){
        String phonenumber = requestBody.get("phonenumber");
        String password = requestBody.get("newPassword");
        Result result = societyUserLoginService.changePassword(phonenumber, password);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/2/28 22:25
     * @description:
     * 获取用户角色
     * @param: null
     * @return: null
     */
    @GetMapping("/getRole")
    @Operation(summary = "更改密码")
    public Result getRole(@RequestHeader("Token") String token){
        System.out.println("//////////////////" + token);
        Result result = societyUserLoginService.getRole(token);
        return result;
    }

}
