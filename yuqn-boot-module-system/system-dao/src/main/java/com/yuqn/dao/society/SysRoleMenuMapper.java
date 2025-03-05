package com.yuqn.dao.society;

import com.yuqn.entity.society.SysMenu;
import com.yuqn.entity.society.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
* @author yuqn
* @description 针对表【sys_role_menu】的数据库操作Mapper
* @createDate 2024-09-16 16:03:30
* @Entity com.yuqn.entity.society.SysRoleMenu
*/
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    List<SysMenu> getUserMenu(String userId);
}




