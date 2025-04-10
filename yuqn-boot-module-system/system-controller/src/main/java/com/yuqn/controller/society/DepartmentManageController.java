package com.yuqn.controller.society;

import com.yuqn.entity.society.SocietyDepartment;
import com.yuqn.entity.society.SocietyDepartmentUserRole;
import com.yuqn.service.society.SocietyDepartmentUserRoleService;
import com.yuqn.vo.DepartmentUpdateVo;
import com.yuqn.vo.Result;
import com.yuqn.vo.UserDepartmentRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

@RestController
@RequestMapping("/society/departmentManage")
@Tag(name = "部门管理模块")
public class DepartmentManageController {
    @Autowired
    private SocietyDepartmentUserRoleService societyDepartmentUserRoleService;
    /**
     * @author: yuqn
     * @Date: 2025/3/6 20:46
     * @description:
     * 获取部门用户列表
     * @param: null
     * @return: null
     */
    @GetMapping("/getDepartmentUserList")
    @Operation(summary = "获取用户列表")
    public Result getExamineAndApprove(@RequestParam String societyId , @RequestParam String departmentId){
        Result result = societyDepartmentUserRoleService.getDepartmentuserList(societyId,departmentId);
        return result;
    }
    /**
     * @author: yuqn
     * @Date: 2025/3/6 20:46
     * @description:
     * 新增部门用户
     * @param: null
     * @return: null
     */
    @PostMapping("/addDepartmentUser")
    @Operation(summary = "新增部门用户")
    public Result addDepartmentUser(@RequestBody UserDepartmentRole userDepartmentRole,
                                    @RequestHeader("Token") String token){
        System.out.println("=======================departmentId = " + userDepartmentRole);
        Result result = null;
        if(userDepartmentRole.getRoleId().equals("3")){
            result = societyDepartmentUserRoleService.addDepartmentUser(userDepartmentRole.getSocietyBodyId(),
                    userDepartmentRole.getDepartmentId(),
                    userDepartmentRole.getUserId(),token);
        }else{
//            result = Result.ok("非教师身份待完善");
            result = societyDepartmentUserRoleService.addDepartmentUser(userDepartmentRole.getSocietyBodyId(),
                    userDepartmentRole.getDepartmentId(),
                    userDepartmentRole.getUserId(),token);
        }
        return result;
    }
    /**
     * @author: yuqn
     * @Date: 2025/3/6 20:46
     * @description:
     * 解散部门deleteDepartment
     * @param: null
     * @return: null
     */
    @PostMapping("/deleteDepartment")
    @Operation(summary = "解散部门")
    public Result deleteDepartment(@RequestBody UserDepartmentRole userDepartmentRole,
                                    @RequestHeader("Token") String token){
        Result result = null;
        if(userDepartmentRole.getRoleId().equals("3")){
            result = societyDepartmentUserRoleService.deleteDepartment(userDepartmentRole.getDepartmentId(),token);
        }else{
            result = Result.ok("非教师身份待完善");
        }
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/6 20:46
     * @description:
     * 获取部门用户列表
     * @param: null
     * @return: null
     */
    @GetMapping("/getDepartmentDetailsById")
    @Operation(summary = "获取用户列表")
    public Result getDepartmentDetailsById(@RequestParam String departmentId,@RequestHeader("Token") String token){
        Result result = societyDepartmentUserRoleService.getDepartmentDetailsById(departmentId);
        return result;
    }
    /**
     * @author: yuqn
     * @Date: 2025/3/6 20:46
     * @description:
     * 获取部门用户列表
     * @param: null
     * @return: null
     */
    @GetMapping("/getDepartmentUserMember")
    @Operation(summary = "获取部门的角色列表，包含部长副部长干事")
    public Result getDepartmentUserMember(@RequestParam String departmentId){
        Result result = societyDepartmentUserRoleService.getDepartmentUserMember(departmentId);
        return result;
    }
    /**
     * @author: yuqn
     * @Date: 2025/3/14 0:09
     * @description:
     * 提交部门更改申请
     * @param: null
     * @return: null
     */
    @PostMapping("/updateDepartment")
    @Operation(summary = "部门编辑")
    public Result updateDepartment(@RequestBody DepartmentUpdateVo departmentUpdateVo,@RequestHeader("Token") String token){
        System.out.println("departmentUpdateVo = " + departmentUpdateVo);
        System.out.println("token = " + token);
        Result result = societyDepartmentUserRoleService.updateDepartment(departmentUpdateVo,token);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/19 21:29
     * @description:
     * 将用户移除部门
     * @param: null
     * @return: null
     */
    @PostMapping("/deleteDepartmentUserById")
    @Operation(summary = "将用户移除部门")
    public Result deleteDepartmentUserById(@RequestBody SocietyDepartmentUserRole societyDepartmentUserRole, @RequestHeader("Token") String token){
        Result result = societyDepartmentUserRoleService.deleteDepartmentUserById(societyDepartmentUserRole,token);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/6 20:46
     * @description:
     * 获取部门邀请码
     * @param: null
     * @return: null
     */
    @GetMapping("/getDepartmentCode")
    @Operation(summary = "获取部门邀请码")
    public Result getDepartmentCode(@RequestParam String departmentId){
        Result result = societyDepartmentUserRoleService.getDepartmentCode(departmentId);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/6 20:46
     * @description:
     * 根据邀请码获取部门
     * @param: null
     * @return: null
     */
    @GetMapping("/getDepartmentDetailsByCode")
    @Operation(summary = "根据邀请码获取部门")
    public Result getDepartmentDetailsByCode(@RequestParam String departmentCode,@RequestHeader("Token") String token){
        Result result = societyDepartmentUserRoleService.getDepartmentDetailsByCode(departmentCode,token);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/6 20:46
     * @description:
     * 加入部门
     * @param: null
     * @return: null
     */
    @PostMapping("/joinDepartment")
    @Operation(summary = "加入部门")
    public Result joinDepartment(@RequestBody SocietyDepartment societyDepartment,@RequestHeader("Token") String token){
        Result result = societyDepartmentUserRoleService.joinDepartment(societyDepartment.getId(),token);
        return result;
    }
}
