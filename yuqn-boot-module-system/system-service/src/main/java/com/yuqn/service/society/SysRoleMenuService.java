package com.yuqn.service.society;

import com.yuqn.entity.society.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuqn.vo.Result;

/**
* @author yuqn
* @description 针对表【sys_role_menu】的数据库操作Service
* @createDate 2024-09-16 16:03:30
*/
public interface SysRoleMenuService extends IService<SysRoleMenu> {
    // 获取角色功能列表
    Result getUserMenu(String roleId);

}
