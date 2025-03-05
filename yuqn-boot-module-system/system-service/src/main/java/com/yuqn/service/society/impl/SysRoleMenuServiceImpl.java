package com.yuqn.service.society.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuqn.entity.society.SysMenu;
import com.yuqn.entity.society.SysRoleMenu;
import com.yuqn.service.society.SysRoleMenuService;
import com.yuqn.dao.society.SysRoleMenuMapper;
import com.yuqn.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author yuqn
* @description 针对表【sys_role_menu】的数据库操作Service实现
* @createDate 2024-09-16 16:03:30
*/
@Slf4j
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu>
    implements SysRoleMenuService{

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public Result getUserMenu(String roleId) {
        Result result = null;
        List<SysMenu> userMenu = null;
        try {
            userMenu = sysRoleMenuMapper.getUserMenu(roleId);
            result = Result.ok(userMenu);
        }catch (Exception e){
            log.error("获取用户菜单失败");
            result = Result.fail("获取用户菜单失败");
        }finally {
            return result;
        }
    }
}




