package com.yuqn.controller.society;

import com.yuqn.service.society.SocietyUserLoginService;
import com.yuqn.service.society.SysRoleMenuService;
import com.yuqn.vo.CollegeMajorClass;
import com.yuqn.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/home")
@Tag(name = "用户主页接口")
public class HomeController {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * @author: yuqn
     * @Date: 2025/3/1 23:55
     * @description:
     * 获取用户的功能权限
     * @param: null
     * @return: null
     */
    @GetMapping("/getUserMenu")
    @Operation(summary = "获取用户功能列表")
    public Result getUserEnum(@RequestParam(value = "roleId", required = true) String roleId){
        Result userMenu = sysRoleMenuService.getUserMenu(roleId);
        return userMenu;
    }
}
