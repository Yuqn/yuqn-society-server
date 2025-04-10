package com.yuqn.dao.society;

import com.yuqn.entity.society.SocietyDepartment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuqn.vo.SocietyUserListVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author yuqn
* @description 针对表【society_department】的数据库操作Mapper
* @createDate 2024-09-16 16:03:30
* @Entity com.yuqn.entity.society.SocietyDepartment
*/
@Mapper
public interface SocietyDepartmentMapper extends BaseMapper<SocietyDepartment> {
    // 查看部门人数
    int getDepartmentUserCount(String departmentId);
    // 获取部门成员列表
    List<SocietyUserListVo> getDepartmentUserList(String societyId);
}




