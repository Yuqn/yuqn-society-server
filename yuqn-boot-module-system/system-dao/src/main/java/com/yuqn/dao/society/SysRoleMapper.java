package com.yuqn.dao.society;

import com.yuqn.entity.society.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yuqn
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2024-09-16 16:03:30
* @Entity com.yuqn.entity.society.SysRole
*/
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * @author: yuqn
     * @Date: 2025/3/5 2:23
     * @description:
     * 获取社团部长副部长
     * @param: null
     * @return: null
     */
    String getSocietyRoleUser(String societyId,String roleId);
    /**
     * @author: yuqn
     * @Date: 2025/3/5 2:23
     * @description:
     * 获取部门部长副部长
     * @param: null
     * @return: null
     */
    String getDepartmentMinisterOrBiceMinister(String departmentId,String roleId);
}




