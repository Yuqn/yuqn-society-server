package com.yuqn.dao.society;

import com.yuqn.entity.test.YuqnTest;
import com.yuqn.vo.UserBodyRole;
import com.yuqn.vo.UserDepartmentRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author yuqn
 * @description 操作
 * @createDate 2024-09-16 16:03:30
 * @Entity com.yuqn.entity.society.SocietyUser
 */
@Mapper
public interface SocietyUserLoginMapper {
    List<UserBodyRole> getSocietyRole(String userId);
    List<UserDepartmentRole> getDepartmentRole(String userId);
}
