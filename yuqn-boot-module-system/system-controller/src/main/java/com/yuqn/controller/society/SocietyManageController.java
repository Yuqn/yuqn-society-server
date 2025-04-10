package com.yuqn.controller.society;

import com.yuqn.service.society.SocietyBodyService;
import com.yuqn.service.society.SocietyBodyUserRoleService;
import com.yuqn.service.society.SocietyDepartmentService;
import com.yuqn.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/society/manage")
@Tag(name = "社团管理模块")
public class SocietyManageController {
    @Autowired
    private SocietyBodyService societyBodyService;
    @Autowired
    private SocietyDepartmentService societyDepartmentService;
    @Autowired
    private SocietyBodyUserRoleService societyBodyUserRoleService;
    /**
     * @author: yuqn
     * @Date: 2025/3/6 20:46
     * @description:
     * 获取社团成员列表
     * @param: null
     * @return: null
     */
    @GetMapping("/getSocietyUserList")
    @Operation(summary = "获取社团用户列表")
    public Result getSocietyUserList(@RequestParam String societyId){
        Result result = societyBodyService.getSocietyUserList(societyId);
        return result;
    }
    /**
     * @author: yuqn
     * @Date: 2025/3/6 20:46
     * @description:
     * 获取社团详细列表
     * @param: null
     * @return: null
     */
    @GetMapping("/getSocietyDetails")
    @Operation(summary = "获取社团详情")
    public Result getSocietyDetails(@RequestParam String societyId){
        System.out.println("societyId = " + societyId);
        Result result = societyBodyService.getSocietyDetails(societyId);
        return result;
    }
    /**
     * @author: yuqn
     * @Date: 2025/3/7 2:22
     * @description:
     * 编辑社团信息
     * @param: null
     * @return: null
     */
    @PostMapping("/societyUpdate")
    @Operation(summary = "获取社团详情")
    public Result societyUpdate(@RequestBody SocietyUpdateVo societyUpdateVo, @RequestHeader("Token") String token){
        Result result = societyBodyService.societyUpdate(societyUpdateVo,token);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2024/11/20 8:55
     * @description:
     * 获取社团详细数据
     * @param: null
     * @return: null
     */
    @GetMapping("/getSocietyDetailsById")
    @Operation(summary = "获取社团列表")
    public Result getSocietyDetailsById(@RequestParam String societyId){
        Result societyList = societyBodyService.getSocietyDetailsById(societyId);
        return societyList;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/7 2:22
     * @description:
     * 新增部门
     * @param: null
     * @return: null
     */
    @PostMapping("/addDepartment")
    @Operation(summary = "新增部门")
    public Result addDepartment(@RequestBody AddDepartmentVo addDepartmentVo , @RequestHeader("Token") String token){
        System.out.println("addDepartmentVo = " + addDepartmentVo);
        Result result = null;
        if("3".equals(addDepartmentVo.getUserRole())){
            // 教师身份直接添加
            result = societyDepartmentService.addDepartment(addDepartmentVo,token);
        }else{
            // 非教师身份，需要进入审批
//            result = Result.ok("非教师身份，待审核");
            result = societyDepartmentService.addDepartment(addDepartmentVo,token);
        }
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/7 2:22
     * @description:
     * 解散社团
     * @param: null
     * @return: null
     */
    @PostMapping("/deleteDepartment")
    @Operation(summary = "解散")
    public Result deleteDepartment(@RequestBody AddDepartmentVo addDepartmentVo, @RequestHeader("Token") String token){
        System.out.println("addDepartmentVo = " + addDepartmentVo);
        Result result = null;
        if("3".equals(addDepartmentVo.getUserRole())){
            // 教师身份直接添加
            result = societyDepartmentService.deleteDepartment(addDepartmentVo,token);
        }else{
            // 非教师身份，需要进入审批
//            result = Result.ok("非教师身份，待审核");
            result = societyDepartmentService.deleteDepartment(addDepartmentVo,token);
        }
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/15 3:24
     * @description:
     * 获取社团成员列表，用户社团编辑
     * @param: null
     * @return: null
     */
    @GetMapping("/getSocietyUserListForUpdate")
    @Operation(summary = "获取社团成员列表")
    public Result getSocietyUserListForUpdate(@RequestParam String societyId){
        Result result = societyBodyUserRoleService.getUserListById(societyId);
        return result;
    }

}
