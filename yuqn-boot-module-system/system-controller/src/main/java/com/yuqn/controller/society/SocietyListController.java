package com.yuqn.controller.society;

import com.yuqn.entity.society.SocietyDepartment;
import com.yuqn.service.society.SocietyBodyService;
import com.yuqn.service.society.SocietyDepartmentService;
import com.yuqn.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/society/list")
@Tag(name = "社团搜索模块接口")
public class SocietyListController {
    @Autowired
    private SocietyBodyService societyBodyService;
    @Autowired
    private SocietyDepartmentService societyDepartmentService;

    /**
     * @author: yuqn
     * @Date: 2024/11/20 8:55
     * @description:
     * 获取社团列表数据
     * @param: null
     * @return: null
     */
    @GetMapping("/getSocietyList")
    @Operation(summary = "获取社团列表")
    public Result getSocietyList(@RequestParam String searchValue){
        Result societyList = societyBodyService.getSocietyList(searchValue);
        System.out.println("searchValue = " + searchValue);
        return societyList;
    }
    /**
     * @author: yuqn
     * @Date: 2025/3/5 3:19
     * @description:
     * 查看部门列表
     * @param: null
     * @return: null
     */
    @GetMapping("/getDepartmentById")
    @Operation(summary = "获取部门列表")
    public Result getDepartmentById(@RequestParam String societyId,@RequestHeader("Token") String token){
        Result departmentById = societyDepartmentService.getDepartmentById(societyId,token);
        return departmentById;
    }
    /**
     * @author: yuqn
     * @Date: 2025/3/5 3:20
     * @description:
     * 查看部门成员
     * @param: null
     * @return: null
     */
    @GetMapping("/getDepartmentUserById")
    @Operation(summary = "获取部门成员列表")
    public Result getDepartmentUserById(String departmentId){
        Result result = societyDepartmentService.getDepartmentUserById(departmentId);
        return result;
    }
}
